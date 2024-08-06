package com.ssafy.back.serviceCenter.faq.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class QuestionDto {
	private int questionNo;
	private String title;
	private String contents;
	private String type;
}
