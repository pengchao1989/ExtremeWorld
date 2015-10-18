package com.jixianxueyuan.dto.biz;

public class GoodsDTO {

	private long id;
	private String name;
	private String taobaoId;
	private String type;
	private double startPrice;
	private double endPrice;
	private String cover;
	private String des;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTaobaoId() {
		return taobaoId;
	}
	public void setTaobaoId(String taobaoId) {
		this.taobaoId = taobaoId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getStartPrice() {
		return startPrice;
	}
	public void setStartPrice(double startPrice) {
		this.startPrice = startPrice;
	}
	public double getEndPrice() {
		return endPrice;
	}
	public void setEndPrice(double endPrice) {
		this.endPrice = endPrice;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	
	
}
