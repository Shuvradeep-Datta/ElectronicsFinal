package com.deep.electronic.store;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.deep.electronic.store.entities.User;
import com.deep.electronic.store.repository.UserRepository;
import com.deep.electronic.store.security.JwtHelper;

@SpringBootTest
class ElectronicStoreApplicationTests {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	JwtHelper jwtHelper;

	@Test
	void contextLoads() {
	}
	
	@Test
	void generateToken() {
		User user = userRepository.findByEmail("sddatta505@gmail.com").get();
		String token = jwtHelper.generateToken(user);
		System.out.println(token);
		
		String usernameFromToken = jwtHelper.getUsernameFromToken(token);
		System.out.println(usernameFromToken);
		
		boolean tokenExpired = jwtHelper.isTokenExpired(token);
		System.out.println(tokenExpired);
		
	}

}
