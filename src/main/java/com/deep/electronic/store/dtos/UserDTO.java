package com.deep.electronic.store.dtos;

import java.util.ArrayList;
import java.util.List;

import com.deep.electronic.store.entities.Role;
import com.deep.electronic.store.validate.ImageNameValid;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

	private String userId;
	
	@Size(min=3,max =20,message="Invalid Name !!")
	private String name;
	@Email(message ="Invalid Email")
	private String email;
	@Size(min = 5, max = 21, message = "Invalid Email")
	private String password;
	@NotBlank
	private String gender;
	@Size(min=3, max =100, message = "Take a look to About")
	private String about;
	@ImageNameValid
	private String imageName;
	private List<RoleDto> roles = new ArrayList<>();


}
