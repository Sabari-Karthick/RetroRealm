package com.Batman.service.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.Batman.dto.cart.CartRequestDto;
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

@Service("Cart Service")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartService implements ICartService {

	private final ICartRespository cartRepository;

	private final GameFeignClinet gameFeignClinet;
	
	private static final String SERVICE_NAME = "cartService";

	@Override
	public Cart addToCart(CartRequestDto cartRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InputFieldException(bindingResult.getFieldError().getDefaultMessage());
		}

		Integer userId = cartRequest.getUserId();
		Integer newCartItem = cartRequest.getGameId();

		Optional<Cart> userCartOptional = cartRepository.findByUserId(userId);
		Cart cart;
		if (userCartOptional.isPresent()) {
			cart = userCartOptional.get();
			Set<Integer> cartItems = cart.getCartItems();
			Set<Integer> selectedCartItems = cart.getSelectedCartItems();
			if (!cartItems.contains(newCartItem)) {
				cartItems.add(newCartItem);
				selectedCartItems.add(newCartItem);
				cart.setCartItems(cartItems);
				cart.setSelectedCartItems(selectedCartItems);
				cart.setTotalPrice(calculatePrice(cartItems));
				return cartRepository.save(cart);
			}
		} else {
			Set<Integer> cartItems = new HashSet<>();
			Set<Integer> selectedCartItems = new HashSet<>();
			cartItems.add(newCartItem);
			selectedCartItems.add(newCartItem);
			cart = Cart.builder().userId(userId).cartItems(cartItems).selectedCartItems(selectedCartItems)
					.totalPrice(calculatePrice(selectedCartItems)).build();
			return cartRepository.save(cart);
		}
		return userCartOptional.get();
	}

	@Override
	public Cart getCartOfUser(Integer userId) {
		log.info("Entering Get Cart of User {} ...",userId);
		/**
		 * 
		 * Consideration Needed whether to store total amount 
		 * and Do we need to update the cart when discount is applied
		 * and whether an notification needs to be send when cart price is dropped
		 */
		
		
		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new NoCartAvailableException("USER_CART_IS_EMPTY"));
		log.info("Leaving Get Cart of User {} ...",userId);
		return cart;
	}

	@Override
	public Cart removeItemFromCart(Integer userId, Integer gameId) {
		Optional<Cart> userCartOptional = cartRepository.findByUserId(userId);
		Cart cart;
		if (userCartOptional.isPresent()) {
			cart = userCartOptional.get();
			Set<Integer> cartItems = cart.getCartItems();
			Set<Integer> selectedCartItems = cart.getSelectedCartItems();
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
			throw new NoCartAvailableException("USER_CART_IS_EMPTY");
		}
		return userCartOptional.get();
	}

	@Retry(name = SERVICE_NAME,fallbackMethod = "retryFallback")
	private Double calculatePrice(Set<Integer> gameIds) {
		log.info("Sending Get Total Price Request For Game Service ...");
		 Double totalPrice = gameFeignClinet.getTotalPrice(gameIds);
		 log.info("Leaving Get Total Price Request For Game Service ...");
		 return totalPrice;
	}

	@Override
	public Cart updateSelectedItemsCart(Integer userId, Integer gameId) {
		Optional<Cart> userCartOptional = cartRepository.findByUserId(userId);
		Cart cart;
		if (userCartOptional.isPresent()) {
			cart = userCartOptional.get();
			Set<Integer> cartItems = cart.getCartItems();
			Set<Integer> selectedCartItems = cart.getSelectedCartItems();
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
			throw new NoCartAvailableException("USER_CART_IS_EMPTY");
		}
		return userCartOptional.get();
	}

	@Override
	public CartValueResponse getCartItems(Integer userId) {
		log.info("Entering getCartItems ...");
		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new NoCartAvailableException("USER_CART_IS_EMPTY"));
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
