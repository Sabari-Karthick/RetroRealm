package com.Batman.dto;

import java.util.Set;

import com.Batman.enums.OrderType;
import com.Batman.enums.PaymentType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetails {
 
	  private Integer orderId;
	  
	  private Integer userId;
	  
	  private Set<Integer> gameIds;
	  
	  private String orderStatus;
	  
	  private Double totalPrice;
	  
	  private PaymentType paymentType;

	  private OrderType orderType;
	
}
