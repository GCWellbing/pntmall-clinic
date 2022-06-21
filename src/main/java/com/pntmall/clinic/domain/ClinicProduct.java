package com.pntmall.clinic.domain;

import com.pntmall.common.type.SearchDomain;

public class ClinicProduct extends SearchDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4313969660710096440L;
	
	private Integer memNo;
	private Integer pno;
	private String pname;
	private Integer salePrice;
	private String matnr;
	private String status;
	
	public Integer getMemNo() {
		return memNo;
	}
	public void setMemNo(Integer memNo) {
		this.memNo = memNo;
	}
	public Integer getPno() {
		return pno;
	}
	public void setPno(Integer pno) {
		this.pno = pno;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public Integer getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Integer salePrice) {
		this.salePrice = salePrice;
	}
	public String getMatnr() {
		return matnr;
	}
	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

}
