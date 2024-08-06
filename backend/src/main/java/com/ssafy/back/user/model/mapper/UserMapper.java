package com.ssafy.back.user.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.back.user.model.dto.UserDto;

@Mapper
public interface UserMapper {
	int selectCntById(String userId) throws SQLException;
	void insertUser(UserDto userDto) throws SQLException;
	
	UserDto selectLoginUser(UserDto userDto) throws SQLException;
	
	UserDto selectUserById(String userId) throws SQLException;

	int selectUserCnt() throws SQLException;
	
	List<UserDto> selectAllUser() throws SQLException;
	
	String selectId(Map<String, String> findInfo) throws SQLException;
	String selectPwd(Map<String, String> findInfo) throws SQLException;
	
	
	boolean updateUser(Map<String, String> userInfo) throws SQLException;
	boolean updatePwd(Map<String, String> pwdInfo) throws SQLException;
	
	void deleteBookMark(String userId) throws SQLException; 
	void deleteRefreshToken(String userId) throws SQLException; 
	void deleteUserInfo(String userId) throws SQLException; 
}
