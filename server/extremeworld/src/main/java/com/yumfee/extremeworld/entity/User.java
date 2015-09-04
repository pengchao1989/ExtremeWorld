package com.yumfee.extremeworld.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tb_userinfo")
@PrimaryKeyJoinColumn(name = "userId")
public class User extends UserBase
{
	private String gender;
	private String birth;
	private String avatar;
	private String description;
	private String signature;
	private String bg;
	private String qq;
	private String weixin;
	private String geoHash;
	private String hobbyStamp;
	private Date   geoModifyTime;
	
	//private List<Topic> topics = new ArrayList<Topic>();
	
	private Country country;
	
	private List<Hobby> hobbys = new ArrayList<Hobby>();
	private List<Site> sites = new ArrayList<Site>();
	private List<Topic> agreeTopics = new ArrayList<Topic>();
	
	private List<User> followings = new ArrayList<User>();
	private List<User> followers = new ArrayList<User>();
	 
	
	/*private List<Credit> credits = new ArrayList<Credit>(); //user自己的技能xue习列表
*/	/*private List<Score> scores = new ArrayList<Score>();  //user对别人的Credit评分*/	
	
/*	public UserInfo(Long id)
	{
		super(id);
	}*/
	
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
	
	public String getBg() {
		return bg;
	}
	public void setBg(String bg) {
		this.bg = bg;
	}
	public String getQq()
	{
		return qq;
	}
	public void setQq(String qq)
	{
		this.qq = qq;
	}
	public String getWeixin()
	{
		return weixin;
	}
	public void setWeixin(String weixin)
	{
		this.weixin = weixin;
	}
	
	public String getGeoHash() {
		return geoHash;
	}
	public void setGeoHash(String geoHash) {
		this.geoHash = geoHash;
	}
	public String getHobbyStamp() {
		return hobbyStamp;
	}
	public void setHobbyStamp(String hobbyStamp) {
		this.hobbyStamp = hobbyStamp;
	}
	public Date getGeoModifyTime() {
		return geoModifyTime;
	}
	public void setGeoModifyTime(Date geoModifyTime) {
		this.geoModifyTime = geoModifyTime;
	}
	
	@OneToOne
	@JoinColumn(name = "country_id")
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	
	/*	@OneToMany(fetch = FetchType.LAZY, mappedBy="user")
	public List<Topic> getTopics() {
		return topics;
	}
	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}*/
	@JsonIgnore
	//关系维护端,mappedBy属性定义了userinfo为双向关系的维护端
	//@ManyToMany(mappedBy = "userInfos")
	//关系被维护端
	//referencedColumnName指向对象的列名
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tb_user_hobby",
	joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "userId" ) },
	inverseJoinColumns = { @JoinColumn(name="hobby_id", referencedColumnName = "id") })
	public List<Hobby> getHobbys()
	{
		return hobbys;
	}
	public void setHobbys(List<Hobby> interests)
	{
		this.hobbys = interests;
	}
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tb_user_site",
	joinColumns = { @JoinColumn(name="user_id", referencedColumnName= "userId")},
	inverseJoinColumns = { @JoinColumn(name="site_id", referencedColumnName = "id") })
	public List<Site> getSites()
	{
		return sites;
	}
	public void setSites(List<Site> sites)
	{
		this.sites = sites;
	}
	
	@JsonIgnore
	@ManyToMany(mappedBy = "agrees")
	public List<Topic> getAgreeTopics()
	{
		return agreeTopics;
	}
	public void setAgreeTopics(List<Topic> agreeTopics)
	{
		this.agreeTopics = agreeTopics;
	}
	
	
	@JsonIgnore
	@ManyToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinTable(name = "tb_following",
	joinColumns = { @JoinColumn(name="uid")},
	inverseJoinColumns = { @JoinColumn(name="fuid") })
	public List<User> getFollowings() {
		return followings;
	}
	public void setFollowings(List<User> followings) {
		this.followings = followings;
	}
	
	@JsonIgnore
	@ManyToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinTable(name = "tb_follower",
	joinColumns = { @JoinColumn(name="uid")},
	inverseJoinColumns = { @JoinColumn(name="fuid") })
	public List<User> getFollowers() {
		return followers;
	}
	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}
	
	
/*	@OneToMany(fetch = FetchType.LAZY, mappedBy="user")
	public List<Credit> getCredits() {
		return credits;
	}
	public void setCredits(List<Credit> credits) {
		this.credits = credits;
	}*/
	
/*	@OneToMany(fetch = FetchType.LAZY, mappedBy="user")
	public List<Score> getScores() {
		return scores;
	}
	public void setScores(List<Score> scores) {
		this.scores = scores;
	}*/
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public boolean equals(Object obj) {   
        if (obj instanceof User) {   
            User u = (User) obj;   
            return this.id.equals(u.id);   
        }   
        return super.equals(obj); 
	}
}
