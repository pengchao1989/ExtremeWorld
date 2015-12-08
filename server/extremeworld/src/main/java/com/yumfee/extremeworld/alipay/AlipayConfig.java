package com.yumfee.extremeworld.alipay;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088112995767113";
	
	// 收款支付宝账号
	public static String seller_email = "yumfee@gmail.com";
	// 商户的私钥
	public static String key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALYsFQecsJ5Yq3oo\n" +
            "QJ/97cCJCrgu75G5cT/R5tOV2cYM+RfXM12eBqKGvI5ngiH8CSOaXJolyAo794UE\n" +
            "INazIAvQzCXehCCKhEtA1OYYanGy112igW2rhMZbAiGLAtMtO/uHz79DivpQeiZV\n" +
            "ePIyIqAdYpj8TXS28vslAdTi7ZW3AgMBAAECgYBChJIhN2ueRg5HCo+eW+AGX4PU\n" +
            "gP7lHOtnPEOmmp7sHCyQMfoFgwA1NDGBZfY0Zo0HvQN3HbiPVBorLbi37KwURpKh\n" +
            "4L7cRYjo/jKbhChOkvzAmWze1h3FEPFb2gjmCYzeWF+s9NsixKCPpfyyZhqfmcKJ\n" +
            "tbrUt6HvH6JjpDKKEQJBAOCn0gmaEdj98iLESiqC9xdfKjQ+MtrqZeu4pXBa28XY\n" +
            "CCPRsiOO4+Tag8FeoEEml8Nbv4fxHIWOjcps8O9lakUCQQDPltqhqeF1aVnrKy+H\n" +
            "w0p5dR/Uj7H//PSip1bhzLO+6SPsmpxtixROBOBy+oAgTQpAdRYAaVOx41woAd3e\n" +
            "Xp3LAkEArum7HQK6Nqqx5ePSovrJC4dqwrZSviOWK0vec3YqewXSgKD6A7lzMnH3\n" +
            "94yHVXKI4vACVgOiY1I0j5D2nk3lyQJBAMeY/uLOWw/vmhu6TFs3dVZPPam9+KJw\n" +
            "cDAswiceEf4QYRBQoPPa9E0H91/WaHemW6MacWs32teCCswU72Wuh/0CQA/J9WXz\n" +
            "irvyOvgKwancYq22GsaqK94KTRWvUhnNoCUl5nT/s7s3g6w6QVg51NoPIaSd2E+F\n" +
            "Q8EqIB1f6jrmXZE=";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "MD5";

}
