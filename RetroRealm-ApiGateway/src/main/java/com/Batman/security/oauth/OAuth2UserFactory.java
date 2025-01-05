package com.Batman.security.oauth;

import java.util.Map;

import javax.naming.AuthenticationException;

import lombok.SneakyThrows;

public class OAuth2UserFactory {
	
	private OAuth2UserFactory() {}
	
	@SneakyThrows
	public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
		if (registrationId.equalsIgnoreCase("Google")) {
			return new GoogleOAuth2UserInfo(attributes);
		} else if (registrationId.equalsIgnoreCase("Facebook")) {
			return new FacebookOAuth2UserInfo(attributes);
		}

		else {
			throw new AuthenticationException();
		}
	}

}
