package com.yumfee.extremeworld.rest.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RemindDTO {

	private String content;
	private String targetContent;
	private String targetType;
	private Long targetId;
	private Date createTime;
	
	private UserMinDTO speaker;
	private UserMinDTO listener;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTargetContent() {
		return targetContent;
	}
	public void setTargetContent(String targetContent) {
		this.targetContent = targetContent;
	}
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public UserMinDTO getSpeaker() {
		return speaker;
	}
	public void setSpeaker(UserMinDTO speaker) {
		this.speaker = speaker;
	}
	public UserMinDTO getListener() {
		return listener;
	}
	public void setListener(UserMinDTO listener) {
		this.listener = listener;
	}
}
