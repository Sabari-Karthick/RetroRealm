package com.Batman.helper;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.batman.exception.InternalException;
import com.batman.resolvers.ServiceUrlResolver;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserServiceUrlResolver {

	@Value("${is-local}")
	boolean isLocal;

	private static final String USER_SERVICE = "user-service";
	
	private final ServiceUrlResolver serviceUrlResolver;

	public UserServiceUrlResolver(@Lazy ServiceUrlResolver serviceUrlResolver) {
		this.serviceUrlResolver = serviceUrlResolver;
	}

	public String getUserByMailUrl() {
		log.info("Entering UserServiceUrlResolver getUserByMailURL ....");
		String userServiceBaseUrl = getBaseURL();
		String fetchUserByMailURL = userServiceBaseUrl + "/api/v1/users/mail";
		log.debug("Resolved URL :: {}", fetchUserByMailURL);
		log.info("Entering UserServiceUrlResolver getUserByMailURL ....");
		return fetchUserByMailURL;
	}

	public String getUpdateUserAuthProviderUrl() {
		log.info("Entering UserServiceUrlResolver getUpdateUserAuthProviderUrl ....");
		String userServiceBaseUrl = getBaseURL();
		String updateUserAuthProviderURL = userServiceBaseUrl + "/api/v1/users/provider/update";
		log.debug("Resolved URL :: {}", updateUserAuthProviderURL);
		log.info("Leaving UserServiceUrlResolver getUpdateUserAuthProviderUrl ....");
		return updateUserAuthProviderURL;
	}

	private String getBaseURL() {
		Map<String, String> serviceUrls = serviceUrlResolver.resolveServiceUrls();
        String userServiceBaseUrl = serviceUrls.get(USER_SERVICE);
        if(StringUtils.isBlank(userServiceBaseUrl)) {
        	log.error("User Service Url is Null or Empty ...");
        	throw new InternalException("Cannot Reslove User Service");
        }
        log.info("User Service Base Url :: {}",userServiceBaseUrl);
        return userServiceBaseUrl;
	}

}
