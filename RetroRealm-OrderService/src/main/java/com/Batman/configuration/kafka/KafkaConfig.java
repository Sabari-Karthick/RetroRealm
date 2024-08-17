package com.Batman.configuration.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.Batman.constants.KafkaConstants;

@Configuration
public class KafkaConfig {

	@Bean
	//ProducerFactory<String, Order> producerFactory() {  /** For Testing **/
		ProducerFactory<String, Integer> producerFactory() {
		Map<String, Object> configuration = new HashMap<>();

		configuration.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.HOST);
		configuration.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configuration.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		return new DefaultKafkaProducerFactory<>(configuration);
	}

	@Bean
	// KafkaTemplate<String, Order> getTemplate() {
	KafkaTemplate<String, Integer> getTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

}
