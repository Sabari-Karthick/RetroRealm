package com.Batman.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.Batman.dto.CartValueResponse;
import com.Batman.dto.OrderRequest;
import com.Batman.entity.Order;
import com.Batman.enums.OrderStatus;
import com.Batman.exception.wrapper.InputFieldException;
import com.Batman.exception.wrapper.InvalidCartDetails;
import com.Batman.exception.wrapper.RecordNotAvailableException;
import com.Batman.feign.CartFeignClient;
import com.Batman.repository.IOrderRepository;
import com.Batman.service.IOrderService;

import lombok.RequiredArgsConstructor;

@Service("Order Service")
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements IOrderService {

	private final IOrderRepository orderRepository;

	private final CartFeignClient cartFeignClient;

//	private final PaymentFeignClient paymentFeignClient;

	@Override
	public String placeOrder(OrderRequest orderRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InputFieldException(bindingResult.getFieldError().getDefaultMessage());
		}
		CartValueResponse userCartValue = cartFeignClient.getUserCartValue(orderRequest.getUserId());
		if (!userCartValue.getCartItems().equals(orderRequest.getGameIds())
				|| !userCartValue.getTotalPrice().equals(orderRequest.getTotalPrice())) {
			throw new InvalidCartDetails("INVALID_CART_DETAILS");
		}
		Order order = Order.builder().orderItems(orderRequest.getGameIds()).userId(orderRequest.getUserId())
				.orderPrice(orderRequest.getTotalPrice()).paymentType(orderRequest.getPaymentType()).build();
//		if(paymentFeignClient.pay(Double amount)) {
//			order.setOrderStatus(OrderStatus.COMPLETED);
//		orderRepository.save(order);
//		return "Purchase Success";
//		}else {
//			order.setOrderStatus(OrderStatus.FAILED);
//		orderRepository.save(order);
//		return "Purchase Failed";
//		}
		order.setOrderStatus(OrderStatus.COMPLETED);
		orderRepository.save(order);
		return "Purchase Success";
	}

	@Override
	public List<Order> getOrdersOfUser(Integer userId) {
		List<Order> userOrders = orderRepository.findAllByUserId(userId);
		if (userOrders.isEmpty()) {
			throw new RecordNotAvailableException("USER_HAVE_NO_ORDERS");
		}
		return userOrders;
	}

	@Override
	public Order getOrderById(Integer orderId) {
		return orderRepository.findById(orderId)
				.orElseThrow(() -> new RecordNotAvailableException("ORDER_NOT_FOUND_FOR_ID"));
	}

}
