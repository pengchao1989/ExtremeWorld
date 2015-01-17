package com.yumfee.extremeworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.Site;
import com.yumfee.extremeworld.repository.SiteDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class SiteService
{
	private SiteDao siteDao;

	public Site getSite(Long id)
	{
		return siteDao.findOne(id);
	}
	
	public void saveSite(Site site)
	{
		siteDao.save(site);
	}
	
	public List<Site> getAllSite()
	{
		return (List<Site>) siteDao.findAll();
	}
	
	public Page<Site> getAllSite(int pageNumber, int pageSize)
	{
		PageRequest pageRequest = new PageRequest(pageNumber - 1,pageSize);
		return siteDao.findAll(pageRequest);
	}
	
	@Autowired
	public void setSiteDao(SiteDao siteDao)
	{
		this.siteDao = siteDao;
	}
	


	
	
	
}
