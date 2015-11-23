package com.yumfee.extremeworld.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tb_course_catalogue")
public class CourseCatalogue extends IdEntity{

	private String name;
	private CourseTaxonomy courseTaxonomy;
	
	private List<Course> courses;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "taxonomy_id")
	public CourseTaxonomy getCourseTaxonomy() {
		return courseTaxonomy;
	}
	public void setCourseTaxonomy(CourseTaxonomy courseTaxonomy) {
		this.courseTaxonomy = courseTaxonomy;
	}
	
	@JsonIgnore
	@OneToMany(mappedBy = "courseCatalogue")
	public List<Course> getCourses() {
		return courses;
	}
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	
	
	
}
