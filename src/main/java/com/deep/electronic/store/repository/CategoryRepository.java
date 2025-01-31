package com.deep.electronic.store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deep.electronic.store.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

	Page<Category> findByTitleContaining(String keywords,Pageable pageable);
}
