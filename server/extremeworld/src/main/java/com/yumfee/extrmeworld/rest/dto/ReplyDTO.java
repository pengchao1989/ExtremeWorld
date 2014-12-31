package com.yumfee.extrmeworld.rest.dto;

import java.util.Date;
import java.util.List;

import com.yumfee.extremeworld.entity.UserInfo;

public class ReplyDTO
{
	private Long id;
	private String content;
	private Date createTime;
	private UserInfoMinDTO userInfo;
	private List<SubReplyDTO> subReplys;
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public Date getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
	public UserInfoMinDTO getUserInfo()
	{
		return userInfo;
	}
	public void setUserInfo(UserInfoMinDTO userInfo)
	{
		this.userInfo = userInfo;
	}
	public List<SubReplyDTO> getSubReplys()
	{
		return subReplys;
	}
	public void setSubReplys(List<SubReplyDTO> subReplys)
	{
		this.subReplys = subReplys;
	}

}
