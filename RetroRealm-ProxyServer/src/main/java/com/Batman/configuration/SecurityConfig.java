package com.Batman.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.HttpBasicSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.LogoutSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import com.Batman.security.jwt.JwtFilter;
import com.Batman.security.oauth.OAuth2SuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtFilter jwtFilter;
	
	
	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	
	@Value("${allowed.paths}")
	private String[] allowedPaths;
	
//	private final ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;
	

	@Bean
	SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {

        http
                .csrf(CsrfSpec::disable)
                .authorizeExchange(request -> request
                        .pathMatchers(allowedPaths).permitAll()
                        .anyExchange().authenticated())
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .httpBasic(HttpBasicSpec::disable)
                .oauth2Login(oauth2 -> oauth2
                		 .authenticationSuccessHandler(oAuth2SuccessHandler))
//                		 .authorizationRequestRepository(authorizationRequestRepository))
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .formLogin(FormLoginSpec::disable)
                .logout(LogoutSpec::disable);

		return http.build();

	}
	


}
