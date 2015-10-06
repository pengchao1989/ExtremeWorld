package com.yumfee.extremeworld.entity.biz;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.yumfee.extremeworld.entity.Hobby;
import com.yumfee.extremeworld.entity.IdEntity;

@Entity
@Table(name = "biz_category")
public class Category extends IdEntity {

	private String ename;
	private String name;
	private Date createTime;
	private Hobby hobby;
	private CategoryPredefined categoryPredefined;
	
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
	
	@ManyToOne
	@JoinColumn(name = "hobby_id")
	public Hobby getHobby() {
		return hobby;
	}
	public void setHobby(Hobby hobby) {
		this.hobby = hobby;
	}
	
	@ManyToOne
	@JoinColumn(name = "predefined_id")
	public CategoryPredefined getCategoryPredefined() {
		return categoryPredefined;
	}
	public void setCategoryPredefined(CategoryPredefined categoryPredefined) {
		this.categoryPredefined = categoryPredefined;
	}
}
