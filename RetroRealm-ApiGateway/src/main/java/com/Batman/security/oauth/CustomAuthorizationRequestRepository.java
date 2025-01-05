package com.Batman.security.oauth;

import java.time.Duration;
import java.util.Base64;

import javax.crypto.SecretKey;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.client.web.server.ServerAuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import com.Batman.helper.CookieHelper;
import com.Batman.helper.EncryptionHelper;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
/**
 * 
 * 
 *  serialize the entire OAuth2AuthorizationRequest object into a string, and attach it as a cookie. 
 * 
 * This eliminates the need to store the OAuth2AuthorizationRequest objects in-memory, as would be the case with Spring's default implementation of the ServerAuthorizationRequestRepository interface.
 * 
 * 
 */
@Component
@Slf4j
public class CustomAuthorizationRequestRepository
		implements ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> {

	private static final Duration OAUTH_COOKIE_EXPIRY = Duration.ofMinutes(5);
	private static final Base64.Encoder B64E = Base64.getEncoder();
	private static final Base64.Decoder B64D = Base64.getDecoder();
	public static final String OAUTH_COOKIE_NAME = "OAUTH";

	private final SecretKey secretKey;

	public CustomAuthorizationRequestRepository() {
		this.secretKey = EncryptionHelper.generateKey();
	}

	public CustomAuthorizationRequestRepository(@NonNull char[] encryptionPassword) {
		byte[] salt = { 0 }; // A static salt is OK for these short lived session cookies
		this.secretKey = EncryptionHelper.generateKey(encryptionPassword, salt);
	}

	@Override
	public Mono<OAuth2AuthorizationRequest> loadAuthorizationRequest(ServerWebExchange exchange) {
		log.info("Entering loadAuthorizationRequest ...");
		Mono<OAuth2AuthorizationRequest> authorizationRequest = retrieveCookie(exchange);
		log.info("Leaving  loadAuthorizationRequest ...");
		return authorizationRequest;
	}

	@Override
	public Mono<Void> saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
			ServerWebExchange exchange) {
		if (authorizationRequest == null) {
			this.removeCookie(exchange);
			return Mono.empty();
		}
		ServerHttpResponse response = exchange.getResponse();
		this.attachCookie(response, authorizationRequest);
		return Mono.empty();
	}

	@Override
	public Mono<OAuth2AuthorizationRequest> removeAuthorizationRequest(ServerWebExchange exchange) {
		log.info("Entering removeAuthorizationRequest ...");
		Mono<OAuth2AuthorizationRequest> authorizationRequest = retrieveCookie(exchange);
		log.info("Leaving  removeAuthorizationRequest ...");
		return authorizationRequest;
	}

	private Mono<OAuth2AuthorizationRequest> retrieveCookie(ServerWebExchange exchange) {
		log.info("Entering CustomAuthorizationRequestRepository retrieveCookie ...");
		MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();
		Mono<OAuth2AuthorizationRequest> request = CookieHelper.retrieve(cookies, OAUTH_COOKIE_NAME).map(this::decrypt)
				.map(Mono::just).orElse(Mono.empty());
		log.info("Entering CustomAuthorizationRequestRepository retrieveCookie ...");
		return request;
	}

	private void removeCookie(ServerWebExchange exchange) {
		log.info("Entering CustomAuthorizationRequestRepository removeCookie ...");
		String expiredCookie = CookieHelper.generateExpiredCookie(OAUTH_COOKIE_NAME);
		ServerHttpResponse response = exchange.getResponse();
		response.getHeaders().add(HttpHeaders.SET_COOKIE, expiredCookie);
		log.info("Leaving CustomAuthorizationRequestRepository removeCookie ...");
	}

	private void attachCookie(ServerHttpResponse response, OAuth2AuthorizationRequest value) {
		log.info("Entering CustomAuthorizationRequestRepository attachCookie ...");
		String cookie = CookieHelper.generateCookie(OAUTH_COOKIE_NAME, this.encrypt(value), OAUTH_COOKIE_EXPIRY);
		response.getHeaders().add(HttpHeaders.SET_COOKIE, cookie);
		log.info("Leaving CustomAuthorizationRequestRepository attachCookie ...");
	}

	private String encrypt(OAuth2AuthorizationRequest authorizationRequest) {
		log.info("Entering CustomAuthorizationRequestRepository ecrypt ... ");
		byte[] bytes = SerializationUtils.serialize(authorizationRequest);
		byte[] encryptedBytes = EncryptionHelper.encrypt(this.secretKey, bytes);
		 String encodedString = B64E.encodeToString(encryptedBytes);
		 log.info("Leaving CustomAuthorizationRequestRepository ecrypt ... ");
		 return encodedString;
	}

	private OAuth2AuthorizationRequest decrypt(String encrypted) {
		log.info("Entering CustomAuthorizationRequestRepository decrypt ... ");
		byte[] encryptedBytes = B64D.decode(encrypted);
		byte[] bytes = EncryptionHelper.decrypt(this.secretKey, encryptedBytes);
		OAuth2AuthorizationRequest oauth2AuthorizationRequest = (OAuth2AuthorizationRequest) SerializationUtils
				.deserialize(bytes);
		log.info("Leaving CustomAuthorizationRequestRepository decrypt ... ");
		return oauth2AuthorizationRequest;
	}
}
