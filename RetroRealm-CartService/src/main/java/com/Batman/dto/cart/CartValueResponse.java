package com.Batman.dto.cart;

import java.util.List;

import com.Batman.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class CartValueResponse {
	List<CartItem> cartItems;
	Double totalPrice;
}
