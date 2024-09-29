//package com.Batman.security.oauth;
//
//import java.util.Collections;
//import java.util.Map;
//import java.util.Objects;
//
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import com.Batman.dto.User;
//import com.Batman.dto.UserPrincipal;
//import com.Batman.enums.AuthenticationProiver;
//import com.Batman.enums.Role;
//import com.Batman.feignclinet.UserFeignClinet;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class CustomOauth2Service extends DefaultOAuth2UserService {
//
//	private final UserFeignClinet userClient;
//
//	@Override
//	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//		log.info("Entering loadUser...");
//		String provider = userRequest.getClientRegistration().getRegistrationId();
//		OAuth2User oAuth2User = super.loadUser(userRequest);
//		OAuth2UserInfo oAuth2UserInfo = OAuth2UserFactory.getOAuth2UserInfo(provider, oAuth2User.getAttributes());
//		User user = userClient.getUserByMail(Map.of("userMail",oAuth2UserInfo.getEmail())).orElse(null);
//		if (Objects.isNull(user)) {
//			User user2 = new User();
//			user2.setName(oAuth2UserInfo.getFirstName());
//			user2.setEmail(oAuth2UserInfo.getEmail());
//			user2.setAuthenticationProvider(AuthenticationProiver.valueOf(provider.toUpperCase()));
//			user2.setRoles(Collections.singleton(Role.GAMER));
//			user = userClient.registerUser(user2);
//			log.info("Adding the new User...");
//		} else {
//			user.setAuthenticationProvider(AuthenticationProiver.valueOf(provider.toUpperCase()));
//			user = userClient.registerUser(user);
//			log.info("Updating the  User...");
//		}
//		 UserPrincipal userPrincipal = new UserPrincipal(user, oAuth2User.getAttributes());
//		 log.info("Leaving loadUser...");
//		 return userPrincipal;
//	}
//	
//	
//
//}
//
////public class CustomOauth2Service extends DefaultReactiveOAuth2UserService {
////
////	private final UserFeignClinet userClient;
////
////	@Override
////	public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
////		log.info("Entering loadUser...");
////		String provider = userRequest.getClientRegistration().getRegistrationId();
////		OAuth2User oAuth2User = (OAuth2User) super.loadUser(userRequest);
////		OAuth2UserInfo oAuth2UserInfo = OAuth2UserFactory.getOAuth2UserInfo(provider, oAuth2User.getAttributes());
////		User user = userClient.getUserByMail(Map.of("userMail",oAuth2UserInfo.getEmail())).orElse(null);
////		if (Objects.isNull(user)) {
////			User user2 = new User();
////			user2.setName(oAuth2UserInfo.getFirstName());
////			user2.setEmail(oAuth2UserInfo.getEmail());
////			user2.setAuthenticationProvider(AuthenticationProiver.valueOf(provider.toUpperCase()));
////			user2.setRoles(Collections.singleton(Role.GAMER));
////			user = userClient.registerUser(user2);
////			log.info("Adding the new User...");
////		} else {
////			user.setAuthenticationProvider(AuthenticationProiver.valueOf(provider.toUpperCase()));
////			user = userClient.registerUser(user);
////			log.info("Updating the  User...");
////		}
////		 UserPrincipal userPrincipal = new UserPrincipal(user, oAuth2User.getAttributes());
////		 log.info("Leaving loadUser...");
////		 return Mono.just(userPrincipal);
////	}
////
////}
