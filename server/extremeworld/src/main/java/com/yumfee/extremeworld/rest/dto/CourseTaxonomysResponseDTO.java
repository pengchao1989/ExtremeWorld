package com.yumfee.extremeworld.rest.dto;

import java.util.List;

public class CourseTaxonomysResponseDTO {

	List<CourseTaxonomyDTO> courseTaxonomyList;

	public List<CourseTaxonomyDTO> getCourseTaxonomyList() {
		return courseTaxonomyList;
	}

	public void setCourseTaxonomyList(List<CourseTaxonomyDTO> courseTaxonomyList) {
		this.courseTaxonomyList = courseTaxonomyList;
	}
	
}
