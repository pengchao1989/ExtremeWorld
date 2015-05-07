package com.yumfee.extremeworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.Taxonomy;
import com.yumfee.extremeworld.repository.TaxonomyDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class TaxonomyService {

	@Autowired
	private TaxonomyDao taxonomyDao;
	
	public List<Taxonomy> getTaxonomyByHobby(Long hobbyId)
	{
		return taxonomyDao.findByHobbyId(hobbyId);
	}
}
