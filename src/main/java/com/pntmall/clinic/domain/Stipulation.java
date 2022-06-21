package com.pntmall.clinic.domain;

import com.pntmall.common.type.Domain;

public class Stipulation extends Domain {

	private static final long serialVersionUID = -7468670654352173286L;
	private String title;
	private String content;
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



}
