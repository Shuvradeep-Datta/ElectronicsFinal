package com.deep.electronic.store.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {
	
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
		
	}

}
