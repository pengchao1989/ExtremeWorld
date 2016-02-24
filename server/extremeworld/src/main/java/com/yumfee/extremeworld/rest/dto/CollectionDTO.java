package com.yumfee.extremeworld.rest.dto;

import java.util.Date;

public class CollectionDTO {
	
	private Long id;
	private Date createTime;
	private TopicDTO topic;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public TopicDTO getTopic() {
		return topic;
	}
	public void setTopic(TopicDTO topic) {
		this.topic = topic;
	}
}
