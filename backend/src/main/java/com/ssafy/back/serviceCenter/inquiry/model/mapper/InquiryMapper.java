package com.ssafy.back.serviceCenter.inquiry.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.back.serviceCenter.inquiry.model.dto.InquiryDto;
import com.ssafy.back.serviceCenter.inquiry.model.dto.InquiryListParameterDto;

@Mapper
public interface InquiryMapper {
	void insertInquiry(InquiryDto inquiryDto) throws SQLException;
	
	int selectInquiryCnt() throws SQLException;
	
	InquiryDto selectInquiry(int inquiryNo) throws SQLException;
	
	List<InquiryDto> selectAllInquiry() throws SQLException;
	
	void updateInquiry(int inquiryNo) throws SQLException;
}
