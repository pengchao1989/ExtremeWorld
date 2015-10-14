package com.yumfee.extremeworld.entity.biz;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.yumfee.extremeworld.entity.IdEntity;

@Entity
@Table(name = "biz_goods")
public class Goods extends IdEntity{

	private String name;
	private String taobaoId;
	private String type;
	private double startPrice;
	private double endPrice;
	private int weight;
	private String cover;
	private String des;
	
	private Shop shop;
	
	private List<Category> categorys;
	
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
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
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
	@ManyToOne
	@JoinColumn(name = "shop_id")
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "biz_goods_category",
	joinColumns = { @JoinColumn(name = "goods_id", referencedColumnName = "id" ) },
	inverseJoinColumns = { @JoinColumn(name="category_id", referencedColumnName = "id") })
	public List<Category> getCategorys() {
		return categorys;
	}
	public void setCategorys(List<Category> categorys) {
		this.categorys = categorys;
	}
}
