package com.Batman.constants;

public class KafkaConstants {
	public static final String TOPIC = "discount-service.discount"; //Needs to be moved to external configuration file or properties
	public static final String GROUP_ID = "discount-processing-group";
	public static final Long INTERVAL = 8000L;
	public static final Long MAX_FAILURE = 5L;
}
