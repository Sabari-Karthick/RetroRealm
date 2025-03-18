package com.Batman.service.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.Batman.constants.KafkaConstants;
import com.Batman.dto.OrderDetails;
import com.Batman.dto.PaymentDetails;
import com.Batman.entity.Payment;
import com.Batman.enums.PaymentStatus;
import com.Batman.enums.PaymentType;
import com.Batman.helper.CommonMapper;
import com.Batman.repository.IPaymentRepository;
import com.Batman.service.IPaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentService implements IPaymentService {

	private final IPaymentRepository paymentRepository;

	private final KafkaTemplate<String, PaymentDetails> kafkaTemplate;

	private final CommonMapper commonMapper;

	@Override
	public Payment pay(Double amount, PaymentType paymentType, String orderId) {
		ZonedDateTime initiatedTime = ZonedDateTime.now(ZoneId.systemDefault());
		log.info("Payment Request Arrived for amount {} with Type {} initated at {}", amount, paymentType,
				initiatedTime);

		Payment payment = new Payment();
		try {
			// Actual Payment Logic Needs to be implemented
			Thread.sleep(10000);
			log.info("Making Payment... ");
			payment.setPaymentStatus(PaymentStatus.COMPLETED);
		} catch (Exception ignore) {
			Thread.currentThread().interrupt();
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
	public void processPayment(@Payload OrderDetails orderDetails,
			@Header(value = KafkaHeaders.RECEIVED_PARTITION, required = false) int partition,
			@Header(value = KafkaHeaders.RECEIVED_KEY, required = false) String key,
			ConsumerRecord<String, OrderDetails> consumerRecord) { // Need to refractor with required Metadata
		log.info("Entering Process Payment ....");

		if (Objects.isNull(orderDetails)) {
			log.error("Received null payload for topic: {}, key : {}, partition: {}, offset: {}",
					KafkaConstants.ORDERS_TOPIC, key, partition, consumerRecord.offset());

			sendToDLT(consumerRecord, "Null payload received");
			return;
		}

		String orderId = orderDetails.getOrderId();
		Integer userId = orderDetails.getUserId();
		log.info("Processing Order Id :: {} For User :: {}", orderId, userId);

		// Needs to Decide How the Payment Details going to be do
		Payment payment = pay(orderDetails.getTotalPrice(), orderDetails.getPaymentType(), orderId);
		sendPaymentEvent(payment);

		log.info("Leaving Process Payment ....");
	}

	/*
	 * Needs To Decide the DLT Configurations
	 * 
	 */
	private void sendToDLT(ConsumerRecord<String, OrderDetails> consumerRecord, String message) {
		log.info("Entering Payment Service sendtoDLT ...");
		log.info(consumerRecord + " " + message);
		log.info("Leaving Payment Service sendtoDLT ...");
	}

	private void sendPaymentEvent(Payment payment) {

		log.info("Entering Send Payment Event ....");

		log.info("Payment ID :: {}, Payment Status :: {}", payment.getPaymentId(), payment.getPaymentStatus());

		PaymentDetails paymentDetails = commonMapper.convertToResponse(payment, PaymentDetails.class);

		CompletableFuture<SendResult<String, PaymentDetails>> completableFuture = kafkaTemplate
				.send(frameProducerMessage(paymentDetails));
		completableFuture.whenComplete((result, ex) -> {
			if (Objects.isNull(ex)) {
				log.info("Successfully sent payment details to Kafka topic: {}, partition: {}, offset: {}, key: {}",
						result.getRecordMetadata().topic(), result.getRecordMetadata().partition(),
						result.getRecordMetadata().offset(), result.getProducerRecord().key());
			} else {
				log.error("Failed to send payment details to Kafka topic: {}, payment: {}",
						KafkaConstants.PAYMENT_SETVICE_TOPIC, payment, ex);
			}
		});
		log.info("Leaving Send Payment Event ....");

	}

	private ProducerRecord<String, PaymentDetails> frameProducerMessage(PaymentDetails paymentDetails) {
		log.info("Framing Producer Record for Payment ID {} for Order :: {} ...", paymentDetails.getPaymentId(),
				paymentDetails.getOrderId());
		ProducerRecord<String, PaymentDetails> producerRecord = new ProducerRecord<>(
				KafkaConstants.PAYMENT_SETVICE_TOPIC, paymentDetails.getPaymentId().toString(), paymentDetails);
		log.info("Producer Record is Framed for Payment ID {} for Order {} with Key {}...",
				paymentDetails.getPaymentId(), paymentDetails.getOrderId(), producerRecord.key());
		return producerRecord;
	}

}
