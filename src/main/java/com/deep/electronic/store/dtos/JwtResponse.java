package com.deep.electronic.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
	
	private String token;
	private UserDTO userDto;
//	private String jwtToken;
	
	private RefreshTokenDto refreshTokenDto;

}
