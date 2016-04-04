API Document
=======

## 简要说明 ##

 - 服务器地址http://dev.jxianxueyuan.com
 - api构成url + "api" + "secure" + "#version" + "#hobby" + “method”
 - 标准RESTful方式
	 返回值如
	 {
		  "status" : 1,
		  "encryp" : true,
		  "content" : ""
		  "errorInfo" : null
	  }
	  content 类型分为普通bean和page类型，以适应典型的普通对象数据及分页数据，bean的定义可参考server、android中dto文件下的class，或者直接参考接口中的json数据
	  普通bean示例http://dev.jxianxueyuan.com/api/v1/skateboard/topic/146
	  分页示例http://dev.jxianxueyuan.com/api/v1/skateboard/topic
 - 接口分为加密和未加密
	未加密：http://dev.jxianxueyuan.com/api/v1/skateboard/topic/146 
	加密：http://dev.jxianxueyuan.com/api/secure/v1/skateboard/topic/146
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
	请求方式：GET、POST
	入参：    Long userId; String hobbyStamp;String device;
	说明：握手接口GET和POST都能获取到同样的结果。目前android调用的为POST接口。POST会有3个参数上传，分别是userId（没有的话传-1），hobbyStamp兴趣戳，这个戳从app包信息里取，device设备名称（iphone6s，samsung s6）


----------
	接口名称:account_qq_login
	URL地址:http://115.28.8.25/api/v1/hobby/account/qq_login
	请求方式:GET
	入参：qqOpenId
	示例：http://115.28.8.25/api/v1/bmx/account/qq_login?qqOpenId=3F888E5AF0F11CE9707E551E637E63CF


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
	URL地址：http://115.28.8.25/api/secure/v1/hobby/topic
	请求方式：GET
	入参：type、taxonomyId、page
	参考示例：http://115.28.8.25/api/secure/v1/skateboard/topic?page=1（请求该hobby下所有topic）
			http://115.28.8.25/api/secure/v1/skateboard/topic?type=discuss&taxonomyId=2&page=1
			http://115.28.8.25/api/secure/v1/skateboard/topic??type=course&magicType=sb&courseId=1859&sortType=agree (请求教学id为1859下的动态列表，magicType相当于是个tag的意思，这里是取采集的nikesb中的视频)
	说明：该接口为获取主题列表。支持客户端定义pageSize，参数值为page.size,默认pageSize为15;
	type的取值为预定义的值、taxonomyId通过握手接口获得。若不填这两个参数则获取该hobby下所有数据


----------
	接口名称：topic
	URL地址：http://115.28.8.25/api/secure/v1/hobby/topic/{id}
	请求方式：GET
	入参：
	参考示例：http://115.28.8.25/api/secure/v1/hobby/topic/151
	补充：该接口获取主题详情。


----------
	接口名称：topic
	URL地址：http://115.28.8.25/api/secure/v1/{hobby}/topic
	请求方式：POST
	{"user":{"id":15},"content":"测试","hobbys":[{"id":3}],"type":"discuss","title":"测sigh","taxonomy":{"id":11}}
	说明:发布主题接口。必传参数user、title、type、taxonomy、hobbys，		user、hobby等的定义可参见上面提到的DTO对象

> 简单情况：{"user":{"id":15},"content":"测试","hobbys":[{"id":3}],"type":"discuss","title":"测sigh","taxonomy":{"id":11}}
> 
> 复杂情况：json
> content={"videoDetail":{"thumbnail":"http://video.jixianxueyuan.com/d19efad8-cbbf-4f95-a772-e44ff8bae64a","videoSource":"http://video.jixianxueyuan.com/video_2015-11-29-Frt0nRxr"},"user":{"id":15},"content":"帮伟哥提数据","hobbys":[{"id":3}],"type":"s_video","mediaWrap":{"medias":[{"path":"http://img.jixianxueyuan.com/img_2015-11-29-2VWlqTQ6","type":"img"},{"path":"http://img.jixianxueyuan.com/img_2015-11-29-sGjpoV3X","type":"img"}]},"title":"帮伟哥提数据","status":0,"replyCount":0,"imageCount":0,"allReplyCount":0,"agreeCount":0,"viewCount":0}

	


----------
	接口名称：topic_user
	URL地址：http://115.28.8.25/api/secure/v1/hobby/topic/user/{id}
	请求方式：GET
	入参: page
	示例：http://115.28.8.25/api/secure/v1/hobby/topic/user/1?page=1
	说明：获取某个user的topic列表

----------
	接口名称：reply
	URL地址：http://115.28.8.25/api/secure/v1/reply
	请求方式：GET
	入参：topicId、page
	说明：获取某个主题下的回复。


----------
	接口名称：reply
	URL地址：http://115.28.8.25/api/secure/v1/reply
	请求方式：POST
	入参：
	{"content":"测试回复","topic":{"id":153},"user":{"id":15}}
	说明：该接口为提交回复。


----------
	接口名称：sub_reply
	URL地址：http://115.28.8.25/api/secure/v1/sub_reply
	请求方式：GET
	入参：topicId、page
	说明：目前android没有直接调用该接口，因为reply接口中已经把子回复数据带下来了


----------
	接口名称：sub_reply
	URL地址：http://115.28.8.25/api/secure/v1/sub_reply
	请求方式：POST
	入参：{"content":"测试子回复","reply":{"id":153},"user":{"id":15}}
	说明：该接口为提交子回复。
	

----------
	接口名称：topic_agree
	URL地址：http://115.28.8.25/api/secure/v1/topic_agree
	请求方式：POST
	入参：{"topicId":153,"userId":15}
	说明：对一个主题点赞
	

----------
	接口名称：user
	URL地址：http://115.28.8.25/api/secure/v1/user/{id}
	请求方式：GET
	示例：http://115.28.8.25/api/secure/v1/user/15
	说明：获取某个user的信息


----------
	接口名称：remind
	URL地址：http://115.28.8.25/api/secure/v1/remind/{userId}
	请求方式：GET
	示例：http://115.28.8.25/api/secure/v1/remind/15?page=1
	入参：page
	说明：获取用户的提醒列表
	返回值中的speaker、listener分别是发言者和倾听者
	返回值中的content为提醒内容、targetContent为引用内容
	targetType的定义：
		public static final int TARGET_TYPE_TOPIC = 1;
		public static final int TARGET_TYPE_REPLY = 2;
		public static final int TARGET_TYPE_SUB_REPLY = 3;


----------
	接口名称：near_friend
	URL地址：http://115.28.8.25/api/v1/{hobby}/geo/near_friend
	请求方式：GET
	入参：userId、latitude、longitude、page
	示例：http://192.168.0.107:8023/api/v1/bmx/geo/near_friend?userId=15&latitude=31.271252&longitude=104.650953&page=1
	说明：附近的人


----------
	接口名称:course_taxonomy
	URL地址:http://115.28.8.25/api/secure/v1/{hobby}/course_taxonomy/
	请求方式：GET
	入参:无
	示例:http://115.28.8.25/api/secure/v1/skateboard/course_taxonomy/
	说明:获取某个hobby下的完整的教学目录(就是动作集合),建议本地缓存(虽然很小才50k,但是基本不会变化),增加下拉刷新.

----------
	接口名称:market
	URL地址:http://115.28.8.25/api/secure/v1/{hobby}/biz/market/
	请求方式：GET
	入参:无
	示例:http://115.28.8.25/api/secure/v1/skateboard/biz/market/
	说明:获取商城信息,目前里面只包含一个categoryList商品分类,随着功能扩展可能会逐渐包含其它信息.

----------
	接口名称:shop
	URL地址:http://115.28.8.25/api/secure/v1/hobby/biz/shop/
	请求方式:GET
	入参:page
	示例:http://115.28.8.25/api/secure/v1/skateboard/biz/shop/
	说明:获取某个hobby下的商店列表

----------
	接口名称:goods/shop
	URL地址:http://115.28.8.25/api/secure/v1/{hobby}/biz/goods/shop/{id}
	请求方式:GET
	入参:page
	示例:http://115.28.8.25/api/secure/v1/skateboard/biz/goods/shop/1
	说明:获取某个店铺下的商品列表,{id}为商店id

----------
	接口名称:goods/category
	URL地址:http://115.28.8.25/api/secure/v1/{hobby}/biz/goods/category/{id}
	请求方式:GET
	入参:page
	示例:http://115.28.8.25/api/secure/v1/skateboard/biz/goods/category/1
	说明:获取某个category(分类-如鞋子)下的商品列表,{id}为商店category id, 这个id从market接口中能够拿到


----------
	接口名称:sponsorship
	URL地址:http://115.28.8.25/api/secure/v1/{hobby}/sponsorship
	请求方式:GET
	入参:page
	示例:http://115.28.8.25/api/secure/v1/skateboard/sponsorship?page=1
	说明:获取某个hobby下的赞助列表

----------
	接口名称:sponsorship
	URL地址:http://115.28.8.25/api/secure/v1/{hobby}/sponsorship
	请求方式:POST
	入参:page
	示例:http://115.28.8.25/api/secure/v1/skateboard/sponsorship
	说明:创建一条赞助
	参数示例：{"sum":0.01,"message":"不用找了","user":{"id":1},"hobby":{"id":1}}
	
----------
	接口名称:course_taxonomy
	URL地址:http://115.28.8.25/api/secure/v1/{hobby}/course_taxonomy
	请求方式:GET
	入参:无
	示例:http://115.28.8.25/api/secure/v1/skateboard/course_taxonomy
	说明:获取一个hobby的教学列表，例如在skateboard中就是几百个动作，分了类的有结构

----------
	接口名称:course
	URL地址:http://115.28.8.25/api/secure/v1/{hobby}/course/{id}
	请求方式:GET
	入参:courseId
	示例:http://115.28.8.25/api/secure/v1/skateboard/course/1859
	说明:获取一个教学详情，比如name、描述、创建者等信息
	
	
----------
	接口名称:collection
	URL地址:http://115.28.8.25/api/secure/v1/collection
	请求方式:GET
	入参:page
	示例:http://115.28.8.25/api/secure/v1/collection?page=1
	说明:获取一个用户的收藏列表
	
----------
	接口名称:collection
	URL地址:http://115.28.8.25/api/secure/v1/collection/{topicId}
	请求方式:POST
	入参:topicId
	示例:http://115.28.8.25/api/secure/v1/collection/1824
	说明:添加一个收藏
	
----------
	接口名称:collection
	URL地址:http://115.28.8.25/api/secure/v1/collection/{topicId}
	请求方式:DELETE
	入参:topicId
	示例:http://115.28.8.25/api/secure/v1/collection/1824
	说明:删除一个收藏
	
	
----------
	接口名称:profile/update_attribute
	URL地址:http://115.28.8.25/api/secure/v1/profile/update_attribute
	请求方式:POST
	入参:attributeName、attributeValue
	    attributeName取值：gender、signature、bg、avatar
	示例:http://115.28.8.25/api/secure/v1/profile/update_attribute
	参数:更新性别  {"attributeName":"gender","attributeValue":"male"}
	    更新头像  {"attributeName":"avatar","attributeValue":"http://img.abc.com/123.jpg"}
	
	说明:更新用户个人信息的某个属性
	
	
----------
	接口名称:geo/publish_location
	URL地址:http://115.28.8.25/api/secure/v1/{hobby}/geo/publish_location
	请求方式:POST
	入参:{latitude:104.123, longitude:32.123, address:"四川省成都市高新区软件园"}
	示例:http://115.28.8.25/api/secure/v1/skateboard/geo/publish_location
	说明:更新地理位置，android目前每次启动应用进入首页后静默上报一次
	