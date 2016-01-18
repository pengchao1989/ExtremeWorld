package com.jixianxueyuan.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.trade.TradeConfigs;
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

        TradeConfigs.defaultTaokePid="mm_111250070_0_0";
        //AlibabaSDK.turnOnDebug();
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
        final CloudPushService cloudPushService = AlibabaSDK.getService(CloudPushService.class);
        if(cloudPushService != null) {



            cloudPushService.register(applicationContext,  new CommonCallback() {

                @Override
                public void onSuccess() {
                    Log.d("MyApplication", "init cloudchannel success");
                    if(getMine().getUserInfo() != null){
                        if(getMine().getUserInfo().getId() != null){
                            long userId = getMine().getUserInfo().getId();
                            cloudPushService.bindAccount(String.valueOf(userId));
                        }
                    }
                }

                @Override
                public void onFailed(String errorCode, String errorMessage) {
                    Log.d("MyApplication", "init cloudchannel fail" + "err:" + errorCode + " - message:"+ errorMessage);
                }
            });
        }else{
            Log.i("MyApplication", "CloudPushService is null");
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
