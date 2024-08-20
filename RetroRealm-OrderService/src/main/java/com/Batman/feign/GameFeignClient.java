package com.Batman.feign;

import java.util.List;
import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "game-service")
public interface GameFeignClient {
	
	List<String> getGameNamesByGameIds(Set<Integer> gameIds);

}
