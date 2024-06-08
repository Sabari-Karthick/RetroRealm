package com.Batman.restcontroller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Batman.dto.RegistrationRequest;
import com.Batman.service.IUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
	
	private final IUserService userService;
	
	@GetMapping("/profile")
	public String getMethodName() {
		log.info("** Getting User Profile **");
		return "Batman";
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest user) {
		
		String message = userService.registerUser(user);
		
		return ResponseEntity.ok(message);
	}
	
	

}
