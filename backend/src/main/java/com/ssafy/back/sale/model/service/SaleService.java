package com.ssafy.back.sale.model.service;

import java.util.List;

import com.ssafy.back.deal.model.HouseInfoDto;
import com.ssafy.back.sale.model.SaleDto;

public interface SaleService {

	// 아파트 목록 불러오기
	List<HouseInfoDto> aptList(String aptname);
	
	// 매물 관련
	void registSale(SaleDto saleDto);
	List<SaleDto> saleList(long aptCode);
	void modifySale(SaleDto saleDto);
	SaleDto getSale(int sale_no);
	void deleteSale(int sale_no);
	
}
