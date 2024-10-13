package com.Batman.service;

import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.Batman.dto.User;
import com.Batman.dto.UserPrincipal;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserService implements ReactiveUserDetailsService {

	private static final String USER_SERVICE_GET_USER_URL = "http://localhost:8082/api/v1/users/mail";

	@Override
	public Mono<UserDetails> findByUsername(String userMail) {
		log.info("Entering loadUserByUsername...");
		Mono<User> userMono = WebClient.create().post().uri(USER_SERVICE_GET_USER_URL)
				.bodyValue(Map.of("userMail", userMail)).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
					log.error("Error While retrieving User Details ...");
					throw new UsernameNotFoundException("AUTHENTICATION_ERROR");
				}).bodyToMono(User.class);
		return userMono.map(UserPrincipal::new);
	}

}
