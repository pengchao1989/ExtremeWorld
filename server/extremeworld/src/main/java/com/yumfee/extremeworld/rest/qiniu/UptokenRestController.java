package com.yumfee.extremeworld.rest.qiniu;

import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;


@RestController
@RequestMapping(value = "/api/v1/uptoken")
public class UptokenRestController
{
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Token getUptoken() throws JSONException
	{
		String ACCESS_KEY = "-iKeYoaf3toQqqFfpdvNX5VBXX9qTL7FDN6GwcQj";
		String SECRET_KEY = "LZkUYSfHyE3al25SQoI1AWF1HoI8NKnshhGtxLtW";
		
		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		
		String bucketName = "extreme";
	    //String token = auth.uploadToken(bucketName);
	    String token =  auth.uploadToken(bucketName, null, 3600, 
	    		new StringMap().
	    		put("persistentOps", "avthumb/flv/r/24/vcodec/libx264").
	    		put("persistentNotifyUrl", "http://fake.com/qiniu/notify").
	    		put("persistentPipeline", "myPipiLine"));
	    		
	    
		return new Token(token);
	}
	
	class Token
	{
		private String uptoken;
		
		public Token(String token)
		{
			this.uptoken = token;
		}

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
