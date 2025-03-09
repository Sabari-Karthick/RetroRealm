package com.Batman.configuration.kafka;

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

import com.Batman.dto.OrderDetails;

@Configuration
public class KafkaConfig {
	
	
	@Value("${kafka.host}")
	private String host;

	@Bean
	ProducerFactory<String, OrderDetails> producerFactory() {
		Map<String, Object> configuration = new HashMap<>();

		configuration.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,host);
		configuration.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configuration.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		return new DefaultKafkaProducerFactory<>(configuration);
	}

	@Bean
	KafkaTemplate<String, OrderDetails> getTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

}
