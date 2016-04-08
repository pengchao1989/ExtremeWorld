package com.yumfee.extremeworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.Exhibition;
import com.yumfee.extremeworld.repository.ExhibitionDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class ExhibitionService {

	@Autowired
	private ExhibitionDao exhibitionDao;
	
	public Page<Exhibition> getAllByHobby(Long hobbyId, int pageNumber, int pageSize,String sortType){
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return exhibitionDao.findByHobbyId(hobbyId, pageRequest);
	}
	
	public Exhibition saveExhibition(Exhibition exhibition){
		return exhibitionDao.save(exhibition);
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} 
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
}
