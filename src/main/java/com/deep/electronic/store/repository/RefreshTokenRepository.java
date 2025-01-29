package com.deep.electronic.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deep.electronic.store.entities.RefreshToken;
import com.deep.electronic.store.entities.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer>{
	
	Optional<RefreshToken> findByToken(String token);
	
	Optional<RefreshToken> findByUser (User user);

}
