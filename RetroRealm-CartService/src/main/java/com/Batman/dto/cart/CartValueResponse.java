package com.Batman.dto.cart;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class CartValueResponse {
	Set<String> cartItems;
	Double totalPrice;
}
