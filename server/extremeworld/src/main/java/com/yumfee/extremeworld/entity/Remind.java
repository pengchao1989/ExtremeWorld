package com.yumfee.extremeworld.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_remind")
public class Remind extends IdEntity{
	
	private String content;
	private String targetContent;
	private String targetType;
	private Long targetId;
	private User speaker;
	private User listener;
	
	
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
	@ManyToOne
	@JoinColumn(name = "speaker_id")
	public User getSpeaker() {
		return speaker;
	}
	public void setSpeaker(User speaker) {
		this.speaker = speaker;
	}
	
	@ManyToOne
	@JoinColumn(name = "listener_id")
	public User getListener() {
		return listener;
	}
	public void setListener(User listener) {
		this.listener = listener;
	}
	
	
	
}
