package com.yumfee.extremeworld.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.CloudpushNoticeAndroidRequest;
import com.taobao.api.response.CloudpushNoticeAndroidResponse;
import com.yumfee.extremeworld.config.ClientConfigManage;
import com.yumfee.extremeworld.entity.ClientConfig;
import com.yumfee.extremeworld.entity.User;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class PushManage {
	
	@Autowired
	private ClientConfigManage clientConfigManage;
	
	public void pushNotice(User listenerUser,String title, String content){
		ClientConfig clientConfig = clientConfigManage.getCilentConfig(listenerUser.getHobbyStamp());
		String appkey = clientConfig.getBaichuanAppKey();
		String secret = clientConfig.getBaichuanAppSecret();
		String url = "http://gw.api.taobao.com/router/rest";

		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        CloudpushNoticeAndroidRequest req=new CloudpushNoticeAndroidRequest();
        req.setTitle(title);
        req.setSummary(content);
        req.setTarget("account");
        req.setTargetValue(String.valueOf(listenerUser.getId()));
        try {
            CloudpushNoticeAndroidResponse response = client.execute(req);
            if(response.isSuccess()){
                System.out.println("push  notice is success!");
            }
        }
        catch (Exception e){
            System.out.println("push notice is error!");
        }
	}
	
	public void pushMessage(){
		
	}
}
