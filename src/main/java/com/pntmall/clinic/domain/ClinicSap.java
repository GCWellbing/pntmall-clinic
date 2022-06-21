package com.pntmall.clinic.domain;

import com.pntmall.common.type.Domain;

public class ClinicSap extends Domain {

	private static final long serialVersionUID = -4010285671854576538L;

	private String clinicSellCd;
	private String businessName;
	private String businessNo;
	private String businessOwner;
	private String tel;
	private String mtel;
	private String zip;
	private String addr1;
	private String addr2;
	private String email;
	private String subject;


	public String getClinicSellCd() {
		return clinicSellCd;
	}
	public void setClinicSellCd(String clinicSellCd) {
		this.clinicSellCd = clinicSellCd;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getBusinessNo() {
		return businessNo;
	}
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	public String getBusinessOwner() {
		return businessOwner;
	}
	public void setBusinessOwner(String businessOwner) {
		this.businessOwner = businessOwner;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getMtel() {
		return mtel;
	}
	public void setMtel(String mtel) {
		this.mtel = mtel;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getAddr1() {
		return addr1;
	}
	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}
	public String getAddr2() {
		return addr2;
	}
	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

}
