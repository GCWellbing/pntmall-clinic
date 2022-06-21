package com.pntmall.clinic.domain;

import com.pntmall.common.type.Domain;

public class ReservationProduct extends Domain {

	private static final long serialVersionUID = 5968167248327648119L;
	private Integer pno;
	private Integer resNo;
	private String pname;
	private String salePrice;
	private String nutritionName;

	public Integer getPno() {
		return pno;
	}
	public void setPno(Integer pno) {
		this.pno = pno;
	}
	public Integer getResNo() {
		return resNo;
	}
	public void setResNo(Integer resNo) {
		this.resNo = resNo;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}
	public String getNutritionName() {
		return nutritionName;
	}
	public void setNutritionName(String nutritionName) {
		this.nutritionName = nutritionName;
	}



}
