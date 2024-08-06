package com.ssafy.back.serviceCenter.faq.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.back.serviceCenter.faq.model.dto.QuestionDto;

@Mapper
public interface FAQMapper {
	List<QuestionDto> selectQuestionList() throws SQLException;
}
