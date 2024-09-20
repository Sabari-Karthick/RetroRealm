package com.Batman.security.oauth;

import java.util.Collections;
import java.util.Objects;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
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

@Service
@RequiredArgsConstructor
public class CustomOauth2Service extends DefaultOAuth2UserService {

	private final UserFeignClinet userClient;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		String provider = userRequest.getClientRegistration().getRegistrationId();
		OAuth2User oAuth2User = super.loadUser(userRequest);
		OAuth2UserInfo oAuth2UserInfo = OAuth2UserFactory.getOAuth2UserInfo(provider, oAuth2User.getAttributes());
		User user = userClient.findByEmail(oAuth2UserInfo.getEmail()).orElse(null);
		if (Objects.isNull(user)) {
			User user2 = new User();
			user2.setName(oAuth2UserInfo.getFirstName());
			user2.setEmail(oAuth2UserInfo.getEmail());
			user2.setAuthenticationProvider(AuthenticationProiver.valueOf(provider.toUpperCase()));
			user2.setRoles(Collections.singleton(Role.GAMER));
			user = userClient.saveUser(user2);
		} else {
			user.setAuthenticationProvider(AuthenticationProiver.valueOf(provider.toUpperCase()));
			user = userClient.saveUser(user);
		}
		return new UserPrincipal(user, oAuth2User.getAttributes());
	}

}
