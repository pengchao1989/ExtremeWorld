package com.yumfee.extremeworld.rest.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "danmu")
public class DanmuDTO {

	private String text;
	private String color;
	private String position;
	private String size;
	private int time;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
	
}
