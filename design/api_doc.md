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

