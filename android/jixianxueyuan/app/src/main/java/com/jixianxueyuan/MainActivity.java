package com.jixianxueyuan;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jixianxueyuan.activity.CreateShortVideoActivity;
import com.jixianxueyuan.activity.HomeActivity;
import com.jixianxueyuan.dto.BaseInfoDTO;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.UserInfoDTO;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.record.ui.record.ImportVideoActivity;
import com.jixianxueyuan.record.ui.record.MediaRecorderActivity;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends Activity {

    @InjectView(R.id.activity_qq_login)
    Button qqLoginButton;

    @InjectView(R.id.activity_main_danmu)
    Button danmuButton;

    @InjectView(R.id.activity_main_appname)
    ShimmerTextView appNameTextView;

    Tencent mTencent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        final Shimmer shimmer = new Shimmer();
        shimmer.setRepeatCount(0);
        shimmer.start(appNameTextView);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        requestBaseInfo();

    }

    @OnClick(R.id.activity_qq_login)void qqLogin()
    {

        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);

        //requestLogin();




/*        mTencent = Tencent.createInstance("101220015", this.getApplicationContext());
        mTencent.setOpenId("");
        if (!mTencent.isSessionValid())
        {
            mTencent.login(this, "get_user_info,add_t", new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    MyLog.d("MainActivity", "login info =" + o.toString());
                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            });
        }*/
    }

    private void requestBaseInfo()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.baseInfo;

        MyRequest<BaseInfoDTO> myRequest = new MyRequest<BaseInfoDTO>(Request.Method.GET,url,BaseInfoDTO.class,
                new Response.Listener<MyResponse<BaseInfoDTO>>(){

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

    private void requestLogin()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.account_login + "?qqOpenId=DC5F63930F15A7B7DC3AFE50843763BB";

        MyRequest<UserInfoDTO> myRequest = new MyRequest<UserInfoDTO>(Request.Method.GET, url, UserInfoDTO.class,
                new Response.Listener<MyResponse<UserInfoDTO>>() {
                    @Override
                    public void onResponse(MyResponse<UserInfoDTO> response) {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
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

    @OnClick(R.id.activity_main_danmu) void danmu()
    {
        /*Intent intent = new Intent(this, VideoDetailActivity.class);*/
        Intent intent = new Intent(this, ImportVideoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.activity_main_record) void record()
    {
        Intent intent = new Intent(this, MediaRecorderActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResult(requestCode, resultCode, data);
    }



}
