package com.yumfee.extremeworld.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_app_key")
public class AppKey extends IdEntity{
	private String baichuanAppKey;
	private String baichuanAppSecret;
	private String duibaAppKey;
	private String duibaAppSecret;
	
	private Hobby hobby;
	
	public String getBaichuanAppKey() {
		return baichuanAppKey;
	}
	public void setBaichuanAppKey(String baichuanAppKey) {
		this.baichuanAppKey = baichuanAppKey;
	}
	public String getBaichuanAppSecret() {
		return baichuanAppSecret;
	}
	public void setBaichuanAppSecret(String baichuanAppSecret) {
		this.baichuanAppSecret = baichuanAppSecret;
	}
	
	
	public String getDuibaAppKey() {
		return duibaAppKey;
	}
	public void setDuibaAppKey(String duibaAppKey) {
		this.duibaAppKey = duibaAppKey;
	}
	public String getDuibaAppSecret() {
		return duibaAppSecret;
	}
	public void setDuibaAppSecret(String duibaAppSecret) {
		this.duibaAppSecret = duibaAppSecret;
	}
	@OneToOne
	@JoinColumn( name = "hobby_id" )
	public Hobby getHobby() {
		return hobby;
	}
	public void setHobby(Hobby hobby) {
		this.hobby = hobby;
	}
}
