package com.batman.feignclients;

import java.util.Set;

import com.batman.constants.ExternalServiceConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("game-service")
public interface GameFeignClient {

    @PutMapping(value = ExternalServiceConstants.GAME_SERVICE_ADD_DISCOUNT)
    ResponseEntity<?> addDiscountToGames(@RequestParam final Set<Integer> gameIds, @RequestParam Double discountValue);

    @PostMapping(value = ExternalServiceConstants.GAME_SERVICE_VALIDATE_IDS)
    ResponseEntity<Boolean> validateGameIds(@RequestBody final Set<String> gameIds);

}
