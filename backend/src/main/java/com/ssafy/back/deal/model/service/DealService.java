package com.ssafy.back.deal.model.service;

import java.util.List;

import com.ssafy.back.deal.model.HouseDealDto;
import com.ssafy.back.deal.model.HouseInfoDto;

public interface DealService {

	List<HouseInfoDto> getHouseInfo(String dongCode) throws Exception;

	List<HouseDealDto> getDealInfo(String aptCode);

	void addBookmark(String code, String user_id);

	void deleteBookmark(String code, String user_id);

	List<Long> listBookmark(String user_id);

	List<Long> popularApt();

	HouseInfoDto getAptInfo(long aptCode);
}
