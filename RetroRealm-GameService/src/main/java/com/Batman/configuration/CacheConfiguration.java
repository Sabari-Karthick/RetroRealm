package com.Batman.configuration;

import java.time.Duration;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import static com.Batman.constants.GameConstants.GAME_OWNER;

@Configuration
public class CacheConfiguration {

	@Bean
	RedisCacheConfiguration redisCacheConfiguration() { // Default Configuration for Redis-cache
		return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(60)).disableCachingNullValues()
				.serializeValuesWith(RedisSerializationContext.SerializationPair
						.fromSerializer(new GenericJackson2JsonRedisSerializer()));

	}

	@Bean
	RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() { // Cache Specific Configurations
		return builder -> builder.withCacheConfiguration(GAME_OWNER,
				RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(300)));
	}

//	RedisSerializer<Object> redisSerializer() {
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,
//				JsonTypeInfo.As.PROPERTY);
//		return new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
//	}

}
