package com.deep.electronic.store.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Categories")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Category {
	@Id
	@Column(name = "Category_Id")
	private String categoryId;
	
	@Column(name = "category_title",length=60)
	@NotBlank
	private String title;
	
	@Column(name = "category_desc",length = 50)
	private String description;
	
	private String coverImage;
	
	@OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@Builder.Default
	private List<Product> products =new ArrayList<>();
	

}
