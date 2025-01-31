package com.deep.electronic.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deep.electronic.store.entities.CartItem;
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer>{

}
