package com.pntmall.clinic.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.pntmall.common.type.SearchDomain;

public class QnaSearch extends SearchDomain {

	private static final long serialVersionUID = 4728119240095141274L;
	private Integer qnaNo;
	private String cate;
	private Integer pno;
	private Integer clinicMemNo;
	private String title;
	private String question;
	private String answer;
	private String status;
	@DateTimeFormat(pattern="yyyy.MM.dd")
	private Date fromDate;
	@DateTimeFormat(pattern="yyyy.MM.dd")
	private Date toDate;
	@DateTimeFormat(pattern="yyyy.MM.dd")
	private Date fromQdate;
	@DateTimeFormat(pattern="yyyy.MM.dd")
	private Date toQdate;

	public Integer getQnaNo() {
		return qnaNo;
	}
	public void setQnaNo(Integer qnaNo) {
		this.qnaNo = qnaNo;
	}
	public String getCate() {
		return cate;
	}
	public void setCate(String cate) {
		this.cate = cate;
	}
	public Integer getPno() {
		return pno;
	}
	public void setPno(Integer pno) {
		this.pno = pno;
	}
	public Integer getClinicMemNo() {
		return clinicMemNo;
	}
	public void setClinicMemNo(Integer clinicMemNo) {
		this.clinicMemNo = clinicMemNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Date getFromQdate() {
		return fromQdate;
	}
	public void setFromQdate(Date fromQdate) {
		this.fromQdate = fromQdate;
	}
	public Date getToQdate() {
		return toQdate;
	}
	public void setToQdate(Date toQdate) {
		this.toQdate = toQdate;
	}


}
