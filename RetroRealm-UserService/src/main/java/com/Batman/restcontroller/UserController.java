package com.Batman.restcontroller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Batman.dto.RegistrationRequest;
import com.Batman.dto.UpdateProviderRequest;
import com.Batman.service.IUserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final IUserService userService;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest user, BindingResult bindingResult) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(user, bindingResult));

	}
	
	@PutMapping("/provider/update")
	public ResponseEntity<?> updateUser(@Valid @RequestBody final UpdateProviderRequest user, BindingResult bindingResult) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserProviderDetails(user,bindingResult));

	}

	@PostMapping("/mail")
	public ResponseEntity<?> getUserByMail(@RequestBody @NotNull final Map<String, String> request) {
		return  ResponseEntity.ok(userService.getUserByEmail(request.get("userMail")));
     }

}
