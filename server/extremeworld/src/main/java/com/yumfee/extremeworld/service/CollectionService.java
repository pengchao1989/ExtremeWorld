package com.yumfee.extremeworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.Collection;
import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.repository.CollectionDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class CollectionService {

	private CollectionDao collectionDao;
	
	public Page<Collection> getCollectionByUser(long userId, int pageNumber, int pageSize,String sortType){
		
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		
		return collectionDao.findByUserId(userId, pageRequest);
	}
	
	public Collection saveCollection(Collection collection){
		return collectionDao.save(collection);
	}

	public CollectionDao getCollectionDao() {
		return collectionDao;
	}
	@Autowired
	public void setCollectionDao(CollectionDao collectionDao) {
		this.collectionDao = collectionDao;
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
