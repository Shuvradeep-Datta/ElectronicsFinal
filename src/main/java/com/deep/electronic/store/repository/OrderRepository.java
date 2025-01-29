package com.deep.electronic.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deep.electronic.store.entities.Order;
import com.deep.electronic.store.entities.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
	List<Order> findByUser(User user);
}
