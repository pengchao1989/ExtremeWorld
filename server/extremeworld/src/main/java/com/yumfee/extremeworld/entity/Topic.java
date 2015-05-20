package com.yumfee.extremeworld.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tb_topic")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "t", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue("1")
public class Topic extends IdEntity
{
	private String type;
	private String title;
	private String excerpt;
	private String content;
	private int imageCount;
	private int replyCount;
	private int viewCount;
	private int agreeCount;
	private Date createTime;
	private Date updateTime;
	private int status;
	private String ip;

	private MediaWrap mediaWrap;
	
	private Taxonomy taxonomy;
	
	private User user;
	
	private Course coursce;
	
	private List<Hobby> hobbys;
	
	
	public String getType() {
		return type;
	}
	public void setType(String t) {
		this.type = t;
	}
	@NotBlank
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getExcerpt()
	{
		return excerpt;
	}
	public void setExcerpt(String excerpt)
	{
		this.excerpt = excerpt;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public int getImageCount()
	{
		return imageCount;
	}
	public void setImageCount(int imageCount)
	{
		this.imageCount = imageCount;
	}
	public int getReplyCount()
	{
		return replyCount;
	}
	public void setReplyCount(int replyCount)
	{
		this.replyCount = replyCount;
	}
	
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	public int getAgreeCount() {
		return agreeCount;
	}
	public void setAgreeCount(int agreeCount) {
		this.agreeCount = agreeCount;
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
	public Date getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public String getIp()
	{
		return ip;
	}
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "mediawrap_id")
	public MediaWrap getMediaWrap() {
		return mediaWrap;
	}
	public void setMediaWrap(MediaWrap mediaWrap) {
		this.mediaWrap = mediaWrap;
	}
	
	
	@ManyToOne
	@JoinColumn(name = "taxonomy_id")
	public Taxonomy getTaxonomy() {
		return taxonomy;
	}
	public void setTaxonomy(Taxonomy taxonomy) {
		this.taxonomy = taxonomy;
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
	
	// optional表示该对象可有可无，它的值为true表示该外键可以为null，它的值为false表示该外键为not null  
	@ManyToOne(fetch=FetchType.LAZY, optional = false)
	@JoinTable(name = "tb_course_topic",
	joinColumns = { @JoinColumn(name="topic_id", referencedColumnName= "id")},
	inverseJoinColumns = { @JoinColumn(name="course_id", referencedColumnName = "id") })
	@JsonIgnore
	public Course getCourse()
	{
		return coursce;
	}
	public void setCourse(Course coursce)
	{
		this.coursce = coursce;
	}
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tb_topic_hobby",
	joinColumns = { @JoinColumn(name = "topic_id", referencedColumnName = "id" ) },
	inverseJoinColumns = { @JoinColumn(name="hobby_id", referencedColumnName = "id") })
	public List<Hobby> getHobbys() {
		return hobbys;
	}
	public void setHobbys(List<Hobby> hobbys) {
		this.hobbys = hobbys;
	}
	
}
