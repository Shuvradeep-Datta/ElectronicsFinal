package com.deep.electronic.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deep.electronic.store.dtos.AddItemToCartRequest;
import com.deep.electronic.store.dtos.CartDTO;
import com.deep.electronic.store.payload.ApiResponseMessage;
import com.deep.electronic.store.services.CartService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/carts")
@Tag(name = "CartController",description = "This is cart based controller")
public class CartController {
	
	@Autowired
	private CartService cartService;

	//add Item to the cart
//	@PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
	@PostMapping("/{userId}")
	public ResponseEntity<CartDTO> addItemToCart(@PathVariable String userId,
			@RequestBody AddItemToCartRequest request) {

		CartDTO cartDto = cartService.addItemToCart(userId, request);
		return new ResponseEntity<CartDTO>(cartDto, HttpStatus.OK);
	}
	
//	@PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
	@DeleteMapping("/{userId}/items/{itemId}")
	public ResponseEntity<ApiResponseMessage> deleteItermToCart(@PathVariable String userId, @PathVariable int itemId) {

		cartService.removeItemFromCart(userId, itemId);
		ApiResponseMessage message = ApiResponseMessage.builder().message("Item is removed !!").success(true)
				.httpStatus(HttpStatus.OK).build();
		return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
	}
//	@PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> clearItem(@PathVariable String userId) {

		cartService.clearItem(userId);
		ApiResponseMessage message = ApiResponseMessage.builder().message("Cart is cleared").success(true)
				.httpStatus(HttpStatus.OK).build();
		return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
	}
//	@PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
	@GetMapping("/{userId}")
	public ResponseEntity<CartDTO> addItemToCart(@PathVariable String userId) {

		CartDTO cartByUser = cartService.getCartByUser(userId);

		return new ResponseEntity<>(cartByUser, HttpStatus.OK);
	}
	
	
	
	
	
}
