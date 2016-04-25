package com.yumfee.extremeworld.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yumfee.extremeworld.proto.CourseMinProto.CourseMinDTO;

@Entity
@Table(name = "tb_course_new")
public class Course extends IdEntity
{
	private String name;
	private String content;
	private String type;    //course:正常状态   revision:修订版本
	private Date createTime;
	private Date modifyTime;
	private Long pid;
	private int sortWeight;
	
	private CourseTaxonomy courseTaxonomy;
	
	private User user;  //create user
	
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

	
	public int getSortWeight() {
		return sortWeight;
	}

	public void setSortWeight(int sortWeight) {
		this.sortWeight = sortWeight;
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
	
	public CourseMinDTO buildCourseMinDTO(){
		return CourseMinDTO.newBuilder().setId(this.id).setName(this.name).build();
		
	}
}
