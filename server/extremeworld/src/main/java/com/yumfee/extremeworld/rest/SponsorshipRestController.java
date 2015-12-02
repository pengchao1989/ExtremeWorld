package com.yumfee.extremeworld.rest;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.config.HobbyPathConfig;
import com.yumfee.extremeworld.entity.Sponsorship;
import com.yumfee.extremeworld.rest.dto.MyPage;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.SponsorshipDTO;
import com.yumfee.extremeworld.service.SponsorshipService;

@RestController
@RequestMapping(value = "/api/secure/v1/{hobby}/sponsorship")
public class SponsorshipRestController {
	private static final String PAGE_SIZE = "30";
	
	@Autowired
	SponsorshipService sponsorshipService;
	@Autowired
	private Validator validator;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse list(
			@PathVariable String hobby,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		
		long hobbyId = HobbyPathConfig.getHobbyId(hobby);
		Page<Sponsorship> sponsorshipPage = sponsorshipService.getAllByHobbyId(hobbyId, pageNumber, pageSize, sortType);
		
		MyPage<SponsorshipDTO, Sponsorship> mySponsorshipPage = new MyPage<SponsorshipDTO, Sponsorship>(SponsorshipDTO.class, sponsorshipPage);
		
		return MyResponse.ok(mySponsorshipPage, true);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public MyResponse create(@RequestBody Sponsorship sponsorship, UriComponentsBuilder uriBuilder){
		//JSR303
		BeanValidators.validateWithException(validator,sponsorship);

		sponsorshipService.save(sponsorship);
		
		Sponsorship result = sponsorshipService.get(sponsorship.getId());
		SponsorshipDTO dto = BeanMapper.map(result, SponsorshipDTO.class);
		
		
		return MyResponse.ok(dto);
	}
}
