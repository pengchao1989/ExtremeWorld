package com.jixianxueyuan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jixianxueyuan.R;
import com.jixianxueyuan.dto.ErrorInfo;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.qq.QQOpenInfo;
import com.jixianxueyuan.dto.qq.QQUserInfo;
import com.jixianxueyuan.dto.request.ReferenceAvatarDTO;
import com.jixianxueyuan.dto.request.UserInfoRequest;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.ToastUtils;
import com.jixianxueyuan.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Calendar;

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
    private ReferenceAvatarDTO referenceAvatarDTO;


    @InjectView(R.id.register_avatar)ImageView avatarImageView;
    @InjectView(R.id.register_avatar_random)Button randomButton;
    @InjectView(R.id.register_avatar_select)Button selectButton;
    @InjectView(R.id.register_nick_name)EditText nickNameEditText;
    @InjectView(R.id.register_birth)EditText birthEditText;
    @InjectView(R.id.register_gender_radiogroup)RadioGroup genderRadiogroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        userInfoDTO = new UserInfoRequest();
        userInfoDTO.setGender("female");

        ButterKnife.inject(this);

        initView();
        requestReferenceAvatar();
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
                RadioButton rb = (RadioButton) RegisterActivity.this.findViewById(radioButtonId);
                userInfoDTO.setGender(rb.getText().toString());
            }
        });

    }

    private void refreshAvatarView(){
        String url = referenceAvatarDTO.getUrl() + "!AndroidProfileAvatar";

        ImageLoader.getInstance().displayImage(url, avatarImageView);
    }

    @OnClick(R.id.register_submit)void onRegister(){
        requestRegister();
    }

    private boolean buildUserIofoParam(){
        userInfoDTO.setBirth(birthEditText.getText().toString());
        userInfoDTO.setName(nickNameEditText.getText().toString());
        userInfoDTO.setAvatar(qqUserInfo.getFigureurl_qq_1());
        userInfoDTO.setId(16L);

        return true;
    }

    private boolean checkParam(){
        int birth = Integer.parseInt(birthEditText.getText().toString());
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        if(birth < (currentYear - 99) || birth > currentYear){
            Toast.makeText(RegisterActivity.this, getString(R.string.birth_err),Toast.LENGTH_SHORT).show();
            return false;
        }

        if(nickNameEditText.getText().length() == 0){
            Toast.makeText(RegisterActivity.this, getString(R.string.name_empty_err),Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    private void requestRegister(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.account_register();

        if(!checkParam()){
            return;
        }
        buildUserIofoParam();

        MyRequest<UserInfoRequest> myRequest = new MyRequest(Request.Method.POST,url,UserInfoRequest.class, userInfoDTO,
                new Response.Listener<MyResponse>() {
                    @Override
                    public void onResponse(MyResponse response) {
                        if(response.getStatus() == MyResponse.status_ok){
                            Toast.makeText(RegisterActivity.this, R.string.register_success, Toast.LENGTH_LONG).show();
                        }else if(response.getStatus() == MyResponse.status_error){
                            ErrorInfo errorInfo = response.getErrorInfo();
                            Toast.makeText(RegisterActivity.this, getString(R.string.err_name_repeat), Toast.LENGTH_SHORT).show();
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

    private void requestReferenceAvatar(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.reference_avatar();

        MyRequest<ReferenceAvatarDTO> myRequest = new MyRequest<ReferenceAvatarDTO>(Request.Method.GET, url, ReferenceAvatarDTO.class,
                new Response.Listener<MyResponse<ReferenceAvatarDTO>>() {
                    @Override
                    public void onResponse(MyResponse<ReferenceAvatarDTO> response) {
                        if(response.getStatus() == MyResponse.status_ok){
                            referenceAvatarDTO = response.getContent();
                            refreshAvatarView();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(myRequest);
    }

    @OnClick(R.id.register_avatar_random)void onRandomClick(){
        requestReferenceAvatar();
    }
    @OnClick(R.id.register_avatar_select)void onSelectClick(){

    }
}
