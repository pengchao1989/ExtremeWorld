package com.yumfee.extremeworld.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "tb_sub_reply")
public class SubReply extends IdEntity
{
	private String content;
	private Date createTime;
	
	private User user;
	
	private Reply reply;
	private SubReply preSubReply;

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
	@JoinColumn(name = "reply_id")
	public Reply getReply() {
		return reply;
	}

	public void setReply(Reply reply) {
		this.reply = reply;
	}

	@ManyToOne
	@JoinColumn(name = "pid")
	public SubReply getPreSubReply()
	{
		return preSubReply;
	}

	public void setPreSubReply(SubReply preSubReply)
	{
		this.preSubReply = preSubReply;
	}
	
	
	
	
	
}
