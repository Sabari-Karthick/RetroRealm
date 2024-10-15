package com.Batman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Main application class for the RetroRealm Eureka Service Registry.
 *
 * This application acts as the Eureka Server for service discovery,
 * enabling microservices to register themselves and discover other
 * services in the system.
 * 
 * <p>
 * Annotations used:
 * <ul>
 *   <li>{@code @SpringBootApplication} - Marks this as a Spring Boot application.</li>
 *   <li>{@code @EnableEurekaServer} - Enables the Eureka Server functionality.</li>
 * </ul>
 * </p>
 *
 * @author SK
 */
@SpringBootApplication
@EnableEurekaServer
public class RetroRealmServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetroRealmServiceRegistryApplication.class, args);
	}

}
