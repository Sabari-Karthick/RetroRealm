package com.Batman.constants;

public class KafkaConstants {
	
	private KafkaConstants() {}

	public static final String TOPIC = "payment-service.payments";
	public static final String ORDERS_TOPIC = "order-service.orders";
	public static final String GROUP_ID = "order-processing-payment-group";
}
