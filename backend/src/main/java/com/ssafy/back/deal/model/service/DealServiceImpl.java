package com.ssafy.back.deal.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.back.deal.model.HouseDealDto;
import com.ssafy.back.deal.model.HouseInfoDto;
import com.ssafy.back.deal.model.mapper.DealMapper;

@Service
public class DealServiceImpl implements DealService {

	DealMapper dealMapper;
		
	public DealServiceImpl(DealMapper dealMapper) {
		super();
		this.dealMapper = dealMapper;
	}

	@Override
	public List<HouseInfoDto> getHouseInfo(String dongCode) throws Exception {
		return dealMapper.selectAptInfo(dongCode);
	}

	@Override
	public List<HouseDealDto> getDealInfo(String aptCode) {
		return dealMapper.selectAptDeal(aptCode);
	}

	@Override
	public void addBookmark(String code, String user_id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("aptCode", code);
		params.put("user_id", user_id);
		dealMapper.insertBookmark(params);
	}

	@Override
	public void deleteBookmark(String code, String user_id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("aptCode", code);
		params.put("user_id", user_id);
		dealMapper.deleteBookmark(params);
	}

	@Override
	public List<Long> listBookmark(String user_id) {
		return dealMapper.selectBookmarks(user_id);
	}

	@Override
	public List<Long> popularApt() {
		return dealMapper.popularAptFromBookmark();
	}

	@Override
	public HouseInfoDto getAptInfo(long aptCode) {
		return dealMapper.selectApt(aptCode);
	}

}
