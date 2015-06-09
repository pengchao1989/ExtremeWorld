package com.yumfee.extremeworld.rest.qiniu;

import org.json.JSONException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;


@RestController
@RequestMapping(value = "/api/v1/uptoken")
public class UptokenRestController
{
	String ACCESS_KEY = "-iKeYoaf3toQqqFfpdvNX5VBXX9qTL7FDN6GwcQj";
	String SECRET_KEY = "LZkUYSfHyE3al25SQoI1AWF1HoI8NKnshhGtxLtW";
	
	@RequestMapping(value = "picture", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Token getPictureUptoken() throws JSONException
	{

		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		
		String bucketName = "extreme";


		String encodedEntryURI = UrlSafeBase64.encodeToString(bucketName + ":" + "123456789.jpg");
		
		System.out.println("getUptoken  encodedEntryURI = " + encodedEntryURI);

		
	    //String token = auth.uploadToken(bucketName);
		//视频切片vframe/jpg/offset/7/w/480/h/360
		//MP4->FLV avthumb/flv/r/24/vcodec/libx264
	    String token =  auth.uploadToken(bucketName, null);	
	    
	    
		return new Token(token);
	}
	
	@RequestMapping(value = "upvideo", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Token getVideoUptoken() throws JSONException
	{

		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		
		String bucketName = "extreme";


		String encodedEntryURI = UrlSafeBase64.encodeToString(bucketName + ":" + "123456789.jpg");
		
		System.out.println("getUptoken  encodedEntryURI = " + encodedEntryURI);

		
	    //String token = auth.uploadToken(bucketName);
		//视频切片vframe/jpg/offset/7/w/480/h/360
		//MP4->FLV avthumb/flv/r/24/vcodec/libx264
	    String token =  auth.uploadToken(bucketName, null, 3600, 
	    		new StringMap().
	    		put("persistentOps", "vframe/jpg/offset/20/w/480/h/360|saveas/"+encodedEntryURI).
	    		put("persistentNotifyUrl", "http://fake.com/qiniu/notify").
	    		put("persistentPipeline", "myPipiLine"));	
	    
	    
		return new Token(token);
	}
	
	@RequestMapping(value = "video/{fileName}",method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Token getVideoUptoken(@PathVariable("fileName") String fileName)
	{
		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		
		String bucketName = "extreme";


		String encodedEntryURI = UrlSafeBase64.encodeToString(bucketName + ":" + fileName);
		
		System.out.println("getVideoUptoken encodedEntryURI = " + encodedEntryURI);

		
	    //String token = auth.uploadToken(bucketName);
		//视频切片vframe/jpg/offset/7/w/480/h/360
		//MP4->FLV avthumb/flv/r/24/vcodec/libx264
	    String token =  auth.uploadToken(bucketName, null, 3600, 
	    		new StringMap().
	    		put("persistentOps", "vframe/jpg/offset/2/w/480/h/360|saveas/"+encodedEntryURI).
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
