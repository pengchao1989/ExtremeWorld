package com.yumfee.extremeworld.rest.dto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.SecurityUtils;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.security.utils.Cryptos;

import com.yumfee.extremeworld.config.MyErrorCode;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

public class MyResponse {
	
	public static int STATUS_OK = 1;
	public static int STATUS_ERROR = -1;

	public int status;
	public boolean encryp = false;
	
	public Object content;
	public Error error;
	
	public static MyResponse ok(Object content)
	{
		MyResponse response = new MyResponse();
		response.setContent(content);
		response.setStatus(STATUS_OK);
		return response;
	}
	
	public static MyResponse ok(Object content,boolean isEncryp){
		isEncryp = false;
		MyResponse response = new MyResponse();
		if(isEncryp){
			response.encryp = true;
			
			JsonMapper jsonMapper = new JsonMapper();
			String jsonContent = jsonMapper.toJson(content);

/*			try {
				//gzip压缩
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				GZIPOutputStream gzip = new GZIPOutputStream(out);
				gzip.write(jsonContent.getBytes("ISO8859-1"));
				gzip.close();*/
				
				
				byte[] vi = Cryptos.generateIV();
				byte[] encryByte = Cryptos.aesEncrypt(jsonContent.getBytes(), response.getCurrentToken(), vi);
				String encrypContent = Base64.encodeBase64String(encryByte);
				String viStr = Base64.encodeBase64String(vi);
				response.setContent(viStr + ":" + encrypContent);
/*			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  */
			
		}else
		{
			response.setContent(content);
		}
		
		response.setStatus(STATUS_OK); 
		
		return response;
	}
	
	public static MyResponse err(MyErrorCode errorCode){
		MyResponse response = new MyResponse();
		response.setStatus(STATUS_ERROR);
		Error error = new Error();
		error.setErrorCode(errorCode.getErrorCode());
		error.setErrorInfo(errorCode.getErrorInfo());
		response.setError(error);
		return response;
	}
	
	public static MyResponse create()
	{
		MyResponse response = new MyResponse();
		response.setStatus(201);
		return response;
	}
	
	public static MyResponse noContent()
	{
		MyResponse response = new MyResponse();
		response.setStatus(204);//错误码204
		response.setContent(null);
		return response;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
	
	private byte[] getCurrentToken(){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.token;
	}
}
