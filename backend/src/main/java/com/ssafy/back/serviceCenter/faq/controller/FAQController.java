package com.ssafy.back.serviceCenter.faq.controller;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.back.serviceCenter.faq.model.dto.QuestionDto;
import com.ssafy.back.serviceCenter.faq.model.service.FAQService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("service/FAQ")
@CrossOrigin("http://localhost:5173")
@Slf4j
@Api(tags = { "FAQ API V1" })
public class FAQController {
	private FAQService faqService;
	
	public FAQController(FAQService faqService) {
		this.faqService = faqService;
	}
	
	// 자주 묻는 질문 가져오기
	@ApiOperation(value = "자주 묻는 질문 목록", notes = "자주 묻는 질문의 목록을 반환해 줍니다.")
	@GetMapping
	public ResponseEntity<?> listQuestion() {
		log.debug("listQuestion call");
		try {

			List<QuestionDto> questionList = faqService.listQuestion();

			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			return ResponseEntity.ok().headers(header).body(questionList);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	// 에러 처리
	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
