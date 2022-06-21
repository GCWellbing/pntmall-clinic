package com.pntmall.clinic.domain;

import java.util.Date;

import com.pntmall.common.type.Domain;

public class ClinicAdjust extends Domain {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6850706703497611053L;

	private Integer sno;
	private Integer year;
	private Integer quarter;
	private Integer memNo;
	private Integer orderCnt;
	private Integer totSaleAmt;
	private Integer saleAmt;
	private Integer pickupSaleAmt;
	private Integer dpackSaleAmt;
	private Integer totSupplyAmt;
	private Float fee;
	private Float promoFee;
	private Float pickupFee;
	private Float dpackFee;
	private Integer adjustAmt;
	private Integer status;
	private String deadline;
	private String paymentDate;
	private String sapResult;
	private String sapMsg;
	private String sapGjahr;
	private String sapBelnr;
	private Date sapDate;
	private String clinicId;
	private String clinicName;
	
	public Integer getSno() {
		return sno;
	}
	public void setSno(Integer sno) {
		this.sno = sno;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getQuarter() {
		return quarter;
	}
	public void setQuarter(Integer quarter) {
		this.quarter = quarter;
	}
	public Integer getMemNo() {
		return memNo;
	}
	public void setMemNo(Integer memNo) {
		this.memNo = memNo;
	}
	public Integer getTotSaleAmt() {
		return totSaleAmt;
	}
	public void setTotSaleAmt(Integer totSaleAmt) {
		this.totSaleAmt = totSaleAmt;
	}
	public Integer getSaleAmt() {
		return saleAmt;
	}
	public void setSaleAmt(Integer saleAmt) {
		this.saleAmt = saleAmt;
	}
	public Integer getPickupSaleAmt() {
		return pickupSaleAmt;
	}
	public void setPickupSaleAmt(Integer pickupSaleAmt) {
		this.pickupSaleAmt = pickupSaleAmt;
	}
	public Integer getDpackSaleAmt() {
		return dpackSaleAmt;
	}
	public void setDpackSaleAmt(Integer dpackSaleAmt) {
		this.dpackSaleAmt = dpackSaleAmt;
	}
	public Integer getTotSupplyAmt() {
		return totSupplyAmt;
	}
	public void setTotSupplyAmt(Integer totSupplyAmt) {
		this.totSupplyAmt = totSupplyAmt;
	}
	public Float getFee() {
		return fee;
	}
	public void setFee(Float fee) {
		this.fee = fee;
	}
	public Float getPromoFee() {
		return promoFee;
	}
	public void setPromoFee(Float promoFee) {
		this.promoFee = promoFee;
	}
	public Float getPickupFee() {
		return pickupFee;
	}
	public void setPickupFee(Float pickupFee) {
		this.pickupFee = pickupFee;
	}
	public Float getDpackFee() {
		return dpackFee;
	}
	public void setDpackFee(Float dpackFee) {
		this.dpackFee = dpackFee;
	}
	public Integer getAdjustAmt() {
		return adjustAmt;
	}
	public void setAdjustAmt(Integer adjustAmt) {
		this.adjustAmt = adjustAmt;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getSapResult() {
		return sapResult;
	}
	public void setSapResult(String sapResult) {
		this.sapResult = sapResult;
	}
	public String getSapMsg() {
		return sapMsg;
	}
	public void setSapMsg(String sapMsg) {
		this.sapMsg = sapMsg;
	}
	public String getSapGjahr() {
		return sapGjahr;
	}
	public void setSapGjahr(String sapGjahr) {
		this.sapGjahr = sapGjahr;
	}
	public String getSapBelnr() {
		return sapBelnr;
	}
	public void setSapBelnr(String sapBelnr) {
		this.sapBelnr = sapBelnr;
	}
	public Date getSapDate() {
		return sapDate;
	}
	public void setSapDate(Date sapDate) {
		this.sapDate = sapDate;
	}
	public String getClinicId() {
		return clinicId;
	}
	public void setClinicId(String clinicId) {
		this.clinicId = clinicId;
	}
	public String getClinicName() {
		return clinicName;
	}
	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}
	public Integer getOrderCnt() {
		return orderCnt;
	}
	public void setOrderCnt(Integer orderCnt) {
		this.orderCnt = orderCnt;
	}
	
}
