package com.ssafy.back.serviceCenter.inquiry.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryListParameterDto {
	private String searchFilter;
	private String searchWord;
	private int currentPage;
	private int sizePerPage;
	private int start;
}
