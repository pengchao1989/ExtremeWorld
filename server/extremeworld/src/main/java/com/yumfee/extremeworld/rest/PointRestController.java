package com.yumfee.extremeworld.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.config.ClientConfigManage;
import com.yumfee.extremeworld.entity.AppKey;
import com.yumfee.extremeworld.entity.Point;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.rest.dto.MyPage;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.PointDTO;
import com.yumfee.extremeworld.service.PointService;
import com.yumfee.extremeworld.service.UserService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

import cn.com.duiba.credits.sdk.CreditConsumeParams;
import cn.com.duiba.credits.sdk.CreditConsumeResult;
import cn.com.duiba.credits.sdk.CreditNotifyParams;
import cn.com.duiba.credits.sdk.CreditTool;

@RestController
@RequestMapping(value = "/api/secure/v1/{hobby}/point")
public class PointRestController {
	
	private static final String PAGE_SIZE = "20";
	
	@Autowired
	PointService pointServer;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private ClientConfigManage clientConfigManage;
	
	@RequestMapping(value="duiba_auto_login", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse getAutoLoginUrl(@PathVariable String hobby,
			@RequestParam(value = "redirect", defaultValue = "") String redirect){
		
		AppKey clientConfig = clientConfigManage
				.getCilentConfig(hobby);
		
		CreditTool tool=new CreditTool(clientConfig.getDuibaAppKey(), clientConfig.getDuibaAppSecret());
		
		long userId = getCurrentUserId();
		User user = userService.getUser(userId);
		
		Map params=new HashMap();
		params.put("uid",String.valueOf(user.getId()));
		params.put("credits",String.valueOf(user.getPoint()));
		if(StringUtils.isEmpty(redirect)){
		    //redirect是目标页面地址，默认积分商城首页是：http://www.duiba.com.cn/chome/index
		    //此处请设置成一个外部传进来的参数，方便运营灵活配置
		    params.put("redirect",redirect);
		}
		String url=tool.buildUrlWithSign("http://www.duiba.com.cn/autoLogin/autologin?",params);
		return MyResponse.ok(url);
	}
	
	@RequestMapping(value="duiba_deduction_points", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public String deductionPoints(@PathVariable String hobby,
			HttpServletRequest request){
		
		AppKey clientConfig = clientConfigManage
				.getCilentConfig(hobby);
		
		CreditTool tool=new CreditTool(clientConfig.getDuibaAppKey(), clientConfig.getDuibaAppSecret());

		try {
		    CreditConsumeParams params= tool.parseCreditConsume(request);//利用tool来解析这个请求
		    String uid=params.getUid();//用户id
		    Long credits=params.getCredits();
		    String type=params.getType();//获取兑换类型
		    String alipay=params.getAlipay();//获取支付宝账号
		    //其他参数参见 params的属性字段

		    //TODO 开发者系统对uid用户扣除credits个积分
		    
		    User user = userService.getUser(Long.parseLong(uid));
		    user.setPoint(user.getPoint() - credits);

		    String bizId=UUID.randomUUID().toString();//返回开发者系统中的业务订单id
		    CreditConsumeResult result=new CreditConsumeResult(true);
		    result.setBizId(bizId);
		    result.setCredits(credits);
		    return result.toString();
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    return "";
		}
	}
	
	@RequestMapping(value="duiba_receive_result", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public String receiveDuiBaResult(@PathVariable String hobby,HttpServletRequest request){
		AppKey clientConfig = clientConfigManage
				.getCilentConfig(hobby);
		
		/*
		*  兑换订单的结果通知请求的解析方法
		*  当兑换订单成功时，兑吧会发送请求通知开发者，兑换订单的结果为成功或者失败，如果为失败，开发者需要将积分返还给用户
		*/
		CreditTool tool=new CreditTool(clientConfig.getDuibaAppKey(), clientConfig.getDuibaAppSecret());

		try {
		    CreditNotifyParams params= tool.parseCreditNotify(request);//利用tool来解析这个请求
		    String orderNum=params.getOrderNum();
		    if(params.isSuccess()){
		        //兑换成功
		    }else{
		        //兑换失败，根据orderNum，对用户的金币进行返还，回滚操作
		    }
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		
		return "ok";
	}
	
	@RequestMapping(value="points", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse getUserPoint(){
		long userId = getCurrentUserId();
		long points = pointServer.getUserTotalPoint(userId);
		return MyResponse.ok(points, true);
	}
	
	@RequestMapping(value="history", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse getHistory(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		
		long userId = getCurrentUserId();
		Page<Point> pointPage = pointServer.getAll(userId, pageNumber, pageSize, sortType);
		
		MyPage<PointDTO, Point> myPointPage = new MyPage<PointDTO, Point>(PointDTO.class, pointPage);
		
		return MyResponse.ok(myPointPage,true);
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}

}
