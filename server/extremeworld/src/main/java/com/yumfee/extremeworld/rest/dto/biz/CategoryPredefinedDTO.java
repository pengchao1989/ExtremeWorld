package com.yumfee.extremeworld.rest.dto.biz;

import java.util.Date;

public class CategoryPredefinedDTO {
	private String ename;
	private String name;
	private Date createTime;
	
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
