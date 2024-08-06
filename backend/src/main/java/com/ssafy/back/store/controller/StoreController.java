package com.ssafy.back.store.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ssafy.back.store.model.StoreDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/store")
@CrossOrigin("http://localhost:5173")
@Slf4j
@Api(tags = {"Store 컨트롤러  API V1"})
public class StoreController {

	@Value("${open.api.key.encoding}")
	private String serviceKey;
	
	@Value("${store.info.api.url}")
	private String requestUrl;

	private static String getTagValue(String tag, Element eElement) {
	    NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
	    Node nValue = (Node) nlList.item(0);
	    if(nValue == null) 
	        return null;
	    return nValue.getNodeValue();
	}
	
	// 선택한 필터에 따라 붙여줄 옵션 리턴
	private String optionParams(String filter) throws Exception {
		if("restaurant".equals(filter)) {
			return "&" + URLEncoder.encode("indsLclsCd","UTF-8") + "=" + URLEncoder.encode("I2", "UTF-8");
		}else if("market".equals(filter)) {
			return "&" + URLEncoder.encode("indsLclsCd","UTF-8") + "=" + URLEncoder.encode("G2", "UTF-8");
		}else if("edu".equals(filter)) {
			return "&" + URLEncoder.encode("indsLclsCd","UTF-8") + "=" + URLEncoder.encode("P1", "UTF-8");
		}else if("medical".equals(filter)) {
			return "&" + URLEncoder.encode("indsLclsCd","UTF-8") + "=" + URLEncoder.encode("Q1", "UTF-8");
		}else if("leisure".equals(filter)) {
			return "&" + URLEncoder.encode("indsLclsCd","UTF-8") + "=" + URLEncoder.encode("R1", "UTF-8");
		}else{
			return "";
		}
	}
	
	@ApiOperation(value="공공데이터에 요청 보내기", notes = "반경의 경도(cx),위도(cy)값을 받아 요청")
	@GetMapping("/list")
	// 경도 위도를 어떻게 전달 받을까...param, pathvariable
	public ResponseEntity<?> aptDealInfoFromOpenAPI(@RequestParam("filter") String filter, @RequestParam("lat") String lat, @RequestParam("lon") String lon) throws Exception{	// 지역코드랑 동 이름 넘겨받기
		
		System.out.println(lat+" "+lon);
		// 필수 : servicekey, radius, cx, cy, numOfRows, pageNo, type
		// 추가 : inds?clsCd(상권업종 분류코드)
		//		? = L, M, S
		
		StringBuilder urlBuilder = new StringBuilder(requestUrl); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") +"="+ serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("radius","UTF-8") + "=" + URLEncoder.encode("500", "UTF-8")); /*반경*/
        urlBuilder.append("&" + URLEncoder.encode("cx","UTF-8") + "=" + URLEncoder.encode(lon, "UTF-8")); /*중심점 경도(lon)*/
        urlBuilder.append("&" + URLEncoder.encode("cy","UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8")); /*중심점 위도(lat)*/
        urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8")); /*타입*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("50", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append(optionParams(filter));
        
        DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
        Document doc = dBuilder.parse(urlBuilder.toString());
        
        doc.getDocumentElement().normalize();
        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
        
        NodeList nList = doc.getElementsByTagName("item");
        System.out.println("파싱할 리스트 수 : "+ nList.getLength());  // 파싱할 리스트 수
        
        // 해당하는 구군의 결과가 전부 나오므로 선택한 읍면동으로 한번 더 필터링
        
        List<StoreDto> stores = new ArrayList<>();
        
        for(int temp = 0; temp < nList.getLength(); temp++){		
        	Node nNode = nList.item(temp);
        	if(nNode.getNodeType() == Node.ELEMENT_NODE){
        						
        		Element eElement = (Element) nNode;
        		StoreDto store = new StoreDto();
        		store.setName(getTagValue("bizesNm", eElement));
        		store.setLat(getTagValue("lat", eElement));
        		store.setLon(getTagValue("lon", eElement));
        		store.setRdnmAdr(getTagValue("rdnmAdr", eElement));
        		store.setIndsSclsNm(getTagValue("indsSclsNm", eElement));
//        		System.out.println("######################");
//        		//System.out.println(eElement.getTextContent());
//        		System.out.println("가게이름  : " + getTagValue("bizesNm", eElement));
//        		System.out.println("위도  : " + getTagValue("lat", eElement));
//        		System.out.println("경도  : " + getTagValue("lon", eElement));
        		stores.add(store);
        	}	// for end
        }	// if end
		
        return new ResponseEntity<List<StoreDto>>(stores, HttpStatus.OK);
		
	}
}
