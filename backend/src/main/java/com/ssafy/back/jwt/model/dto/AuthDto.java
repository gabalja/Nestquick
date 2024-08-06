package com.ssafy.back.jwt.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDto {
	private String userId;
	private String refreshToken;
	
	public AuthDto(String userId, String refreshToken) {
		this.userId = userId;
		this.refreshToken = refreshToken;
	}
}
