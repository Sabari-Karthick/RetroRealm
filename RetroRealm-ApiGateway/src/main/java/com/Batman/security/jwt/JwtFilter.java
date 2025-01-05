package com.Batman.security.jwt;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.Batman.helper.PathChecker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter implements WebFilter {

	private final JwtProvider provider;
	
	private final PathChecker pathChecker;

	private static final String COOKIE_NAME = "RETROAUTH";
	
	@Value("${allowed.paths}")
	private String[] allowedPaths;

	@Override
	public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
		log.info("Entered Authentication Filter.......");
		ServerHttpRequest request = exchange.getRequest();
		String requestedPath = request.getPath().toString();
		if(pathChecker.isAllowed(requestedPath)) {
			log.info("Leaving Authentication Filter ...");
			return chain.filter(exchange);
		}
		log.info("REQUESTED PATH ::: {}", requestedPath);
		Optional<HttpCookie> cookie = Optional.ofNullable(request.getCookies().getFirst(COOKIE_NAME));
		String token = cookie.map(HttpCookie::getValue).orElseGet(() -> {
			log.info("No Cookie Found, Getting token from Request Header ...");
			return provider.resolveToken(request);
		});
		if (token != null && provider.validateToken(token)) {
			log.info("Entering Authentication ...");
			return provider.getAuthentication(token).flatMap(authentication -> {
				log.info("SuccessFully Authenticated ...");
				return chain.filter(exchange)
						.contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
			}).doOnError(error -> {
				log.error("Error while authenticating in JWT Filter ::: {}", error.getMessage());
			}).onErrorResume(e -> {
				log.error("Authentication failed: {}", e.getMessage());
				return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.clearContext());
			});
		}
		log.info("Leaving Authentication Filter ...");
		return chain.filter(exchange);
	}
}
