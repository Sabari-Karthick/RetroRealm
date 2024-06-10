package com.Batman.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.Batman.dto.CartValueResponse;

@FeignClient("CART-SERVICE")
public interface CartFeignClient {

	@GetMapping("api/v1/cart/value/{userId}")
	CartValueResponse getUserCartValue(@PathVariable("userId") final Integer userId);
}
