package com.jixianxueyuan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jixianxueyuan.activity.HomeActivity;
import com.jixianxueyuan.activity.RegisterActivity;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.config.HobbyType;
import com.jixianxueyuan.dto.BaseInfoDTO;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.UserDTO;
import com.jixianxueyuan.dto.qq.QQOpenInfo;
import com.jixianxueyuan.dto.qq.QQUserInfo;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends Activity {

    @InjectView(R.id.activity_qq_login)
    Button qqLoginButton;

    @InjectView(R.id.activity_main_appname)
    ShimmerTextView appNameTextView;

    Tencent tencent;

    //String openId = null;
    QQOpenInfo qqOpenInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        initView();

        final Shimmer shimmer = new Shimmer();
        shimmer.setRepeatCount(0);
        shimmer.start(appNameTextView);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);


        requestBaseInfo();


        //若本地有登录信息，则直接进行登录
        Mine mine = MyApplication.getContext().getMine();
        if(mine.getUserInfo() != null && mine.getUserInfo().getId() != null){
            //直接进入Hone页
            if(mine.getUserInfo().getId() > 0){
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else{
            //等待用户点击登录按钮进行登录
        }



    }

    private void initView()
    {
        String hobby = Util.getApplicationMetaString(this, "HOBBY");
        switch (hobby){
            case HobbyType.SKATEBOARD:
                appNameTextView.setText(this.getResources().getText(R.string.app_name_skateboard));
                break;
            case HobbyType.PARKOUR:
                appNameTextView.setText(this.getResources().getText(R.string.app_name_packour));
                break;
            case HobbyType.BMX:
                appNameTextView.setText(this.getResources().getText(R.string.app_name_bmx));
                break;
        }
    }

    @OnClick(R.id.activity_qq_login)
    void qqLogin() {

        requestQQOpenId();
    }

    private void requestBaseInfo() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.baseInfo();

        MyRequest<BaseInfoDTO> myRequest = new MyRequest<BaseInfoDTO>(Request.Method.GET, url, BaseInfoDTO.class,
                new Response.Listener<MyResponse<BaseInfoDTO>>() {

                    @Override
                    public void onResponse(MyResponse<BaseInfoDTO> response) {

                        //基础信息，持久化到client中，保证每天只更新一次
                        MyApplication myApplication = (MyApplication) MyApplication.getContext();
                        myApplication.setBaseInfoDTO(response.getContent());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(myRequest);
    }

    private void requestLogin() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.account_login() + "?qqOpenId=" + qqOpenInfo.getOpenid();

        MyRequest<UserDTO> myRequest = new MyRequest<UserDTO>(Request.Method.GET, url, UserDTO.class,
                new Response.Listener<MyResponse<UserDTO>>() {
                    @Override
                    public void onResponse(MyResponse<UserDTO> response) {

                        if (response.getStatus() == ServerMethod.STATUS_OK) {

                            //持久化用户信息
                            Mine mine = MyApplication.getContext().getMine();
                            mine.setUserInfo(response.getContent());
                            mine.WriteSerializationToLocal(MainActivity.this);

                            //登录完成，进入HOME页
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (response.getStatus() == ServerMethod.STATUS_NO_CONTENT) {


                            MyLog.d("MainActivity", "STATUS_NO_CONTENT");
                            //请求QQ方userInfo
                            requestQQuserInfo();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        queue.add(myRequest);
    }

    private void requestQQOpenId(){

        tencent = Tencent.createInstance("101228787", this.getApplicationContext());
        tencent.setOpenId("");
        if (!tencent.isSessionValid()) {
            tencent.login(this, "get_user_info,add_t", new IUiListener() {
                @Override
                public void onComplete(Object response) {

                    MyLog.d("MainActivity", "login info =" + response.toString());

                    Gson gson = new Gson();

                    qqOpenInfo = gson.fromJson(response.toString(), QQOpenInfo.class);
                    if (qqOpenInfo != null) {
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
                if(qqUserInfo != null){
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    Bundle bundle = new Bundle();
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

}
