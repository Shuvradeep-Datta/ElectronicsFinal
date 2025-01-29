package com.deep.electronic.store.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deep.electronic.store.dtos.CreateOrderRequest;
import com.deep.electronic.store.dtos.OrderDto;
import com.deep.electronic.store.payload.PageableResponse;

@Service
public interface OrderService {

	//create 
	
	OrderDto createOrder(CreateOrderRequest orderDto);
	
	//remove
	
	void removeOrder(String orderId);
	
	//get order of User
	
	List<OrderDto>getOrdersOfUser(String userId);
	
	//get orders
	
	PageableResponse<OrderDto>getOrders(int pageNumber, int pageSize,String sortBy,String sortDir);
	
	//Update By Admin
	OrderDto updateOrderByAdmin(CreateOrderRequest orderDto,String orderId);
	
	//Update by End User
	OrderDto updateOrderByUser(CreateOrderRequest orderDto,String orderId);
	
	
}
