package com.ssafy.back.sale.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.back.deal.model.HouseInfoDto;
import com.ssafy.back.sale.model.SaleDto;

@Mapper
public interface SaleMapper {

	// 아파트 목록
	List<HouseInfoDto> selectAptList(String aptName);

	// 매물 관련
	void insertSale(SaleDto saleDto);

	List<SaleDto> selectSaleList(long aptCode);

	void updateSale(SaleDto saleDto);

	SaleDto selectSale(int sale_no);

	void deleteSale(int sale_no);
	
	// 매물 파일 관련
	void insertOptions(SaleDto saleDto);

	String[] getOptions(int sale_no);
	
	void deleteOptions(int sale_no);
}
