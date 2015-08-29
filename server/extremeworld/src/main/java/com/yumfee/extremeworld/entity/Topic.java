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
	private String magicType;
	private String title;
	private String excerpt;
	private String content;
	private int imageCount;
	private int replyCount;
	private int allReplyCount;
	private int viewCount;
	private int agreeCount;
	private Date createTime;
	private Date updateTime;
	private int status;
	private String ip;
	
	private VideoDetail videoDetail;

	private MediaWrap mediaWrap;
	
	private Taxonomy taxonomy;
	
	private User user;
	
	private Course course;
	
	private List<Hobby> hobbys;
	
	private List<User> agrees;
	
	
	public String getType() {
		return type;
	}
	public void setType(String t) {
		this.type = t;
	}
	public String getMagicType() {
		return magicType;
	}
	public void setMagicType(String magicType) {
		this.magicType = magicType;
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
	public int getAllReplyCount() {
		return allReplyCount;
	}
	public void setAllReplyCount(int allReplyCount) {
		this.allReplyCount = allReplyCount;
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
	@JoinColumn(name = "video_detail_id")
	public VideoDetail getVideoDetail()
	{
		return videoDetail;
	}

	public void setVideoDetail(VideoDetail videoDetail)
	{
		this.videoDetail = videoDetail;
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
	
	@ManyToOne
	@JoinColumn(name = "course_id")
	public Course getCourse()
	{
		return course;
	}
	public void setCourse(Course course)
	{
		this.course = course;
	}
	
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



	
	@ManyToMany(cascade = { CascadeType.PERSIST },fetch = FetchType.LAZY)
	@JoinTable(name = "tb_topic_agree",
	joinColumns = { @JoinColumn(name = "topic_id", referencedColumnName = "id" ) },
	inverseJoinColumns = { @JoinColumn(name="user_id", referencedColumnName = "userId") })
	public List<User> getAgrees()
	{
		return agrees;
	}
	public void setAgrees(List<User> agrees)
	{
		this.agrees = agrees;
	}

	
	
	
	
}
