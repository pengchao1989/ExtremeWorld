package com.yumfee.extremeworld.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "tb_reply")
public class Reply extends IdEntity
{
	private String content;
	private Date createTime;
	private int floor;
	private int subReplyCount;
	private int agreeCount;
	
	private List<SubReply> subReplys = new ArrayList<SubReply>();
	
	private User user;
	private Topic topic;
	private MediaWrap mediaWrap;
	
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
	
	public int getSubReplyCount() {
		return subReplyCount;
	}

	public void setSubReplyCount(int subReplyCount) {
		this.subReplyCount = subReplyCount;
	}

	public int getAgreeCount() {
		return agreeCount;
	}

	public void setAgreeCount(int agreeCount) {
		this.agreeCount = agreeCount;
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
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "mediawrap_id")
	public MediaWrap getMediaWrap() {
		return mediaWrap;
	}
	public void setMediaWrap(MediaWrap mediaWrap) {
		this.mediaWrap = mediaWrap;
	}
}
