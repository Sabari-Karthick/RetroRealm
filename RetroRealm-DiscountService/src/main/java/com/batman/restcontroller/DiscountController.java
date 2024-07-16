package com.batman.restcontroller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.batman.dto.discount.DiscountRequest;
import com.batman.entity.Discount;
import com.batman.service.IDiscountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/v1/discount/")
@RequiredArgsConstructor
@Slf4j
public class DiscountController {

	private final IDiscountService service;
	
	@PostMapping("/create")
	public ResponseEntity<?> postMethodName(@Valid @RequestBody final DiscountRequest request,BindingResult bindingResult) {
		log.info("Discount Creation Request Arrived ......");
		Discount discount = service.createDiscount(request, bindingResult);
		return new ResponseEntity<Discount>(discount,HttpStatus.CREATED);
	}
	
	
	@GetMapping("/search")
	public ResponseEntity<?> fetchDiscounts(@RequestParam("ids[]") final Set<Integer> gameIds) {
		return ResponseEntity.ok(service.getDiscountByGameIds(gameIds));
	}
	
	
	
}
