package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Site;

public interface SiteDao extends PagingAndSortingRepository<Site, Long>
{

}
