package com.Batman.configuration;

import java.util.HashMap;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.Batman.constants.KafkaConstants;

@Configuration
@EnableKafka
public class KafkaConfig {
	
	
	@Bean
	ConsumerFactory<String, Integer> getConsumerFactory(){
		
		HashMap<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,KafkaConstants.HOST);
		config.put(ConsumerConfig.GROUP_ID_CONFIG,KafkaConstants.GROUP_ID);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		
		return new DefaultKafkaConsumerFactory<>(config);
		
	}
	
	@Bean
	ConcurrentKafkaListenerContainerFactory<String, Integer> kafkaListenerContainerFactory(){
		ConcurrentKafkaListenerContainerFactory<String, Integer> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(getConsumerFactory());
		return factory;
	}

}
