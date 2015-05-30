package com.jixianxueyuan.server;

import com.squareup.okhttp.MediaType;

/**
 * Created by pengchao on 2015/4/12.
 */
public class ServerMethod {

    public static final int status_ok = 1;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    //static final String server_url = "http://jixianxueyuan.com:8080/extremeworld/";
    static final String server_url = "http://192.168.1.6:8023/extremeworld/";
    static final String api_version = "api/v1/";
    static final String hobby = "all";

    static final String method_url = server_url + api_version;


    public static final String topic = method_url + "all/" + "topic";
    public static final String video = method_url + "all/" + "video";
    public static final String courseTaxonomy = method_url + "course_taxonomy";
    public static final String damaku = method_url + "danmubi";
    public static final String reply = method_url + "reply";


}
