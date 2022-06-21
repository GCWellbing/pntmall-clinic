package com.pntmall.clinic.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.pntmall.common.type.SearchDomain;

public class BbsSearch extends SearchDomain {

	private static final long serialVersionUID = 3224823735450939516L;
	private Integer bbsNo;
	private String cate;
	private String title;
	private String content;
	private String status;
	private String fixYn;
	@DateTimeFormat(pattern="yyyy.MM.dd")
	private Date fromCdate;
	@DateTimeFormat(pattern="yyyy.MM.dd")
	private Date toCdate;
	private String userName;

	public Integer getBbsNo() {
		return bbsNo;
	}
	public void setBbsNo(Integer bbsNo) {
		this.bbsNo = bbsNo;
	}
	public String getCate() {
		return cate;
	}
	public void setCate(String cate) {
		this.cate = cate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFixYn() {
		return fixYn;
	}
	public void setFixYn(String fixYn) {
		this.fixYn = fixYn;
	}
	public Date getFromCdate() {
		return fromCdate;
	}
	public void setFromCdate(Date fromCdate) {
		this.fromCdate = fromCdate;
	}
	public Date getToCdate() {
		return toCdate;
	}
	public void setToCdate(Date toCdate) {
		this.toCdate = toCdate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}




}
