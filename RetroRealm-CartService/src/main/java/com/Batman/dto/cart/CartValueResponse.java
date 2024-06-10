package com.Batman.dto.cart;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class CartValueResponse {
	Set<Integer> cartItems;
	Double totalPrice;
}
