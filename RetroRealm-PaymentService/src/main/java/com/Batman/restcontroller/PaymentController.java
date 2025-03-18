package com.Batman.restcontroller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
	
//	private final IPaymentService paymentService;

//	@PostMapping("/pay")
//	ResponseEntity<?> pay(@RequestBody PaymentRequest paymentRequest) {
//		Payment payment = paymentService.pay(paymentRequest.getAmount(), paymentRequest.getPaymentType());
//		if(payment.getPaymentStatus().equals(PaymentStatus.COMPLETED)) {
//			return ResponseEntity.ok(payment.getPaymentId());
//		}else {
//			return ResponseEntity.internalServerError().body(payment.getPaymentId());
//		}
//	}
	
}
