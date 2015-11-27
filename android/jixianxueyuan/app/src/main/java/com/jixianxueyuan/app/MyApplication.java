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
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MyApplication extends Application {

	private static MyApplication application;
    private RequestQueue mRequestQueue;


    Mine mine;
    AppInfomation appInfomation;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
	public void onCreate() {
		super.onCreate();
		application = this;

        appInfomation = AppInfomation.getInstance();

        //设置app rest api的hobby值
        String currentHobbyStamp = Util.getApplicationMetaString(this, "HOBBY");
        appInfomation.setCurrentHobbyStamp(currentHobbyStamp);
        ServerMethod.setHobby(currentHobbyStamp);

        //初始化本地用户信息
        mine = Mine.getInstance();
        mine.SerializationFromLocal(this);

        //初始化imageLoader
        initImageLoader();

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
                if(getMine().getUserInfo().getId() != null){
                    long userId = getMine().getUserInfo().getId();
                    cloudPushService.bindAccount(String.valueOf(userId));
                }
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

    public Mine getMine() {
        return mine;
    }

    public void setMine(Mine mine) {
        this.mine = mine;
    }

    public AppInfomation getAppInfomation() {
        return appInfomation;
    }

    public void setAppInfomation(AppInfomation appInfomation) {
        this.appInfomation = appInfomation;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            //在这里调用Volley.newRequestQueue()
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    private void initImageLoader(){
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MyApplication.this));
    }
}
