package com.deep.electronic.store.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.deep.electronic.store.payload.ApiResponseMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	
	// Exception handling for Resource Not Found..
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponseMessage> resourceNotFoundException(ResourceNotFoundException ex) {

		logger.info("Exception Occured !!");
		ApiResponseMessage response = ApiResponseMessage.builder().message(ex.getMessage())
				.httpStatus(HttpStatus.NOT_FOUND).success(true).build();
		return new ResponseEntity<ApiResponseMessage>(response, HttpStatus.NOT_FOUND);

	}
	
	// Exception handling for Token Not Found..
		@ExceptionHandler(TokenNotFoundException.class)
		public ResponseEntity<ApiResponseMessage> tokenNotFoundException(TokenNotFoundException ex) {

			logger.info("Exception Occured !!");
			ApiResponseMessage response = ApiResponseMessage.builder().message(ex.getMessage())
					.httpStatus(HttpStatus.NOT_FOUND).success(true).build();
			return new ResponseEntity<ApiResponseMessage>(response, HttpStatus.NOT_FOUND);

		}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handlerMethodArgumentNotValidException(
			MethodArgumentNotValidException ex) {
		Map<String, Object> response = new HashMap<>();
		List<ObjectError> errors = ex.getAllErrors();
		errors.stream().forEach(t -> {
			String message = t.getDefaultMessage();
			String field = ((FieldError) t).getField();
			response.put(field, message);
		});
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handlerIllegalArgumentException(IllegalArgumentException ex){
		Map<String, Object> response = new HashMap<>();
		String message = ex.getMessage();
		response.put("The cause of the error", message);
		System.out.println("Please change the default page Size");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
			
	}
	
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, Object>> handlerDataIntegrityViolationException(DataIntegrityViolationException ex){
		Map<String, Object> response = new HashMap<>();
		String message = ex.getMessage();
		response.put("The Cause of the error", message);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	// Exception handling for Bad Api Request Exception Not Found..
		@ExceptionHandler(BadApiRequestExtension.class)
		public ResponseEntity<ApiResponseMessage> resourceBadApiRequestException(BadApiRequestExtension ex) {

			logger.info("Exception Occured !!");
			ApiResponseMessage response = ApiResponseMessage.builder().message(ex.getMessage())
					.httpStatus(HttpStatus.BAD_REQUEST).success(false).build();
			return new ResponseEntity<ApiResponseMessage>(response, HttpStatus.BAD_REQUEST);

		}
		
		
	

}
