package com.Batman.configuration.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import lombok.extern.slf4j.Slf4j;

import static com.Batman.constants.KafkaConstants.*;

@Configuration
@Slf4j
public class KafkaTopicConfig {
	
	
	@Value("${kafka.host}")
	private String host;

	@Value("${kafka.topic.partitions}")
	private int partitions;

	@Value("${kafka.topic.replication-factor}")
	private short replicationFactor;

	@Bean
	KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, host);
		return new KafkaAdmin(configs);
	}

	@Bean
	NewTopic topic() {
		log.info("Entering Topic Creation ...");
		return new NewTopic(TOPIC, partitions, replicationFactor);
	}

}
