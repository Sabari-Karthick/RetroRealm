package com.Batman.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.Batman.dto.RegistrationRequest;
import com.Batman.entity.User;
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

	private final CommonMapper mapper;

	@Override
	public User registerUser(RegistrationRequest user,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InputFieldException(bindingResult.getFieldError().getDefaultMessage());
		}
		log.info("Entering registerUser ...");
		User userEntity = mapper.convertToEntity(user, User.class);
		log.info("Registering user {}", user.getName());
		User savedUser = userRepository.save(userEntity);
		log.info("Leaving registerUser ...");
		return savedUser;

	}

	@Override
	public User getUserByEmail(String userMail) {
		if(StringUtils.isBlank(userMail)) {
			log.error("User Mail is Null");
			throw new InputFieldException("User Mail is Null");
		}
		return userRepository.findByEmail(userMail).orElseThrow(() -> {
			log.error("No User Found with these mail :: {}",userMail);
			return new UserNotFoundException("USER_NOT_FOUND");
		});
	}

	@Override
	public List<User> getAllUser() {
		return userRepository.findAll();
	}

}
