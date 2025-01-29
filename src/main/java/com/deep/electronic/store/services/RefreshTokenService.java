package com.deep.electronic.store.services;

import org.springframework.stereotype.Component;

import com.deep.electronic.store.dtos.RefreshTokenDto;
import com.deep.electronic.store.dtos.UserDTO;

@Component
public interface RefreshTokenService {

	// create

	RefreshTokenDto createRefreshToken(String username);

	// find By Token

	RefreshTokenDto findByToken(String Token);

	// Verify
	RefreshTokenDto verifyRefreshToken(RefreshTokenDto refreshTokenDto);
	
	UserDTO getUser(RefreshTokenDto dto);

}
