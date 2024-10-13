package com.Batman.security.oauth;

import java.net.URI;
import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.Batman.enums.Role;
import com.Batman.security.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements ServerAuthenticationSuccessHandler{

	private final JwtProvider jwtProvider;


	@Override
	@SneakyThrows
	public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
		log.info("Entering onAuthenticationSuccess... ");
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		String email = (String) oAuth2User.getAttributes().get("email");
		String token = jwtProvider.createToken(email, Role.GAMER.toString());
/*		AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		authenticationResponse.setUserMail(email);
		authenticationResponse.setToken(token);*/
		/*
		 * ServerHttpRequest request =
		 * webFilterExchange.getExchange().getRequest().mutate()
		 * .header("Authorization", "Bearer " + token) .build();
		 * webFilterExchange.getExchange().mutate().request(request).build();
		 * 
		 * WebSessionServerRequestCache requestCache = new
		 * WebSessionServerRequestCache(); return
		 * requestCache.getRedirectUri(webFilterExchange.getExchange())
		 * .defaultIfEmpty(new URI("/home")) // Default URI if no request is cached
		 * .flatMap(redirectUri -> { return
		 * webFilterExchange.getExchange().getResponse().setComplete(); })
		 * .then(webFilterExchange.getChain().filter(webFilterExchange.getExchange()));
		 * // return
		 * webFilterExchange.getChain().filter(webFilterExchange.getExchange()) //
		 * .then(Mono.fromRunnable(() -> { //
		 * log.info("Leaving onAuthenticationSuccess ..."); // })); //
		 */		
		ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
		ResponseCookie cookie = createCookie(token);
		response.addCookie(cookie);
		response.setRawStatusCode(302);
		response.getHeaders().setLocation(URI.create("/home"));
		log.info("Leaving onAuthenticationSuccess ...");
		return response.setComplete();
		/*
		 * String successResponse =
		 * objectMapper.writeValueAsString(authenticationResponse); DataBufferFactory
		 * bufferFactory = response.bufferFactory(); DataBuffer dataBuffer =
		 * bufferFactory.wrap(successResponse.getBytes(StandardCharsets.UTF_8));
		 * response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		 * response.setRawStatusCode(200); respon Mono<Void> result =
		 * response.writeWith(Mono.just(dataBuffer)).then(); // for simple response
		 * flush is not necessary log.info("Leaving onAuthenticationSuccess ...");
		 * return result;
		 */
	}

	private ResponseCookie createCookie(String token) {
		log.info("Entering createCookie ...");
        ResponseCookie cookie = ResponseCookie.from("RSESSION", token)
                    .httpOnly(true)   // Prevent access via JavaScript to avoid XSS
                    .secure(true)     // Send over HTTPS only
                    .path("/")        // Set the path for the cookie to which paths and all this to be send
                    .maxAge(Duration.ofHours(5))
                    .build();
        log.info("Leaving createCookie ...");
        return cookie;
	}

}
