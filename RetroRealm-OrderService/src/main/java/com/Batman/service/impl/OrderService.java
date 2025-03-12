package com.Batman.service.impl;

import com.Batman.constants.KafkaConstants;
import com.Batman.dto.CartValueResponse;
import com.Batman.dto.OrderDetails;
import com.Batman.dto.OrderRequest;
import com.Batman.entity.Order;
import com.Batman.enums.OrderStatus;
import com.Batman.exception.wrapper.InputFieldException;
import com.Batman.exception.wrapper.InvalidCartDetailsException;
import com.Batman.exception.wrapper.RecordNotAvailableException;
import com.Batman.feign.CartFeignClient;
import com.Batman.repository.IOrderRepository;
import com.Batman.service.IOrderService;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;

    private final CartFeignClient cartFeignClient;

    private final KafkaTemplate<String, OrderDetails> kafkaTemplate;

    private static final String SERVICE_NAME = "order-service";

    @Override
    public Order placeOrder(OrderRequest orderRequest, BindingResult bindingResult) {
        log.info("Entering Place Order ...");
        if (bindingResult.hasErrors()) {
            log.error("Input Field Invalid Error ...");
            FieldError fieldError = bindingResult.getFieldError();
            String errorMessage = (fieldError != null) ? fieldError.getDefaultMessage() : "Invalid input field";
            throw new InputFieldException(errorMessage);
        }

        Integer userId = orderRequest.getUserId();
        log.debug("Placing User Id for User Id :: {}", userId);

        CartValueResponse userCartValue = fetchCartDetails(userId);

        validateOrderDetailsWithCartDetails(orderRequest, userCartValue);

        Order orderEntity = Order.builder().orderItems(orderRequest.getGameIds()).userId(orderRequest.getUserId())
                .orderPrice(orderRequest.getTotalPrice()).paymentType(orderRequest.getPaymentType()).orderStatus(OrderStatus.INITIATED).orderType(orderRequest.getOrderType()).build();

        Order order = orderRepository.save(orderEntity);
        log.info("Order is Placed with status {} with Id ::{}", order.getOrderStatus(), order.getOrderId());
        log.info("Sending Order Placed Event ...");


        OrderDetails orderDetails = OrderDetails.builder().gameIds(order.getOrderItems())
                .orderId(order.getOrderId()).orderStatus(order.getOrderStatus().toString())
                .totalPrice(order.getOrderPrice()).userId(order.getUserId()).paymentType(order.getPaymentType()).orderType(order.getOrderType()).build();

        //Assuming the Payment Details are already available
        //If no this event no needs to be published
        CompletableFuture<SendResult<String, OrderDetails>> future = kafkaTemplate.send(frameProducerRecord(orderDetails));
        boolean done = future.isDone();
        log.info("Order Placed Event is Published :: {}", done);
        return order;
    }

    private ProducerRecord<String, OrderDetails> frameProducerRecord(OrderDetails orderDetails) {
        log.info("Framing Producer Record for Order ID {} ...", orderDetails.getOrderId());
        ProducerRecord<String, OrderDetails> producerRecord = new ProducerRecord<>(KafkaConstants.TOPIC, orderDetails.getOrderId().toString(), orderDetails);
        log.info("Producer Record is Framed for Order ID {} with Key {}...", orderDetails.getOrderId(), producerRecord.key());
        return producerRecord;
    }

    private void validateOrderDetailsWithCartDetails(OrderRequest orderRequest, CartValueResponse userCartValue) {
        if (!userCartValue.getCartItems().equals(orderRequest.getGameIds())) {
            log.error("Invalid Cart Error Game Ids are not matching.. ");
            throw new InvalidCartDetailsException("INVALID_CART_ITEM_DETAILS");
        }

        if (!userCartValue.getTotalPrice().equals(orderRequest.getTotalPrice())) {
            log.error("Invalid Cart Error Game Ids are not matching.. ");
            throw new InvalidCartDetailsException("INVALID_CART_ITEM_DETAILS");
        }

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
        log.info("Entering Get Order By Id in Order Service...");
        log.debug("Order Id :: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RecordNotAvailableException("ORDER_NOT_FOUND_FOR_ID"));
        log.info("Leaving Get Order By Id in Order Service...");
        return order;
    }

    @Retry(name = SERVICE_NAME, fallbackMethod = "retryFallback")
    private CartValueResponse fetchCartDetails(Integer userId) {
        log.info("Entering Get Cart Response ...");
        CartValueResponse cartValueResponse = cartFeignClient.getUserCartValue(userId);
        if (Objects.isNull(cartValueResponse))
            throw new InvalidCartDetailsException("CART_DETAILS_NOT_FOUND");
        log.info("Leaving Get Cart Response ...");
        return cartValueResponse;
    }

    public ResponseEntity<?> retryFallback(Exception ex) {
        log.info("Entering Retry Fallback Method of Order Service ...");
        log.error(ex.getMessage());
        return new ResponseEntity<>("Cannot Make Order, Please Try Again Later...", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
