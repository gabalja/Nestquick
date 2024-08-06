package com.ssafy.back.sale.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.back.deal.model.HouseInfoDto;
import com.ssafy.back.sale.model.SaleDto;
import com.ssafy.back.sale.model.service.SaleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/sale")
@CrossOrigin("http://localhost:5173")
@Api(tags = {"매물 컨트롤러  API V1"})
@Slf4j
public class SaleController {
	
	@Value("${file.path}")
	private String uploadPath;
	
	@Value("${file.path.upload-images}")
	private String uploadImagePath;
	
	@Value("${file.path.upload-files}")
	private String uploadFilePath;

	private SaleService saleService;
	
	public SaleController(SaleService saleService) {
		super();
		this.saleService = saleService;
	}
	
	// 매물 등록 시 houseinfo에 있는 건물 목록 불러와 그 중에서 검색/선택할 수 있도록
	// 건물 선택 시 apt_id랑 address는 houseinfo 토대로  모달에서 검색 후 등록
	// pathvariable...
	@ApiOperation(value = "아파트 목록", notes = "입력한 이름으로 아파트 리스트 검색")
	@GetMapping("/aptlist/{aptname}")
	public ResponseEntity<?> aptList(@PathVariable("aptname") String aptname){
		
		List<HouseInfoDto> aptList = saleService.aptList(aptname);
		
		if (aptList != null)
			return new ResponseEntity<List<HouseInfoDto>>(aptList, HttpStatus.OK);
		else
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	// 매물 목록
	// 동 선택 후 넘어온 아파트 코드 리스트로 for문 돌면서 전체 매물 목록
	// 아파트 코드 넘겨 받아서 검색!
	@GetMapping("/list")
	public ResponseEntity<?> saleList(@RequestParam long aptCode){
		List<SaleDto> saleList = saleService.saleList(aptCode);
		
		if(saleList != null)
			return new ResponseEntity<List<SaleDto>>(saleList, HttpStatus.OK);
		else
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	// 매물 등록
	@ApiOperation(value = "매물 등록", notes = "~ 처리.")
	@PostMapping(value = "/regist", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> registSale(@RequestPart("other") SaleDto saleDto, @RequestPart(value="imagefile", required=true) MultipartFile file) throws Exception, IOException {
		log.debug("registSale param : {}", saleDto);

		System.out.println("##############"+file.getOriginalFilename());
		
		// 파일
		String today = new SimpleDateFormat("yyMMdd").format(new Date());
		String saveFolder = uploadPath + File.separator + today;
		File folder = new File(saveFolder);
		if (!folder.exists())
			folder.mkdirs();
		String originalFileName = file.getOriginalFilename();
		if (!originalFileName.isEmpty()) {
			String saveFileName = UUID.randomUUID().toString()
					+ originalFileName.substring(originalFileName.lastIndexOf('.'));

			file.transferTo(new File(folder, saveFileName));
			saleDto.setImage(File.separator + today + File.separator + saveFileName);
		}
		// 매물 등록 후
		// 중개인은 자신의 페이지에서 매물 리스트를 확인할 수 있도록 한다
		// 목록 반환? URL에 /list 요청?
		saleService.registSale(saleDto);
		
		List<SaleDto> saleList = saleService.saleList(0);
		if (saleList != null)
			return new ResponseEntity<List<SaleDto>>(saleList, HttpStatus.OK);
		else
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	// 매물 수정
	@ApiOperation(value = "매물 수정", notes = "수정내용 DTO로 받아 수정")
	@PostMapping(value = "/modify")
	public ResponseEntity<?> modifySale(@RequestBody SaleDto saleDto) {
		log.debug("modifySale param : {}", saleDto);

		// 매물 수정 후 해당 매물을 다시 보여준다
		saleService.modifySale(saleDto);

		// 매물 수정 시 파일이 수정된다면?
		// 그냥 무조건 변경?
		// 파일이 바뀐 경우에만 새로 저장한다면...원본파일 명을 저장해둬야 하잖아?
		
		SaleDto sale = saleService.getSale(saleDto.getSale_no());
		if (sale != null)
			return new ResponseEntity<SaleDto>(sale, HttpStatus.OK);
		else
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	// 매물 상세
	@ApiOperation(value = "매물 상세", notes = "sale_no를 조건으로 매물 정보 가져옴")
	@GetMapping(value = "/view/{sale_no}")
	public ResponseEntity<?> viewSale(@PathVariable("sale_no") int sale_no) {
		
		SaleDto sale = saleService.getSale(sale_no);
		
		log.debug("registSale param : {}", sale);

		if (sale != null)
			return new ResponseEntity<SaleDto>(sale, HttpStatus.OK);
		else
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	// 매물 삭제
	@ApiOperation(value = "매물 삭제", notes = "sale_no를 조건으로 매물 삭제")
	@GetMapping("/delete/{sale_no}")
	public ResponseEntity<?> deleteSale(@PathVariable("sale_no") int sale_no){
		
		saleService.deleteSale(sale_no);
		
		// 매물 삭제 후 매물 목록 다시 받아오기
		List<SaleDto> saleList = saleService.saleList(0);
		if (saleList != null)
			return new ResponseEntity<List<SaleDto>>(saleList, HttpStatus.OK);
		else
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
