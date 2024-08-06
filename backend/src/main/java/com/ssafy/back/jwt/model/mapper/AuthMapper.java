package com.ssafy.back.jwt.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.back.jwt.model.dto.AuthDto;

@Mapper
public interface AuthMapper {
	AuthDto selectRefreshToken(String userId);
	int insertRefreshToken(AuthDto authDto);
	int updateRefreshToken(AuthDto authDto);
}
