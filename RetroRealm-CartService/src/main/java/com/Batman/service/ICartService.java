package com.Batman.service;

import org.springframework.validation.BindingResult;

import com.Batman.dto.cart.CartRequest;
import com.Batman.dto.cart.CartValueResponse;
import com.Batman.entity.Cart;

public interface ICartService {
	
	Cart addToCart(CartRequest cartRequest, BindingResult bindingResult);

	Cart getCartOfUser(Integer userId);

	Cart removeItemFromCart(Integer userId, String gameId);

	Cart updateSelectedItemsCart(Integer userId, String gameId);

	CartValueResponse getCartItems(Integer userId);
}
