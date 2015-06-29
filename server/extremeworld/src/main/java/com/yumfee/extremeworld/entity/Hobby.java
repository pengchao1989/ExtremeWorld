package com.yumfee.extremeworld.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tb_hobby")
public class Hobby extends IdEntity
{
	private String name;
	private String description;
	private Date createTime;
	
	private List<Taxonomy> taxonomys = new ArrayList<Taxonomy>();
	private List<User> users= new ArrayList<User>();
	private List<Site> sites = new ArrayList<Site>();
	private List<Topic> topics = new ArrayList<Topic>();
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public Date getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
	
	//关系被维护端
	//referencedColumnName指向对象的列名
	@ManyToMany(mappedBy = "hobbys")
	public List<User> getUsers()
	{
		return users;
	}
	public void setUsers(List<User> users)
	{
		this.users = users;
	}
	
	//关系被维护端
	//referencedColumnName指向对象的列名
	@ManyToMany(mappedBy = "hobbys")
	public List<Site> getSites() {
		return sites;
	}
	public void setSites(List<Site> sites) {
		this.sites = sites;
	}
	
	//关系被维护端
	//referencedColumnName指向对象的列名
	@ManyToMany(mappedBy = "hobbys")
	public List<Topic> getTopics() {
		return topics;
	}
	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}
	
	//关系被维护端
	//referencedColumnName指向对象的列名
	@OneToMany(mappedBy = "hobby")
	public List<Taxonomy> getTaxonomys() {
		return taxonomys;
	}
	public void setTaxonomys(List<Taxonomy> taxonomys) {
		this.taxonomys = taxonomys;
	}

	
	
	
	
}
