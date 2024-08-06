package com.ssafy.back.user.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
	private String userId;
	private String userPwd;
	private String userName;
	private String emailId;
	private String emailDomain;
	private String phoneNumber;
	private String joinDate;
	private int permission;
	private String img;
}
