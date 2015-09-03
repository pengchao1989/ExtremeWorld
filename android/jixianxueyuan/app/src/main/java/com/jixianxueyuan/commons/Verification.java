package com.jixianxueyuan.commons;

import com.jixianxueyuan.util.StringUtils;

/**
 * Created by pengchao on 9/3/15.
 */
public class Verification {
    public static boolean checkNickName(String nickName,String error /*out*/){

        if(StringUtils.isBlank(nickName)){
            error = "Nickname is not empty";
            return false;
        }
        if(StringUtils.isContainCharacters(nickName, " \\ / : * ? \" < > |#@")){
            error = "NickName don't has" + " \\ / : * ? \" < > |#@";
            return false;
        }

        return true;
    }
}
