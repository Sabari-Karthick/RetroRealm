package com.Batman.service;

import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.Batman.dto.AuthenticationRequest;
import com.Batman.dto.AuthenticationResponse;
import com.Batman.dto.User;
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

	private static final String USER_SERVICE_GET_USER_URL = "http://localhost:8082/api/v1/users/mail";

	public Mono<?> login(AuthenticationRequest authRequest) {
		log.info("Entering Login ...");
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
		Mono<User> userMono = WebClient.create().post().uri(USER_SERVICE_GET_USER_URL)
				.bodyValue(Map.of("userMail", authRequest.getEmail())).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new UsernameNotFoundException("AUTHENTICATION_ERROR")))
				.bodyToMono(User.class);
		Mono<ResponseEntity<AuthenticationResponse>> authResponse = userMono
				.switchIfEmpty(Mono.error(new UsernameNotFoundException("EMAIL_NOT_FOUND")))
				.map(user -> {
					String token = jwtProvider.createToken(user.getEmail(), user.getRoles().iterator().next().name());
					AuthenticationResponse response = new AuthenticationResponse();
					response.setUserMail(user.getEmail());
					response.setToken(token);
					return ResponseEntity.ok(response);
				});
//		User user = userFeignClinet.getUserByMail(Map.of("userMail",authRequest.getEmail())).orElseThrow(() -> {
//			log.error("USER WITH THIS MAIL NOT FOUND...");
//			return new UsernameNotFoundException("EMAIL_NOT_FOUND");
//		});

		log.info("Leaving Login ...");
		return authResponse;
	}

}
