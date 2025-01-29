package com.deep.electronic.store.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
	private String cartId;
	private Date createdAt;
	private UserDTO user;
	private List<CartItemDTO> items = new ArrayList<>();
	
	

}
