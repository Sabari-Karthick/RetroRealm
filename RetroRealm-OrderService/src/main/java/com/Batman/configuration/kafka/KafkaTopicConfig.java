package com.Batman.configuration.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import static com.Batman.constants.KafkaConstants.*;

@Configuration
public class KafkaTopicConfig {

	@Bean
	KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, HOST);
		return new KafkaAdmin(configs);
	}
	
	 @Bean
	     NewTopic topic() {
	        return new NewTopic(TOPIC, 1, (short) 1);
	    }

}
