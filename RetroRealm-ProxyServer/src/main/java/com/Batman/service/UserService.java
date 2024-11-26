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
import com.Batman.helper.UserServiceUrlResolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService {

	private final UserServiceUrlResolver userServiceUrlResolver;

	@Override
	public Mono<UserDetails> findByUsername(String userMail) {
		log.info("Entering loadUserByUsername...");
		Mono<User> userMono = WebClient.create().post().uri(userServiceUrlResolver.getUserByMailUrl())
				.bodyValue(Map.of("userMail", userMail)).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
					log.error("Error While retrieving User Details ...");
					throw new UsernameNotFoundException("AUTHENTICATION_ERROR");
				}).bodyToMono(User.class);
		return userMono.map(UserPrincipal::new);
	}

}
