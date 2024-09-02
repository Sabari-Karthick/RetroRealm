package com.batman.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import lombok.extern.slf4j.Slf4j;

import static com.batman.constants.KafkaConstants.*;

@Configuration
@Slf4j
public class KafkaTopicConfig {

	@Bean
	KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, HOST);
		return new KafkaAdmin(configs);
	}

	@Bean
	NewTopic topic() {
		log.info("Entering Topic Creation ...");
		return new NewTopic(TOPIC, 1, (short) 1);
	}

}