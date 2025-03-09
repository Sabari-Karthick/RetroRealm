package com.Batman.restcontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Batman.dto.OrderRequest;
import com.Batman.entity.Order;
import com.Batman.service.IOrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {

	private final IOrderService orderService;

	@PostMapping("/create")
	public ResponseEntity<?> checkout(@Valid @RequestBody final OrderRequest orderRequest,
			BindingResult bindingResult) {
		Order order = orderService.placeOrder(orderRequest, bindingResult);
		return new ResponseEntity<>(order, HttpStatus.CREATED);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<?> fetchAllUserOrders(@PathVariable(value = "userId") final Integer userId) {
		return ResponseEntity.ok(orderService.getOrdersOfUser(userId));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<?> fetchOrderById(@PathVariable(value = "orderId") final Integer orderId) {
		return ResponseEntity.ok(orderService.getOrderById(orderId));
	}

}
