package com.deep.electronic.store.services.impl;

import java.time.Instant;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deep.electronic.store.dtos.RefreshTokenDto;
import com.deep.electronic.store.dtos.UserDTO;
import com.deep.electronic.store.entities.RefreshToken;
import com.deep.electronic.store.entities.User;
import com.deep.electronic.store.exception.ResourceNotFoundException;
import com.deep.electronic.store.exception.TokenNotFoundException;
import com.deep.electronic.store.repository.RefreshTokenRepository;
import com.deep.electronic.store.repository.UserRepository;
import com.deep.electronic.store.services.RefreshTokenService;

@Service
public class RefreshTokenServiceImpl  implements RefreshTokenService{
	
	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private  RefreshTokenRepository refreshTokenRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	
	

	@Override
	public RefreshTokenDto createRefreshToken(String username) {
		
		User user = userRepository.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User not found !!"));
		
		RefreshToken refreshToken = refreshTokenRepository.findByUser(user).orElse(null);
		if(refreshToken ==null) {
			refreshToken = RefreshToken.builder()
					.user(user)
					.token(UUID.randomUUID().toString())
					.expiryDate(Instant.now().plusSeconds(5*24*3600))
					.build();
		}else {
			refreshToken.setToken(UUID.randomUUID().toString());
			refreshToken.setExpiryDate(Instant.now().plusSeconds(5*24*3600));
		}
		
		RefreshToken saveToken = refreshTokenRepository.save(refreshToken);
		return modelMapper.map(saveToken, RefreshTokenDto.class);
	}

	@Override
	public RefreshTokenDto findByToken(String Token) {
		
		RefreshToken refreshToken = refreshTokenRepository.findByToken(Token).orElseThrow(()->new TokenNotFoundException("Token Not found !!"));
		
		return this.modelMapper.map(refreshToken, RefreshTokenDto.class);
	}

	@Override
	public RefreshTokenDto verifyRefreshToken(RefreshTokenDto token) {
		
		var refreshToken = this.modelMapper.map(token, RefreshToken.class);
		
		if(token.getExpiryDate().compareTo(Instant.now())<0) {
			refreshTokenRepository.delete(refreshToken);
			throw new RuntimeException("Refresh token Expired !!");
		}
		
		
		
		return token;
		
		
		
	}

	@Override
	public UserDTO getUser(RefreshTokenDto dto) {
		RefreshToken refreshToken = refreshTokenRepository.findByToken(dto.getToken())
				.orElseThrow(() -> new ResourceNotFoundException("Token not found !!"));
		User user = refreshToken.getUser();
		return this.modelMapper.map(user, UserDTO.class);
	}


}
