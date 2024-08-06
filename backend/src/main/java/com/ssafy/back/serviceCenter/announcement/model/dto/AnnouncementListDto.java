package com.ssafy.back.serviceCenter.announcement.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AnnouncementListDto {
	private List<AnnouncementDto> announcementList;
	private int currentPage;
	private int totalPageCount;
}
