package com.Batman.dto;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import com.Batman.enums.AuthenticationProiver;
import com.Batman.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class RegistrationRequest {

  
   @Size(min = 8,max = 20,message = "INVALID_LENGTH")	
   private String name;

    @Size(min = 6, max = 16, message = "PASSWORD_CHARACTER_LENGTH")
    private String password;
 
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    
    @Email(message = "INVALID_EMAIL")
    @NotBlank(message = "EMAIL_CANNOT_BE_EMPTY")
    private String email;
    
    private AuthenticationProiver authenticationProvider;

    private Set<Role> roles = Collections.singleton(Role.GAMER);
}
