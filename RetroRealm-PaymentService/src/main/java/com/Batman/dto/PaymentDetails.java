package com.Batman.dto;

import com.Batman.enums.PaymentStatus;
import com.Batman.enums.PaymentType;

import lombok.Data;

@Data
public class PaymentDetails {
	
	private Integer paymentId;
	
	private Integer orderId;
	
	private PaymentType paymentType;
	
	private PaymentStatus paymentStatus;
	
	private Double amount;

}
