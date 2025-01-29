package com.deep.electronic.store.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
/*                                 This is Product Entity Page                                             */
	@Id
	private String productId;
	
	@Column(name = "title")
	private String title;
	@Column(name = "description", length = 10000)
	private String description;
	@Column(name = "price")
	private int price;
	private int discountedPrice;
	@Column(name = "Quantity")
	private int quantity;
	@Column(name = "date")
	private Date addedDate;

	private boolean live;
	private boolean stock;
	
	private String productImage;
	
	@ManyToOne
	@JoinColumn(name = "categoryId")
	private Category category;

}
