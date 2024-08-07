package com.Batman.restcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Batman.dto.PaymentRequest;
import com.Batman.service.IPaymentService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
	
	private final IPaymentService paymentService;

	@GetMapping("/pay")
	ResponseEntity<?> pay(@RequestBody PaymentRequest paymentRequest) {
		return ResponseEntity.ok(paymentService.pay(paymentRequest.getAmount(), paymentRequest.getPaymentType()));
	}
	
}
