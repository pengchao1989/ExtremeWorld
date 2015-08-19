package com.jixianxueyuan.app;

import com.jixianxueyuan.dto.HandshakeDTO;

import java.io.Serializable;

/**
 * Created by pengchao on 8/7/15.
 */
public class AppInfomation implements Serializable {

    static AppInfomation appInfomation = null;


    String currentHobby;
    HandshakeDTO handshakeDTO;

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

    public HandshakeDTO getHandshakeDTO() {
        return handshakeDTO;
    }

    public void setHandshakeDTO(HandshakeDTO handshakeDTO) {
        this.handshakeDTO = handshakeDTO;
    }
}
