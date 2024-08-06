package com.ssafy.back.jwt.model.service;

import com.ssafy.back.jwt.model.dto.AuthDto;

public interface AuthService {
	int setRefreshToken(AuthDto authDto);
	AuthDto getRefreshToken(String userId);
	int updateRefreshToken(AuthDto authDto);
}	
