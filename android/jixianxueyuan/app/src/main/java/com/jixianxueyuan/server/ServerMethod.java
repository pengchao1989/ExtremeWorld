package com.jixianxueyuan.server;


/**
 * Created by pengchao on 2015/4/12.
 */
public class ServerMethod {

    static public final int STATUS_OK = 1;
    static public final int STATUS_NO_CONTENT = 204;



    //static final String server_url = "http://jixianxueyuan.com/";
    static final String server_url = "http://192.168.1.2:8023/";
    static final String api_version_secure = "api/secure/v1/";
    static final String api_version_anon = "api/v1/";
    static String hobby = "skateboard/";

    public static void setHobby(String h){
        hobby = h + "/";
    }

    static final String method_url_secure = server_url + api_version_secure;
    static final String method_url_none = server_url + api_version_anon;

    public static final String videoUploadToken = server_url + api_version_anon + "uptoken/video";
    public static final String imgUploadToken = server_url + api_version_anon + "uptoken/picture";

    public static final String handshake(){return method_url_none + "handshake";};
    public static final String account_qq_login() { return method_url_none + hobby + "account/qq_login";};
    public static final String verification_code(){return method_url_none + "verification_code";};
    public static final String verification_code_check(){return method_url_none + "verification_code/check";};
    public static final String invite(){return method_url_none + "invite";};
    public static final String account_qq_register() {return method_url_none + hobby + "account/qq_register";};
    public static final String account_phone_register() {return method_url_none + hobby + "account/phone_register";};
    public static final String reference_avatar(){return method_url_none + "reference_avatar";};

    public static final String profile_update() {return method_url_secure + "/profile/update";};
    public static final String user(){return method_url_secure + "user/";};
    public static final String topic(){return method_url_secure + hobby + "topic";};
    public static final String topic_user(){return method_url_secure + hobby + "topic/user/";};
    public static final String video(){return method_url_secure + hobby + "video";};
    public static final String courseTaxonomy() {return method_url_secure + hobby  + "course_taxonomy";};
    public static final String course(){return method_url_secure + hobby + "course/";};
    public static final String course_explain () {return method_url_secure + hobby + "course/explain/";};
    public static final String damaku(){return method_url_secure + "danmubi";};
    public static final String reply(){return  method_url_secure + "reply";};
    public static final String sub_reply(){return method_url_secure + "sub_reply";};
    public static final String zan() {return method_url_secure + "topic_agree";}
    public static final String near_friend (){return method_url_secure + hobby + "geo/near_friend";};
    public static final String remind() {return method_url_secure + "remind/";};


}
