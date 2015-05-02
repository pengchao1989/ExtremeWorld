package com.yumfee.extremeworld.entity;

import java.util.Date;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tb_course")
public class Course extends IdEntity
{
	private String name;
	private String content;
	private String type;    //course:正常状态   revision:修订版本
	private String t;
	private Date createTime;
	private Date modifyTime;
	private Long pid;
	
	private CourseTaxonomy courseTaxonomy;
	
	private UserInfo userInfo;
	
	@NotBlank
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@NotBlank
	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreateTime()
	{
		return createTime;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
	
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getModifyTime()
	{
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime)
	{
		this.modifyTime = modifyTime;
	}
	
	

	public Long getPid()
	{
		return pid;
	}

	public void setPid(Long pid)
	{
		this.pid = pid;
	}

	@ManyToOne
	@JoinColumn(name = "user_id")
	public UserInfo getUserInfo()
	{
		return userInfo;
	}

	public void setUserInfo(UserInfo user)
	{
		this.userInfo = user;
	}

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "taxonomy_id")
	public CourseTaxonomy getCourseTaxonomy()
	{
		return courseTaxonomy;
	}

	public void setCourseTaxonomy(CourseTaxonomy courseTaxonomy)
	{
		this.courseTaxonomy = courseTaxonomy;
	}
	
}
