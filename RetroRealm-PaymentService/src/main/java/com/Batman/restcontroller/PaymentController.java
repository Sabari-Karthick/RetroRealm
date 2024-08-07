package com.Batman.restcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Batman.dto.PaymentRequest;
import com.Batman.entity.Payment;
import com.Batman.enums.PaymentStatus;
import com.Batman.service.IPaymentService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
	
	private final IPaymentService paymentService;

	@PostMapping("/pay")
	ResponseEntity<?> pay(@RequestBody PaymentRequest paymentRequest) {
		Payment payment = paymentService.pay(paymentRequest.getAmount(), paymentRequest.getPaymentType());
		if(payment.getPaymentStatus().equals(PaymentStatus.COMPLETED)) {
			return ResponseEntity.ok(payment.getId());
		}else {
			return ResponseEntity.internalServerError().body(payment.getId());
		}
	}
	
}
