package com.pntmall.clinic.domain;

import com.pntmall.common.type.SearchDomain;

public class OrderSearch extends SearchDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5787614508201240488L;

	private String sdate;
	private String edate;
	private Integer orderGubun;
	private String oname;
	private String omtel;
	private String[] status;
	private Integer pickupClinic;
	private String payType;
	private Integer gradeNo;
	private String device;
	private String firstOrderYn;
	private Integer spayAmt;
	private Integer epayAmt;
	private String keytype;
	private String keyword;
	private Integer memNo;
	private String clinicId;	
	private Integer clinicMemNo;
	
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		this.edate = edate;
	}
	public Integer getOrderGubun() {
		return orderGubun;
	}
	public void setOrderGubun(Integer orderGubun) {
		this.orderGubun = orderGubun;
	}
	public String getOname() {
		return oname;
	}
	public void setOname(String oname) {
		this.oname = oname;
	}
	public String getOmtel() {
		return omtel;
	}
	public void setOmtel(String omtel) {
		this.omtel = omtel;
	}
	public String[] getStatus() {
		return status;
	}
	public void setStatus(String[] status) {
		this.status = status;
	}
	public Integer getPickupClinic() {
		return pickupClinic;
	}
	public void setPickupClinic(Integer pickupClinic) {
		this.pickupClinic = pickupClinic;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public Integer getGradeNo() {
		return gradeNo;
	}
	public void setGradeNo(Integer gradeNo) {
		this.gradeNo = gradeNo;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getFirstOrderYn() {
		return firstOrderYn;
	}
	public void setFirstOrderYn(String firstOrderYn) {
		this.firstOrderYn = firstOrderYn;
	}
	public Integer getSpayAmt() {
		return spayAmt;
	}
	public void setSpayAmt(Integer spayAmt) {
		this.spayAmt = spayAmt;
	}
	public Integer getEpayAmt() {
		return epayAmt;
	}
	public void setEpayAmt(Integer epayAmt) {
		this.epayAmt = epayAmt;
	}
	public String getKeytype() {
		return keytype;
	}
	public void setKeytype(String keytype) {
		this.keytype = keytype;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Integer getMemNo() {
		return memNo;
	}
	public void setMemNo(Integer memNo) {
		this.memNo = memNo;
	}
	public String getClinicId() {
		return clinicId;
	}
	public void setClinicId(String clinicId) {
		this.clinicId = clinicId;
	}
	public Integer getClinicMemNo() {
		return clinicMemNo;
	}
	public void setClinicMemNo(Integer clinicMemNo) {
		this.clinicMemNo = clinicMemNo;
	}
	
}
