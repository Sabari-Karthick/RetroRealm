package com.Batman.constants;

public class KafkaConstants {
	
	private KafkaConstants() {}

	public static final String TOPIC = "order-service.orders";
	public static final String PAYMENT_TOPIC = "payment-service.payments";
	public static final String GROUP_ID = "payment-processing-order-group";
}
