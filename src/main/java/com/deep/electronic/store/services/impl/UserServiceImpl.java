package com.deep.electronic.store.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.deep.electronic.store.dtos.UserDTO;
import com.deep.electronic.store.entities.Role;
import com.deep.electronic.store.entities.User;
import com.deep.electronic.store.exception.ResourceNotFoundException;
import com.deep.electronic.store.helper.Helper;
import com.deep.electronic.store.payload.PageableResponse;
import com.deep.electronic.store.repository.RoleRepository;
import com.deep.electronic.store.repository.UserRepository;
import com.deep.electronic.store.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private ModelMapper mapper;
	
	@Value("${spring.user.images.path}")
	private String imageUploadPath;


	@Override
	public UserDTO createUser(UserDTO userDto) {
		String userId = UUID.randomUUID().toString();
		userDto.setUserId(userId);
		
		User user= dtoToEntity(userDto);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		
		//Get the Normal User
		Role role = new Role();
		role.setRoleId(UUID.randomUUID().toString());
		role.setName("ROLE_NORMAL");
		Role roleNormal = roleRepository.findByName("ROLE_NORMAL").orElse(role);
		user.setRoles(List.of(roleNormal));
		
		
		User save = userRepository.save(user);
		
		UserDTO userDtos =entityToDto(save);
		
		return userDtos;
		
		
	}

	

	@Override
	public UserDTO updateUser(String userId, UserDTO userDto) {
// Finding User
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("userId not Found"));

		user.setName(userDto.getName());
		// Email not required....
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setGender(userDto.getGender());
		user.setAbout(userDto.getAbout());
		user.setImageName(userDto.getImageName());
		User saveUser = userRepository.save(user);
		
		

		UserDTO userDtos = entityToDto(saveUser);
		
// Save user
		

		return userDtos;
	}

	@Override
	public void deleteUser(String userId) {
		
		//finding its there or not
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("UserId not found"));
		
		
		String fullPath = imageUploadPath+user.getImageName();
		
	
		try {
			Path path = Path.of(fullPath);
			Files.delete(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		userRepository.delete(user);

	}

	@Override
	public PageableResponse<UserDTO> getAllUser(int pageNumber, int pageSize,String sortBy,String sortDir) {
		
		Sort sort =(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
		
		
		
		

		Page<User> allPages = userRepository.findAll(pageable);
		PageableResponse<UserDTO> response = Helper.getPageableResponse(allPages, UserDTO.class);
		
		return response;
		
//		List<UserDTO> getAllUserDto = new ArrayList<>();
//
//		for (User user : getAllUser) {
//			UserDTO dto = entityToDto(user);
//			getAllUserDto.add(dto);
//		}
//
//		return getAllUserDto;
		

	}

	@Override
	public UserDTO getUserById(String userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("userId not Found"));

		return entityToDto(user);
	}

	@Override
	public UserDTO getUserByEmail(String email) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Email Not found Exception"));
		return entityToDto(user);
	}

	@Override
	public List<UserDTO> searchUser(String keywords) {
		List<User> user = userRepository.findByNameContaining(keywords);
		List<UserDTO> userDtos = user.stream().map((t) -> entityToDto(t)).toList();
		return userDtos;

	}
	
	private UserDTO entityToDto(User user) {

//		UserDTO userDTO = UserDTO.builder().userId(user.getUserId()).name(user.getName()).email(user.getEmail())
//				.password(user.getPassword()).gender(user.getGender()).about(user.getAbout())
//				.imageName(user.getImageName()).build();
		return mapper.map(user, UserDTO.class);
	}


	private User dtoToEntity(UserDTO userDto) {

//		User user = User.builder().userId(userDto.getUserId()).name(userDto.getName()).email(userDto.getEmail())
//				.password(userDto.getPassword()).gender(userDto.getGender()).about(userDto.getAbout())
//				.imageName(userDto.getImageName()).build();
		return mapper.map(userDto,User.class);
	}

}
