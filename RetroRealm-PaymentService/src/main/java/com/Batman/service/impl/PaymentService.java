package com.Batman.service.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.Batman.constants.KafkaConstants;
import com.Batman.dto.OrderDetails;
import com.Batman.dto.PaymentDetails;
import com.Batman.entity.Payment;
import com.Batman.enums.PaymentStatus;
import com.Batman.enums.PaymentType;
import com.Batman.repository.IPaymentRepository;
import com.Batman.service.IPaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service("payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentService implements IPaymentService{
	
	private final IPaymentRepository paymentRepository;  
	
	private KafkaTemplate<String, PaymentDetails> kafkaTemplate;

	@Override
	public Payment pay(Double amount, PaymentType paymentType,Integer orderId) {
		ZonedDateTime initiatedTime = ZonedDateTime.now(ZoneId.systemDefault());
		log.info("Payment Request Arrived for amount {} with Type {} initated at {}",amount,paymentType,initiatedTime);
		
		Payment payment = new Payment();
		try {
			//Actual Payment Logic Needs to be implemented
			Thread.sleep(10000);
			log.info("Making Payment... ");
            payment.setPaymentStatus(PaymentStatus.COMPLETED);			
		} catch (Exception ignore) {
			log.error("Error While Making Payment ...");
			payment.setPaymentStatus(PaymentStatus.FAILED);			
		}
		ZonedDateTime completedTime = ZonedDateTime.now(ZoneId.systemDefault());
		payment.setIntiatedTime(initiatedTime);
		payment.setCompletedTime(completedTime);
		payment.setPaymentType(paymentType);
		payment.setAmount(amount);
		payment.setOrderId(orderId);
		return paymentRepository.save(payment);
	}
	
	
	@KafkaListener(topics = KafkaConstants.ORDERS_TOPIC, groupId = KafkaConstants.GROUP_ID)
	public void processPayment(@Payload OrderDetails orderDetails,@Header(value = KafkaHeaders.RECEIVED_PARTITION) int partition) {
		log.info("Entering Process Payment ....");
		
		Integer orderId = orderDetails.getOrderId();
		Integer userId = orderDetails.getUserId();
		log.info("Processing Order Id :: {} For User :: {}",orderId,userId);
		
		//Needs to Decide How the Payment Details going to be do 
		Payment payment = pay(orderDetails.getTotalPrice(), orderDetails.getPaymentType(),orderId);
		sendPaymentEvent(payment);
		
		log.info("Leaving Process Payment ....");
	}


	private void sendPaymentEvent(Payment payment) {
		
		log.info("Entering Send Payment Event ....");
		
		log.info("Payment ID :: {}",payment.getPaymentId());
		
		if(-5 > Math.random())
		   kafkaTemplate.send(KafkaConstants.TOPIC,null);
		
		log.info("Leaving  Send Payment Event ....");
		
	}

}
