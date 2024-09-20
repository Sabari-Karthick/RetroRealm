package com.Batman.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class AuthenticationRequest {
	
	
	@Email
	@NotBlank
	private String email;
	
	@NotBlank
	@Size(min = 6)
	private String password;
}
