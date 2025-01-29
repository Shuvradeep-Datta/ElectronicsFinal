package com.deep.electronic.store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deep.electronic.store.entities.Category;
import com.deep.electronic.store.entities.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, String>{

	Page<Product> findByTitleContaining(String subTitles,Pageable pageable);
	
	Page<Product>findByLiveTrue(Pageable pageable);
	
	Page<Product> findByCategory(Category category,Pageable pageable);
}
