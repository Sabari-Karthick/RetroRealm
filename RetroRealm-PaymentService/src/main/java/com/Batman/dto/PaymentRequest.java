package com.Batman.dto;

import com.Batman.enums.PaymentType;

import lombok.Data;

@Data
public class PaymentRequest {
	
	private Double amount;
	private PaymentType paymentType;
	

}
