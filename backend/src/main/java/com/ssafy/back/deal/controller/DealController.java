package com.ssafy.back.deal.controller;

import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ssafy.back.deal.model.HouseDealDto;
import com.ssafy.back.deal.model.HouseInfoDto;
import com.ssafy.back.deal.model.service.DealService;
import com.ssafy.back.jwt.annotation.AuthRequired;
import com.ssafy.back.jwt.util.JWTUtil;
import com.ssafy.back.user.model.dto.UserDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/deal")
@CrossOrigin("http://localhost:5173")
@Slf4j
@Api(tags = { "Deal 컨트롤러  API V1" })
public class DealController {

	// SERVICE KEY IS NOT REGISTERED ERROR. 오류 발생으로 인코딩 키 사용
	@Value("${open.api.key.encoding}")
	private String serviceKey;

	@Value("${apt.deal.api.url}")
	private String requestUrl;

	DealService dealService;
	JWTUtil jwtUtil;

	private String userId;

	public DealController(DealService dealService, JWTUtil jwtUtil) {
		super();
		this.dealService = dealService;
		this.jwtUtil = jwtUtil;
	}

	private static String getTagValue(String tag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);
		if (nValue == null)
			return null;
		return nValue.getNodeValue();
	}

	// 프론트에서 선택한 지역과 동 이름을 받아
	// 지도에 표시할 실거래가 정보를 받아오고
	@ApiOperation(value = "공공데이터에 요청 보내기", notes = "법정동 코드를 전달받아 공공 API에 요청을 보내고 XML 데이터 받기")
	@GetMapping("/APIaptdeal")
	public ResponseEntity<?> aptDealInfoFromOpenAPI(@RequestParam("code") String code,
			@RequestParam("code") String dongName) throws Exception { // 지역코드랑 동 이름 넘겨받기

		System.out.println("백에서 출력하는 " + code);

		StringBuilder urlBuilder = new StringBuilder(requestUrl); /* URL */
		urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey); /* Service Key */
		urlBuilder
				.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /* 페이지번호 */
		urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
				+ URLEncoder.encode("100", "UTF-8")); /* 한 페이지 결과 수 */
		urlBuilder.append("&" + URLEncoder.encode("LAWD_CD", "UTF-8") + "="
				+ URLEncoder.encode(code.substring(0, 5), "UTF-8")); /* 지역코드 */
		urlBuilder.append(
				"&" + URLEncoder.encode("DEAL_YMD", "UTF-8") + "=" + URLEncoder.encode("201512", "UTF-8")); /* 계약월 */

		DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
		Document doc = dBuilder.parse(urlBuilder.toString());

		doc.getDocumentElement().normalize();
		System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

		NodeList nList = doc.getElementsByTagName("item");
		System.out.println("파싱할 리스트 수 : " + nList.getLength()); // 파싱할 리스트 수

		// 해당하는 구군의 결과가 전부 나오므로 선택한 읍면동으로 한번 더 필터링

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				System.out.println("######################");
				// System.out.println(eElement.getTextContent());
				System.out.println("법정동  : " + getTagValue("법정동", eElement));
			} // for end
		} // if end

//        // URL로 생성
//        URL url = new URL(urlBuilder.toString());
//        
//        // 연결 생성
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        
//        // 요청 셋팅
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-type", "application/json");
//        
//        // 응답 코드 확인
//        System.out.println("Response code: " + conn.getResponseCode());
//        BufferedReader rd;
//        
//        // 정상적인 응답을 받았다면
//        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        } else {
//            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//        }
//        
//        
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = rd.readLine()) != null) {
//        	// 와 xml이 그냥 한줄로 들어오네 우와
//            sb.append(line).append("\n").append("#####");
//        }
//        rd.close();
//        conn.disconnect();
//        System.out.println(sb.toString());

		return null;

	}

	@ApiOperation(value = "DB에서 아파트 목록 가져오기", notes = "dongCode로 DB의 houseinfo")
	@GetMapping("/DBaptlist")
	public ResponseEntity<?> aptListFromDB(@RequestParam("code") String code) throws Exception { // 지역코드

		List<HouseInfoDto> aptList = dealService.getHouseInfo(code);

		return new ResponseEntity<List<HouseInfoDto>>(aptList, HttpStatus.OK);
	}

	@ApiOperation(value = "DB에서 아파트 거래 목록 가져오기", notes = "aptCode로 DB의 housedeal")
	@GetMapping("/DBaptDeal")
	public ResponseEntity<?> aptDealFromDB(@RequestParam("code") String code) throws Exception { // 지역코드

		List<HouseDealDto> aptList = dealService.getDealInfo(code);

		return new ResponseEntity<List<HouseDealDto>>(aptList, HttpStatus.OK);
	}

	// 북마크 추가
	// @AuthRequired
	@ApiOperation(value = "선택한 아파트 북마크에 추가", notes = "aptCode + 로그인 사용자")
	@GetMapping("/addBookmark")
	public ResponseEntity<?> addBookmark(HttpServletRequest request, @RequestParam("code") String code) throws Exception { // 지역코드

		dealService.addBookmark(code, userId);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 북마크 삭제
	// @AuthRequired
	@ApiOperation(value = "선택한 아파트 북마크 삭제", notes = "aptCode + 로그인 사용자")
	@GetMapping("/deleteBookmark")
	public ResponseEntity<?> deleteBookmark(HttpServletRequest request, @RequestParam("code") String code) throws Exception { // 지역코드
		
		dealService.deleteBookmark(code, userId);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 북마크 목록 반환
	@ApiOperation(value = "로그인 사용자의 북마크 목록", notes = "로그인 사용자")
	@GetMapping("/listBookmark")
	public ResponseEntity<?> listBookmark(HttpServletRequest request)
			throws Exception { // 지역코드
		// Authorization에 포함된 accessToken의 userId를 가져온다.

//		System.out.println("북마크 목록 반환");
//		System.out.println(request.getHeader("Authorization"));
		String userId = jwtUtil.getUserId(request.getHeader("Authorization"));
		this.userId = userId;

		//System.out.println(userId);

		List<Long> bookmarks = dealService.listBookmark(userId);

		return new ResponseEntity<List<Long>>(bookmarks, HttpStatus.OK);
	}
	
	// 북마크 랭킹
	@ApiOperation(value = "인기 아파트", notes = "북마크 개수를 기준으로 목록 가져옴 (5개)")
	@GetMapping("/popularApt")
	public ResponseEntity<?> popularApt() throws Exception {

		List<HouseInfoDto> topApt = new ArrayList<HouseInfoDto>();
		for (Long aptCode : dealService.popularApt()) {
			HouseInfoDto houseInfo = dealService.getAptInfo(aptCode);
			topApt.add(houseInfo);
		}

		return new ResponseEntity<List<HouseInfoDto>>(topApt, HttpStatus.OK);
	}
	
	// 특정 아파트 정보
	@ApiOperation(value = "아파트 코드로 특정 아파트 정보 가져오기")
	@GetMapping("/aptinfo/{aptCode}")
	public ResponseEntity<?> aptInfoByaptCode(@PathVariable long aptCode) throws Exception {

		HouseInfoDto houseInfo = dealService.getAptInfo(aptCode);

		return new ResponseEntity<HouseInfoDto>(houseInfo, HttpStatus.OK);
	}

}
