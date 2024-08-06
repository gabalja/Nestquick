package com.ssafy.back.user.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.back.jwt.annotation.AuthRequired;
import com.ssafy.back.jwt.model.dto.AuthDto;
import com.ssafy.back.jwt.model.service.AuthService;
import com.ssafy.back.jwt.util.JWTUtil;
import com.ssafy.back.serviceCenter.inquiry.model.dto.InquiryDto;
import com.ssafy.back.serviceCenter.inquiry.model.service.InquiryService;
import com.ssafy.back.user.model.dto.UserDto;
import com.ssafy.back.user.model.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:5173")
@Slf4j
public class UserController {
	@Value("${file.path}")
	private String uploadPath;
	
	@Value("${jwt.accesstoken.expiretime}")
	private Integer accessTokenExpireTime;
	
	@Value("${jwt.refreshtoken.expiretime}")
	private Integer refreshTokenExpireTime;
	
	private final UserService userService;
	private final JWTUtil jwtUtil;
	private final AuthService authService;
	private final InquiryService inquiryService;
	
	private UserController(UserService userService, JWTUtil jwtUtil, AuthService authService, InquiryService inquiryService) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
		this.authService = authService;
		this.inquiryService = inquiryService;
	}
	
	@ApiOperation(value="id 중복체크", notes="id 중복체크")
	@GetMapping("/{userId}")
	public ResponseEntity<?> idCheck(@PathVariable String userId){
		log.debug("idCheck call, userId : {}", userId);
		
		try {
			if(userService.idCheck(userId) == 0) 
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			
			else 
				return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value="회원가입", notes="회원가입")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "회원가입 성공"),
		// https://stackoverflow.com/questions/26587082/http-status-code-for-username-already-exists-when-registering-new-account
		@ApiResponse(responseCode = "409", description = "아이디 중복")
	})
	@PostMapping("/signUp")
	public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
		log.debug("signUp call, userDto : {}", userDto);
		try {
			userService.signUp(userDto);
			
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("message", "회원가입이 완료되었습니다.");
			
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.CREATED);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value="로그인", notes="로그인 성공시 JWT를 반환합니다.")
	@ApiResponses({
		// https://stackoverflow.com/questions/11714485/restful-login-failure-return-401-or-custom-response
		@ApiResponse(responseCode = "201", description = "로그인 성공"),
		@ApiResponse(responseCode = "401", description = "로그인 실패")
	})
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDto userDto, HttpServletResponse response) throws UnsupportedEncodingException {
		Map<String, Object> result = new HashMap<String, Object>();
		
		UserDto validUser;
		try {
			validUser = userService.login(userDto);
			
			// 일치하는 사용자가 없는 경우.
			if(validUser == null) {
				result.put("message", "아이디 또는 패스워드를 확인해주세요.");
				return new ResponseEntity<Map<String, Object>>(result, HttpStatus.UNAUTHORIZED);
			}
			
			// AccessToken, RefreshToken 발급
			// https://velog.io/@yaytomato/%ED%94%84%EB%A1%A0%ED%8A%B8%EC%97%90%EC%84%9C-%EC%95%88%EC%A0%84%ED%95%98%EA%B2%8C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%B2%98%EB%A6%AC%ED%95%98%EA%B8%B0
			String accessToken = jwtUtil.createAccessToken(validUser.getUserId());
			String refreshToken = jwtUtil.createRefreshToken(validUser.getUserId());
			
			// RefreshToken은 HttpOnly Cookie로 발급
			Cookie cookie = new Cookie("refreshToken", refreshToken);
			cookie.setMaxAge(refreshTokenExpireTime);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			response.addCookie(cookie);
			
			// 이미 발급 받은 refreshToken이 있는지 확인.
			AuthDto authDto = authService.getRefreshToken(validUser.getUserId());
			
			if(authDto != null) {
				authDto.setRefreshToken(refreshToken);
				authService.updateRefreshToken(authDto);
			}
			else {
				// 발급받은 refreshToken을 DB에 저장.
				authService.setRefreshToken(new AuthDto(validUser.getUserId(), refreshToken));
			}
			
			// AccessToken은 JSON으로 전달
			result.put("accessToken", accessToken);
			result.put("message", userDto.getUserId() + "님 환영합니다!");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.CREATED);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@ApiOperation(value="로그아웃", notes="해당 사용자의 refresh token을 삭제합니다.")
	@ApiResponses({
		// https://stackoverflow.com/questions/11714485/restful-login-failure-return-401-or-custom-response
		@ApiResponse(responseCode = "200", description = "로그아웃 성공"),
		@ApiResponse(responseCode = "401", description = "로그아웃 실패")
	})
	@DeleteMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		System.out.println("logout!!!");
		String userId;
		try {
			userId = jwtUtil.getUserId(request.getHeader("Authorization"));
			
			System.out.println("userId : " + userId);
			
			// RefreshToken은 HttpOnly Cookie로 발급
			Cookie cookie = new Cookie("refreshToken", "");
			cookie.setMaxAge(0);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			response.addCookie(cookie);
			
			authService.updateRefreshToken(new AuthDto(userId, ""));
			
			result.put("message", "정상적으로 로그아웃되었습니다.");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		} catch (ParseException e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value="마이페이지", notes="사용자 정보를 반환합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "사용자 정보 반환 성공"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "404", description = "사용자 정보 없음")
	})
	@AuthRequired
	@GetMapping("/myPage")
	public ResponseEntity<?> myPage(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		
		// Authorization에 포함된 accessToken의 userId를 가져온다.
		String userId;
		try {
			userId = jwtUtil.getUserId(request.getHeader("Authorization"));
			System.out.println(userId);
			Cookie[] cookieList = request.getCookies();
			
			String requestRefreshToken = "";
			
			for(Cookie cookie : cookieList) {
				if(cookie.getName().equals("refreshToken")) {
					requestRefreshToken = cookie.getValue();
				}
			}
			
			// 요청으로 들어온 refreshToken이 Database의 refreshToken과 일치하는지 확인.
			String dbRefreshToken = authService.getRefreshToken(userId).getRefreshToken();
			
			if(!requestRefreshToken.equals(dbRefreshToken)) {
				// RefreshToken은 HttpOnly Cookie로 발급
				Cookie cookie = new Cookie("refreshToken", "");
				cookie.setMaxAge(0);
				cookie.setHttpOnly(true);
				cookie.setPath("/");
				response.addCookie(cookie);
				
				result.put("message", "로그인이 필요합니다.");
				return new ResponseEntity<Map<String, Object>>(result, HttpStatus.FORBIDDEN);
			}
			
			// 해당 userId의 정보를 획득.
			UserDto user;
			try {
				user = userService.myPage(userId);
			} catch (Exception e) {
				return exceptionHandling(e);
			}
			
			// 사용자 정보 중 비밀번호는 전송하지 말아야한다.
			user.setUserPwd("");
			
			result.put("userInfo", user);
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		} catch (ParseException e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value="회원 수", notes="회원 수를 반환합니다.")
	@AuthRequired
	@GetMapping("/myPage/userCnt")
	public ResponseEntity<?> userCnt(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			result.put("userCnt", userService.getUserCnt());
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value="문의 수", notes="처리되지 않은 문의 수를 반환합니다.")
	@AuthRequired
	@GetMapping("/myPage/inquiryCnt")
	public ResponseEntity<?> inquiryCnt(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			result.put("inquiryCnt", inquiryService.getInquiryCnt());
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value="회원 목록", notes="전체 회원 목록을 반환합니다.")
	@AuthRequired
	@GetMapping("/myPage/userList")
	public ResponseEntity<?> userList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			result.put("userList", userService.showUserList());
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value="문의 목록", notes="전체 문의 목록을 반환합니다.")
	@AuthRequired
	@GetMapping("/myPage/inquiryList")
	public ResponseEntity<?> inquiryList(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("문의 목록!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			List<InquiryDto> list = inquiryService.showInquiryList();
			System.out.println(list.size());
			result.put("inquiryList", list);
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value="아이디/비밀번호 찾기")
	@PostMapping("/find")
	public ResponseEntity<?> find(@RequestBody Map<String, String> findInfo) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		System.out.println(findInfo);
		
		String answer = "";
		try {
			if(findInfo.get("findWhat").equals("findId")) answer = userService.findId(findInfo);
			else answer = userService.findPwd(findInfo);
			
			result.put("answer", answer);
			result.put("confirm", (int)(Math.random()*10)+1);
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value="수정 회원 정보", notes="수정할 회원 정보를 반환합니다.")
	@AuthRequired
	@GetMapping("/modify/{userId}")
	public ResponseEntity<?> userInfo(@PathVariable String userId) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			result.put("userInfo", userService.myPage(userId));
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value="회원 정보 수정", notes="사용자 정보를 수정합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "404", description = "사용자 정보 없음")
	})
	@AuthRequired
	@PutMapping("/modify")
	public ResponseEntity<?> modify(@RequestBody Map<String, String> requestMap, HttpServletRequest request) throws ParseException {
		System.out.println(requestMap);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		// accessToken에 저장된 userId를 획득.
		String accessTokenUserId = jwtUtil.getUserId(request.getHeader("Authorization"));
		String requestUserId = requestMap.get("userId");
		
		if(!requestUserId.equals(accessTokenUserId)) {
			result.put("message", "권한이 없습니다.");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.FORBIDDEN);
		}
		
		boolean isUpdated;
		try {
			isUpdated = userService.modifyUserInfo(requestMap);
			
			if(!isUpdated) {
				
				result.put("message", "일치하는 사용자가 없습니다.");
				return new ResponseEntity<Map<String, Object>>(result, HttpStatus.NOT_FOUND);
			}

			result.put("message", "회원 정보가 수정되었습니다. 다시 로그인해주세요.");
			authService.updateRefreshToken(new AuthDto(accessTokenUserId, ""));
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value="비밀번호 수정", notes="비밀번호를 수정합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "비밀번호 수정 성공"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
	})
	@AuthRequired
	@PutMapping("/modifyPwd")
	public ResponseEntity<?> modifyPwd(@RequestBody Map<String, String> requestMap, HttpServletRequest request) throws ParseException {
		System.out.println(requestMap);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		// accessToken에 저장된 userId를 획득.
		String accessTokenUserId = jwtUtil.getUserId(request.getHeader("Authorization"));
		String requestUserId = requestMap.get("userId");
		
		if(!requestUserId.equals(accessTokenUserId)) {
			result.put("message", "권한이 없습니다.");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.FORBIDDEN);
		}
		
		boolean isUpdated;
		try {
			isUpdated = userService.modifyPwd(requestMap);
			
			if(!isUpdated) {
				result.put("message", "잘못된 비밀번호가 입력되었습니다.");
				return new ResponseEntity<Map<String, Object>>(result, HttpStatus.NOT_FOUND);
			}

			result.put("message", "비밀번호가 수정되었습니다. 다시 로그인해주세요.");
			authService.updateRefreshToken(new AuthDto(accessTokenUserId, ""));
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@ApiOperation(value="회원 정보 삭제", notes="사용자 정보를 삭제합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "사용자 정보 삭제 성공"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "404", description = "사용자 정보 없음")
	})
	@AuthRequired
	@DeleteMapping("/withdrawal/{userId}")
	public ResponseEntity<?> withdrawal(@PathVariable String userId) {
		Map<String, String> result = new HashMap<String, String>();
		
		try {
			userService.withdrawal(userId);
			return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	//에러 처리
	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
