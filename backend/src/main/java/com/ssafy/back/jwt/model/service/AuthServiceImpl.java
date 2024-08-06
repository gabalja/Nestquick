package com.ssafy.back.jwt.model.service;

import org.springframework.stereotype.Service;

import com.ssafy.back.jwt.model.dto.AuthDto;
import com.ssafy.back.jwt.model.mapper.AuthMapper;

@Service
public class AuthServiceImpl implements AuthService {

	private final AuthMapper authMapper;
	
	private AuthServiceImpl(AuthMapper authMapper) {
		this.authMapper = authMapper;
	}
	
	@Override
	public int setRefreshToken(AuthDto authDto) {
		return authMapper.insertRefreshToken(authDto);
	}
	
	@Override
	public AuthDto getRefreshToken(String userId) {
		return authMapper.selectRefreshToken(userId);
	}
	
	@Override
	public int updateRefreshToken(AuthDto authDto) {
		return authMapper.updateRefreshToken(authDto);
	}
	
}
