package com.yumfee.extremeworld.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "tb_reply")
public class Reply extends IdEntity
{
	private String content;
	private Date createTime;
	private int floor;
	
	private List<SubReply> subReplys = new ArrayList<SubReply>();
	
	private User user;
	private Topic topic;
	
	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	@OneToMany
	@JoinColumn(name = "reply_id")
	public List<SubReply> getSubReplys()
	{
		return subReplys;
	}

	public void setSubReplys(List<SubReply> subReplys)
	{
		this.subReplys = subReplys;
	}

	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	@ManyToOne
	@JoinColumn(name = "topic_id")
	public Topic getTopic()
	{
		return topic;
	}

	public void setTopic(Topic topic)
	{
		this.topic = topic;
	}
	
	
	
	
}
