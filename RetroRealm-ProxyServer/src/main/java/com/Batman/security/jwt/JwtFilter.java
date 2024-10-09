package com.Batman.security.jwt;

import java.util.Optional;

import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.Batman.exceptions.JwtAuthenticationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter implements WebFilter {

	private final JwtProvider provider;

	private static final String COOKIE_NAME = "RSESSION";
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		log.info("Entered Authentication Filter.......");
		ServerHttpRequest request = exchange.getRequest();
		log.info("REQUESTED PATH ::: {}",request.getPath().toString());
		Optional<HttpCookie> cookie = Optional.ofNullable(request.getCookies().getFirst(COOKIE_NAME));
	    String token = cookie
	        .map(HttpCookie::getValue)
	        .orElseGet(() -> {
	            log.info("No Cookie Found, Getting token from Request Header ...");
	            return provider.resolveToken(request);
	        });
		try {
			if (token != null && provider.validateToken(token)) {
				log.info("Entering Authentication ...");
				Mono<Authentication> authenticationMono = provider.getAuthentication(token).switchIfEmpty(Mono.error(new JwtAuthenticationException("AUTHENTICATION_FAILED")));
				log.info("SuccessFully Authenticated ...");
				return authenticationMono.flatMap(authentication -> 
			    chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
			);
			}
		}

		catch (IllegalArgumentException | JwtAuthenticationException e) {
			log.error("Error While Authentication ...");
			return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.clearContext());
		}
		log.info("Leaving Authentication Filter ...");
		return chain.filter(exchange);
	}
}
