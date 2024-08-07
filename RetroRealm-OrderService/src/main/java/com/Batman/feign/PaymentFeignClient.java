package com.Batman.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.Batman.dto.PaymentRequest;


@FeignClient("PAYMENT-SERVICE")
public interface PaymentFeignClient {
	
	@PostMapping("/pay")
	ResponseEntity<?> pay(@RequestBody PaymentRequest paymentRequest);

}
