package com.Batman.helper;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserServiceUrlResolver {

	@Value("${is-local}")
	boolean isLocal;

	private final DiscoveryClient discoveryClient;

	public String getUserByMailUrl() {
		log.info("Entering UserServiceUrlResolver getUserByMailURL ....");
		String userServiceBaseUrl = getBaseURL();
		if (StringUtils.isEmpty(userServiceBaseUrl)) {
			log.error("User Service URL Cannot be Constructed ...");
			throw new InternalError("USER_SERVICE_RESOLVE_ERROR");
		}

		String fetchUserByMailURL = isLocal ? "http://localhost:8081/api/v1/users/mail"
				: userServiceBaseUrl + "/api/v1/users/mail";
		log.debug("Resolved URL :: {}", fetchUserByMailURL);
		log.info("Entering UserServiceUrlResolver getUserByMailURL ....");
		return fetchUserByMailURL;
	}

	public String getUpdateUserAuthProviderUrl() {
		log.info("Entering UserServiceUrlResolver getUpdateUserAuthProviderUrl ....");
		String userServiceBaseUrl = getBaseURL();
		if (StringUtils.isEmpty(userServiceBaseUrl)) {
			log.error("User Service URL Cannot be Constructed ....");
			throw new InternalError("USER_SERVICE_RESOLVE_ERROR");
		}
		String updateUserAuthProviderURL = isLocal ? "http://localhost:8081/api/v1/users/provider/update"
				: userServiceBaseUrl + "/api/v1/users/provider/update";
		log.debug("Resolved URL :: {}", updateUserAuthProviderURL);
		log.info("Leaving UserServiceUrlResolver getUpdateUserAuthProviderUrl ....");
		return updateUserAuthProviderURL;
	}

	private String getBaseURL() {
		List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
		String userServiceUrl = "";
		if (instances != null && !instances.isEmpty()) {
			ServiceInstance serviceInstance = instances.get(0);
			String host = serviceInstance.getHost();
			int port = serviceInstance.getPort();
			String scheme = serviceInstance.getScheme();
			userServiceUrl = scheme + "://" + host + ":" + port;
		}
		return userServiceUrl;
	}

}
