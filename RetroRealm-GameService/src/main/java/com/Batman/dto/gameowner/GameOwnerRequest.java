package com.Batman.dto.gameowner;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameOwnerRequest {
	
	@NotBlank(message = "Company name cannot be empty")
	private String companyName;

	
	@Email(message = "Wrong Email Format")
	@NotBlank(message = "Email cannot be empty")
	private String email;

	@NotNull(message = "Verification cannot be empty")
	private Boolean isVerified;
}
