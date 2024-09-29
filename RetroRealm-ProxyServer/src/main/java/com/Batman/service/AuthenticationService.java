//package com.Batman.service;
//
//import java.util.Map;
//
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.Batman.dto.AuthenticationRequest;
//import com.Batman.dto.AuthenticationResponse;
//import com.Batman.dto.User;
//import com.Batman.feignclinet.UserFeignClinet;
//import com.Batman.security.jwt.JwtProvider;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class AuthenticationService {
//
//	private final UserFeignClinet userFeignClinet;
//
//	private final AuthenticationManager authenticationManager;
//
//	private final JwtProvider jwtProvider;
//
//
//	public AuthenticationResponse login(AuthenticationRequest authRequest) {
//		log.info("Entering Login ...");
//		authenticationManager
//				.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
//		User user = userFeignClinet.getUserByMail(Map.of("userMail",authRequest.getEmail())).orElseThrow(() -> {
//			log.error("USER WITH THIS MAIL NOT FOUND...");
//			return new UsernameNotFoundException("EMAIL_NOT_FOUND");
//		});
//		String token = jwtProvider.createToken(user.getEmail(), user.getRoles().iterator().next().name());
//		AuthenticationResponse response = new AuthenticationResponse();
//		response.setResponse(user);
//		response.setToken(token);
//		log.info("Leaving Login ...");
//		return response;
//	}
//
//}
