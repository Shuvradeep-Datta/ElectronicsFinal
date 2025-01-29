package com.deep.electronic.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deep.electronic.store.entities.Cart;
import com.deep.electronic.store.entities.User;
@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
	Optional<Cart> findByUser(User user);

}
