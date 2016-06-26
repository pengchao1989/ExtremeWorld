package com.jixianxueyuan.server;


/**
 * Created by pengchao on 2015/4/12.
 */
public class ServerMethod {

    static public final int STATUS_OK = 1;
    static public final int STATUS_ERROR = -1;
    static public final int STATUS_NO_CONTENT = 204;

    //static final String server_url = "http://dev.jixianxueyuan.com/";
    static final String server_url = "http://www.jixianxueyuan.com/";
    //static final String server_url = "http://192.168.1.7:8023/";
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
    public static final String imageModifyToken = server_url + api_version_anon + "uptoken/picture_modify";

    public static final String handshake(){return method_url_none + "handshake";};
    public static final String account_qq_login() { return method_url_none + hobby + "account/qq_login";};
    public static final String verification_code(){return method_url_none + "verification_code";};
    public static final String verification_code_check(){return method_url_none + "verification_code/check";};
    public static final String invite(){return method_url_none + "invite";};
    public static final String get_inviter(){return method_url_none + "invite/inviter";}
    public static final String account_qq_register() {return method_url_none + hobby + "account/qq_register";};
    public static final String account_phone_register() {return method_url_none + hobby + "account/phone_register";};
    public static final String reference_avatar(){return method_url_none + "referensitece_avatar";};
    public static final String check_version(){return method_url_none + hobby + "check_version";}

    public static final String profile_update() {return method_url_secure + "/profile/update";};
    public static final String profile_update_attribute(){return method_url_secure + "/profile/update_attribute";};
    public static final String user(){return method_url_secure + "user/";};
    public static final String topic(){return method_url_secure + hobby + "topic";};
    public static final String topic_fine(){return method_url_secure + hobby + "topic/fine";};
    public static final String topic_user(){return method_url_secure + hobby + "topic/user/";};
    public static final String topic_extra(){return method_url_secure + hobby + "topic/extra/";};
    public static final String video(){return method_url_secure + hobby + "video";};
    public static final String courseTaxonomy() {return method_url_secure + hobby  + "course_taxonomy";};
    public static final String course(){return method_url_secure + hobby + "course/";};
    public static final String course_explain () {return method_url_secure + hobby + "course/explain/";};
    public static final String site(){return method_url_secure + hobby + "site";};
    public static final String damaku(){return method_url_secure + "danmubi";};
    public static final String reply(){return  method_url_secure + "reply";};
    public static final String reply_hot(){return method_url_secure + "reply/hot";};
    public static final String sub_reply(){return method_url_secure + "sub_reply";};
    public static final String zan() {return method_url_secure + "topic_agree";}
    public static final String near_friend (){return method_url_secure + hobby + "geo/near_friend";};
    public static final String publish_location() {return method_url_secure + hobby + "geo/publish_location";}
    public static final String remind() {return method_url_secure + "remind/";};
    public static final String collection() {return method_url_secure + "collection";};
    public static final String biz_market(){return method_url_secure + hobby + "biz/market";};
    public static final String biz_shop(){return method_url_secure + hobby + "biz/shop";};
    public static final String goods(){return  method_url_secure + hobby + "biz/goods";};
    public static final String goods_of_shop(){return method_url_secure + hobby + "biz/goods/shop/";};
    public static final String goods_of_category (){return method_url_secure + hobby + "biz/goods/category/";}
    public static final String sponsorship(){return method_url_secure + hobby + "sponsorship";}
    public static final String exhibition() {return method_url_secure + hobby + "exhibition";}
    public static final String topic_score() {return method_url_secure + hobby + "topic_score";}
}
