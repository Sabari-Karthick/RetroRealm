package com.Batman.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import com.Batman.security.jwt.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtFilter jwtFilter;
	
//	private final CustomOauth2Service oauth2Service;
	
//	private final OAuth2SuccessHandler oAuth2SuccessHandler;

	@Bean
	SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(request -> request
                		.pathMatchers("/oauth2/**",  "/swagger-ui/**","/v3/api-docs/**").permitAll()
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/api/v1/auth/**").permitAll()
                        .pathMatchers("/api/v1/users/register").permitAll()
                        .anyExchange().authenticated())
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
//                .oauth2Login(login -> login
//						.authorizationEndpoint(authorization -> authorization.baseUri("/oauth2/authorize"))
//						.userInfoEndpoint(userInfo -> userInfo.userService(oauth2Service))
//						.successHandler(oAuth2SuccessHandler))
               .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);

		return http.build();

	}

}
