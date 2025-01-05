package com.Batman.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.Batman.dto.AuthenticationRequest;
import com.Batman.dto.AuthenticationResponse;
import com.Batman.dto.User;
import com.Batman.dto.UserPrincipal;
import com.Batman.security.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

	private final ReactiveAuthenticationManager authenticationManager;

	private final JwtProvider jwtProvider;


	public Mono<ResponseEntity<AuthenticationResponse>> login(AuthenticationRequest authRequest) {
	    log.info("Entering Login ...");

	    return authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()))
	        .flatMap(authentication -> {
	        	            UserPrincipal userPricipal = (UserPrincipal)authentication.getPrincipal();
	        	            User user = userPricipal.getUser();
	                        String token = jwtProvider.createToken(user.getEmail(), user.getRoles().iterator().next().name());
	                        AuthenticationResponse response = new AuthenticationResponse();
	                        response.setUserMail(user.getEmail());
	                        response.setToken(token);
	                        return Mono.just(ResponseEntity.ok(response));
	        })
	        .doOnTerminate(() -> log.info("Leaving Login ..."));
	}


}
