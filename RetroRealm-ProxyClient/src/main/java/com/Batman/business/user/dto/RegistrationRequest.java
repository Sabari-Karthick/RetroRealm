package com.Batman.business.user.dto;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RegistrationRequest {

  
   @Size(min = 8,max = 12,message = "UNIQUE_USERNAME")	
   private String username;

    @Size(min = 6, max = 16, message = "PASSWORD_CHARACTER_LENGTH")
    private String password;

    @Size(min = 6, max = 16, message = "PASSWORD2_CHARACTER_LENGTH")
    private String password2;
 
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    
    @Email(message = "INVALID_EMAIL")
    @NotBlank(message = "EMAIL_CANNOT_BE_EMPTY")
    private String email;
    
}
