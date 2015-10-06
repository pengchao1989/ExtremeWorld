package com.yumfee.extremeworld.service.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.biz.CategoryPredefined;
import com.yumfee.extremeworld.repository.biz.CategoryPredefinedDao;

@Component
@Transactional
public class CategoryPredefinedService {

	@Autowired
	CategoryPredefinedDao categoryPredefinedDao;
	
	public List<CategoryPredefined> getAll(){
		return (List<CategoryPredefined>) categoryPredefinedDao.findAll();
	}
}
