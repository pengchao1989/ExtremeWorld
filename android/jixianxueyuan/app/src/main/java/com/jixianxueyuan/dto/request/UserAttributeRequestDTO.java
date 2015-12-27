package com.jixianxueyuan.dto.request;

import java.io.Serializable;

public class UserAttributeRequestDTO implements Serializable{

	private Long userId;
	private String attributeName;
	private String attributeValue;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
}
