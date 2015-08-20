package com.yumfee.extremeworld.rest.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;


@XmlRootElement(name = "UserInfo")
public class UserDTO
{
	private Long id;
	private String loginName;
	private String name;
	private Date registerDate;
	private String gender;
	private String birth;
	private String avatar;
	private String description;
	private String signature;
	private String hobbyStamp;
	private String geoHash;
	private Date geoModifyTime;
	private double distance;
	
	private CountryDTO country;
	
	private List<UserInterestDTO> interests;
	
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

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getRegisterDate()
	{
		return registerDate;
	}
	public void setRegisterDate(Date registerDate)
	{
		this.registerDate = registerDate;
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
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getHobbyStamp() {
		return hobbyStamp;
	}
	public void setHobbyStamp(String hobbyStamp) {
		this.hobbyStamp = hobbyStamp;
	}
	public String getGeoHash() {
		return geoHash;
	}
	public void setGeoHash(String geoHash) {
		this.geoHash = geoHash;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getGeoModifyTime() {
		return geoModifyTime;
	}
	public void setGeoModifyTime(Date geoModifyTime) {
		this.geoModifyTime = geoModifyTime;
	}
	public CountryDTO getCountry() {
		return country;
	}
	public void setCountry(CountryDTO country) {
		this.country = country;
	}
	public List<UserInterestDTO> getInterests()
	{
		return interests;
	}
	public void setInterests(List<UserInterestDTO> interests)
	{
		this.interests = interests;
	}
	
}
