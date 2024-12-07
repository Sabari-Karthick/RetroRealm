package com.batman.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.batman.dto.events.DiscountPlacedEvent;

@Configuration
public class KafkaConfig {
	
	@Value("${kafka.host}")
	private String host;

	@Bean
	ProducerFactory<String, DiscountPlacedEvent> producerFactory() {
		Map<String, Object> configuration = new HashMap<>();

		configuration.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
		configuration.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configuration.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(configuration);
	}

	@Bean
	KafkaTemplate<String, DiscountPlacedEvent> getTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}


}
