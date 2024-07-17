package com.Batman.feignclinet;

import java.util.List;
import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Batman.dto.discount.DiscountDto;

@FeignClient(name = "discount-service")
public interface DiscountFeignClinet {
	
	@GetMapping("/game/search")
	List<DiscountDto> fetchDiscounts(@RequestParam final Set<Integer> gameIds);
}
