package com.Batman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class RetroRealmServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetroRealmServiceRegistryApplication.class, args);
	}

}
