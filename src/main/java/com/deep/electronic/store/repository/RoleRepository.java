package com.deep.electronic.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deep.electronic.store.entities.Role;

public interface RoleRepository extends JpaRepository<Role,String> {
	Optional<Role > findByName(String name);

}
