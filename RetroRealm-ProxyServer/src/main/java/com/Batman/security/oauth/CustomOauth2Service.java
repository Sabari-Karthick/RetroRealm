package com.Batman.security.oauth;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.Batman.dto.User;
import com.Batman.dto.UserPrincipal;
import com.Batman.enums.AuthenticationProiver;
import com.Batman.enums.Role;
import com.Batman.exceptions.UserRegistrationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2Service extends DefaultReactiveOAuth2UserService {

	
	private static final String USER_SERVICE_GET_USER_URL = "http://localhost:8082/api/v1/users/mail";
	private static final String USER_SERVICE_UPDATE_USER_URL = "http://localhost:8082/api/v1/users/provider/update";

	@Override
	public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("Entering loadUser...");
		String provider = userRequest.getClientRegistration().getRegistrationId();
		Mono<OAuth2User> oAuth2User = super.loadUser(userRequest);
		return oAuth2User.flatMap(oauth2User ->{
			Map<String, Object> oauthAttributes = oauth2User.getAttributes();
			if(Objects.isNull(oauthAttributes)) {
				throw new OAuth2AuthenticationException("FAILED_TO_RETRIEVE_ATTRIBUTES");
			}			
			OAuth2UserInfo oAuth2UserInfo = OAuth2UserFactory.getOAuth2UserInfo(provider, oauthAttributes);
			Mono<User> userMono = WebClient.create().post().uri(USER_SERVICE_GET_USER_URL)
					.contentType(MediaType.APPLICATION_JSON) 
					.bodyValue(Map.of("userMail", oAuth2UserInfo.getEmail())).retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.empty())
					.bodyToMono(User.class);
			return userMono.flatMap(user -> {
				 if (Objects.isNull(user.getEmail())) {
						 User newUser = new User();
						 newUser.setName(oAuth2UserInfo.getFirstName());
						 newUser.setEmail(oAuth2UserInfo.getEmail());
						 newUser.setAuthenticationProvider(AuthenticationProiver.valueOf(provider.toUpperCase()));
						 newUser.setRoles(Collections.singleton(Role.GAMER));
						 log.info("Creating new User ... ");
						 return updateUser(newUser).doOnError(e -> {
							 throw new UserRegistrationException(e.getMessage());
					     }).doOnNext(result -> log.info("User Added ::: {} with Provider ::: {}",newUser.getName(),newUser.getAuthenticationProvider()));
				 } else {
					 log.info("Updating the User...");
					 user.setAuthenticationProvider(AuthenticationProiver.valueOf(provider.toUpperCase()));
					 return updateUser(user).doOnError(e -> {
						 log.error("Load User, User Update Error ...");
						 throw new UserRegistrationException(e.getMessage());
				     }).doOnNext(result -> log.info("User Updated ::: {} with Provider ::: {}",user.getName(),user.getAuthenticationProvider()));
				 }
			 }).map(user -> new UserPrincipal(user, oauthAttributes))
			 .doOnTerminate(() -> log.info("Leaving loadUser..."));
		});
			//Since feign is the client it is blocking by default
//			 return Mono.fromCallable(() -> userClient.getUserByMail(Map.of("userMail",oAuth2UserInfo.getEmail())).orElse(null))
//					 .flatMap(user -> {
//						 if (Objects.isNull(user)) {
//							 return Mono.fromCallable(() -> {
//								 User newUser = new User();
//								 newUser.setName(oAuth2UserInfo.getFirstName());
//								 newUser.setEmail(oAuth2UserInfo.getEmail());
//								 newUser.setAuthenticationProvider(AuthenticationProiver.valueOf(provider.toUpperCase()));
//								 newUser.setRoles(Collections.singleton(Role.GAMER));
//								 log.info("Creating new User ... ");
//								 return userClient.registerUser(newUser);
//							 }).doOnNext(newUser -> log.info("User Added ::: {} with Provider ::: {}",newUser.getName(),newUser.getAuthenticationProvider()));
//						 } else {
//							 return Mono.fromCallable(() -> {
//								 user.setAuthenticationProvider(AuthenticationProiver.valueOf(provider.toUpperCase()));
//								 log.info("Updating the  User...");
//								 return userClient.registerUser(user);
//							 });
//						 }
//					 }).map(user -> new UserPrincipal(user, oauthAttributes))
//					 .doOnTerminate(() -> log.info("Leaving loadUser..."));
		
	}
	
	private Mono<User> updateUser(User user){
		 log.info("Entering updateUser ...");
		 Mono<User> userMono = WebClient.create().put().uri(USER_SERVICE_UPDATE_USER_URL)
				.bodyValue(user).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
					log.error("Error While retrieving User Details ...");
					throw new UsernameNotFoundException("AUTHENTICATION_ERROR");
				 })
				.onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
					log.error("Error While retrieving User Details ...");
					throw new UserRegistrationException("ERROR_WHILE_USER_UPDATE");
				 })
				.bodyToMono(User.class);
		 log.info("Leaving updateUser ...");
		 return userMono;
	}
}
