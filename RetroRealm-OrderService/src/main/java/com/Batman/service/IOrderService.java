package com.Batman.service;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.Batman.dto.OrderRequest;
import com.Batman.entity.Order;

public interface IOrderService {
	
	Order placeOrder(OrderRequest orderRequest, BindingResult bindingResult);

	List<Order> getOrdersOfUser(Integer userId);

	Order getOrderById(Integer orderId);
}
