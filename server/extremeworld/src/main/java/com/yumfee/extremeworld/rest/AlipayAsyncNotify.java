package com.yumfee.extremeworld.rest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yumfee.extremeworld.alipay.AlipayNotify;
import com.yumfee.extremeworld.entity.Trade;
import com.yumfee.extremeworld.service.AccessLogService;
import com.yumfee.extremeworld.service.TradeService;

@RestController
@RequestMapping(value = "/api/alipay/notify")
public class AlipayAsyncNotify {

	@Autowired
	TradeService tradeService;
	
	@Autowired
	AccessLogService accessLogService;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public String async(HttpServletRequest request){
		
		Map<String,String> params = new HashMap<String,String>();
	    Map requestParams = request.getParameterMap();
	    for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
	      String name = (String) iter.next();
	      String[] values = (String[]) requestParams.get(name);
	      String valueStr = "";
	      for (int i = 0; i < values.length; i++) {
	        valueStr = (i == values.length - 1) ? valueStr + values[i]: valueStr + values[i] + ",";
	      }
	      //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
	      //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
	      params.put(name, valueStr);
	    }
	    
	    accessLogService.add(request.getRequestURL().toString(), params.toString());
	    
	    String tradeNo = request.getParameter("out_trade_no");
	    String tradeStatus = request.getParameter("trade_status");
	    System.out.print("tradeNo=" + tradeNo);
	    System.out.print("tradeStatus" + tradeStatus);
	    //String notifyId = request.getParameter("notify_id");

	    if(/*AlipayNotify.verify(params)*/true){
	        if(tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS")) {
	        	Trade trade = tradeService.findByInternalTradeNo(tradeNo);
	        	if(trade == null){
	        		return "fail";
	        	}else{
		        	trade.setTradeStatus(tradeStatus);
		        	tradeService.save(trade);
	        	}
	        }
	        return "success";
	      }else{
	        return "fail";
	      }
	
	}
}
