package com.Batman.configuration.kafka;

import java.util.HashMap;
import java.util.Map;

import com.Batman.constants.KafkaConstants;
import com.Batman.dto.PaymentDetails;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.Batman.dto.OrderDetails;

@Configuration
@EnableKafka
public class KafkaConfig {
	
	
	@Value("${kafka.host}")
	private String host;

	@Bean
	ProducerFactory<String, OrderDetails> producerFactory() {
		Map<String, Object> configuration = new HashMap<>();

		configuration.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,host);
		configuration.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configuration.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//		configuration.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomOrderPartitioner.class.getName());
		configuration.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, RoundRobinPartitioner.class.getName());

		return new DefaultKafkaProducerFactory<>(configuration);
	}

	@Bean
	KafkaTemplate<String, OrderDetails> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}


	@Bean
	ConsumerFactory<String, PaymentDetails> getConsumerFactory(){

		HashMap<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,host);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstants.GROUP_ID);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.Batman.dto.PaymentDetails");
		config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS,"false");
		config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

		return new DefaultKafkaConsumerFactory<>(config);

	}


	@Bean
	ConcurrentKafkaListenerContainerFactory<String, PaymentDetails> kafkaListenerContainerFactory(){
		ConcurrentKafkaListenerContainerFactory<String, PaymentDetails> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(getConsumerFactory());
		return factory;
	}

}
