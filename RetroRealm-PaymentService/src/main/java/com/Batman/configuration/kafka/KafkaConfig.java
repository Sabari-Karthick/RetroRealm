package com.Batman.configuration.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.Batman.constants.KafkaConstants;
import com.Batman.dto.OrderDetails;
import com.Batman.dto.PaymentDetails;

@Configuration
@EnableKafka
public class KafkaConfig {
	
	
	@Value("${kafka.host}")
	private String host;

	@Bean
	ProducerFactory<String, PaymentDetails> producerFactory() {
		Map<String, Object> configuration = new HashMap<>();

		configuration.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,host);
		configuration.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configuration.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//		configuration.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,RoundRobinPartitioner.class.getName()); //For Setting the Partition Strategy

		return new DefaultKafkaProducerFactory<>(configuration);
	}
	
	@Bean
	ConsumerFactory<String, OrderDetails> getConsumerFactory(){
		
		HashMap<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,host);
		config.put(ConsumerConfig.GROUP_ID_CONFIG,KafkaConstants.GROUP_ID);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS,"false");
		config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.Batman.dto.OrderDetails");
		config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//		config.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, null); // For auto commit offset
//		config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, null); //For Enabling or Dis-abling auto Commit of offset
		
		return new DefaultKafkaConsumerFactory<>(config);
		
	}
	

	@Bean
	ConcurrentKafkaListenerContainerFactory<String, OrderDetails> kafkaListenerContainerFactory(){
		ConcurrentKafkaListenerContainerFactory<String, OrderDetails> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(getConsumerFactory());
		return factory;
	}

	@Bean
	KafkaTemplate<String, PaymentDetails> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

}
