//package com.Batman.restcontroller;
//
//import org.springframework.web.bind.annotation.RestController;
//
//import com.Batman.dto.AuthenticationRequest;
//import com.Batman.service.AuthenticationService;
//
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//
//@RestController("/api/v1/auth")
//@RequiredArgsConstructor
//public class AuthenticationController {
//	
//	private final AuthenticationService authenticationService;
//	
//	@PostMapping("/login")
//	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
//		return new ResponseEntity<>(authenticationService.login(request), HttpStatus.OK);
//	}
//	
//
//}
