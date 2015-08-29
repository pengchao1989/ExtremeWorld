package com.yumfee.extremeworld.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_remind")
public class Remind extends IdEntity{
	
	private int type;
	private long sourceId;
	private String content;
	private String targetContent;
	private int targetType;
	private Long targetId;
	private Date createTime;
	private User speaker;
	private User listener;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getSourceId() {
		return sourceId;
	}
	public void setSourceId(long sourceId) {
		this.sourceId = sourceId;
	}
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
	public int getTargetType() {
		return targetType;
	}
	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
