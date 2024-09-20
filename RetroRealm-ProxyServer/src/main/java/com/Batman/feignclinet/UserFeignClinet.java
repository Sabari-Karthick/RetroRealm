package com.Batman.feignclinet;

import java.util.Optional;
import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Batman.dto.User;

//@FeignClient(name = "user-service",url = "127.0.0.1:8888")
@FeignClient(name = "user-service")
public interface UserFeignClinet {
	
	@GetMapping("/api/v1/game/calculate/price")
	Double getTotalPrice(@RequestParam("ids[]") Set<Integer> gameId);
	
	@GetMapping("/api/v1/game/calculate/price")
	Optional<User> findByEmail(String userMail);
	
	@PostMapping()
	User saveUser(User user);
	

}
