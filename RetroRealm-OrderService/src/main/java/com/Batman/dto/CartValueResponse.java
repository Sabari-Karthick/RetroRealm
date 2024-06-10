package com.Batman.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;



@AllArgsConstructor
@Getter
public class CartValueResponse {
	Set<Integer> cartItems;
	Double totalPrice;
}
