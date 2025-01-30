package com.deep.electronic.store;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.deep.electronic.store.entities.Role;
import com.deep.electronic.store.entities.User;
import com.deep.electronic.store.repository.RoleRepository;
import com.deep.electronic.store.repository.UserRepository;
import com.deep.electronic.store.security.JwtHelper;

@SpringBootApplication
@EnableWebMvc
public class ElectronicStoreApplication implements CommandLineRunner {
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtHelper jwtHelper;
	
	

	public static void main(String[] args) {
		
		
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
//		User user1 = userRepository.findByEmail("sddatta505@gmail.com").get();
//		String token = jwtHelper.generateToken(user1);
//		System.out.println(token);
//		
//		Boolean tokenExpired = jwtHelper.isTokenExpired(token);
//		System.out.println(tokenExpired);
//		
//		
//		Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElse(null);
//
//		if (roleAdmin == null) {
//			Role role1 = new Role();
//			role1.setRoleId(UUID.randomUUID().toString());
//			role1.setName("ROLE_ADMIN");
//			roleRepository.save(role1);
//		}
//		
//		Role roleNormal = roleRepository.findByName("ROLE_NORMAL").orElse(null);
//		if (roleNormal == null) {
//			Role role2 = new Role();
//			role2.setRoleId(UUID.randomUUID().toString());
//			role2.setName("ROLE_NORMAL");
//			roleRepository.save(role2);
//		}
		
//		
//		
		//Creation of Admin User
		
		User user = userRepository.findByEmail("sddatta505@gmail.com").orElse(null);
		if(user==null) {
			user = User.builder()
					.userId(UUID.randomUUID().toString())
					.name("Shuvradeep Datta")
					.email("sddatta505@gmail.com")
					.password(passwordEncoder.encode("deep"))
					.gender("MALE")
					.about("Hi Hello How are you !!")
					.imageName("xyz.png")
//					.roles(List.of(roleAdmin,roleNormal))
					.build();
			System.out.println(user);
			
			userRepository.save(user);
			
//			
//			
	
		}
		
	
		
	}
}
	
		
		
		

	


