package com.Batman.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.Batman.dto.CartValueResponse;
import com.Batman.dto.OrderRequest;
import com.Batman.dto.PaymentRequest;
import com.Batman.entity.Order;
import com.Batman.enums.OrderStatus;
import com.Batman.enums.PaymentType;
import com.Batman.exception.wrapper.InputFieldException;
import com.Batman.exception.wrapper.InvalidCartDetails;
import com.Batman.exception.wrapper.RecordNotAvailableException;
import com.Batman.feign.CartFeignClient;
import com.Batman.feign.PaymentFeignClient;
import com.Batman.repository.IOrderRepository;
import com.Batman.service.IOrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("Order Service")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements IOrderService {

	private final IOrderRepository orderRepository;

	private final CartFeignClient cartFeignClient;

	private final PaymentFeignClient paymentFeignClient;

	@Override
	public Order placeOrder(OrderRequest orderRequest, BindingResult bindingResult) {
		log.info("Entering Place Order ...");
		if (bindingResult.hasErrors()) {
			log.error("Input Field Invalid Error ...");
			throw new InputFieldException(bindingResult.getFieldError().getDefaultMessage());
		}
		
		CartValueResponse userCartValue = cartFeignClient.getUserCartValue(orderRequest.getUserId());
		
		if (!userCartValue.getCartItems().equals(orderRequest.getGameIds())
				|| !userCartValue.getTotalPrice().equals(orderRequest.getTotalPrice())) {
			log.error("Invalid Car Error.. ");
			throw new InvalidCartDetails("INVALID_CART_DETAILS");
		}
		
		PaymentType paymentType = orderRequest.getPaymentType();
		Double totalPrice = orderRequest.getTotalPrice();
		
		Order order = Order.builder().orderItems(orderRequest.getGameIds()).userId(orderRequest.getUserId())
				.orderPrice(totalPrice).build();
		
		List<Integer> paymentIds = new ArrayList<>();
        PaymentRequest paymentRequest = PaymentRequest.builder().paymentType(paymentType).amount(totalPrice).build();
        
        ResponseEntity<?> paymentResponse = paymentFeignClient.pay(paymentRequest);
        Integer paymentId = (Integer) paymentResponse.getBody();
        paymentIds.add(paymentId);
        
        if(paymentResponse.getStatusCode().is5xxServerError()) {
        	log.info("Payment Request For the order is Failed ...");
        	order.setOrderStatus(OrderStatus.FAILED);
        }else {
        	log.info("Payment Request For the order is Success ...");
        	order.setOrderStatus(OrderStatus.COMPLETED);
        }
		return orderRepository.save(order);
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
