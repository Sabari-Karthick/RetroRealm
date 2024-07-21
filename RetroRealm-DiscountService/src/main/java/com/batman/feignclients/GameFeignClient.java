package com.batman.feignclients;

import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("game-service")
public interface GameFeignClient {
	@PutMapping("/api/v1/game/add/discount")
	public ResponseEntity<?> addDiscountToGames(@RequestParam final Set<Integer> gameIds,@RequestParam Double discountValue);
}
