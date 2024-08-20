package com.Batman.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderDetailsDto {
 
	  private Integer orderId;
	  
	  private String userMail;
	  
	  private List<String> gameNames;
	  
	  private String orderStatus;
	  
	  private Double totalPrice;
	
}
