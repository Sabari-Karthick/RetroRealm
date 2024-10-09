package com.Batman.security.oauth;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.Batman.dto.User;
import com.Batman.dto.UserPrincipal;
import com.Batman.enums.AuthenticationProiver;
import com.Batman.enums.Role;
import com.Batman.feignclinet.UserFeignClinet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2Service extends DefaultReactiveOAuth2UserService {

	private final UserFeignClinet userClient;

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
			//Since feign is the client it is blocking by default
			 return Mono.fromCallable(() -> userClient.getUserByMail(Map.of("userMail",oAuth2UserInfo.getEmail())).orElse(null))
					 .flatMap(user -> {
						 if (Objects.isNull(user)) {
							 return Mono.fromCallable(() -> {
								 User newUser = new User();
								 newUser.setName(oAuth2UserInfo.getFirstName());
								 newUser.setEmail(oAuth2UserInfo.getEmail());
								 newUser.setAuthenticationProvider(AuthenticationProiver.valueOf(provider.toUpperCase()));
								 newUser.setRoles(Collections.singleton(Role.GAMER));
								 log.info("Creating new User ... ");
								 return userClient.registerUser(newUser);
							 }).doOnNext(newUser -> log.info("User Added ::: {} with Provider ::: {}",newUser.getName(),newUser.getAuthenticationProvider()));
						 } else {
							 return Mono.fromCallable(() -> {
								 user.setAuthenticationProvider(AuthenticationProiver.valueOf(provider.toUpperCase()));
								 log.info("Updating the  User...");
								 return userClient.registerUser(user);
							 });
						 }
					 }).map(user -> new UserPrincipal(user, oauthAttributes))
					 .doOnTerminate(() -> log.info("Leaving loadUser..."));
		});
	}

}
