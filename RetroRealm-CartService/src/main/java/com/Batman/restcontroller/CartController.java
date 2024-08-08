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

import com.Batman.dto.cart.CartRequestDto;
import com.Batman.entity.Cart;
import com.Batman.service.ICartService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
	
	private final  ICartService cartService;
	
	private static final String SERVICE_NAME = "cartService";
	
	@PostMapping("/add")
	@RateLimiter(name = SERVICE_NAME)
	public ResponseEntity<?> addItemToCart(@Valid @RequestBody final CartRequestDto cartRequest,BindingResult bindingResult) {
		Cart cart = cartService.addToCart(cartRequest, bindingResult);
		return new ResponseEntity<Cart>(cart,HttpStatus.CREATED);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getUserCart(@PathVariable("userId") @NotNull(message="user Id cannot be empty") final Integer userId){
		return ResponseEntity.ok(cartService.getCartOfUser(userId));
	}
	
	@GetMapping("/value/{userId}")
	public ResponseEntity<?> getUserCartValue(@PathVariable("userId") @NotNull(message="user Id cannot be empty")  final Integer userId){
		 return ResponseEntity.ok(cartService.getCartItems(userId));
	}
	
	@PatchMapping("user/{userId}/remove/{gameId}")
	public ResponseEntity<?> removeItemFromCart(@PathVariable("userId")  @NotNull(message="user Id cannot be empty") final Integer userId,@PathVariable("gameId")  @NotNull(message="game Id cannot be empty") final Integer gameId){
		return ResponseEntity.ok(cartService.removeItemFromCart(userId,gameId));
	}
	
	
	@PutMapping("user/{userId}/update/{gameId}")
	public  ResponseEntity<?> updateCart(@PathVariable("userId")final  @NotNull(message="user Id cannot be empty")  Integer userId,@PathVariable("gameId")  @NotNull(message="game Id cannot be empty")  final Integer gameId){
		return ResponseEntity.ok(cartService.updateSelectedItemsCart(userId,gameId));
	}
	
	
	
	
	

}
