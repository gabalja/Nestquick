package com.ssafy.back.user.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.back.serviceCenter.inquiry.model.dto.InquiryDto;
import com.ssafy.back.serviceCenter.inquiry.model.mapper.InquiryMapper;
import com.ssafy.back.user.model.dto.UserDto;
import com.ssafy.back.user.model.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {
	private final UserMapper userMapper;
	private final InquiryMapper inquiryMapper;
	
	public UserServiceImpl(UserMapper userMapper, InquiryMapper inquiryMapper) {
		this.userMapper = userMapper;
		this.inquiryMapper = inquiryMapper;
	}

	@Override
	public int idCheck(String userId) throws Exception {
		return userMapper.selectCntById(userId);
	}
	
	@Override
	public void signUp(UserDto userDto) throws Exception {
		userMapper.insertUser(userDto);
	}

	@Override
	public UserDto login(UserDto userDto) throws Exception{
		return userMapper.selectLoginUser(userDto);
	}

	@Override
	public UserDto myPage(String userId) throws Exception {
		return userMapper.selectUserById(userId);
	}

	@Override
	public int getUserCnt() throws Exception {
		return userMapper.selectUserCnt();
	}

	@Override
	public List<UserDto> showUserList() throws Exception {
		return userMapper.selectAllUser();
	}

	@Override
	public String findId(Map<String, String> findInfo) throws Exception {
		return userMapper.selectId(findInfo);
	}

	@Override
	public String findPwd(Map<String, String> findInfo) throws Exception {
		return userMapper.selectPwd(findInfo);
	}
	
	@Override
	public boolean modifyPwd(Map<String, String> pwdInfo) throws Exception {
		boolean result = userMapper.updatePwd(pwdInfo);
		System.out.println(result);
		return result;
	}
	
	@Override
	public boolean modifyUserInfo(Map<String, String> userInfo) throws Exception {
		return userMapper.updateUser(userInfo);
	}

	@Override
	public void withdrawal(String userId) throws Exception {
		userMapper.deleteBookMark(userId);
		userMapper.deleteRefreshToken(userId);
		userMapper.deleteUserInfo(userId);
	}
}
