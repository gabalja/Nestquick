package com.ssafy.back.store.model;

public class StoreDto {

	private String name;
	private String lat;
	private String lon;
	private String rdnmAdr; // 도로명주소
	private String indsSclsNm; // 상권업종소분류명

	public String getRdnmAdr() {
		return rdnmAdr;
	}

	public void setRdnmAdr(String rdnmAdr) {
		this.rdnmAdr = rdnmAdr;
	}

	public String getIndsSclsNm() {
		return indsSclsNm;
	}

	public void setIndsSclsNm(String indsSclsNm) {
		this.indsSclsNm = indsSclsNm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	@Override
	public String toString() {
		return "StoreDto [name=" + name + ", lat=" + lat + ", lon=" + lon + "]";
	}

}
