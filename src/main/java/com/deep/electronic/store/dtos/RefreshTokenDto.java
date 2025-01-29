package com.deep.electronic.store.dtos;

import java.time.Instant;

import com.deep.electronic.store.entities.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDto {

	private int Id;
	private String token;
	private Instant expiryDate;
	
}
