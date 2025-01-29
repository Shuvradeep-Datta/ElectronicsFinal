package com.deep.electronic.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deep.electronic.store.dtos.CreateOrderRequest;
import com.deep.electronic.store.dtos.OrderDto;
import com.deep.electronic.store.payload.PageableResponse;
import com.deep.electronic.store.services.OrderService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/orders")
@Tag(name = "OrderController")
public class OrderController {
	
	@Autowired
	private OrderService orderService;

	// Create
	@PostMapping("/")
//	@PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
	public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest orderDto) {
		OrderDto order = orderService.createOrder(orderDto);
		return new ResponseEntity<OrderDto>(order, HttpStatus.CREATED);

	}

	// Delete
	@DeleteMapping("/{orderId}")
//	@PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
	public ResponseEntity<String> removeOrder(@PathVariable String orderId) {
		orderService.removeOrder(orderId);
		return new ResponseEntity<String>(orderId + "Deleted Successfully", HttpStatus.OK);
	}

	// Get By User
//	@PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
	@GetMapping("/users/{userId}")
	public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId) {
		List<OrderDto> orderOfUser = orderService.getOrdersOfUser(userId);
		return new ResponseEntity<List<OrderDto>>(orderOfUser, HttpStatus.OK);
	}

	// Get
//	@PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
	@GetMapping("/")
	public ResponseEntity<PageableResponse<OrderDto>> getOrders(
			@RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(name = "pageNumber", defaultValue = "10", required = false) int pageSize,
			@RequestParam(name = "sortBy", defaultValue = "orderStatus", required = false) String sortBy,
			@RequestParam(name = "sortDir", defaultValue = "desc", required = false) String sortDir) {
		PageableResponse<OrderDto> orders = orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);

		return new ResponseEntity<PageableResponse<OrderDto>>(orders, HttpStatus.OK);
	}
//	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/admin/{orderId}")
	public ResponseEntity<OrderDto> updateOrderByAdmin(@RequestBody CreateOrderRequest orderDto,@PathVariable String orderId) {
		OrderDto updateOrderByAdmin = orderService.updateOrderByAdmin(orderDto, orderId);
		return new ResponseEntity<OrderDto>(updateOrderByAdmin, HttpStatus.OK);
	}
//	@PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
	@PutMapping("/user/{orderId}")
	public ResponseEntity<OrderDto> updateOrderByUser(@RequestBody CreateOrderRequest orderDto,@PathVariable String orderId) {
		OrderDto updateOrderByUser = orderService.updateOrderByUser(orderDto, orderId);
		return new ResponseEntity<OrderDto>(updateOrderByUser, HttpStatus.OK);
	}
	
}

