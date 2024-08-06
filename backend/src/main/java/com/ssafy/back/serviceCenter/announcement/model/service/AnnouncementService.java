package com.ssafy.back.serviceCenter.announcement.model.service;

import java.util.List;

import com.ssafy.back.serviceCenter.announcement.model.dto.AnnouncementDto;
import com.ssafy.back.serviceCenter.announcement.model.dto.AnnouncementFileInfoDto;
import com.ssafy.back.serviceCenter.announcement.model.dto.AnnouncementListDto;
import com.ssafy.back.serviceCenter.announcement.model.dto.AnnouncementListParameterDto;

public interface AnnouncementService {
	AnnouncementListDto listAnnouncement(AnnouncementListParameterDto listParameterDto) throws Exception;
	List<AnnouncementDto> listRecentAnnouncement(int month) throws Exception;
	
	AnnouncementDto viewAnnouncement(int announcementNo) throws Exception;
	void updateHit(int announcementNo) throws Exception;
	
	void registAnnouncement(AnnouncementDto announcementDto) throws Exception;
	
	void modifyAnnouncement(AnnouncementDto announcementDto, String path) throws Exception;
	
	void deleteAnnouncement(int announcementNo, String path) throws Exception;
	
}	
