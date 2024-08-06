package com.ssafy.back.sale.model;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class SaleDto {

	private int sale_no;
//	DB에서 BigInt 타입이라 long
	private long apt_id;
	private String seller_id;
	private String title;
	private String description;
	private String address;
	private String sale_type;
	private int price;
	private int maintenance_cost;
	private String build_type;
	private int build_year;
	private float area;
	private int floor;
	private Date regist_date;
	private String image;
	private String imgurl;
	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	private int hit;
	
	// 옵션 목록
	String[] options;

	public String[] getOptions() {
		return options;
	}

	public void setOptions(String[] options) {
		this.options = options;
	}

	public int getSale_no() {
		return sale_no;
	}

	public void setSale_no(int sale_no) {
		this.sale_no = sale_no;
	}

	public long getApt_id() {
		return apt_id;
	}

	public void setApt_id(long apt_id) {
		this.apt_id = apt_id;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSale_type() {
		return sale_type;
	}

	public void setSale_type(String sale_type) {
		this.sale_type = sale_type;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getMaintenance_cost() {
		return maintenance_cost;
	}

	public void setMaintenance_cost(int maintenance_cost) {
		this.maintenance_cost = maintenance_cost;
	}

	public String getBuild_type() {
		return build_type;
	}

	public void setBuild_type(String build_type) {
		this.build_type = build_type;
	}

	public int getBuild_year() {
		return build_year;
	}

	public void setBuild_year(int build_year) {
		this.build_year = build_year;
	}

	public float getArea() {
		return area;
	}

	public void setArea(float area) {
		this.area = area;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public Date getRegist_date() {
		return regist_date;
	}

	public void setRegist_date(Date regist_date) {
		this.regist_date = regist_date;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	@Override
	public String toString() {
		return "SaleDto [sale_no=" + sale_no + ", apt_id=" + apt_id + ", seller_id=" + seller_id + ", title=" + title
				+ ", description=" + description + ", address=" + address + ", sale_type=" + sale_type + ", price="
				+ price + ", maintenance_cost=" + maintenance_cost + ", build_type=" + build_type + ", build_year="
				+ build_year + ", area=" + area + ", floor=" + floor + ", regist_date=" + regist_date + ", image="
				+ image + ", hit=" + hit + "]";
	}

}
