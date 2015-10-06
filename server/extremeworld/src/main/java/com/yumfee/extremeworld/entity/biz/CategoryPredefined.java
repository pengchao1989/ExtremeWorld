package com.yumfee.extremeworld.entity.biz;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.yumfee.extremeworld.entity.IdEntity;

@Entity
@Table(name = "biz_category_predefined")
public class CategoryPredefined extends IdEntity{

	private String ename;
	private String name;
	private Date createTime;
	private List<Category> categorys;
	
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
	
	@OneToMany(mappedBy = "categoryPredefined")
	public List<Category> getCategorys() {
		return categorys;
	}
	public void setCategorys(List<Category> categorys) {
		this.categorys = categorys;
	}
	
	
}
