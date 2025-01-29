package com.deep.electronic.store.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deep.electronic.store.dtos.UserDTO;
import com.deep.electronic.store.payload.ApiResponseMessage;
import com.deep.electronic.store.payload.PageableResponse;
import com.deep.electronic.store.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Tag(name = "UserController",description = "This is User based Controller")
public class UserController {

	@Autowired
	private UserService userService;

	Logger logger = LoggerFactory.getLogger(UserController.class);
	//Create User 
	
	@PostMapping("/")
	@Operation(summary = "create new User !!")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "Success | OK"),
			@ApiResponse(responseCode = "401",description = "not authorized !!"),
			@ApiResponse(responseCode = "201",description = "new User created !!")
	})
	public ResponseEntity<UserDTO> createUser( @Valid @RequestBody UserDTO user) {
		UserDTO createUser = userService.createUser(user);
		logger.info("User Created Successfully:{}", user);
		return new ResponseEntity<>(createUser, HttpStatus.CREATED);
	}
	//Update
	@PutMapping("/{userId}")
	public ResponseEntity<UserDTO> updateUser(@PathVariable String userId,@Valid @RequestBody UserDTO userDto) {
		UserDTO updateUser = userService.updateUser(userId, userDto);
		logger.info("User want to updated with id:{}",userId);
		logger.info("User updated Successfully {}",updateUser);
		return new ResponseEntity<>(updateUser, HttpStatus.OK);
	}
	
	@GetMapping("/")
	@Operation(summary = "get All User",tags = {"user-controller"})
	public ResponseEntity<PageableResponse<UserDTO>>getAllUser(
			@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber , 
			@RequestParam(value = "pageSize",defaultValue = "10",required = false)  int pageSize,
			@RequestParam(value="sortBy",defaultValue = "name",required=false) String sortBy,
			@RequestParam(value = "sortDir",defaultValue = "desc",required = false)String sortDir){
		PageableResponse<UserDTO> getAllList = userService.getAllUser(pageNumber,pageSize,sortBy,sortDir);
		
		return new ResponseEntity<PageableResponse<UserDTO>>(getAllList, HttpStatus.OK);
	}
	
	//Delete
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) {

		userService.deleteUser(userId);
		ApiResponseMessage message = ApiResponseMessage.builder().message("User Deleted Successfully").success(true)
				.httpStatus(HttpStatus.OK).build();

		logger.info("User Deleted Successfully");
		return new ResponseEntity<>(message, HttpStatus.OK);

	}
	//Get By Id
	@GetMapping("/{userId}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) {

		 UserDTO userById = userService.getUserById(userId);
		logger.info("User getting SUccessfully{}",userById);
		return new ResponseEntity<>(userById, HttpStatus.OK);

	}
	//Get By Email
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
		UserDTO userDto = userService.getUserByEmail(email);

		return new ResponseEntity<>(userDto, HttpStatus.OK);

	}
	//Get By KeyWord
	
	@GetMapping("/search/{keywords}")
	public ResponseEntity<List<UserDTO>> searchUser(@PathVariable String keywords){
		List<UserDTO> searchUser = userService.searchUser(keywords);
		return new ResponseEntity<List<UserDTO>>(searchUser, HttpStatus.OK);
	}
	
	
	

	
	
	

}
