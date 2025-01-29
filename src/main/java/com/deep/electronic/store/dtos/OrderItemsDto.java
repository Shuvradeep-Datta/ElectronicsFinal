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
public class OrderItemsDto {
	private int orderItemId;

	private int quantity;

	private int totalPrice;

	private ProductDTO product;

	
}
