package com.Batman.restcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

	@GetMapping("/pay")
	String pay() {
		return "Payed!!";
	}
	@GetMapping("/getPay")
	String getPay() {
		return "Bill";
	}
}
