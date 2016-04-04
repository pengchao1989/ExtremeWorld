API Document
=======

## 简要说明 ##
 - 使用百川提供的推送服务
 - 百川推送分为两种类型：1.通知，2消息。业务上全部使用消息服务
 - 推送的内容为json串，结构为{type:N,content:"json"}  
 - N为前后台约定的业务类型，content为对象的json字符串
 
 
 **推送提醒**
 
 - type=1
 - content的值参见api_doc中的remind接口，就是一个remind对象的json串
 
   
  **推送主题（待开发）**
 
  **推送商品（待开发）**
  
