package com.deep.electronic.store.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deep.electronic.store.dtos.UserDTO;
import com.deep.electronic.store.entities.User;
import com.deep.electronic.store.payload.PageableResponse;
@Service
public interface UserService {
	
	//Created
	UserDTO createUser(UserDTO user);
	
	//Update
	UserDTO updateUser(String userId,UserDTO userDto);
	
	//Delete
	void deleteUser(String userId);
	
	//Get All User
	
	PageableResponse<UserDTO> getAllUser(int pageNumber, int pageSize,String sortBy,String sortDir);
	//Get Single User by Id
	UserDTO getUserById(String userId);
	
	//get Single User by email

	UserDTO getUserByEmail(String email);
	//Search user
	List<UserDTO> searchUser(String keywords);
	
	//other user specific feature
	
	

}
