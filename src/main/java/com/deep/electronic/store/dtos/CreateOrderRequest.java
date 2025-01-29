package com.deep.electronic.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreateOrderRequest {
	
	private String cartId;
	private String userId;
	private String orderStatus="PENDING";
	private String paymentStatus="NOTPAID";
	private String billingAddress;
	private String billingPhone;
	private String billingName;
	


	
}
