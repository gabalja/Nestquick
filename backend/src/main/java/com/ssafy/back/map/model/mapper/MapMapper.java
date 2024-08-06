package com.ssafy.back.map.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.back.map.model.SidoGugunCodeDto;

@Mapper
public interface MapMapper {

	List<SidoGugunCodeDto> getSido() throws SQLException;
	List<SidoGugunCodeDto> getGugunInSido(String sido) throws SQLException;
	List<SidoGugunCodeDto> getDongInGugun(String gugun) throws SQLException;
	
}
