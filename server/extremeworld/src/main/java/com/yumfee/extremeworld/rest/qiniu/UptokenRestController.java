package com.yumfee.extremeworld.rest.qiniu;

import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.rs.PutPolicy;

@RestController
@RequestMapping(value = "/api/v1/uptoken")
public class UptokenRestController
{
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Token getUptoken() throws AuthException, JSONException
	{
		Config.ACCESS_KEY = "-iKeYoaf3toQqqFfpdvNX5VBXX9qTL7FDN6GwcQj";
		Config.SECRET_KEY = "LZkUYSfHyE3al25SQoI1AWF1HoI8NKnshhGtxLtW";
		
		Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
		
		String bucketName = "extreme";
	    PutPolicy putPolicy = new PutPolicy(bucketName);
	    String uptoken = putPolicy.token(mac);
	    Token token = new Token();
	    token.uptoken = uptoken;
		return token;
	}
	
	class Token
	{
		private String uptoken;

		public String getUptoken()
		{
			return uptoken;
		}

		public void setUptoken(String uptoken)
		{
			this.uptoken = uptoken;
		}
		
	}
}
