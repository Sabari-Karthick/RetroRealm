package com.Batman.dto.cart;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CartRequestDto {
	@NotNull(message = "User Id cannot Be Empty")
	private Integer userId;
	
	@NotNull(message = "Game Id cannot be Empty")
	private Integer  gameId;

}
