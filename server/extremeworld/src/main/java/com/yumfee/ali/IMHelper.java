package com.yumfee.ali;

import java.util.ArrayList;
import java.util.List;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Userinfos;
import com.taobao.api.request.OpenimUsersAddRequest;
import com.taobao.api.request.OpenimUsersDeleteRequest;
import com.taobao.api.request.OpenimUsersUpdateRequest;
import com.taobao.api.response.OpenimUsersAddResponse;
import com.taobao.api.response.OpenimUsersDeleteResponse;
import com.taobao.api.response.OpenimUsersUpdateResponse;
import com.yumfee.extremeworld.entity.User;

public class IMHelper {

	public static void registerIMAccount(User user){
		
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23213193", "3820b4a3eb9e0031b3a79f3e16b208ca");
		OpenimUsersAddRequest req = new OpenimUsersAddRequest();
		List<Userinfos> taobaoUserInfos = new ArrayList<Userinfos>();
		Userinfos userinfos = new Userinfos();
		userinfos.setUserid(user.getLoginName());
		userinfos.setPassword(user.getLoginName());
		userinfos.setNick(user.getName());
		userinfos.setIconUrl(user.getAvatar());
		taobaoUserInfos.add(userinfos);
		req.setUserinfos(taobaoUserInfos);
		try {
			OpenimUsersAddResponse rsp = client.execute(req);
			System.out.println(rsp.getBody());
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void updateIMAccount(User user){
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23213193", "3820b4a3eb9e0031b3a79f3e16b208ca");
		OpenimUsersUpdateRequest req = new OpenimUsersUpdateRequest();
		List<Userinfos> taobaoUserInfos = new ArrayList<Userinfos>();
		Userinfos userinfos = new Userinfos();
		userinfos.setUserid(user.getLoginName());
		userinfos.setPassword(user.getLoginName());
		userinfos.setNick(user.getName());
		userinfos.setIconUrl(user.getAvatar());
		taobaoUserInfos.add(userinfos);
		req.setUserinfos(taobaoUserInfos);
		try {
			OpenimUsersUpdateResponse rsp = client.execute(req);
			System.out.println(rsp.getBody());
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void deleteIMAccount(User user){
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23213193", "3820b4a3eb9e0031b3a79f3e16b208ca");
		OpenimUsersDeleteRequest req = new OpenimUsersDeleteRequest();
		req.setUserids(user.getLoginName());
		OpenimUsersDeleteResponse rsp;
		try {
			rsp = client.execute(req);
			System.out.println(rsp.getBody());
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
