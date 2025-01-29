package com.deep.electronic.store.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDTO {
	
	private String categoryId;
	
	@NotBlank
	@Min(value =4 , message="title must be of minimum 4 characters !!")
	private String title;
	
	
	@NotBlank(message = "Description required !!")
	private String description;
	
	private String coverImage;
	
}
