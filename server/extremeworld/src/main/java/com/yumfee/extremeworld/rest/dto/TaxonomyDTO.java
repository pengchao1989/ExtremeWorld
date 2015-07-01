package com.yumfee.extremeworld.rest.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "taxonomy")
public class TaxonomyDTO {

	private Long id;
	private String name;
	private String type;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
