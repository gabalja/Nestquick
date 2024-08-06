package com.ssafy.back.serviceCenter.faq.model.service;

import java.util.List;

import com.ssafy.back.serviceCenter.faq.model.dto.QuestionDto;

public interface FAQService {
	List<QuestionDto> listQuestion() throws Exception;
}
