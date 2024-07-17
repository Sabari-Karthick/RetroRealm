package com.Batman.dto.discount;

import java.util.Set;

import lombok.Data;

@Data
public class DiscountDto {
	
	private Set<Integer> gameIds;
	
	private Double discountValue;
 
}
