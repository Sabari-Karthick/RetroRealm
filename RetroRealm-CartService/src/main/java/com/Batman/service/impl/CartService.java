package com.Batman.service.impl;

import java.util.*;

import com.Batman.dto.cart.GameInfo;
import com.Batman.entity.CartItem;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.Batman.dto.cart.CartRequest;
import com.Batman.dto.cart.CartValueResponse;
import com.Batman.entity.Cart;
import com.Batman.exception.wrapper.InputFieldException;
import com.Batman.exception.wrapper.NoCartAvailableException;
import com.Batman.feignclinet.GameFeignClient;
import com.Batman.repository.ICartRespository;
import com.Batman.service.ICartService;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("cartService")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartService implements ICartService {

	private static final String USER_CART_IS_EMPTY = "USER_CART_IS_EMPTY";

	private final ICartRespository cartRepository;

	private final GameFeignClient gameFeignClinet;
	
	private static final String SERVICE_NAME = "cart-service";

	@Override
	public Cart addToCart(CartRequest cartRequest, BindingResult bindingResult) {

		// Needs to let the framework handle the validation and exception handling instead of manually checking for errors in the binding result.
		// Since Controller already has @Valid annotation, it will automatically validate the request and throw MethodArgumentNotValidException if there are any validation errors.
		if (bindingResult.hasErrors()) {
		    log.error("Input Field Invalid Error ...");
		    FieldError fieldError = bindingResult.getFieldError();
		    String errorMessage = (fieldError != null) ? fieldError.getDefaultMessage() : "Invalid input field";
		    throw new InputFieldException(errorMessage);
		}


		Integer userId = cartRequest.getUserId();
		String newCartItem = cartRequest.getGameId();

		GameInfo gameInfo = gameFeignClinet.getGameInfoById(newCartItem);

		Optional<Cart> userCartOptional = cartRepository.findByUserId(userId);
		Cart cart;
		if (userCartOptional.isPresent()) {
			cart = userCartOptional.get();
			List<CartItem> cartItems = cart.getCartItems();

			boolean gameAlreadyInCart = cartItems.stream()
					.anyMatch(cartItem -> cartItem.getGameId().equals(newCartItem));

			if(gameAlreadyInCart) {
				log.error("Game Id {} is already present in the Cart {} of User {} ...",newCartItem,cart.getCartId(),userId);
				//	Consideration Needed whether to throw an exception or just return the cart without adding the item again
				return cart;
			}

			log.info("Adding Game Id {} to Cart {} of User {} ...", newCartItem, cart.getCartId(), userId);
			cartItems.add(CartItem.builder().gameId(newCartItem).selectedForCheckout(true).priceAtAddTime(gameInfo.effectiveGamePrice()).build());
			cart.setCartItems(cartItems);

			return cartRepository.save(cart);

		} else {
			log.info("Creating New Cart for User Id {} ...",userId);
			List<CartItem> cartItems = new ArrayList<>();
			cartItems.add(CartItem.builder().gameId(newCartItem).selectedForCheckout(true).priceAtAddTime(gameInfo.effectiveGamePrice()).build());
			cart = Cart.builder().userId(userId).cartItems(cartItems).build();
			return cartRepository.save(cart);
		}
	}

	@Override
	public Cart getCartOfUser(Integer userId) {
		log.info("Entering Get Cart of User {} ...",userId);
		/**
		 * 
		 * Consideration Needed whether to store total amount 
		 * and whether we need to update the cart when discount is applied
		 * and whether an notification needs to be send when cart price is dropped
		 * and whether an cart needs to be created when user is registered
		 */
		
		
		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new NoCartAvailableException(USER_CART_IS_EMPTY));
		log.info("Leaving Get Cart of User {} ...",userId);
		return cart;
	}

	@Override
	public Cart removeItemFromCart(Integer userId, String gameId) {
		Optional<Cart> userCartOptional = cartRepository.findByUserId(userId);

		if (userCartOptional.isPresent()) {
			Cart cart = userCartOptional.get();
			log.info("Removing Game Id {} from Cart {} of User {} ...",gameId,cart.getCartId(),userId);

			List<CartItem> cartItems = cart.getCartItems();

			boolean gameFoundInCart = cartItems.stream()
					.anyMatch(cartItem -> cartItem.getGameId().equals(gameId));

			if(!gameFoundInCart) {
				log.error("Game Id {} is not found in the Cart {} of User {} ...",gameId,cart.getCartId(),userId);
				throw new NoCartAvailableException("GAME_ID_NOT_FOUND_IN_CART");
			}

			cartItems.forEach(cartItem -> {
				if(cartItem.getGameId().equals(gameId)) {
					cartItem.setSelectedForCheckout(false);
				}
			});
		} else {
			throw new NoCartAvailableException(USER_CART_IS_EMPTY);
		}
		return userCartOptional.get();
	}

	@Retry(name = SERVICE_NAME)
	@CircuitBreaker(name = SERVICE_NAME,fallbackMethod = "retryFallback")
	private Double calculatePrice(Set<String> gameIds) {
		log.info("Sending Get Total Price Request For Game Service ...");
		 Double totalPrice = gameFeignClinet.getTotalPrice(gameIds);
		 log.info("Leaving Get Total Price Request For Game Service ...");
		 return totalPrice;
	}

	@Override
	public Cart updateSelectedItemsCart(Integer userId, String gameId) {
		Optional<Cart> userCartOptional = cartRepository.findByUserId(userId);

		if (userCartOptional.isPresent()) {

			Cart cart = userCartOptional.get();
			List<CartItem> cartItems = cart.getCartItems();

			boolean gameFoundInCart = cartItems.stream()
					.anyMatch(cartItem -> cartItem.getGameId().equals(gameId));

			if(!gameFoundInCart) {
				log.error("Game Id {} is not found in the Cart {} of the User {} ...",gameId,cart.getCartId(),userId);
				throw new NoCartAvailableException("GAME_ID_NOT_FOUND_IN_CART");
			}

			cartItems.forEach(cartItem -> {
				if(cartItem.getGameId().equals(gameId)) {
					log.info("Updating Selected For Checkout for Game Id {} in Cart {} of User {} ...",gameId,cart.getCartId(),userId);
					cartItem.setSelectedForCheckout(!cartItem.isSelectedForCheckout());
				}
			});

			return cartRepository.save(cart);

		} else {
			log.error("No Cart Found for User Id {} ...",userId);
			throw new NoCartAvailableException(USER_CART_IS_EMPTY);
		}
	}

	@Override
	public CartValueResponse getCartItems(Integer userId) {
		log.info("Entering getCartItems ...");
		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new NoCartAvailableException(USER_CART_IS_EMPTY));

		// Need to decide who is the source of truth Cart Service or Game Service.
		Double cartPrice = cart.getCartItems().stream()
				.filter(CartItem::isSelectedForCheckout)
				.mapToDouble(CartItem::getPriceAtAddTime)
				.sum();

		CartValueResponse cartValueResponse = new CartValueResponse(cart.getCartItems(), cartPrice);
		log.info("Leaving getCartItems ...");
		return cartValueResponse;
	}
	
	public ResponseEntity<?> retryFallback(Exception ex) {
		log.info("Entering Retry Fallback Method of Cart Service ...");
		log.error(ex.getMessage());
		return new ResponseEntity<>("Please Try Again Later...",HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
