package com.ssafy.back.serviceCenter.inquiry.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.back.jwt.annotation.AuthRequired;
import com.ssafy.back.serviceCenter.inquiry.model.dto.InquiryDto;
import com.ssafy.back.serviceCenter.inquiry.model.service.InquiryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("service/inquiry")
@CrossOrigin("http://localhost:5173")
@Slf4j
@Api(tags = { "Inquiry API V1" })
public class InquiryController {
	private InquiryService inquiryService;

	public InquiryController(InquiryService inquiryService) {
		this.inquiryService = inquiryService;
	}

	@ApiOperation(value = "문의 가져오기", notes = "문의사항을 가져옵니다.")
	@GetMapping
	public ResponseEntity<?> registInquiry(@PathVariable int inquiryNo) {
		log.debug("registInquiry call, inquiryNo : {}", inquiryNo);
		try {
			InquiryDto inquiry = inquiryService.getInquiry(inquiryNo);
			return new ResponseEntity<InquiryDto>(inquiry, HttpStatus.CREATED);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	// 문의 등록
	@AuthRequired
	@ApiOperation(value = "1:1 문의 등록", notes = "문의사항을 등록해 줍니다.")
	@PostMapping
	public ResponseEntity<?> registInquiry(@RequestPart InquiryDto inquiryDto) {
		log.debug("registInquiry call, InquiryDto : {}", inquiryDto);
		try {
			inquiryService.registInquiry(inquiryDto);
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	// 문의 해결
	@AuthRequired
	@ApiOperation(value = "1:1 문의 처리 수정", notes = "문의사항을 상태를 수정해 줍니다.")
	@PutMapping
	public ResponseEntity<?> updateInquiry(@RequestBody int inquiryNo) {
		log.debug("registInquiry call, InquiryDto : {}", inquiryNo);
		try {
			inquiryService.updateInquiry(inquiryNo);
			return new ResponseEntity<Void>(HttpStatus.OK);
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
