package com.deep.electronic.store.services.impl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deep.electronic.store.dtos.AddItemToCartRequest;
import com.deep.electronic.store.dtos.CartDTO;
import com.deep.electronic.store.entities.Cart;
import com.deep.electronic.store.entities.CartItem;
import com.deep.electronic.store.entities.Product;
import com.deep.electronic.store.entities.User;
import com.deep.electronic.store.exception.BadApiRequestExtension;
import com.deep.electronic.store.exception.ResourceNotFoundException;
import com.deep.electronic.store.repository.CartItemRepository;
import com.deep.electronic.store.repository.CartRepository;
import com.deep.electronic.store.repository.ProductRepository;
import com.deep.electronic.store.repository.UserRepository;
import com.deep.electronic.store.services.CartService;
@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	CartItemRepository cartItemRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

	@Override
	public CartDTO addItemToCart(String userId, AddItemToCartRequest request) {

		// fetch the product Id & Quantity
		String productId = request.getProductId();
		int quantity = request.getQuantity();
		
		
		
		if(quantity<=0) {
			throw new BadApiRequestExtension("Quantity should be not less than 0 !!");
		}
		// find product from db
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Id Not Found in database!!"));

		// find User from db
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found in database !!!"));

		
		// find cart from Cart Repository
		Cart cart = null;
		try {
			cart = cartRepository.findByUser(user).get();
		} catch (NoSuchElementException ex) {
			cart = new Cart();
			cart.setCreatedAt(new Date());
			cart.setCartId(UUID.randomUUID().toString());
		}

		// perform Operation
		// if cart item already present; then update

//		List<CartItem> items = cart.getItems();
//		boolean updated =false;

		AtomicReference<Boolean> updated = new AtomicReference<>(false);
		List<CartItem> items = cart.getItems();
		items = items.stream().map(item -> {

			if (item.getProduct().getProductId().equals(productId)) {
				item.setQuantity(quantity);
				item.setTotalPrice(quantity * product.getDiscountedPrice());
				updated.set(true);
			}

			return item;
		}).collect(Collectors.toList());
//		cart.setItems(updatedItems);
	
		
		
		
		

		if (!updated.get()) {
			CartItem cartItem = CartItem.builder().quantity(quantity).totalPrice(quantity * product.getDiscountedPrice())
					.cart(cart).product(product).build();

			cart.getItems().add(cartItem);
		} else {

		}

		cart.setUser(user);
		Cart updatedCart = cartRepository.save(cart);
		
		

		return mapper.map(updatedCart, CartDTO.class);
	}

	@Override
	public void removeItemFromCart(String userId, int cartItem) {
	
		CartItem cartItem2 = cartItemRepository.findById(cartItem).orElseThrow(()->new ResourceNotFoundException("Cart Item not found !!"));
		cartItemRepository.delete(cartItem2);
	}

	@Override
	public void clearItem(String userId) {
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found  !!"));
		Cart cart = cartRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException("cart not found"));
		cart.getItems().clear();
		
		
		cartRepository.save(cart);
	}

	@Override
	public CartDTO getCartByUser(String userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User Id not Found !!"));

		Cart cart = cartRepository.findByUser(user).get();

		return mapper.map(cart, CartDTO.class);
	}

	

}
