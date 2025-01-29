package com.deep.electronic.store.payload;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponseMessage {

	
	private String imageName;
	private String message;
	private boolean success;
	private HttpStatus satus;
}
