package com.yumfee.extremeworld.rest.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yumfee.extremeworld.entity.User;

public class ReplyDTO
{
	private Long id;
	private String content;
	private Date createTime;
	private int floor;
	private int subReplyCount;
	private UserMinDTO user;
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
	public UserMinDTO getUser()
	{
		return user;
	}
	public void setUser(UserMinDTO user)
	{
		this.user = user;
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
