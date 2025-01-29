package com.deep.electronic.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
	
	private int cartItemId;
	private int quantity;
	private int totalPrice;	
	private ProductDTO product;

}
