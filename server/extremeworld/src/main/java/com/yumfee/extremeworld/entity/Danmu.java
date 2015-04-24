package com.yumfee.extremeworld.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_danmu")
public class Danmu extends IdEntity{

	private String text;
	private String color;
	private String position;
	private String size;
	private int time;
	private Date createTime;
	
	private UserInfo userInfo;
	private Video video;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getUserInfo()
	{
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo)
	{
		this.userInfo = userInfo;
	}

	@ManyToOne
	@JoinColumn(name = "video_id")
	public Video getVideo()
	{
		return video;
	}

	public void setVideo(Video video)
	{
		this.video = video;
	}
/*	@Override
	public String toString() {
		return "{\"text\"=\"" + text + "\", \"color\"=\"" + color + "\", \"position\"=\""
				+ position + "\", \"size\"=\"" + size + "\", \"time\"=" + time + "}";
	}*/
	
	
	
}
