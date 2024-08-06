package com.ssafy.back.serviceCenter.inquiry.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.back.serviceCenter.inquiry.model.dto.InquiryDto;
import com.ssafy.back.serviceCenter.inquiry.model.mapper.InquiryMapper;

@Service
public class InquiryServiceImpl implements InquiryService {
	private InquiryMapper inquiryMapper;

	public InquiryServiceImpl(InquiryMapper inquiryMapper) {
		this.inquiryMapper = inquiryMapper;
	}

	@Override
	public void registInquiry(InquiryDto inquiryDto) throws Exception {
		inquiryMapper.insertInquiry(inquiryDto);
	}
	
	@Override
	public int getInquiryCnt() throws Exception {
		return inquiryMapper.selectInquiryCnt();
	}

	@Override
	public List<InquiryDto> showInquiryList() throws Exception {
		return inquiryMapper.selectAllInquiry();
	}

	@Override
	public InquiryDto getInquiry(int inquiryNo) throws Exception {
		return inquiryMapper.selectInquiry(inquiryNo);
	}

	@Override
	public void updateInquiry(int inquiryNo) throws Exception {
		inquiryMapper.updateInquiry(inquiryNo);
	}
}
