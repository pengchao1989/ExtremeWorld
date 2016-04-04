API Document
=======

## 简要说明 ##
 - 使用百川提供的推送服务
 - 百川推送分为两种类型：1.通知，2消息。业务上全部使用消息服务
 - 推送的内容为json串，结构为{type:N,content:"json"}  
 - N为前后台约定的业务类型，content为对象的json字符串
 - 为了实现推送到指定账号，前后台已经约定好，以user信息中的id作bindAccount的参数！
 
 
 **推送提醒**
 
 - type=1
 - content的值参见api_doc中的remind接口，就是一个remind对象的json串
 - 后台测试例子{type:1, content:{ "type": 1,"sourceId": 0,"content": "我好像全程在问，正脸呢","targetContent": "滑板女神 高孝周","targetType": 1, "targetId": 27622,"createTime": "2016-04-04 19:48:05", "speaker": {"id": 334,"name": "武陵","avatar": null,"gender": "male"}, "listener": {"id": 1,"name": "扳手扳死你","avatar": "http://img.jixianxueyuan.com/xxxx.jpg","gender": "male"}}}
 
   
  **推送主题（待开发）**
 
  **推送商品（待开发）**
  
