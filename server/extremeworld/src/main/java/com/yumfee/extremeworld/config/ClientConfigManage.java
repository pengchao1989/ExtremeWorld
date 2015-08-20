package com.yumfee.extremeworld.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.ClientConfig;
import com.yumfee.extremeworld.repository.ClientConfigDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class ClientConfigManage {
	
	
	private ClientConfigDao clientConfigDao;
	
	List<ClientConfig> clientConfigList = null;
	
	
	public ClientConfig getCilentConfig(String hobbyName){
		
		clientConfigList = (List<ClientConfig>) clientConfigDao.findAll();
		
		for(ClientConfig clientConfig: clientConfigList){
			if(clientConfig.getHobby().geteName().equals(hobbyName)){
				return clientConfig;
			}
		}
		return null;
	}

	public List<ClientConfig> getClientConfigList() {
		return clientConfigList;
	}

	@Autowired
	public void setClientConfigDao(ClientConfigDao clientConfigDao) {
		this.clientConfigDao = clientConfigDao;
	}

}
