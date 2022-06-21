package com.pntmall.clinic.domain;

import com.pntmall.common.type.Domain;

public class OrderMemo extends Domain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3158474486475971182L;
	
	private Integer mno;
	private String orderid;
	private String memo;
	private String adminId;
	
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public Integer getMno() {
		return mno;
	}
	public void setMno(Integer mno) {
		this.mno = mno;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
