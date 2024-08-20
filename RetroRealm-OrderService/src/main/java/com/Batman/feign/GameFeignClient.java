package com.Batman.feign;

import java.util.List;
import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "game-service")
public interface GameFeignClient {
	
	@GetMapping("/names")
	public List<String> getNamesByGameId(@RequestParam("ids[]") final Set<Integer> gameIds);

}
