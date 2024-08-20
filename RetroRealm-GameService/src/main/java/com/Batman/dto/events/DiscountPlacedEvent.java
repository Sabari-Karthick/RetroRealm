package com.Batman.dto.events;

import java.util.Set;

import lombok.Data;


@Data
public class DiscountPlacedEvent {
	Set<Integer> gameIds;
	Double discountValue;
}
