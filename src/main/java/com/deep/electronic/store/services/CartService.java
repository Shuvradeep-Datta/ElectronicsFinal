package com.deep.electronic.store.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deep.electronic.store.dtos.AddItemToCartRequest;
import com.deep.electronic.store.dtos.CartDTO;

@Service
public interface CartService {

	//Add Item to Cart
	CartDTO addItemToCart(String userId, AddItemToCartRequest request);
	
	//Remove Item From Cart
	void removeItemFromCart(String userId,int cartItem);
	
	
	//remove all Item from cart
	
	void clearItem(String userId);

	CartDTO getCartByUser(String userId);
}
