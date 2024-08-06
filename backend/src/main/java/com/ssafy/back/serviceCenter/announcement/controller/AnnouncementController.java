package com.ssafy.back.serviceCenter.announcement.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.back.jwt.annotation.AuthRequired;
import com.ssafy.back.serviceCenter.announcement.model.dto.AnnouncementDto;
import com.ssafy.back.serviceCenter.announcement.model.dto.AnnouncementFileInfoDto;
import com.ssafy.back.serviceCenter.announcement.model.dto.AnnouncementListDto;
import com.ssafy.back.serviceCenter.announcement.model.dto.AnnouncementListParameterDto;
import com.ssafy.back.serviceCenter.announcement.model.service.AnnouncementService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("service/announcement")
@CrossOrigin("http://localhost:5173")
@Slf4j
@Api(tags = {"공지사항 API V1"})
public class AnnouncementController {
	@Value("${file.path}")
	private String uploadPath;
	
	@Value("${file.path.upload-images}")
	private String uploadImagePath;
	
	@Value("${file.path.upload-files}")
	private String uploadFilePath;
	
	private AnnouncementService announcementService;
	
	public AnnouncementController(AnnouncementService announcementService) {
		this.announcementService = announcementService;
	}
	
	//get방식 dto 파라미터는 form으로 넘어감!!!!!
	//공지사항 불러오기
	@ApiOperation(value="공지사항 목록", notes = "<big>조건에 따라</big> 공지사항의 목록을 반환해 줍니다.")
	@GetMapping
	public ResponseEntity<?> listAnnouncement(@ModelAttribute AnnouncementListParameterDto listParameterDto) {
		log.debug("listAnnouncement call, parameter : {}", listParameterDto);
		try {
			 AnnouncementListDto announcementListDto = announcementService.listAnnouncement(listParameterDto);
			 HttpHeaders header = new HttpHeaders();
			 header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			 return ResponseEntity.ok().headers(header).body(announcementListDto);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value="최근 공지사항 목록", notes = "<big>1달 이내</big> 공지사항의 목록을 반환해 줍니다.")
	@GetMapping("/recentList/{month}")
	public ResponseEntity<?> recentAnnouncement(@PathVariable int month) {
		log.debug("recentAnnouncement call");
		try {
			
			 List<AnnouncementDto> list = announcementService.listRecentAnnouncement(month);
			 return new ResponseEntity<List<AnnouncementDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	//공지사항 보기
	@AuthRequired
	@ApiOperation(value="공지사항 보기", notes = "공지 번호와 <big>일치하는</big> 공지사항을 반환해 줍니다.")
	@GetMapping(value="/{announcementNo}")
	public ResponseEntity<?> viewAnnouncement(@PathVariable int announcementNo) {
		log.debug("viewAnnouncement call, announcementNo : " + announcementNo);
		try {
			announcementService.updateHit(announcementNo);
			return new ResponseEntity<AnnouncementDto>(announcementService.viewAnnouncement(announcementNo), HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	//수정할 공지사항 가져오기
	@AuthRequired
	@ApiOperation(value="공지사항 가져오기", notes = "수정할 공지 번호와 <big>일치하는</big> 공지사항을 반환해 줍니다.")
	@GetMapping(value="/modify/{announcementNo}")
	public ResponseEntity<?> getAnnouncement(@PathVariable int announcementNo) {
		log.debug("viewAnnouncement call, announcementNo : " + announcementNo);
		try {
			return new ResponseEntity<AnnouncementDto>(announcementService.viewAnnouncement(announcementNo), HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	//공지사항 등록
	@ApiOperation(value="공지사항 등록", notes = "작성한 공지사항을 등록해 줍니다.")
	@PostMapping
	public ResponseEntity<?> registAnnouncement(@RequestPart AnnouncementDto announcementDto, @RequestPart(required=false, value="uploadFile") MultipartFile[] uploadFile) {
		//파일 업로드
		if(uploadFile != null && !uploadFile[0].isEmpty()) {
			log.debug("uploadPath : {}, uploadImagePath : {}, uploadFilePath : {}", uploadPath, uploadImagePath, uploadFilePath);
			
			//저장 폴더 생성
			String today = new SimpleDateFormat("yyMMdd").format(new Date());
			String saveFolderName = uploadPath + File.separator +  today;
			File saveFolder = new File(saveFolderName);
			log.debug("저장 폴더 : {}", saveFolder);
			if(!saveFolder.exists())
				saveFolder.mkdirs();
			
			List<AnnouncementFileInfoDto> fileInfoList = new ArrayList<>();
			for(MultipartFile file : uploadFile) {
				AnnouncementFileInfoDto announcementFileInfoDto = new AnnouncementFileInfoDto();
				String originalName = file.getOriginalFilename();
				if(!originalName.isEmpty()) {
					//파일 이름 변경(유일성 확보, 임의 접근 방지)
					String saveName = UUID.randomUUID().toString() + originalName.substring(originalName.lastIndexOf('.'));
					announcementFileInfoDto.setSaveFolder(today);
					announcementFileInfoDto.setOriginalName(originalName);
					announcementFileInfoDto.setSaveName(saveName);
					
					//파일 저장
					try {
						log.debug("원본 파일 이름 : {}, 실제 저장 파일 이름 : {}", originalName, saveName);
						file.transferTo(new File(saveFolder, saveName));
					} catch (IllegalStateException e) {
						return exceptionHandling(e);
					} catch (IOException e) {
						return exceptionHandling(e);
					}
				}
				//생성된 파일 리스트에 저장
				fileInfoList.add(announcementFileInfoDto);
			}
			//리스트 Dto로 set
			announcementDto.setUploadFileList(fileInfoList);
		}
		
		log.debug("registAnnouncement call, announcementDto : {}", announcementDto);
		try {
			announcementService.registAnnouncement(announcementDto);
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	//공지사항 수정
	@ApiOperation(value="공지사항 수정", notes = "공지 번호와 <big>일치하는</big> 공지사항을 수정해 줍니다.")
	@PutMapping
	public ResponseEntity<?> modifyAnnouncement(@RequestPart AnnouncementDto announcementDto, @RequestPart(required = false) MultipartFile[] newFile) {
		log.debug("modifyAnnouncement call, announcementDto : {}", announcementDto);
		try {
			//신규 파일 있을 시
			if(newFile != null && !newFile[0].isEmpty()) {
				log.debug("uploadPath : {}, uploadImagePath : {}, uploadFilePath : {}", uploadPath, uploadImagePath, uploadFilePath);
				
				//저장 폴더 생성
				String today = new SimpleDateFormat("yyMMdd").format(new Date());
				String saveFolderName = uploadPath + File.separator + today;
				
				File saveFolder = new File(saveFolderName);
				log.debug("저장 폴더 : {}", saveFolder);
				if(!saveFolder.exists())
					saveFolder.mkdirs();
				
				List<AnnouncementFileInfoDto> fileInfoList = announcementDto.getNewFileList();
				for(MultipartFile file : newFile) {
					AnnouncementFileInfoDto announcementFileInfoDto = new AnnouncementFileInfoDto();
					String originalName = file.getOriginalFilename();
					if(!originalName.isEmpty()) {
						//파일 이름 변경(유일성 확보, 임의 접근 방지)
						String saveName = UUID.randomUUID().toString() + originalName.substring(originalName.lastIndexOf('.'));
						announcementFileInfoDto.setSaveFolder(saveFolderName);
						announcementFileInfoDto.setOriginalName(originalName);
						announcementFileInfoDto.setSaveName(saveName);
						
						//파일 저장
						try {
							log.debug("원본 파일 이름 : {}, 실제 저장 파일 이름 : {}", originalName, saveName);
							file.transferTo(new File(saveFolder, saveName));
						} catch (IllegalStateException e) {
							return exceptionHandling(e);
						} catch (IOException e) {
							return exceptionHandling(e);
						}
					}
					//생성된 파일 리스트에 저장
					fileInfoList.add(announcementFileInfoDto);
				}
				//기존 uploadFileList에 추가
				for(AnnouncementFileInfoDto file : fileInfoList)
					announcementDto.getUploadFileList().add(file);
			}
			
			announcementService.modifyAnnouncement(announcementDto, uploadPath);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	//공지사항 삭제
	@AuthRequired
	@ApiOperation(value="공지사항 삭제", notes = "공지 번호와 <big>일치하는</big> 공지사항을 삭제합니다.")
	@DeleteMapping(value = "/{announcementNo}")
	public ResponseEntity<?> deleteAnnouncement(@PathVariable int announcementNo) {
		log.debug("deleteAnnouncement call, announcementNo : {}", announcementNo);
		try {
			announcementService.deleteAnnouncement(announcementNo, uploadPath);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	//파일 다운로드
	@GetMapping(value="/download/{saveFolder}/{originalName}/{saveName}")
	public ResponseEntity<?> downloadFile(@PathVariable String saveFolder, @PathVariable String originalName, @PathVariable String saveName){
//		log.debug("downloadFile call, announcementFileInfoDto : {}", announcementFileInfoDto);
		
		try {
			
			//파일 정보를 읽어와 resource로 저장
			Path filePath = Paths.get(uploadPath + File.separator + saveFolder + File.separator + saveName);
			Resource resource = new InputStreamResource(Files.newInputStream(filePath));
			
			System.out.println(filePath);
			
			
			HttpHeaders headers = new HttpHeaders();
			//다운로드를 위한 파일임을 알림, 파일 이름 공백 제거해서 인코딩
			headers.setContentDisposition(ContentDisposition.builder("attachment").filename(URLEncoder.encode(originalName, "UTF-8").replaceAll("\\+", "%20")).build());
			//8비트로 이루어진 바이너리 타입으로 설정
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
		}
	}
	
	//에러 처리
	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
