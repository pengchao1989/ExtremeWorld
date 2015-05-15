package com.yumfee.extremeworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	public List<User> getFollowings(Long id)
	{
		return userDao.findOne(id).getFollowings();
	}
	
	public List<User> getFollowers(Long id)
	{
		return userDao.findOne(id).getFollowers();
	}
	
}
