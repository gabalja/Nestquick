package com.ssafy.back.serviceCenter.announcement.model.service;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.back.serviceCenter.announcement.model.dto.AnnouncementDto;
import com.ssafy.back.serviceCenter.announcement.model.dto.AnnouncementFileInfoDto;
import com.ssafy.back.serviceCenter.announcement.model.dto.AnnouncementListDto;
import com.ssafy.back.serviceCenter.announcement.model.dto.AnnouncementListParameterDto;
import com.ssafy.back.serviceCenter.announcement.model.mapper.AnnouncementMapper;
import com.ssafy.back.serviceCenter.faq.model.dto.QuestionDto;
import com.ssafy.back.serviceCenter.inquiry.model.dto.InquiryDto;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {
	private AnnouncementMapper announcementMapper;
	
	public AnnouncementServiceImpl(AnnouncementMapper announcementMapper) {
		this.announcementMapper = announcementMapper;
	}
	
	@Override
	public AnnouncementListDto listAnnouncement(AnnouncementListParameterDto listParameterDto) throws SQLException {
		List<AnnouncementDto> resultList = announcementMapper.selectAnnouncementList(listParameterDto);
		int totalAnnouncementCnt = announcementMapper.selectAnnouncementCnt(listParameterDto);
		AnnouncementListDto announcementListDto = new AnnouncementListDto();
		announcementListDto.setAnnouncementList(resultList);
		announcementListDto.setCurrentPage(listParameterDto.getCurrentPage());
		announcementListDto.setTotalPageCount((totalAnnouncementCnt-1) / listParameterDto.getSizePerPage() + 1);
		
		return announcementListDto;
	}

	@Override
	public AnnouncementDto viewAnnouncement(int announcementNo) throws Exception {
		AnnouncementDto announcementDto = announcementMapper.selectAnnouncement(announcementNo);
		List<AnnouncementFileInfoDto> uploadFileList = announcementMapper.selectFileInfoList(announcementNo);
		announcementDto.setUploadFileList(uploadFileList);
		return announcementDto;
	}
	
	
	@Override
	public void updateHit(int announcementNo) throws Exception {
		announcementMapper.updateHit(announcementNo);
	}

	@Override
	@Transactional
	public void registAnnouncement(AnnouncementDto announcementDto) throws Exception {
		announcementMapper.insertAnnouncement(announcementDto);
		List<AnnouncementFileInfoDto> fileInfoList = announcementDto.getUploadFileList();
		if(fileInfoList != null && !fileInfoList.isEmpty()) {
			announcementMapper.insertFile(announcementDto);
		}
	}

	@Override
	@Transactional
	public void modifyAnnouncement(AnnouncementDto announcementDto, String path) throws Exception {
		//파일 삭제, 추가, 내용 수정
		List<AnnouncementFileInfoDto> deleteFileList = announcementDto.getDeleteFileList();
		for(AnnouncementFileInfoDto file : deleteFileList)
			announcementMapper.deleteModifiedFile(file.getSaveName());
		
		List<AnnouncementFileInfoDto> newFileList = announcementDto.getNewFileList();
		if(newFileList != null && !newFileList.isEmpty()) {
			announcementMapper.insertFile(announcementDto);
		}
		
		announcementMapper.updateAnnouncement(announcementDto);
		for(AnnouncementFileInfoDto fileInfo : deleteFileList) {
			File file = new File(path + File.separator + fileInfo.getSaveFolder() + File.separator + fileInfo.getSaveName());
			file.delete();
		}
	}

	@Override
	@Transactional
	public void deleteAnnouncement(int announcementNo, String path) throws Exception {
		List<AnnouncementFileInfoDto> fileList = announcementMapper.selectFileInfoList(announcementNo);
		announcementMapper.deleteAllFile(announcementNo);
		announcementMapper.deleteAnnouncement(announcementNo);
		for(AnnouncementFileInfoDto fileInfo : fileList) {
			File file = new File(path + File.separator + fileInfo.getSaveFolder() + File.separator + fileInfo.getSaveName());
			file.delete();
		}
	}

	@Override
	public List<AnnouncementDto> listRecentAnnouncement(int month) throws Exception {
		return announcementMapper.selectRecentAnnouncementList(month);
	}
}
