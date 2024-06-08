package com.Batman.restcontroller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Batman.dto.cart.CartRequestDto;
import com.Batman.entity.Cart;
import com.Batman.service.ICartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
	
	private final  ICartService cartService;
	
	@PostMapping("/add")
	public ResponseEntity<?> addItemToCart(@Valid @RequestBody final CartRequestDto cartRequest,BindingResult bindingResult) {
		Cart cart = cartService.addToCart(cartRequest, bindingResult);
		return new ResponseEntity<Cart>(cart,HttpStatus.CREATED);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getUserCart(@PathVariable("userId") final Integer userId){
		return ResponseEntity.ok(cartService.getCartOfUser(userId));
	}
	
	@PatchMapping("user/{userId}/remove/{gameId}")
	public ResponseEntity<?> removeItemFromCart(@PathVariable("userId")final Integer userId,@PathVariable("gameId") final Integer gameId){
		return ResponseEntity.ok(cartService.removeItemFromCart(userId,gameId));
	}
	
	
	@PutMapping("user/{userId}/update/{gameId}")
	public  ResponseEntity<?> updateCart(@PathVariable("userId")final Integer userId,@PathVariable("gameId") final Integer gameId){
		return ResponseEntity.ok(cartService.updateSelectedItemsCart(userId,gameId));
	}
	
	

}
