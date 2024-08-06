package com.ssafy.back.deal.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.back.deal.model.HouseDealDto;
import com.ssafy.back.deal.model.HouseInfoDto;

@Mapper
public interface DealMapper {

	List<HouseInfoDto> selectAptInfo(String dongCode);

	List<HouseDealDto> selectAptDeal(String aptCode);

	void insertBookmark(Map<String, String> params);

	void deleteBookmark(Map<String, String> params);

	List<Long> selectBookmarks(String user_id);

	List<Long> popularAptFromBookmark();

	HouseInfoDto selectApt(long aptCode);
}
