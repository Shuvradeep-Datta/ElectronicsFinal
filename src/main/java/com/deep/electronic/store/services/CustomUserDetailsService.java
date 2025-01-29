package com.deep.electronic.store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.deep.electronic.store.entities.User;
import com.deep.electronic.store.exception.ResourceNotFoundException;
import com.deep.electronic.store.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User Not Found !!"));
		return user;
		
		
	}
	
	

}
