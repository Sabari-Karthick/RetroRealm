package com.Batman.service;

import org.springframework.validation.BindingResult;

import com.Batman.dto.cart.CartRequestDto;
import com.Batman.entity.Cart;

public interface ICartService {
       Cart addToCart(CartRequestDto cartRequest,BindingResult bindingResult);
       Cart getCartOfUser(Integer userId);
       Cart removeItemFromCart(Integer userId,Integer gameId);
}
