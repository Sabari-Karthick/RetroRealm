//package com.Batman.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.Batman.security.jwt.JwtFilter;
//import com.Batman.security.oauth.CustomOauth2Service;
//import com.Batman.security.oauth.OAuth2SuccessHandler;
//
//import lombok.RequiredArgsConstructor;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//	
//	private final JwtFilter jwtFilter;
//	
//	private final CustomOauth2Service oauth2Service;
//	
//	private final OAuth2SuccessHandler oAuth2SuccessHandler;
//
//	@Bean
//	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(request -> request
//                		.requestMatchers("/oauth2/**",  "/swagger-ui/**","/v3/api-docs/**").permitAll()
//                        .requestMatchers("/eureka/**").permitAll()
//                        .requestMatchers("/api/v1/auth/**").permitAll()
//                        .requestMatchers("/api/v1/users/register").permitAll()
//                        .anyRequest().authenticated())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .oauth2Login(login -> login
//						.authorizationEndpoint(authorization -> authorization.baseUri("/oauth2/authorize"))
//						.userInfoEndpoint(userInfo -> userInfo.userService(oauth2Service))
//						.successHandler(oAuth2SuccessHandler))
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//		return http.build();
//
//	}
//
//}
