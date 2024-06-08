package com.Batman.business.user.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.Batman.business.user.dto.RegistrationRequest;

@FeignClient(name = "USER-SERVICE",contextId = "userClientService",path = "/api/v1/users")
public interface UserService {
   
	   @PostMapping("/register")
	   ResponseEntity<String> register(@RequestBody RegistrationRequest request);
	   
	   @GetMapping("/profile")
	   String name();
}
