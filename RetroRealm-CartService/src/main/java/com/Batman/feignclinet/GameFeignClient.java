package com.Batman.feignclinet;

import java.util.Set;

import com.Batman.dto.cart.GameInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "game-service")
public interface GameFeignClient {
	
	@GetMapping("/api/v1/game/calculate/price")
	Double getTotalPrice(@RequestParam("ids[]") Set<String> gameId);

	@GetMapping("/api/v1/game/{gameId}")
	GameInfo getGameInfoById(@PathVariable String gameId);

}
