package com.yumfee.extremeworld.push;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.BeanMapper;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.CloudpushMessageAndroidRequest;
import com.taobao.api.request.CloudpushNoticeAndroidRequest;
import com.taobao.api.response.CloudpushMessageAndroidResponse;
import com.taobao.api.response.CloudpushNoticeAndroidResponse;
import com.yumfee.extremeworld.config.ClientConfigManage;
import com.yumfee.extremeworld.entity.AppKey;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.rest.dto.PointDTO;
import com.yumfee.extremeworld.rest.dto.RemindDTO;

//Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class PushManage {

	@Autowired
	private ClientConfigManage clientConfigManage;

	public void pushNotice(User listenerUser, String title, String content) {
		AppKey clientConfig = clientConfigManage
				.getCilentConfig(listenerUser.getHobbyStamp());
		String appkey = clientConfig.getBaichuanAppKey();
		String secret = clientConfig.getBaichuanAppSecret();
		String url = "http://gw.api.taobao.com/router/rest";

		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		CloudpushNoticeAndroidRequest req = new CloudpushNoticeAndroidRequest();
		req.setTitle(title);
		req.setSummary(content);
		req.setTarget("account");
		req.setTargetValue(String.valueOf(listenerUser.getId()));
		try {
			CloudpushNoticeAndroidResponse response = client.execute(req);
			if (response.isSuccess()) {
				System.out.println("push  notice is success!");
			}
		} catch (Exception e) {
			System.out.println("push notice is error!");
		}
	}

	public void pushMessage(User listenerUser, int type, Object content) {
		
		if(StringUtils.isNoneEmpty(listenerUser.getPlateForm()) && "android".equals(listenerUser.getPlateForm())){
			AppKey clientConfig = clientConfigManage
					.getCilentConfig(listenerUser.getHobbyStamp());
			String appkey = clientConfig.getBaichuanAppKey();
			String secret = clientConfig.getBaichuanAppSecret();
			String url = "http://gw.api.taobao.com/router/rest";

			TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);

			CloudpushMessageAndroidRequest req = new CloudpushMessageAndroidRequest();
			req.setTarget("account");
			req.setTargetValue(String.valueOf(listenerUser.getId()));
			
			
			String contentJson = buildContent(type,content);
			req.setBody(contentJson);

			try {
				CloudpushMessageAndroidResponse response = client.execute(req);
				System.out.println(response.getBody());
				if (response.isSuccess()) {
					System.out.println("push message is success!");
				}
			} catch (Exception e) {
				System.out.println("push message is error!");
			}
		}
	}

	private String buildContent(int type,Object content) {
		
		String pushMessageJson = "";
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			String contentJson = "";
			switch(type){
			case PushMessageType.REMIND:
				RemindDTO remindDTO = BeanMapper.map(content, RemindDTO.class);
				contentJson = mapper.writeValueAsString(remindDTO);
				break;
			case PushMessageType.POINT:
				PointDTO pointDTO = BeanMapper.map(content, PointDTO.class);
				contentJson = mapper.writeValueAsString(pointDTO);
				break;
			}
			PushMessage pussMessage = new PushMessage();
			pussMessage.setType(type);
			pussMessage.setContent(contentJson);
			
			pushMessageJson = mapper.writeValueAsString(pussMessage);
			
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pushMessageJson;
	}
}
