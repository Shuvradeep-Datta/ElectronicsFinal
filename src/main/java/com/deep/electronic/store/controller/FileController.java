package com.deep.electronic.store.controller;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.deep.electronic.store.dtos.UserDTO;
import com.deep.electronic.store.payload.ImageResponseMessage;
import com.deep.electronic.store.services.FileService;
import com.deep.electronic.store.services.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
public class FileController {
	
		@Autowired
		UserService userService;
		
	
		
		@Autowired
		FileService fileService;
		
		@Value("${spring.user.images.path}")
		private String imageUploadPath;
		
		
	

	//Upload Image
	
	
	@PostMapping("/upload/{userId}")
//	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<ImageResponseMessage> uploadUserImage(@PathVariable("userId") String userId  ,@RequestParam("userImage") MultipartFile image) throws IOException {
		String imageName = fileService.uploadFile(image, imageUploadPath);
		
		UserDTO user = userService.getUserById(userId);
		user.setImageName(imageName);
		UserDTO updateUser = userService.updateUser(userId, user);
		ImageResponseMessage imageResponseMessage = ImageResponseMessage.builder().imageName(imageName).success(true).satus(HttpStatus.CREATED).message("Image Uploaded Successfully !!").build();
		return new ResponseEntity<ImageResponseMessage>(imageResponseMessage,HttpStatus.CREATED);

		
	}
	
	
	
	// Serve Image....

	@GetMapping("/uplodImage/{userId}")
//	@PreAuthorize("hasAnyRole('ADMIN')")
	public void getUserImage(@PathVariable("userId") String userId, HttpServletResponse response) throws IOException {
		UserDTO user = userService.getUserById(userId);
		InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);

		StreamUtils.copy(resource, response.getOutputStream());

	}
	

	
	
	
	

	
	
	
	
	
	
}
