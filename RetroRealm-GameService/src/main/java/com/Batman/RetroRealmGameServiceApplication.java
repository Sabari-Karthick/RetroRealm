package com.Batman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main application class for the RetroRealm Game Service.
 * This application is responsible for managing game-related operations.
 * It provides functionalities to create, retrieve, update, and delete games.
 *
 * <p>
 * Annotations used:
 * <ul>
 *   <li>{@code @SpringBootApplication} - Marks this as a Spring Boot application.</li>
 *   <li>{@code @EnableDiscoveryClient} - Enables the application to register with a service discovery server.</li>
 *   <li>{@code @EnableCaching} - Enables caching capabilities for the application.</li>
 * </ul>
 * </p>
 *
 * @author SK
 *
 */



@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class RetroRealmGameServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetroRealmGameServiceApplication.class, args);
	}

}