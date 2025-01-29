package com.deep.electronic.store.controller;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.deep.electronic.store.dtos.JwtRequest;
import com.deep.electronic.store.dtos.JwtResponse;
import com.deep.electronic.store.dtos.RefreshTokenDto;
import com.deep.electronic.store.dtos.RefreshTokenRequest;
import com.deep.electronic.store.dtos.UserDTO;
import com.deep.electronic.store.entities.User;
import com.deep.electronic.store.exception.BadApiRequestExtension;
import com.deep.electronic.store.security.JwtHelper;
import com.deep.electronic.store.services.RefreshTokenService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtHelper jwtHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	RefreshTokenService refreshTokenService;
	
	@Autowired
	ModelMapper mapper;
	
	Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	
	
	
	@PostMapping("/regenerate-Token")
	public ResponseEntity<JwtResponse> regenerateRefreshToken(@RequestBody RefreshTokenRequest request) {

		RefreshTokenDto refreshTokenDto = refreshTokenService.findByToken(request.getRefreshToken());
		RefreshTokenDto verifyRefreshToken = refreshTokenService.verifyRefreshToken(refreshTokenDto);
		UserDTO user = refreshTokenService.getUser(verifyRefreshToken);
		String jwtToken = jwtHelper.generateToken(mapper.map(user, User.class));

		JwtResponse jwtResponse = JwtResponse.builder().token(jwtToken).userDto(user).refreshTokenDto(refreshTokenDto)
				.build();

		return new ResponseEntity<JwtResponse>(jwtResponse, HttpStatus.CREATED);

	}
	
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) throws Exception {
		logger.info("Username {},  Password {}",jwtRequest.getEmail(),jwtRequest.getPassword());
		
		
		this.doAuthenticate(jwtRequest.getEmail(),jwtRequest.getPassword());
		
		User user = (User) userDetailsService.loadUserByUsername(jwtRequest.getEmail());
		String token = jwtHelper.generateToken(user);
		//Refresh Token
		
		RefreshTokenDto refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
		
		JwtResponse response = JwtResponse.builder()
									.token(token)
									.userDto(mapper.map(user, UserDTO.class))
									.refreshTokenDto(refreshToken)
									.build();
		return new ResponseEntity<JwtResponse>(response,HttpStatus.OK);
		
		
	}

	private void doAuthenticate(String email, String password) throws Exception {
		Authentication authentication  = new UsernamePasswordAuthenticationToken(email, password);
		try {
			
			authenticationManager.authenticate(authentication);
		}catch (BadCredentialsException e) {
            throw new Exception(" Invalid Username or Password  !!");
        }

		
	}

	

}
