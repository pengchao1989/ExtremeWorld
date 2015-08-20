package com.jixianxueyuan.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.cchannel.CloudChannelConstants;
import com.alibaba.cchannel.plugin.CloudPushService;
import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.alibaba.cchannel.core.task.RunnableCallbackAdapter;
import com.jixianxueyuan.dto.HandshakeDTO;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MyApplication extends Application {

	private static MyApplication application;


    HandshakeDTO handshakeDTO;
    Mine mine;
    String currentHobby;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
	public void onCreate() {
		super.onCreate();
		application = this;

        //设置app rest api的hobby值
        currentHobby = Util.getApplicationMetaString(this, "HOBBY");
        ServerMethod.setHobby(currentHobby);

        //初始化本地用户信息
        mine = Mine.getInstance();
        mine.SerializationFromLocal(this);

        //初始化imageLoader
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MyApplication.this));

        AlibabaSDK.turnOnDebug();
        AlibabaSDK.asyncInit(this, new InitResultCallback() {

            @Override
            public void onSuccess() {
                Toast.makeText(MyApplication.this, "初始化成功", Toast.LENGTH_SHORT)
                        .show();

                initCloudChannel(MyApplication.this);
            }

            @Override
            public void onFailure(int code, String message) {
                Toast.makeText(MyApplication.this, "初始化异常", Toast.LENGTH_SHORT)
                        .show();
            }

        });
	}

    /**
     * 初始化云推送通道
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        CloudPushService cloudPushService = AlibabaSDK.getService(CloudPushService.class);
        if(cloudPushService != null) {
            if(getMine().getUserInfo() != null){
                long userId = getMine().getUserInfo().getId();
                cloudPushService.bindAccount(String.valueOf(userId));
            }

            cloudPushService.register(applicationContext,  new RunnableCallbackAdapter<Void>() {
                @Override
                public void onSuccess(Void result) {
                    Log.d(CloudChannelConstants.TAG, "init cloudchannel success");
                }


                @Override
                public void onFailed(Exception exception){
                    Log.d(CloudChannelConstants.TAG, "init cloudchannel failed");
                }
            });
        }else{
            Log.i(CloudChannelConstants.TAG, "CloudPushService is null");
        }

    }



    public static MyApplication getContext() {
		return application;
	}

    public HandshakeDTO getHandshakeDTO() {
        return handshakeDTO;
    }

    public void setHandshakeDTO(HandshakeDTO handshakeDTO) {
        this.handshakeDTO = handshakeDTO;
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
