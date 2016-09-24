package com.jixianxueyuan.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.trade.TradeConfigs;
import com.alibaba.wxlib.util.SysUtil;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.duanqu.qupai.upload.AuthService;
import com.duanqu.qupai.upload.QupaiAuthListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jixianxueyuan.commons.Contant;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsDownloader;
import com.umeng.socialize.PlatformConfig;

public class MyApplication extends MultiDexApplication {

	private static MyApplication application;
    private static Context sContext;
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

        //init bugly
        initBugly();

        //设置app rest api的hobby值
        String currentHobbyStamp = Util.getApplicationMetaString(this, "HOBBY");
        appInfomation.setCurrentHobbyStamp(currentHobbyStamp);
        ServerMethod.setHobby(currentHobbyStamp);

        //初始化本地用户信息
        mine = Mine.getInstance();
        mine.SerializationFromLocal(this);

        //初始化imageLoader
        initImageLoader();

        Fresco.initialize(this);

        TradeConfigs.defaultTaokePid="mm_111250070_0_0";
        //AlibabaSDK.turnOnDebug();
        AlibabaSDK.asyncInit(this, new InitResultCallback() {

            @Override
            public void onSuccess() {
                //Toast.makeText(MyApplication.this, "初始化成功", Toast.LENGTH_SHORT).show();

                initCloudChannel(MyApplication.this);


            }

            @Override
            public void onFailure(int code, String message) {
                Toast.makeText(MyApplication.this, "初始化异常", Toast.LENGTH_SHORT)
                        .show();
            }

        });

        initIM();

        initUmenScoial();

        initX5();

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

    private void initIM(){

        SysUtil.setApplication(this);
        if(SysUtil.isTCMSServiceProcess(this)){
            return;
        }
        //第一个参数是Application Context
        //这里的APP_KEY即应用创建时申请的APP_KEY
        YWAPI.init(this, "23213193");
    }


    private void initQuPai(){
        initAuth(getApplicationContext(), Contant.appkey, Contant.appsecret, Contant.space);
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

    private void initAuth(Context context ,String appKey,String appsecret,String space){
        AuthService service = AuthService.getInstance();
        service.setQupaiAuthListener(new QupaiAuthListener() {
            @Override
            public void onAuthError(int errorCode, String message) {
                MyLog.e("Application", "ErrorCode" + errorCode + "message" + message);
            }

            @Override
            public void onAuthComplte(int responseCode, String responseMessage) {
                Contant.accessToken = responseMessage;
            }
        });
        service.startAuth(context, appKey, appsecret, space);
    }

    private void initBugly(){
        CrashReport.initCrashReport(this, "900018639", false);
    }

    private void initUmenScoial(){
        String qqAppId = String.valueOf(Util.getApplicationMetaInteger(this, "QQ_APP_ID"));
        String qqAppKey = Util.getApplicationMetaString(this, "QQ_APP_KEY");
        PlatformConfig.setQQZone(qqAppId, qqAppKey);
        PlatformConfig.setSinaWeibo("492074446", "8109db8a83c52a6df30609a29f2fae21");
        PlatformConfig.setWeixin("wxdb326de61ab1adb6", "46abb9f519afca7dab08b9c155869f19");
    }

    private void initX5() {
        TbsDownloader.needDownload(getApplicationContext(), false);
        QbSdk.initX5Environment(this, QbSdk.WebviewInitType.FIRSTUSE_AND_PRELOAD, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {

            }
        });
    }
}
