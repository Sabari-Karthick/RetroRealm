package com.Batman.restcontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Batman.dto.cart.CartRequest;
import com.Batman.service.ICartService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

	private final ICartService cartService;

	/**
	 * Service Name needs to be in an standard across all the services
	 * 
	 */
	private static final String SERVICE_NAME = "cart-service";

	@PostMapping("/add")
	@RateLimiter(name = SERVICE_NAME)
	public ResponseEntity<?> addItemToCart(@Valid @RequestBody final CartRequest cartRequest,
			BindingResult bindingResult) {
		return new ResponseEntity<>(cartService.addToCart(cartRequest, bindingResult), HttpStatus.CREATED);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getUserCart(
			@PathVariable @NotNull(message = "user Id cannot be empty") final Integer userId) {
		return ResponseEntity.ok(cartService.getCartOfUser(userId));
	}

	@GetMapping("/value/{userId}")
	public ResponseEntity<?> getUserCartValue(
			@PathVariable @NotNull(message = "user Id cannot be empty") final Integer userId) {
		return ResponseEntity.ok(cartService.getCartItems(userId));
	}

	@PatchMapping("user/{userId}/remove/{gameId}")
	public ResponseEntity<?> removeItemFromCart(@NotNull(message = "user Id cannot be empty") final Integer userId,
			@PathVariable @NotNull(message = "game Id cannot be empty") final String gameId) {
		return ResponseEntity.ok(cartService.removeItemFromCart(userId, gameId));
	}

	@PutMapping("user/{userId}/update/{gameId}")
	public ResponseEntity<?> updateCart(
			@PathVariable final @NotNull(message = "user Id cannot be empty") Integer userId,
			@PathVariable @NotNull(message = "game Id cannot be empty") final String gameId) {
		return ResponseEntity.ok(cartService.updateSelectedItemsCart(userId, gameId));
	}

}
