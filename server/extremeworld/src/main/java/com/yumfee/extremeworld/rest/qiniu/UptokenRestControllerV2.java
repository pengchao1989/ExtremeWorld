package com.yumfee.extremeworld.rest.qiniu;

import java.util.UUID;

import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.web.MediaTypes;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import com.yumfee.extremeworld.rest.dto.MyResponse;


@RestController
@RequestMapping(value = "/api/secure/v1/uptoken")
public class UptokenRestControllerV2
{
	String ACCESS_KEY = "-iKeYoaf3toQqqFfpdvNX5VBXX9qTL7FDN6GwcQj";
	String SECRET_KEY = "LZkUYSfHyE3al25SQoI1AWF1HoI8NKnshhGtxLtW";
	
	@RequestMapping(value = "picture", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse getPictureUptoken() throws JSONException
	{

		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		
		String bucketName = "extreme";


		String encodedEntryURI = UrlSafeBase64.encodeToString(bucketName + ":" + "123456789.jpg");
		
		System.out.println("getUptoken  encodedEntryURI = " + encodedEntryURI);

		
	    //String token = auth.uploadToken(bucketName);
		//视频切片vframe/jpg/offset/7/w/480/h/360
		//MP4->FLV avthumb/flv/r/24/vcodec/libx264
	    String tokenStr =  auth.uploadToken(bucketName, null);	
	    Token token = new Token(tokenStr);
	    
		return MyResponse.ok(token,true);
	}
	
	@RequestMapping(value = "picture_modify", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Token getPictureModifyUptoken(
			@RequestParam(value = "key", defaultValue = "key") String key) throws JSONException
	{

		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		
		String bucketName = "extreme";
		
	    String token =  auth.uploadToken(bucketName, key);	
	    System.out.print("picture_modify key=" + key);
	    
	    
		return new Token(token);
	}
	
	@RequestMapping(value = "video", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse getVideoUptoken() throws JSONException
	{

		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		
		String bucketName = "extreme-video";
		String frontImage =  UUID.randomUUID().toString();


		String encodedEntryURI = UrlSafeBase64.encodeToString(bucketName + ":" + frontImage);
		
		System.out.println("getUptoken  encodedEntryURI = " + encodedEntryURI);

		
	    //String token = auth.uploadToken(bucketName);
		//视频切片vframe/jpg/offset/7/w/480/h/360
		//MP4->FLV avthumb/flv/r/24/vcodec/libx264
	    String tokenString =  auth.uploadToken(bucketName, null, 3600, 
	    		new StringMap().
	    		put("persistentOps", "vframe/jpg/offset/2/w/480/h/360|saveas/"+encodedEntryURI). //|saveas/"+encodedEntryURI
	    		put("persistentNotifyUrl", "http://www.jixianxueyuan.com/api/v1/uptoken/upvideo").
	    		put("persistentPipeline", "myvideo"));
	    
	    Token token = new Token(tokenString);
	    token.setMyParam(frontImage);
	    
		return MyResponse.ok(token, true);
	}
	
	@RequestMapping(value = "upvideo", method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public ResponseEntity<?> front(UriComponentsBuilder uriBuilder) {

		return new ResponseEntity(HttpStatus.OK);
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
		private String myParam;
		
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

		public String getMyParam() {
			return myParam;
		}

		public void setMyParam(String myParam) {
			this.myParam = myParam;
		}
		
		
		
	}
}
