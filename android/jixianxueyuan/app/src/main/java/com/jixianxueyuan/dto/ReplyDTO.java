package com.jixianxueyuan.dto;

import java.io.Serializable;
import java.util.List;

public class ReplyDTO implements Serializable
{
	private Long id;
	private String content;
	private String createTime;
	private int floor;
    private int subReplyCount;
	private UserMinDTO user;
	private MediaWrapDTO  mediaWrap;
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
	public String getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}
	public int getFloor() {
		return floor;
	}
    public int getSubReplyCount() {
        return subReplyCount;
    }
    public void setSubReplyCount(int subReplyCount) {
        this.subReplyCount = subReplyCount;
    }
    public void setFloor(int floor) {
		this.floor = floor;
	}
	public UserMinDTO getUser()
	{
		return user;
	}
	public void setUser(UserMinDTO user)
	{
		this.user = user;
	}

	public MediaWrapDTO getMediaWrap() {
		return mediaWrap;
	}

	public void setMediaWrap(MediaWrapDTO mediaWrap) {
		this.mediaWrap = mediaWrap;
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
