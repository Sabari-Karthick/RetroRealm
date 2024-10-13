package com.Batman.security.oauth;


import java.util.Map;

public class FacebookOAuth2UserInfo extends OAuth2UserInfo {

	public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	 @Override
	    public String getId() {
	        return (String) attributes.get("sub");
	    }

	    @Override
	    public String getFirstName() {
	        return (String) attributes.get("name");
	    }

	    @Override
	    public String getLastName() {
	        return (String) attributes.get("family_name");
	    }

	    @Override
	    public String getEmail() {
	        return (String) attributes.get("email");
	    }
}