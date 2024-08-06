package com.ssafy.back.sale.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.back.deal.model.HouseInfoDto;
import com.ssafy.back.sale.model.SaleDto;
import com.ssafy.back.sale.model.mapper.SaleMapper;

@Service
public class SaleServiceImpl implements SaleService {

	private SaleMapper saleMapper;
	
	public SaleServiceImpl(SaleMapper saleMapper) {
		super();
		this.saleMapper = saleMapper;
	}
	
	@Override
	public void registSale(SaleDto saleDto) {
		saleMapper.insertSale(saleDto);
		
		// 옵션 정보 저장
		saleMapper.insertOptions(saleDto);
	}

	@Override
	public List<HouseInfoDto> aptList(String aptname) {
		
		return saleMapper.selectAptList(aptname);
	}

	@Override
	public List<SaleDto> saleList(long aptCode) {
		
		return saleMapper.selectSaleList(aptCode);
	}

	@Override
	public void modifySale(SaleDto saleDto) {

		saleMapper.updateSale(saleDto);
		// 전체 삭제 후 새로 등록
		saleMapper.deleteOptions(saleDto.getSale_no());
		saleMapper.insertOptions(saleDto);
		
	}

	@Override
	public SaleDto getSale(int sale_no) {

		SaleDto getSale = saleMapper.selectSale(sale_no);
		// 옵션 정보 가져오기
		getSale.setOptions(saleMapper.getOptions(sale_no));
		
		return getSale;
	}

	@Override
	public void deleteSale(int sale_no) {

		saleMapper.deleteSale(sale_no);
		saleMapper.deleteOptions(sale_no);
	}

}
