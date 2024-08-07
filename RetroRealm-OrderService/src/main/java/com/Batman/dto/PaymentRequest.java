package com.Batman.dto;

import com.Batman.enums.PaymentType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequest {
	
	private Double amount;
	private PaymentType paymentType;
	

}
