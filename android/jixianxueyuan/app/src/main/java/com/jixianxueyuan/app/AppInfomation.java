package com.jixianxueyuan.app;

import android.content.Context;

import com.jixianxueyuan.dto.BaseInfoDTO;

import java.io.Serializable;

/**
 * Created by pengchao on 8/7/15.
 */
public class AppInfomation implements Serializable {

    static AppInfomation appInfomation = null;


    String currentHobby;
    BaseInfoDTO baseInfoDTO;

    public static synchronized AppInfomation getInstance(){
        if(appInfomation == null){
            appInfomation = new AppInfomation();
        }
        return appInfomation;
    }

    private AppInfomation(){
    }

    public String getCurrentHobby() {
        return currentHobby;
    }

    public void setCurrentHobby(String currentHobby) {
        this.currentHobby = currentHobby;
    }

    public BaseInfoDTO getBaseInfoDTO() {
        return baseInfoDTO;
    }

    public void setBaseInfoDTO(BaseInfoDTO baseInfoDTO) {
        this.baseInfoDTO = baseInfoDTO;
    }
}
