package com.Batman.service;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.Batman.dto.RegistrationRequest;
import com.Batman.dto.UpdateProviderRequest;
import com.Batman.entity.User;

public interface IUserService {
    String registerUser(RegistrationRequest user,BindingResult bindingResult);
    User updateUserProviderDetails(UpdateProviderRequest user,BindingResult bindingResult);
    User getUserByEmail(String userMail);
    List<User> getAllUser();
}
