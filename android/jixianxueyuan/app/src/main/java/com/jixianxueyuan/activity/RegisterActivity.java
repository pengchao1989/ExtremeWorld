package com.jixianxueyuan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jixianxueyuan.R;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.qq.QQOpenInfo;
import com.jixianxueyuan.dto.qq.QQUserInfo;
import com.jixianxueyuan.dto.request.UserInfoRequest;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.Util;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pengchao on 7/9/15.
 */
public class RegisterActivity extends Activity {

    private String hobby;
    private QQOpenInfo qqOpenInfo;
    private QQUserInfo qqUserInfo;

    private UserInfoRequest userInfoDTO;


    @InjectView(R.id.register_nick_name)EditText nickNameEditText;
    @InjectView(R.id.register_birth)EditText birthEditText;
    @InjectView(R.id.register_gender_radiogroup)RadioGroup genderRadiogroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        userInfoDTO = new UserInfoRequest();
        userInfoDTO.setGender("男");

        ButterKnife.inject(this);

        initView();
    }

    private void initView(){

        hobby = Util.getApplicationMetaString(this, "HOBBY");
        MyLog.d(this.getClass().getSimpleName(), "hobby=" + hobby);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle.containsKey("qqOpenInfo") && bundle.containsKey("qqUserInfo") ){
            qqOpenInfo = (QQOpenInfo) bundle.getSerializable("qqOpenInfo");
            qqUserInfo = (QQUserInfo) bundle.getSerializable("qqUserInfo");

            userInfoDTO.setQqOpenId(qqOpenInfo.getOpenid());
        }

        nickNameEditText.setText(qqUserInfo.getNickname());


        genderRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton)RegisterActivity.this.findViewById(radioButtonId);
                userInfoDTO.setGender(rb.getText().toString());
            }
        });

    }

    @OnClick(R.id.register_submit)void onRegister(){
        requestRegister();
    }

    private boolean buildUserIofoParam(){
        userInfoDTO.setBirth(birthEditText.getText().toString());
        userInfoDTO.setName(nickNameEditText.getText().toString());
        userInfoDTO.setAvatar(qqUserInfo.getFigureurl_qq_1());

        return true;
    }

    private void requestRegister(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.account_register;

        buildUserIofoParam();

        MyRequest<UserInfoRequest> myRequest = new MyRequest(Request.Method.POST,url,UserInfoRequest.class, userInfoDTO,
                new Response.Listener<MyResponse>() {
                    @Override
                    public void onResponse(MyResponse response) {
                        if(response.getStatus() == MyResponse.status_ok){
                            Toast.makeText(RegisterActivity.this, R.string.register, Toast.LENGTH_LONG).show();
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

}
