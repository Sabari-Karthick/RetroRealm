package com.Batman.feignclinet;

import java.util.Map;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.Batman.dto.User;

//@FeignClient(name = "user-service",url = "127.0.0.1:8888")
@FeignClient(name = "user-service")
public interface UserFeignClinet {
	
	@PostMapping("/api/v1/users/mail")
	Optional<User> getUserByMail(@RequestBody Map<String, String> request);
	
	@PostMapping("/api/v1/users/register")
	User registerUser(@RequestBody User user);
	

}
