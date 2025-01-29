package com.deep.electronic.store.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {
	
	@Id
	private String cartId;
	
	private Date createdAt;
	
	@OneToOne
	private User user;
	
	//mapping cart Items
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,orphanRemoval = true )
	@Builder.Default
	private List<CartItem> items = new ArrayList<>();
	

}
