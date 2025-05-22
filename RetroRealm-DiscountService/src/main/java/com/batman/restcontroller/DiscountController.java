package com.batman.restcontroller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.batman.dto.discount.DiscountRequest;
import com.batman.entity.Discount;
import com.batman.service.IDiscountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/v1/discount/")
@RequiredArgsConstructor
public class DiscountController {

	private final IDiscountService discountService;
	
	@PostMapping("/create")
	public ResponseEntity<?> createDiscount(@Valid @RequestBody final DiscountRequest request,BindingResult bindingResult) {
		Discount discount = discountService.createDiscount(request, bindingResult);
		return new ResponseEntity<>(discount,HttpStatus.CREATED);
	}
	
	
	@GetMapping("/game/search")
	public ResponseEntity<?> fetchDiscounts(@RequestParam final Set<String> gameIds ) {
		return ResponseEntity.ok(discountService.getDiscountByGameIds(gameIds));
	}
	
	
	@PatchMapping("/expire/{discountId}")
	public ResponseEntity<?> expireDiscount(@PathVariable final Integer discountId) {
		return ResponseEntity.ok(discountService.expireDiscount(discountId));
	}
	
	
	
	
}
