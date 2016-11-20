package com.yumfee.extremeworld.config;

public enum PointType {

	LOGIN("login", 10),
	TOPIC("topic", 20),
	REPLY("reply", 5);
	
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
