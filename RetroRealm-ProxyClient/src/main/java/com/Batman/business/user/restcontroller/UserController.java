package com.Batman.business.user.restcontroller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Batman.business.user.dto.RegistrationRequest;
import com.Batman.business.user.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
	 
	private final UserService userService;
	
	@GetMapping("/profile")
	public String getMethodName() {
		return userService.name();
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest user) {
		
		
		return ResponseEntity.ok(userService.register(user).getBody());
	}
	
	

}
