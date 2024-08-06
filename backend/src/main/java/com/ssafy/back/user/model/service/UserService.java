package com.ssafy.back.user.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.back.user.model.dto.UserDto;

public interface UserService {
	int idCheck(String userId) throws Exception;
	void signUp(UserDto userDto) throws Exception;
	
	UserDto login(UserDto userDto) throws Exception;
	
	UserDto myPage(String userId) throws Exception;
	
	int getUserCnt() throws Exception;
	
	List<UserDto> showUserList() throws Exception;
	
	String findId(Map<String, String> findInfo) throws Exception;
	String findPwd(Map<String, String> findInfo) throws Exception;
	
	boolean modifyUserInfo(Map<String, String> userInfo) throws Exception;
	boolean modifyPwd(Map<String, String> pwdInfo) throws Exception;
	
	void withdrawal(String userId) throws Exception;
}
