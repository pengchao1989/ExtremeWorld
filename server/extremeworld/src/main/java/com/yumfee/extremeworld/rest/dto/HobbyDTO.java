package com.yumfee.extremeworld.rest.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "hobby")
public class HobbyDTO {

	private Long id;
	private String name;
	private List<TaxonomyDTO> taxonomys;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TaxonomyDTO> getTaxonomys() {
		return taxonomys;
	}
	public void setTaxonomys(List<TaxonomyDTO> taxonomys) {
		this.taxonomys = taxonomys;
	}
	
	
	
	
}
