package com.yumfee.extremeworld.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_client_config")
public class ClientConfig extends IdEntity{
	private String baichuanAppKey;
	private String baichuanAppSecret;
	
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
	
	@OneToOne
	@JoinColumn( name = "hobby_id" )
	public Hobby getHobby() {
		return hobby;
	}
	public void setHobby(Hobby hobby) {
		this.hobby = hobby;
	}
}
