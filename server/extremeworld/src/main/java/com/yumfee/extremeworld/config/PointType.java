package com.yumfee.extremeworld.config;

public enum PointType {

	LOGIN("login", 5),
	TOPIC("topic", 10),
	REPLY("reply", 2);
	
	private String type;
	private int count;
	
	
	private  PointType(String type, int count) {
		this.type = type;
		this.count = count;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
