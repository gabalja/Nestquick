package com.ssafy.back.serviceCenter.faq.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.back.serviceCenter.faq.model.dto.QuestionDto;
import com.ssafy.back.serviceCenter.faq.model.mapper.FAQMapper;

@Service
public class FAQServiceImpl implements FAQService{
	private FAQMapper faqMapper;
	
	public FAQServiceImpl(FAQMapper faqMapper) {
		this.faqMapper = faqMapper;
	}
	
	@Override
	public List<QuestionDto> listQuestion() throws Exception {
		return faqMapper.selectQuestionList();
	}
}
