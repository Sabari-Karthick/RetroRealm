package com.Batman.dto;

import java.util.Set;

import com.Batman.enums.AuthenticationProiver;
import com.Batman.enums.Role;

import lombok.Data;

@Data
public class User {
	

	private Integer userID;
	
	private String name;
	
	private String email;
	
	private String password;
	
	
	private AuthenticationProiver authenticationProvider;
	
	private Set<Role> roles;
	
	

}
