package com.jixianxueyuan.app;

import com.google.gson.Gson;
import com.jixianxueyuan.dto.HandshakeDTO;
import com.jixianxueyuan.dto.HobbyDTO;
import com.jixianxueyuan.util.ACache;

import java.util.List;

/**
 * Created by pengchao on 8/7/15.
 */
public class AppInfomation{

    private static final String HANDSHAKEDTO = "handshakeDTO";

    static AppInfomation appInfomation = null;

    String currentHobbyStamp;
    HandshakeDTO handshakeDTO;

    public static synchronized AppInfomation getInstance(){
        if(appInfomation == null){
            appInfomation = new AppInfomation();
        }
        return appInfomation;
    }

    private AppInfomation(){
        serializationFromLocal();
    }

    public boolean isNeedUpdate(){
        if(null == handshakeDTO){
            return true;
        }
        return false;
    }

    public String getCurrentHobbyStamp() {
        return currentHobbyStamp;
    }

    public void setCurrentHobbyStamp(String currentHobbyStamp) {
        this.currentHobbyStamp = currentHobbyStamp;
    }

    public HandshakeDTO getHandshakeDTO() {
        return handshakeDTO;
    }

    public void setHandshakeDTO(HandshakeDTO handshakeDTO) {
        this.handshakeDTO = handshakeDTO;
        writeSerializationToLocal();
    }

    public HobbyDTO getCurrentHobbyInfo(){
        List<HobbyDTO> hobbyDTOList = handshakeDTO.getHobbyDTOList();
        for (HobbyDTO hobbyDTO : hobbyDTOList){
            if(hobbyDTO.geteName().equals(currentHobbyStamp)){
                return hobbyDTO;
            }
        }

        return null;
    }

    private void serializationFromLocal(){
        ACache mCache = ACache.get(MyApplication.getContext());
        handshakeDTO = (HandshakeDTO) mCache.getAsObject(HANDSHAKEDTO);
    }

    private void writeSerializationToLocal(){
        ACache mCache = ACache.get(MyApplication.getContext());
        mCache.put(HANDSHAKEDTO, handshakeDTO,1 * ACache.TIME_HOUR);
    }
}
