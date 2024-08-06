package com.ssafy.back.serviceCenter.announcement.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AnnouncementListParameterDto {
	private String searchFilter;
	private String searchWord;
	private int currentPage;
	private int sizePerPage;
	private int start;
}
