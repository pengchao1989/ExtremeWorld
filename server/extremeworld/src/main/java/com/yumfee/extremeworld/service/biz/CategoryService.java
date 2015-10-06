package com.yumfee.extremeworld.service.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.biz.Category;
import com.yumfee.extremeworld.repository.biz.CategoryDao;

@Component
@Transactional
public class CategoryService {

	@Autowired
	CategoryDao categoryDao;

	public List<Category> getAllByHobbyId(long hobbyId){
		return categoryDao.findByHobbyId(hobbyId);
	}
}
