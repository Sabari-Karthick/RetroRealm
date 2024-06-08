package com.Batman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RetroRealmGameServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetroRealmGameServiceApplication.class, args);
	}

}
