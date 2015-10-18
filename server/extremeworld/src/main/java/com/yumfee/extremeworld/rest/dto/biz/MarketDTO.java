package com.yumfee.extremeworld.rest.dto.biz;

import java.util.List;

public class MarketDTO {
	
	List<CategoryDTO> categoryList;

	public List<CategoryDTO> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<CategoryDTO> categoryList) {
		this.categoryList = categoryList;
	}
	
	
}
