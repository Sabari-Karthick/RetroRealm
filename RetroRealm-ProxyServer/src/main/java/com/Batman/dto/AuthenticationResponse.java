package com.Batman.dto;


import lombok.Data;

@Data
public class AuthenticationResponse {

	
	private User response;
	private String token;
}
