package com.batman.dto.events;

import java.util.Set;

import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class DiscountPlacedEvent {
	Set<String> gameIds;
	Double discountValue;
}
