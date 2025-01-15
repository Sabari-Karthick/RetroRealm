package com.Batman.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.Batman.dto.RegistrationRequest;
import com.Batman.dto.UpdateProviderRequest;
import com.Batman.entity.User;
import com.Batman.enums.AuthenticationProiver;
import com.Batman.enums.Role;
import com.Batman.exception.wrapper.InputFieldException;
import com.Batman.exception.wrapper.UserNotFoundException;
import com.Batman.mapper.CommonMapper;
import com.Batman.repository.IUserRepository;
import com.Batman.service.IUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements IUserService {

	private final IUserRepository userRepository;

	private final PasswordEncoder encoder;

	private final CommonMapper mapper;

	@SuppressWarnings("null")
	@Override
	public String registerUser(RegistrationRequest user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			log.error("Input field Exception in {}",
					bindingResult.getFieldError() + " " + bindingResult.getFieldError().getDefaultMessage());
			throw new InputFieldException(bindingResult.getFieldError().getDefaultMessage());
		}
		log.info("Entering registerUser ...");
		if (userRepository.findByEmail(user.getEmail()).isPresent())
			throw new UserNotFoundException("USER_Exists");
		User userEntity = mapper.convertToEntity(user, User.class);
		log.info("Registering user {}", user.getName());
		if (Objects.isNull(userEntity.getAuthenticationProvider()))
			userEntity.setAuthenticationProvider(AuthenticationProiver.LOCAL);
		if (Objects.isNull(userEntity.getRoles()))
			userEntity.setRoles(Collections.singleton(Role.GAMER));
		userEntity.setPassword(encoder.encode(user.getPassword()));
		User savedUser = userRepository.save(userEntity);
		log.info("Leaving registerUser ...");
		return "User Registered With ID :: " + savedUser.getUserID();

	}

	@Override
	public User getUserByEmail(String userMail) {
		log.info("Entering getUserByEmail ...");
		if (StringUtils.isBlank(userMail)) {
			log.error("User Mail is Null");
			throw new InputFieldException("User Mail is Null");
		}
		return userRepository.findByEmail(userMail).orElseThrow(() -> {
			log.error("No User Found with these mail :: {}", userMail);
			return new UserNotFoundException("USER_NOT_FOUND");
		});
	}

	@Override
	public List<User> getAllUser() {
		return userRepository.findAll();
	}

	@SuppressWarnings("null")
	@Override
	public User updateUserProviderDetails(UpdateProviderRequest user, BindingResult bindingResult) {
		log.info("Entering updateUserProviderDetails ...");
		if (bindingResult.hasErrors()) {
			// Need to fix this possible null
			log.error("Input field Exception in {}",
					bindingResult.getFieldError() + " " + bindingResult.getFieldError().getDefaultMessage());
			throw new InputFieldException(bindingResult.getFieldError().getDefaultMessage());
		}
		User userEntity = mapper.convertToEntity(user, User.class);
		log.info("Updating user {}", user.getName());
		if (Objects.isNull(userEntity.getRoles()))
			userEntity.setRoles(Collections.singleton(Role.GAMER));
		User savedUser = userRepository.save(userEntity);
		log.info("Leaving updateUserProviderDetails ...");
		return savedUser;
	}

}
