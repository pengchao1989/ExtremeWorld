package com.jixianxueyuan.server;

/**
 * Created by pengchao on 2015/4/12.
 */
public class ServerMethod {
    static final String server_url = "http://jixianxueyuan.com:8080/extremeworld/";
    //static final String server_url = "http://192.168.1.4:8023/extremeworld/";
    static final String api_version = "api/v1/";

    static final String method_url = server_url + api_version;


    public static final String topic = method_url + "topic";
    public static final String video = method_url + "video";
    public static final String courseTaxonomy = method_url + "course_taxonomy";
    public static final String damaku = method_url + "danmubi";


}
