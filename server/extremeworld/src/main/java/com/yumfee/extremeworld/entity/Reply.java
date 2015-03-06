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
	
	private List<SubReply> subReplys = new ArrayList<SubReply>();
	
	private UserInfo userInfo;

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
	public UserInfo getUserInfo()
	{
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo)
	{
		this.userInfo = userInfo;
	}
	
	
	
}