package com.Batman.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserServiceUrlResolver {//HardCoded Needs to be changed
	
	@Value("${is-local}")
	boolean isLocal;
	
	public String getUserByMailUrl() {
		return isLocal ? "http://localhost:8081/api/v1/users/mail"  : "http://user-service:8081/api/v1/users/mail";
	}
	
	public String getUpdateUserUrl() {
		return isLocal ? "http://localhost:8081/api/v1/users/provider/update"  : "http://user-service:8081/api/v1/users/provider/update";
	}

}
