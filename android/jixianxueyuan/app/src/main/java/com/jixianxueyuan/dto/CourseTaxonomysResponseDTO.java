package com.jixianxueyuan.dto;

import java.io.Serializable;
import java.util.List;

public class CourseTaxonomysResponseDTO implements Serializable {

	List<CourseTaxonomyDTO> courseTaxonomyList;

	public List<CourseTaxonomyDTO> getCourseTaxonomyList() {
		return courseTaxonomyList;
	}

	public void setCourseTaxonomyList(List<CourseTaxonomyDTO> courseTaxonomyList) {
		this.courseTaxonomyList = courseTaxonomyList;
	}
	
}
