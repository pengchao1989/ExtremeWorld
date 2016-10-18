package com.yumfee.extremeworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.config.GeneralStatus;
import com.yumfee.extremeworld.entity.Group;
import com.yumfee.extremeworld.repository.GroupDao;

@Component
@Transactional
public class GroupService {

	private GroupDao groupDao;
	
	
	public Page<Group> getByHobby(Long hobbyId, int pageNumber, int pageSize,String sortType){
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return groupDao.findByHobbyIdAndStatus(hobbyId, GeneralStatus.PUBLIC, pageRequest);
	}
	

	public GroupDao getGroupDao() {
		return groupDao;
	}

	@Autowired
	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("title".equals(sortType)) {
			sort = new Sort(Direction.ASC, "title");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
}
