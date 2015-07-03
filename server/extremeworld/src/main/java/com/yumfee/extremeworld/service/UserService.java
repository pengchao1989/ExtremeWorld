package com.yumfee.extremeworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.repository.UserDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class UserService {

	@Autowired
	private UserDao userDao;
	
	public User getUser(Long id)
	{
		return userDao.findOne(id);
	}
	
	public List<User> getAll()
	{
		return (List<User>) userDao.findAll();
	}
	
	public Page<User> findByGeoHash(String geoHash,  int pageNumber, int pageSize,String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		
		return userDao.findByGeoHashLike(geoHash, pageRequest);
	}
	
	public User saveUser(User user)
	{
		return userDao.save(user);
	}
	
	public List<User> getFollowings(Long id)
	{
		return userDao.findOne(id).getFollowings();
	}
	
	public List<User> getFollowers(Long id)
	{
		return userDao.findOne(id).getFollowers();
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.ASC, "geoHash");
		} 
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
}
