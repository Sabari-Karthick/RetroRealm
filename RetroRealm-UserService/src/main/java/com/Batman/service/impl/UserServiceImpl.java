package com.Batman.service.impl;

import java.util.Collections;

import org.springframework.stereotype.Service;

import com.Batman.dto.RegistrationRequest;
import com.Batman.entity.User;
import com.Batman.enums.Roles;
import com.Batman.repository.IUserRepository;
import com.Batman.service.IUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService{
	
	
    private final    IUserRepository userRepository;
	
	@Override
	public String registerUser(RegistrationRequest user) {
		User userEntity = User.builder()
		             .username(user.getUsername())
		             .usermail(user.getEmail())
		             .password(user.getPassword())
		             .dateOfBirth(user.getDateOfBirth())
		             .role(Collections.singleton(Roles.USER))
		             .build();
		log.info("Registering user {}",user.getUsername());
		userRepository.save(userEntity);
		return "Success";
	}
     
}
