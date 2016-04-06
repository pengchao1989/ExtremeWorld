package com.yumfee.extremeworld.rest.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Topic")
public class TopicExtraDTO {
	private Long id;
	private boolean isAgreed;
	private boolean isCollected;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean isAgreed() {
		return isAgreed;
	}
	public void setAgreed(boolean isAgreed) {
		this.isAgreed = isAgreed;
	}
	public boolean isCollected() {
		return isCollected;
	}
	public void setCollected(boolean isCollected) {
		this.isCollected = isCollected;
	}
}
