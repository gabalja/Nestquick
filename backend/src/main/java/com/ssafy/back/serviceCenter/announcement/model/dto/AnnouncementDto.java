package com.ssafy.back.serviceCenter.announcement.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AnnouncementDto {
	private int announcementNo;
	private String title;
	private String contents;
	private int hit;
	private String registDate;
	private List<AnnouncementFileInfoDto> deleteFileList;
	private List<AnnouncementFileInfoDto> newFileList;
	private List<AnnouncementFileInfoDto> uploadFileList;
}
