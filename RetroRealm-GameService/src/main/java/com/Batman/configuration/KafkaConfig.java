package com.Batman.configuration;

import java.net.SocketTimeoutException;
import java.util.HashMap;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import com.Batman.constants.KafkaConstants;
import com.Batman.dto.events.DiscountPlacedEvent;
import com.Batman.exception.wrapper.GameNotFoundException;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableKafka
@Slf4j
public class KafkaConfig {
	
	@Value("${kafka.host}")
	private String host;

	
	@Bean
	ConsumerFactory<String, DiscountPlacedEvent> getConsumerFactory(){
		
		HashMap<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,host);
		config.put(ConsumerConfig.GROUP_ID_CONFIG,KafkaConstants.GROUP_ID);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS,"false");
		config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.Batman.dto.events.DiscountPlacedEvent");
		config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		
		
		return new DefaultKafkaConsumerFactory<>(config);
		
	}
	
	@Bean
	ConcurrentKafkaListenerContainerFactory<String, DiscountPlacedEvent> kafkaListenerContainerFactory(){
		ConcurrentKafkaListenerContainerFactory<String, DiscountPlacedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(getConsumerFactory());
		factory.setCommonErrorHandler(errorHandler());
		factory.getContainerProperties().setAckMode(AckMode.RECORD);
		return factory;
	}
	
	
	@Bean
    DefaultErrorHandler errorHandler() {
        BackOff fixedBackOff = new FixedBackOff(KafkaConstants.INTERVAL, KafkaConstants.MAX_FAILURE);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, e) -> {
        	log.error("Consumer Record {} Failed to Process Because of this exception {}",consumerRecord.toString(),e.getClass().getName());
        }, fixedBackOff);
        errorHandler.addRetryableExceptions(SocketTimeoutException.class,RuntimeException.class);
        errorHandler.addNotRetryableExceptions(NullPointerException.class,GameNotFoundException.class,CallNotPermittedException.class);
        return errorHandler;
    }
	
	

}
