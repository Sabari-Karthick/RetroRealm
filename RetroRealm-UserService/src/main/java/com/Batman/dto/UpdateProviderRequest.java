package com.Batman.dto;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import com.Batman.enums.AuthenticationProiver;
import com.Batman.enums.Role;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UpdateProviderRequest {

  
    private String name;
 
    private LocalDate dateOfBirth;
    
    private String email;
    
    @NotNull(message = "PROVIDER_CANNOT_BE_NULL")
    private AuthenticationProiver authenticationProvider;

    private Set<Role> roles = Collections.singleton(Role.GAMER);
}
