package com.Batman.service.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
import com.Batman.feignclinet.GameFeignClinet;
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

	private final GameFeignClinet gameFeignClinet;
	
	private static final String SERVICE_NAME = "cart-service";

	@Override
	public Cart addToCart(CartRequest cartRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
		    log.error("Input Field Invalid Error ...");
		    FieldError fieldError = bindingResult.getFieldError();
		    String errorMessage = (fieldError != null) ? fieldError.getDefaultMessage() : "Invalid input field";
		    throw new InputFieldException(errorMessage);
		}


		Integer userId = cartRequest.getUserId();
		String newCartItem = cartRequest.getGameId();

		Optional<Cart> userCartOptional = cartRepository.findByUserId(userId);
		Cart cart;
		if (userCartOptional.isPresent()) {
			cart = userCartOptional.get();
			Set<String> cartItems = cart.getCartItems();
			Set<String> selectedCartItems = cart.getSelectedCartItems();
			if (!cartItems.contains(newCartItem)) {
				cartItems.add(newCartItem);
				selectedCartItems.add(newCartItem);
				cart.setCartItems(cartItems);
				cart.setSelectedCartItems(selectedCartItems);
				cart.setTotalPrice(calculatePrice(cartItems));
				return cartRepository.save(cart);
			}
			/**
			 * May need to throw an exception
			 * 
			 */
			log.info("Cart Already Contains the Game ...");
			return cart;
		} else {
			Set<String> cartItems = new HashSet<>();
			Set<String> selectedCartItems = new HashSet<>();
			cartItems.add(newCartItem);
			selectedCartItems.add(newCartItem);
			cart = Cart.builder().userId(userId).cartItems(cartItems).selectedCartItems(selectedCartItems)
					.totalPrice(calculatePrice(selectedCartItems)).build();
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
		Cart cart;
		if (userCartOptional.isPresent()) {
			cart = userCartOptional.get();
			Set<String> cartItems = cart.getCartItems();
			Set<String> selectedCartItems = cart.getSelectedCartItems();
			if (cartItems.contains(gameId)) {
				if (selectedCartItems.contains(gameId)) {
					selectedCartItems.remove(gameId);
					cart.setSelectedCartItems(selectedCartItems);
				}
				cartItems.remove(gameId);
				cart.setCartItems(cartItems);
				cart.setTotalPrice(calculatePrice(selectedCartItems));
				return cartRepository.save(cart);
			}
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
		Cart cart;
		if (userCartOptional.isPresent()) {
			cart = userCartOptional.get();
			Set<String> cartItems = cart.getCartItems();
			Set<String> selectedCartItems = cart.getSelectedCartItems();
			if (cartItems.contains(gameId)) {
				if (selectedCartItems.contains(gameId)) {
					selectedCartItems.remove(gameId);
					cart.setSelectedCartItems(selectedCartItems);
				} else {
					selectedCartItems.add(gameId);
					cart.setSelectedCartItems(selectedCartItems);
				}
				cart.setTotalPrice(calculatePrice(selectedCartItems));
				return cartRepository.save(cart);
			}
		} else {
			throw new NoCartAvailableException(USER_CART_IS_EMPTY);
		}
		return userCartOptional.get();
	}

	@Override
	public CartValueResponse getCartItems(Integer userId) {
		log.info("Entering getCartItems ...");
		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new NoCartAvailableException(USER_CART_IS_EMPTY));
		CartValueResponse cartValueResponse = new CartValueResponse(cart.getSelectedCartItems(), cart.getTotalPrice());
		log.info("Leaving getCartItems ...");
		return cartValueResponse;
	}
	
	public ResponseEntity<?> retryFallback(Exception ex) {
		log.info("Entering Retry Fallback Method of Cart Service ...");
		log.error(ex.getMessage());
		return new ResponseEntity<>("Please Try Again Later...",HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
