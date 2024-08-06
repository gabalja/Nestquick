package com.ssafy.back.map.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.back.map.model.SidoGugunCodeDto;
import com.ssafy.back.map.model.mapper.MapMapper;

@Service
public class MapServiceImpl implements MapService {
	
	private MapMapper mapMapper;

	public MapServiceImpl(MapMapper mapMapper) {
		super();
		this.mapMapper = mapMapper;
	}

	@Override
	public List<SidoGugunCodeDto> getSido() throws Exception {
		return mapMapper.getSido();
	}

	@Override
	public List<SidoGugunCodeDto> getGugunInSido(String sido) throws Exception {
		return mapMapper.getGugunInSido(sido);
	}

	@Override
	public List<SidoGugunCodeDto> getDongInGugun(String gugun) throws Exception {
		return mapMapper.getDongInGugun(gugun);
	}

}
