package com.Batman.restcontroller;

import org.springframework.web.bind.annotation.RestController;

import com.Batman.dto.AuthenticationRequest;
import com.Batman.feignclinet.UserFeignClinet;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	
	private final UserFeignClinet feignClinet;
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
		
		return ResponseEntity.ok().build();
	}
	

}
