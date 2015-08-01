package com.jixianxueyuan.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;


import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.jixianxueyuan.MainActivity;
import com.jixianxueyuan.dto.BaseInfoDTO;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.Util;

import java.io.File;

public class MyApplication extends Application {

	private static MyApplication application;


    BaseInfoDTO baseInfoDTO;
    Mine mine;
    String currentHobby;

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;

        AlibabaSDK.turnOnDebug();
        AlibabaSDK.asyncInit(this, new InitResultCallback() {

            @Override
            public void onSuccess() {
                Toast.makeText(MyApplication.this, "初始化成功", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onFailure(int code, String message) {
                Toast.makeText(MyApplication.this, "初始化异常", Toast.LENGTH_SHORT)
                        .show();
            }

        });

        //设置app rest api的hobby值
        currentHobby = Util.getApplicationMetaString(this, "HOBBY");
        ServerMethod.setHobby(currentHobby);

        //初始化本地用户信息
        mine = new Mine();
        mine.SerializationFromLocal(this);


	}


	public static MyApplication getContext() {
		return application;
	}

    public BaseInfoDTO getBaseInfoDTO() {
        return baseInfoDTO;
    }

    public void setBaseInfoDTO(BaseInfoDTO baseInfoDTO) {
        this.baseInfoDTO = baseInfoDTO;
    }

    public Mine getMine() {
        return mine;
    }

    public void setMine(Mine mine) {
        this.mine = mine;
    }

    public String getCurrentHobby() {
        return currentHobby;
    }
}
