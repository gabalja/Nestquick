package com.ssafy.back.serviceCenter.inquiry.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryListDto {
	private List<InquiryDto> inquiryList;
	private int currentPage;
	private int totalPageCount;
}
