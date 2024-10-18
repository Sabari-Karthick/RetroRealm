package com.Batman.configuration;


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
	

	@Bean
	SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {

        http
                .csrf(CsrfSpec::disable)
                .authorizeExchange(request -> request
                        .pathMatchers("/oauth2/**", "/swagger-ui/**", "/v3/api-docs/**", "/login", "/home").permitAll()
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/api/v1/auth/**").permitAll()
                        .pathMatchers("/api/v1/users/register").permitAll()
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
