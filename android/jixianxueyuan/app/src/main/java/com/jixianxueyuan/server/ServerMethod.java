package com.jixianxueyuan.server;


/**
 * Created by pengchao on 2015/4/12.
 */
public class ServerMethod {

    static public final int STATUS_OK = 1;
    static public final int STATUS_NO_CONTENT = 204;

    public static void setHobby(String h){
        ServerMethod.hobby = h + "/";
    }

    //static final String server_url = "http://jixianxueyuan.com/";
    static final String server_url = "http://192.168.1.2:8023/";
    static final String api_version = "api/v1/";
    static String hobby = "skateboard/";

    static final String method_url = server_url + api_version;

    public static final String videoUploadToken = server_url + api_version + "/uptoken/video";
    public static final String imgUploadToken = server_url + api_version + "/uptoken/picture";

    public static final String baseInfo = method_url + "base_info";
    public static final String account_login = method_url + hobby + "account/qqlogin";
    public static final String account_register = method_url + hobby + "account/register";
    public static final String topic = method_url + hobby + "topic";
    public static final String video = method_url + "all/" + "video";
    public static final String courseTaxonomy = method_url + hobby  + "course_taxonomy";
    public static final String course = method_url + hobby + "course/";
    public static final String course_explain = method_url + hobby + "course/explain/";
    public static final String damaku = method_url + "danmubi";
    public static final String reply = method_url + "reply";
    public static final String zan = method_url  + "topic_agree";
    public static final String near_friend = method_url + hobby + "geo/near_friend";






}
