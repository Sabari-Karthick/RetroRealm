package com.Batman.dto;

import java.util.Set;

import lombok.Data;

@Data
public class OrderDetailsDto {
 
	  private Integer orderId;
	  
	  private String userMail;
	  
	  private Set<Integer> gameIds;
	  
	  private String orderStatus;
	  
	  private Double totalPrice;
	
}
