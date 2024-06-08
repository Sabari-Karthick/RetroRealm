package com.Batman.feignclinet;

import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "game-service")
public interface GameFeignClinet {
	
	@GetMapping("/api/v1/game/calculate/price")
	public Double getTotalPrice(@RequestParam("ids[]") Set<Integer> gameId);

}
