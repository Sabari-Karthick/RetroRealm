package com.Batman.dto;

import java.util.Set;

import com.Batman.enums.PaymentType;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {

	@NotNull(message = "User Id cannot be Empty")
	private Integer userId;

	@NotNull(message = "Game Ids cannot be Empty")
	private Set<Integer> gameIds;
	@NotNull(message = "Price cannot be Empty")
	private Double totalPrice;
	@NotNull(message = "Provide a Payment Type")
	private PaymentType paymentType;

}
