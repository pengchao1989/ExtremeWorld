package com.jixianxueyuan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.push.CloudPushService;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jixianxueyuan.activity.NewHomeActivity;
import com.jixianxueyuan.activity.RegisterActivity;
import com.jixianxueyuan.activity.RegisterEnterPhoneActivity;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.UpdateManager;
import com.jixianxueyuan.commons.im.IMHelper;
import com.jixianxueyuan.config.HobbyType;
import com.jixianxueyuan.config.MyErrorCode;
import com.jixianxueyuan.dto.*;
import com.jixianxueyuan.dto.qq.QQOpenInfo;
import com.jixianxueyuan.dto.qq.QQUserInfo;
import com.jixianxueyuan.dto.request.HandshakeRequestDTO;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.Util;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;


public class MainActivity extends Activity {

    @InjectView(R.id.main_video_view)
    VideoView videoView;
    @InjectView(R.id.activity_qq_login)
    LinearLayout qqLoginButton;

    @InjectView(R.id.activity_main_appname)
    ShimmerTextView appNameTextView;

    Tencent tencent;

    //String openId = null;
    private QQOpenInfo qqOpenInfo = null;


    private boolean isTestRegister = false;

    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        initView();

        final Shimmer shimmer = new Shimmer();
        shimmer.setRepeatCount(0);
        shimmer.start(appNameTextView);

/*        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);*/

        if(MyApplication.getContext().getAppInfomation().isNeedUpdate()){
            requestHandshake();
        }


        if(!isTestRegister){
            //若本地有登录信息，则直接进行登录
            Mine mine = MyApplication.getContext().getMine();
            if(mine.getUserInfo() != null && mine.getUserInfo().getId() != null){
                //直接进入Hone页
                if(mine.getUserInfo().getId() > 0){
                    Intent intent = new Intent(MainActivity.this, NewHomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            else{
                //等待用户点击登录按钮进行登录
            }
        }else {

            qqOpenInfo = new QQOpenInfo();
            qqOpenInfo.setOpenid(Util.getUUID());

            QQUserInfo qqUserInfo = new QQUserInfo();
            qqUserInfo.setNickname(Util.getUUID());

            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(RegisterActivity.REGISTER_TYPE, RegisterActivity.REGISTER_TYPE_QQ);
            bundle.putSerializable("qqOpenInfo", qqOpenInfo);
            bundle.putSerializable("qqUserInfo", qqUserInfo);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showVideo();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initView()
    {
        String hobby = Util.getApplicationMetaString(this, "HOBBY");
        String umengKey = Util.getApplicationMetaString(this, "UMENG_APPKEY");
        MyLog.e("MainActivity", "UMENT_KEY=" + umengKey);
/*        switch (hobby){
            case HobbyType.SKATEBOARD:
                appNameTextView.setText(this.getResources().getText(R.string.app_name_skateboard));
                break;
            case HobbyType.PARKOUR:
                appNameTextView.setText(this.getResources().getText(R.string.app_name_packour));
                break;
            case HobbyType.BMX:
                appNameTextView.setText(this.getResources().getText(R.string.app_name_bmx));
                break;
        }*/
        appNameTextView.setText(this.getResources().getText(R.string.app_name));
    }

    private void showVideo(){
/*        DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) videoView.getLayoutParams();
        params.width =  metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        videoView.setLayoutParams(params);*/
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });
        Uri url  = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cover);
        videoView.setVideoURI(url);
        videoView.start();
    }

    @OnClick(R.id.activity_qq_login)
    void qqLogin() {

        requestQQOpenId();
        //requestLogin();
    }

    private void requestHandshake() {
        //带上client-hobby信息给服务器，以告知最后是登录的那个应用程序
        String url = ServerMethod.handshake();
        HandshakeRequestDTO handshakeRequestDTO = new HandshakeRequestDTO();
        String hobbyStamp = Util.getApplicationMetaString(this, "HOBBY");
        long userId = -1;
        UserDTO userDTO = MyApplication.getContext().getMine().getUserInfo();
        if(userDTO != null && userDTO.getId() != null){
            userId = userDTO.getId();
        }
        handshakeRequestDTO.setHobbyStamp(hobbyStamp);
        handshakeRequestDTO.setDevice("android");
        handshakeRequestDTO.setUserId(userId);


        MyRequest<HandshakeDTO> myRequest = new MyRequest<HandshakeDTO>(Request.Method.POST, url, HandshakeDTO.class,handshakeRequestDTO,
                new Response.Listener<MyResponse<HandshakeDTO>>() {

                    @Override
                    public void onResponse(MyResponse<HandshakeDTO> response) {

                        MyApplication myApplication = (MyApplication) MyApplication.getContext();
                        myApplication.getAppInfomation().setHandshakeDTO(response.getContent());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        MyApplication.getContext().getRequestQueue().add(myRequest);
    }

    private void requestLogin() {

        String url = ServerMethod.account_qq_login() + "?qqOpenId=" + qqOpenInfo.getOpenid();

        MyRequest<UserDTO> myRequest = new MyRequest<UserDTO>(Request.Method.GET, url, UserDTO.class,
                new Response.Listener<MyResponse<UserDTO>>() {
                    @Override
                    public void onResponse(MyResponse<UserDTO> response) {

                        if (response.getStatus() == ServerMethod.STATUS_OK) {

                            //持久化用户信息
                            Mine mine = MyApplication.getContext().getMine();
                            mine.setUserInfo(response.getContent());
                            mine.WriteSerializationToLocal(MainActivity.this);

                            CloudPushService cloudPushService = AlibabaSDK.getService(CloudPushService.class);
                            cloudPushService.bindAccount(String.valueOf(mine.getUserInfo().getId()));

                            MobclickAgent.onProfileSignIn(String.valueOf(mine.getUserInfo().getId()));

                            requestHandshake();


                            //登录IM
                            loginIM();

                            //登录完成，进入HOME页
                            //Intent intent = new Intent(MainActivity.this, NewHomeActivity.class);
                            //startActivity(intent);
                            //finish();
                        } else if (response.getStatus() == ServerMethod.STATUS_ERROR) {
                            if(response.getError().getErrorCode() == MyErrorCode.NO_USER.getErrorCode()){
                                MyLog.d("MainActivity", "no user");
                                //请求QQ方userInfo
                                requestQQuserInfo();
                            }



                        }
                        //requestQQuserInfo();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        MyApplication.getContext().getRequestQueue().add(myRequest);
    }

    private void requestQQOpenId(){

        showProgress();

        tencent = Tencent.createInstance("101228787", this.getApplicationContext());
        if (!tencent.isSessionValid()) {
            tencent.login(this, "get_user_info,add_t", new IUiListener() {
                @Override
                public void onComplete(Object response) {

                    MyLog.d("MainActivity", "login info =" + response.toString());

                    Gson gson = new Gson();

                    qqOpenInfo = gson.fromJson(response.toString(), QQOpenInfo.class);
                    if (qqOpenInfo != null) {
                        //tencent.setOpenId(qqOpenInfo.getOpenid());
                        //tencent.setAccessToken(qqOpenInfo.getAccess_token(),"10");
                        //qq登录成功后进行自家用户登录或注册
                        requestLogin();
                    }

                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    private void requestQQuserInfo(){


        UserInfo userInfo = new UserInfo(this, tencent.getQQToken());
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object response) {

                MyLog.d("MainActivity", "qqUserInfo=" + response.toString());

                Gson gson = new Gson();
                QQUserInfo qqUserInfo = gson.fromJson(response.toString(), QQUserInfo.class);
                if (qqUserInfo != null) {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(RegisterActivity.REGISTER_TYPE, RegisterActivity.REGISTER_TYPE_QQ);
                    bundle.putSerializable("qqOpenInfo", qqOpenInfo);
                    bundle.putSerializable("qqUserInfo", qqUserInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        tencent.onActivityResult(requestCode, resultCode, data);
    }

    private void loginIM(){

        IMHelper imHelper = new IMHelper(this);
        imHelper.Login(new IMHelper.LoginResultListener() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(MainActivity.this, NewHomeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String err) {
                Toast.makeText(MainActivity.this, err, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static String getDeviceInfo(Context context) {
        try{
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if( TextUtils.isEmpty(device_id) ){
                device_id = mac;
            }

            if( TextUtils.isEmpty(device_id) ){
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void showProgress(){
        if(progressDialog == null){
            progressDialog = new SpotsDialog(this,R.style.ProgressDialogUpdating);
            progressDialog.setTitle(getString(R.string.loading));
        }
        progressDialog.show();
    }
}
