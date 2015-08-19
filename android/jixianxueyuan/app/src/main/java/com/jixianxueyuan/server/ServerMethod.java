package com.jixianxueyuan.server;


/**
 * Created by pengchao on 2015/4/12.
 */
public class ServerMethod {

    static public final int STATUS_OK = 1;
    static public final int STATUS_NO_CONTENT = 204;



    //static final String server_url = "http://jixianxueyuan.com/";
    static final String server_url = "http://192.168.1.5:8023/";
    static final String api_version = "api/v1/";
    static String hobby = "skateboard/";

    public static void setHobby(String h){
        hobby = h + "/";
    }

    static final String method_url = server_url + api_version;

    public static final String videoUploadToken = server_url + api_version + "/uptoken/video";
    public static final String imgUploadToken = server_url + api_version + "/uptoken/picture";

    public static final String handshake(){return method_url + "handshake";};
    public static final String account_login() { return method_url + hobby + "account/qqlogin";};
    public static final String account_register () {return method_url + hobby + "account/register";};
    public static final String user(){return method_url + "user/";};
    public static final String topic(){return method_url + hobby + "topic";};
    public static final String topic_user(){return method_url + hobby + "topic/user/";};
    public static final String video(){return method_url + hobby + "video";};
    public static final String courseTaxonomy() {return method_url + hobby  + "course_taxonomy";};
    public static final String course(){return method_url + hobby + "course/";};
    public static final String course_explain () {return method_url + hobby + "course/explain/";};
    public static final String damaku(){return method_url + "danmubi";};
    public static final String reply(){return  method_url + "reply";};
    public static final String zan() {return method_url  + "topic_agree";}
    public static final String near_friend (){return method_url + hobby + "geo/near_friend";};
    public static final String remind() {return method_url + "remind/";};






}
