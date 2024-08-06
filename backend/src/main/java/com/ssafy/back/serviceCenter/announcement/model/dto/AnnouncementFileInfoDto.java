package com.ssafy.back.serviceCenter.announcement.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AnnouncementFileInfoDto {
	private String saveFolder;
	private String originalName;
	private String saveName;
}