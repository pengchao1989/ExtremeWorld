package com.yumfee.extremeworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.Sponsorship;
import com.yumfee.extremeworld.repository.SponsorshipDao;

@Component
@Transactional
public class SponsorshipService {

	private SponsorshipDao sponsorshipDao;

	@Autowired
	public void setSponsorshipDao(SponsorshipDao sponsorshipDao) {
		this.sponsorshipDao = sponsorshipDao;
	}
	
	public Sponsorship get(Long id){
		return sponsorshipDao.findOne(id);
	}
	
	public void save(Sponsorship sponsorship){
		sponsorshipDao.save(sponsorship);
	}
	
	public void saveSponsorship(Sponsorship sponsorship){
		sponsorshipDao.save(sponsorship);
	}
	
	public Page<Sponsorship> getAll(int pageNumber, int pageSize,String sortType){
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return sponsorshipDao.findAll(pageRequest);
	}
	
	public Page<Sponsorship> getAllByHobbyId(Long hobbyId, int pageNumber, int pageSize,String sortType){
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return sponsorshipDao.findByHobbyId(hobbyId,pageRequest);
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("sum".equals(sortType)) {
			sort = new Sort(Direction.DESC, "sum");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
}
