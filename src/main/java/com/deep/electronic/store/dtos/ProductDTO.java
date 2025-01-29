package com.deep.electronic.store.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

	
	private String productId;
	private String title;
	private String description;
	private int price;
	private int discountedPrice;
	private int quantity;
	private Date addedDate;
	private boolean live;
	private boolean stock;
	
	private String productImage;
	private CategoryDTO category;
	
}
