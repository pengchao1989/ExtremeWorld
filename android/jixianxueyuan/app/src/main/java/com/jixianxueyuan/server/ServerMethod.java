package com.jixianxueyuan.server;

/**
 * Created by pengchao on 2015/4/12.
 */
public class ServerMethod {
    static final String server_url = "http://121.42.31.88:80/extremeworld/";
    //static final String server_url = "http://192.168.1.4:8023/extremeworld/";
    static final String api_version = "api/v1/";


    public static final String topic = server_url + api_version + "topic";
    public static final String video = server_url + api_version + "video";
    public static final String courseTaxonomy = server_url + api_version + "course_taxonomy";


}
