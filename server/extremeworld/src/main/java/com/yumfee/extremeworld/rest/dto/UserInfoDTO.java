package com.yumfee.extremeworld.rest.dto;

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonFormat;


@XmlRootElement(name = "UserInfo")
public class UserInfoDTO
{
	private Long id;
	private String loginName;
	private String name;
	private String roles;
	private Date registerDate;
	private String nickName;
	private String gender;
	private String birth;
	private String avatar;
	private String description;
	
	private List<UserInterestDTO> interests;
	private List<SiteDTO> sites;
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getLoginName()
	{
		return loginName;
	}
	public void setLoginName(String loginName)
	{
		this.loginName = loginName;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getRoles()
	{
		return roles;
	}
	public void setRoles(String roles)
	{
		this.roles = roles;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getRegisterDate()
	{
		return registerDate;
	}
	public void setRegisterDate(Date registerDate)
	{
		this.registerDate = registerDate;
	}
	public String getNickName()
	{
		return nickName;
	}
	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}
	public String getGender()
	{
		return gender;
	}
	public void setGender(String gender)
	{
		this.gender = gender;
	}
	public String getBirth()
	{
		return birth;
	}
	public void setBirth(String birth)
	{
		this.birth = birth;
	}
	public String getAvatar()
	{
		return avatar;
	}
	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public List<UserInterestDTO> getInterests()
	{
		return interests;
	}
	public void setInterests(List<UserInterestDTO> interests)
	{
		this.interests = interests;
	}
	public List<SiteDTO> getSites()
	{
		return sites;
	}
	public void setSites(List<SiteDTO> sites)
	{
		this.sites = sites;
	}
	
}
