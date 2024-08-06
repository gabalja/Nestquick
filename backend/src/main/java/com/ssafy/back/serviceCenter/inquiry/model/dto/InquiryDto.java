package com.ssafy.back.serviceCenter.inquiry.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InquiryDto {
	private int inquiryNo;
	private String writerId;
	private String title;
	private String contents;
	private String type;
	private String registDate;
	private String status;
}
