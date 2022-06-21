package com.pntmall.clinic.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.pntmall.common.type.SearchDomain;

public class ClinicSearch extends SearchDomain {

	private static final long serialVersionUID = 8846170475313454076L;
	private Integer memNo;
	private String memId;
	private String clinicId;
	private String passwd;
	private String oldPasswd;
	private String businessNo;
	private String businessOwner;
	private String clinicName;
	private String approve;
	private String dispYn;
	private String clinicSearchType;
	private String clinicSearchWord;
	private Double lat;
	private Double lng;
	private String distance;
	private String divisionYn;
	private String pickupYn;
	private String katalkYn;
	private String clinicYn;
	private String sendType;
	private String idSrhType;
	private String name;
	private String mtel1;
	private String mtel2;
	private String email;



	@DateTimeFormat(pattern="yyyy.MM.dd")
	private Date fromCdateJoin;
	@DateTimeFormat(pattern="yyyy.MM.dd")
	private Date toCdateJoin;

	public String getClinicId() {
		return clinicId;
	}
	public void setClinicId(String clinicId) {
		this.clinicId = clinicId;
	}
	public String getBusinessNo() {
		return businessNo;
	}
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	public String getClinicName() {
		return clinicName;
	}
	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}
	public String getApprove() {
		return approve;
	}
	public void setApprove(String approve) {
		this.approve = approve;
	}
	public String getDispYn() {
		return dispYn;
	}
	public void setDispYn(String dispYn) {
		this.dispYn = dispYn;
	}
	public Date getFromCdateJoin() {
		return fromCdateJoin;
	}
	public void setFromCdateJoin(Date fromCdateJoin) {
		this.fromCdateJoin = fromCdateJoin;
	}
	public Date getToCdateJoin() {
		return toCdateJoin;
	}
	public void setToCdateJoin(Date toCdateJoin) {
		this.toCdateJoin = toCdateJoin;
	}
	public String getClinicSearchType() {
		return clinicSearchType;
	}
	public void setClinicSearchType(String clinicSearchType) {
		this.clinicSearchType = clinicSearchType;
	}
	public String getClinicSearchWord() {
		return clinicSearchWord;
	}
	public void setClinicSearchWord(String clinicSearchWord) {
		this.clinicSearchWord = clinicSearchWord;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public String getDivisionYn() {
		return divisionYn;
	}
	public void setDivisionYn(String divisionYn) {
		this.divisionYn = divisionYn;
	}
	public String getPickupYn() {
		return pickupYn;
	}
	public void setPickupYn(String pickupYn) {
		this.pickupYn = pickupYn;
	}
	public String getKatalkYn() {
		return katalkYn;
	}
	public void setKatalkYn(String katalkYn) {
		this.katalkYn = katalkYn;
	}
	public Integer getMemNo() {
		return memNo;
	}
	public void setMemNo(Integer memNo) {
		this.memNo = memNo;
	}
	public String getMemId() {
		return memId;
	}
	public void setMemId(String memId) {
		this.memId = memId;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getClinicYn() {
		return clinicYn;
	}
	public void setClinicYn(String clinicYn) {
		this.clinicYn = clinicYn;
	}
	public String getOldPasswd() {
		return oldPasswd;
	}
	public void setOldPasswd(String oldPasswd) {
		this.oldPasswd = oldPasswd;
	}
	public String getBusinessOwner() {
		return businessOwner;
	}
	public void setBusinessOwner(String businessOwner) {
		this.businessOwner = businessOwner;
	}
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	public String getIdSrhType() {
		return idSrhType;
	}
	public void setIdSrhType(String idSrhType) {
		this.idSrhType = idSrhType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMtel1() {
		return mtel1;
	}
	public void setMtel1(String mtel1) {
		this.mtel1 = mtel1;
	}
	public String getMtel2() {
		return mtel2;
	}
	public void setMtel2(String mtel2) {
		this.mtel2 = mtel2;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}


}
