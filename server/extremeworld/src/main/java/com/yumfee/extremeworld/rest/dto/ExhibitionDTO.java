package com.yumfee.extremeworld.rest.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "exhibition")
public class ExhibitionDTO {
	private String action;
	private String title;
	private String img;
	private String data;

	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
