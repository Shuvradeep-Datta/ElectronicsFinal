package com.deep.electronic.store.services.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.deep.electronic.store.dtos.CreateOrderRequest;
import com.deep.electronic.store.dtos.OrderDto;
import com.deep.electronic.store.entities.Cart;
import com.deep.electronic.store.entities.CartItem;
import com.deep.electronic.store.entities.Order;
import com.deep.electronic.store.entities.OrderItems;
import com.deep.electronic.store.entities.User;
import com.deep.electronic.store.exception.BadApiRequestExtension;
import com.deep.electronic.store.exception.ResourceNotFoundException;
import com.deep.electronic.store.helper.Helper;
import com.deep.electronic.store.payload.PageableResponse;
import com.deep.electronic.store.repository.CartRepository;
import com.deep.electronic.store.repository.OrderRepository;
import com.deep.electronic.store.repository.UserRepository;
import com.deep.electronic.store.services.OrderService;
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	ModelMapper mapper;
	

	
	@Override
	public OrderDto createOrder(CreateOrderRequest orderDto) {
		
		String userId =orderDto.getUserId();
		String cartId =orderDto.getCartId();
		
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("userId not found !!"));
		
		Cart cart = cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFoundException("cartId not found !!"));
		
		List<CartItem> cartItems= cart.getItems();
		
		
		if(cartItems.size()<=0) {
			throw new BadApiRequestExtension("Invalid Number of items in cart !!");
		}
		
		
		Order order = Order.builder()
						.billingName(orderDto.getBillingName())
						.billingAddress(orderDto.getBillingAddress())
						.billingPhone(orderDto.getBillingPhone())
						.orderedDate(new Date())
						.deliveredDate(null)
						.paymentStatus(orderDto.getPaymentStatus())
						.orderStatus(orderDto.getOrderStatus())
						.orderId(UUID.randomUUID().toString())
						.user(user)
						.build();
		
		
		
//		orderItems,amount
		
		AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
		
		List<OrderItems> orderItems = cartItems.stream().map(cartItem -> {
//			CartItem->OrderItem
			OrderItems orderItem = OrderItems.builder()
						.quantity(cartItem.getQuantity())
						.product(cartItem.getProduct())
						.totalPrice(cartItem.getTotalPrice()*cartItem.getProduct().getDiscountedPrice())
						.order(order)
						.build();

			orderAmount.set(orderAmount.get() +orderItem.getTotalPrice());
			
			return orderItem;
		}).collect(Collectors.toList());
		
		order.setOrderItems(orderItems);
		order.setOrderAmount(orderAmount.get());
		
		//clearing the cart
		cart.getItems().clear();
		cartRepository.save(cart);
		Order savedOrder = orderRepository.save(order);
		
		
		return mapper.map(savedOrder, OrderDto.class);
	}

	@Override
	public void removeOrder(String orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("order Id not found !!"));
		orderRepository.delete(order);
		
	}

	@Override
	public List<OrderDto> getOrdersOfUser(String userId) {
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user Id not found !!"));
		
		List<Order> orders = orderRepository.findByUser(user);
		List<OrderDto>orderDto = orders.stream().map(t->mapper.map(t, OrderDto.class)).toList();
		
		return orderDto;
	}

	@Override
	public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
		
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
		Page<Order> page = orderRepository.findAll(pageable);
		
		return Helper.getPageableResponse(page,OrderDto.class);
	}

	@Override
	public OrderDto updateOrderByAdmin(CreateOrderRequest orderDto, String orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order Id Not found !!"));
		order.setOrderStatus(orderDto.getOrderStatus());
		order.setPaymentStatus(orderDto.getPaymentStatus());
		Order saveOrder = orderRepository.save(order);

		OrderDto orderDtos = mapper.map(saveOrder, OrderDto.class);
		return orderDtos;
	}

	@Override
	public OrderDto updateOrderByUser(CreateOrderRequest orderDto, String orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order Id Not found !!"));
		order.setBillingName(orderDto.getBillingName());
		order.setBillingPhone(orderDto.getBillingPhone());
		order.setBillingAddress(orderDto.getBillingAddress());

		Order saveOrder = orderRepository.save(order);

		OrderDto orderDtos = mapper.map(saveOrder, OrderDto.class);
		return orderDtos;
	}

}
