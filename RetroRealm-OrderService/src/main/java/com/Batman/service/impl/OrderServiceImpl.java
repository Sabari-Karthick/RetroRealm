package com.Batman.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.Batman.constants.KafkaConstants;
import com.Batman.dto.CartValueResponse;
import com.Batman.dto.OrderDetailsDto;
import com.Batman.dto.OrderRequest;
import com.Batman.dto.PaymentRequest;
import com.Batman.entity.Order;
import com.Batman.enums.OrderStatus;
import com.Batman.enums.PaymentType;
import com.Batman.exception.wrapper.InputFieldException;
import com.Batman.exception.wrapper.InvalidCartDetailsException;
import com.Batman.exception.wrapper.RecordNotAvailableException;
import com.Batman.feign.CartFeignClient;
import com.Batman.feign.GameFeignClient;
import com.Batman.feign.PaymentFeignClient;
import com.Batman.repository.IOrderRepository;
import com.Batman.service.IOrderService;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
/**
 * 
 */
/**
 * 
 */
@Service("Order Service")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements IOrderService {

	private final IOrderRepository orderRepository;

	private final CartFeignClient cartFeignClient;

	private final PaymentFeignClient paymentFeignClient;
	
	private final GameFeignClient gameFeignClient;
	
	private final KafkaTemplate<String,OrderDetailsDto> kafkaTemplate;

	private static final String SERVICE_NAME = "orderService";

	@Override
	public Order placeOrder(OrderRequest orderRequest, BindingResult bindingResult) {
		log.info("Entering Place Order ...");
		if (bindingResult.hasErrors()) {
			log.error("Input Field Invalid Error ...");
			throw new InputFieldException(bindingResult.getFieldError().getDefaultMessage());
		}

		CartValueResponse userCartValue = getCartResponse(orderRequest.getUserId());

		if (!userCartValue.getCartItems().equals(orderRequest.getGameIds())
				|| !userCartValue.getTotalPrice().equals(orderRequest.getTotalPrice())) {
			log.error("Invalid Cart Error.. ");
			throw new InvalidCartDetailsException("INVALID_CART_DETAILS");
		}

		PaymentType paymentType = orderRequest.getPaymentType();
		Double totalPrice = orderRequest.getTotalPrice();

		Order orderEntity = Order.builder().orderItems(orderRequest.getGameIds()).userId(orderRequest.getUserId())
				.orderPrice(totalPrice).build();

		List<Integer> paymentIds = new ArrayList<>();
		PaymentRequest paymentRequest = PaymentRequest.builder().paymentType(paymentType).amount(totalPrice).build();

		ResponseEntity<?> paymentResponse = null;
		try {
			paymentResponse = makePayment(paymentRequest);
			Integer paymentId = (Integer) paymentResponse.getBody();
			paymentIds.add(paymentId);
			if (paymentResponse.getStatusCode().is5xxServerError()) {
				log.info("Payment Request For the order is Failed ...");
				orderEntity.setOrderStatus(OrderStatus.FAILED);
			} else {
				log.info("Payment Request For the order is Success ...");
				orderEntity.setOrderStatus(OrderStatus.COMPLETED);
			}
		} catch (Exception e) {
			log.error("Error Occured While Creating the Order...");
			log.error(e.getMessage());
			orderEntity.setOrderStatus(OrderStatus.FAILED);
		}

		 Order order = orderRepository.save(orderEntity);
		 log.info("Order is Placed with status {} with Id ::{}",order.getOrderStatus(),order.getOrderId());
		 log.info("Sending Order Placed Event ...");
		 
		 
		 List<String> gameNames = gameFeignClient.getGameNamesByGameIds(order.getOrderItems());
		 OrderDetailsDto orderDetailsDto = OrderDetailsDto.builder().gameNames(gameNames).orderId(order.getOrderId()).orderStatus(order.getOrderStatus().toString()).totalPrice(order.getOrderPrice()).userMail("sabariks2003@gmail.com").build();
		 
         kafkaTemplate.send(KafkaConstants.TOPIC, orderDetailsDto);
         return order;
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

	@Retry(name = SERVICE_NAME, fallbackMethod = "retryFallback")
	private CartValueResponse getCartResponse(Integer userId) {
		log.info("Entering Get Cart Response ...");
		CartValueResponse cartValueResponse = cartFeignClient.getUserCartValue(userId);
		if (Objects.isNull(cartValueResponse))
			throw new InvalidCartDetailsException("CART_DETAILS_NOT_FOUND");
		log.info("Leaving Get Cart Response ...");
		return cartValueResponse;
	}

	/**
	 * This logic is not completed update
	 * 
	 * @param paymentRequest
	 * @return
	 */
	private ResponseEntity<?> makePayment(PaymentRequest paymentRequest) {

		log.info("Entering make Payment ...");
		ResponseEntity<?> payResponse = paymentFeignClient.pay(paymentRequest);
		if (Objects.isNull(payResponse))
			throw new InvalidCartDetailsException("CART_DETAILS_NOT_FOUND");
		log.info("Leaving make Payment...");
		return payResponse;
	}

	public ResponseEntity<?> retryFallback(Exception ex) {
		log.info("Entering Retry Fallback Method of Order Service ...");
		log.error(ex.getMessage());
		return new ResponseEntity<>("Cannot Make Order, Please Try Again Later...", HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
