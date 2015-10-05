API Document
=======

## 简要说明 ##

 - 服务器地址http://www.jxianxueyuan.com
 - api构成url + "api" + "secure" + "version" + “method”
 - 标准RESTful方式
	 返回值如
	 {
		  "status" : 1,
		  "encryp" : true,
		  "content" : ""
		  "errorInfo" : null
	  }
	  content 类型分为普通bean和page类型，以适应典型的普通对象数据及分页数据，bean的定义可参考server、android中dto文件下的class，或者直接参考接口中的json数据
	  普通bean示例http://www.jxianxueyuan.com/api/v1/bmx/topic/146
	  分页示例http://www.jxianxueyuan.com/api/v1/bmx/topic
 - 接口分为加密和未加密
	未加密：http://www.jxianxueyuan.com/api/v1/bmx/topic/146 
	加密：http://www.jxianxueyuan.com/api/secure/v1/bmx/topic/146
	加密方式：AES/128/CBC/PKCS5Padding,加密内容为json中content部分



----------


## 基础类型 ##
**hobby类型**

 - 滑板  skateboard  1
 - 跑酷  parkour 2
 - 小轮车 bmx 3
 - 轮滑  roller-skating 4

----------
**topic类型**

 - 心情 mood 2
 - 讨论 discuss 3
 - 视频 video 4
 - 短视频 s_video 5
 - 活动 activity 6
 - 新闻 news 7
 - 教学 course 8


----------
**API  列表**

 1. 非加密接口

| 名称        | url   |  备注  |
| --------   | -----  | :----  | 
| handshake |http://http://115.28.8.25/api/v1/handshake |握手，下发一些设置及配置信息|
| account_qq_register|http://http://115.28.8.25/api/v1/hobby/account/qq_register| qq注册|
| account_phone_register|http://115.28.8.25/api/v1/hobby/account/phone_register |手机注册|
|verification_code|http://115.28.8.25/api/v1/verification_code|获取验证码|
|verification_code_check|http://115.28.8.25/api/v1/verification_code/check|校验验证码|
|invite|http://115.28.8.25/api/v1/invite|获取推荐人|
|reference_avatar|http://115.28.8.25/api/v1/reference_avatar|随机头像|
|account_qq_login|http://115.28.8.25/api/v1/hobby/account/qq_login|qq登录|


 2. 加密接口
| 名称        | url   |  备注  |
| --------   | -----  | :----  | 
|profile_update|http://115.28.8.25/api/secure/v1/profile/update|更新profile|


----------
	七牛云使用注意事项
	首先简单看下七牛的编程模型http://developer.qiniu.com/docs/v6/api/overview/programming-model.html
	七牛每次上传一个文件需要一个uploadToken，这个token直接通过我们自己的rest接口获取
	获取图片的uploadToken，接口地址为：http://115.28.8.25/api/v1/uptoken/picture
	获取视频的uploadToken，接口地址为：http://115.28.8.25/api/v1/uptoken/video
	拿到这个token后结合七牛的上传sdk就能把文件上传到存储服务器了，上传的空间及一些策略信息都包含在这个token中，客户端会指定一下文件名
	文件名前缀定义：avatar_（头像）、video_（视频）；例如avatar_2015-09-04-TvJJjZWj
	我们的文件存储空间有两个，对应的域名分别为img.jixianxueyuan.com、video.jixianxueyuan.com

----------------------------------------------------------------------------------------------
上下行json对应的数据传输对象（DTO）参考地址
https://github.com/pengchao1989/ExtremeWorld/tree/master/server/extremeworld/src/main/java/com/yumfee/extremeworld/rest/dto

参数分为两种:
一种是httpParam,是直接跟在url后的参数,形如http://test.com?param=value,一般作为GET方式的参数
另一种是POST方式的raw数据格式(区别于form-data\x-www-form-urlencoded),一般作为POST方式的参数


----------
	Blockquote
	接口名称：handshake
	URL地址：http://http://115.28.8.25/api/v1/handshake
	请求方式：GET
	入参：无


----------
	接口名称:account_qq_login
	URL地址:http://115.28.8.25/api/v1/hobby/account/qq_login
	请求方式:GET
	入参：无


----------
	接口名称：account_qq_register
	URL地址：http://115.28.8.25/api/v1/hobby/account/qq_register	
	请求方式：POST
	入参：
	{
		name:"小叮当",
		birth:"1995"
		gender:"male"
		qqOpenId:"ididididiid",
		avatar:"http://img.jixianxueyuan.com/img_2015-09-23-vjRSdi7f"
	}
	补充：URL中的hobby替换为对应的hobby值，gender的值为male/female，birth只传年份的数字


----------
	接口名称：topic
	URL地址：http://115.28.8.25/api/v1/hobby/topic
	请求方式：GET
	入参：page
	参考示例：http://115.28.8.25/api/v1/hobby/topic?page=1
	说明：该接口为获取主题列表。支持客户端定义pageSize，参数值为page.size,默认pageSize为15


----------
	接口名称：topic
	URL地址：http://115.28.8.25/api/v1/hobby/topic
	请求方式：GET
	入参：无
	参考示例：http://115.28.8.25/api/v1/hobby/topic/151
	补充：该接口获取主题详情。


----------
	接口名称：topic
	URL地址：http://115.28.8.25/api/v1/hobby/topic
	请求方式：POST
	入参：
	{"user":{"id":15},"content":"测试","hobbys":[{"id":3}],"type":"discuss","title":"测sigh","taxonomy":{"id":11}}
	说明:必传参数user、title、type、taxonomy、hobbys，		user、hobby等的定义可参见上面提到的DTO对象
	

----------
	接口名称：reply
	URL地址：http://115.28.8.25/api/v1/reply
	请求方式：GET
	入参：topicId、page
	说明：获取某个主题下的回复。


----------
	接口名称：reply
	URL地址：http://115.28.8.25/api/v1/reply
	请求方式：POST
	入参：
	{"content":"测试回复","topic":{"id":153},"user":{"id":15}}
	说明：该接口为提交回复。


----------
接口名称：sub_reply
URL地址：http://115.28.8.25/api/v1/sub_reply
请求方式：GET
入参：topicId、page
说明：目前android没有直接调用该接口，因为reply接口中已经把子回复数据带下来了


----------
接口名称：sub_reply
URL地址：http://115.28.8.25/api/v1/sub_reply
请求方式：POST
入参：{"content":"测试子回复","reply":{"id":153},"user":{"id":15}}
说明：该接口为提交子回复。