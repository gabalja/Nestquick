package com.ssafy.back.serviceCenter.inquiry.model.service;

import java.util.List;

import com.ssafy.back.serviceCenter.inquiry.model.dto.InquiryDto;

public interface InquiryService {
	void registInquiry(InquiryDto inquiryDto) throws Exception;
	
	int getInquiryCnt() throws Exception;
	
	InquiryDto getInquiry(int inquiryNo) throws Exception;
	 
	List<InquiryDto> showInquiryList() throws Exception;
	
	void updateInquiry(int inquiryNo) throws Exception;
}
