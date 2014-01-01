package com.jixianxueyuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.push.CloudPushService;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jixianxueyuan.R;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.config.MyErrorCode;
import com.jixianxueyuan.dto.HandshakeDTO;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.UserDTO;
import com.jixianxueyuan.dto.request.HandshakeRequestDTO;
import com.jixianxueyuan.dto.request.LoginRequestDTO;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.Util;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pengchao on 1/1/14.
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.password)
    TextView password;
    @BindView(R.id.login)
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login)void onLoginClick(){

    }


    private void requestLogin() {

        if (TextUtils.isEmpty(userName.getText().toString()) || TextUtils.isEmpty(password.getText().toString())){
            return;
        }

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setLoginName(userName.getText().toString());
        loginRequestDTO.setPassword(password.getText().toString());

        String url = ServerMethod.login();

        MyRequest<UserDTO> myRequest = new MyRequest<UserDTO>(Request.Method.POST, url, UserDTO.class,loginRequestDTO,
                new Response.Listener<MyResponse<UserDTO>>() {
                    @Override
                    public void onResponse(MyResponse<UserDTO> response) {

                        if (response.getStatus() == ServerMethod.STATUS_OK) {

                            //持久化用户信息
                            Mine mine = MyApplication.getContext().getMine();
                            mine.setUserInfo(response.getContent());
                            mine.WriteSerializationToLocal(LoginActivity.this);

                            CloudPushService cloudPushService = AlibabaSDK.getService(CloudPushService.class);
                            cloudPushService.bindAccount(String.valueOf(mine.getUserInfo().getId()));

                            MobclickAgent.onProfileSignIn(String.valueOf(mine.getUserInfo().getId()));

                            requestHandshake();

                            //登录完成，进入HOME页
                            Intent intent = new Intent(LoginActivity.this, NewHomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
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
}
