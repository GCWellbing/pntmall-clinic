package com.pntmall.clinic.domain;

import com.pntmall.common.type.Domain;

public class MemberLog extends Domain {

	private static final long serialVersionUID = 2337811331122129297L;

	private Integer memNo;
	private String ip;
	private String successYn;
	private String device;

	public Integer getMemNo() {
		return memNo;
	}
	public void setMemNo(Integer memNo) {
		this.memNo = memNo;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getSuccessYn() {
		return successYn;
	}
	public void setSuccessYn(String successYn) {
		this.successYn = successYn;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}



}
