package com.ssafy.back.serviceCenter.announcement.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.back.serviceCenter.announcement.model.dto.AnnouncementDto;
import com.ssafy.back.serviceCenter.announcement.model.dto.AnnouncementFileInfoDto;
import com.ssafy.back.serviceCenter.announcement.model.dto.AnnouncementListParameterDto;
import com.ssafy.back.serviceCenter.faq.model.dto.QuestionDto;
import com.ssafy.back.serviceCenter.inquiry.model.dto.InquiryDto;

@Mapper
public interface AnnouncementMapper {
	List<AnnouncementDto> selectAnnouncementList(AnnouncementListParameterDto listParameterDto) throws SQLException;
	List<AnnouncementDto> selectRecentAnnouncementList(int month) throws SQLException;
	
	AnnouncementDto selectAnnouncement(int announcementNo) throws SQLException;
	void updateHit(int announcementNo) throws SQLException;
	
	void insertAnnouncement(AnnouncementDto announcementDto) throws SQLException;	
	
	void updateAnnouncement(AnnouncementDto announcementDto) throws SQLException;
	
	void deleteAnnouncement(int announcementNo) throws SQLException;
	
	//페이지내이션
	int selectAnnouncementCnt(AnnouncementListParameterDto listParameterDto) throws SQLException;
	
	//파일
	List<AnnouncementFileInfoDto> selectFileInfoList(int announcementNo) throws Exception;
	void insertFile(AnnouncementDto announcementDto) throws Exception;
	void deleteAllFile(int announcementNo) throws Exception;
	void deleteModifiedFile(String saveName) throws Exception;
}
