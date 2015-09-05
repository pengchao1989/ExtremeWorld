package com.jixianxueyuan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jixianxueyuan.R;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.MyErrorHelper;
import com.jixianxueyuan.commons.Verification;
import com.jixianxueyuan.config.ImageLoaderConfig;
import com.jixianxueyuan.config.UploadPrefixName;
import com.jixianxueyuan.dto.AppConfigDTO;
import com.jixianxueyuan.dto.ErrorInfo;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.UserDTO;
import com.jixianxueyuan.dto.qq.QQOpenInfo;
import com.jixianxueyuan.dto.qq.QQUserInfo;
import com.jixianxueyuan.dto.request.ReferenceAvatarDTO;
import com.jixianxueyuan.dto.request.UserRegisterRequest;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.Util;
import com.jixianxueyuan.util.qiniu.QiniuSingleImageUpload;
import com.jixianxueyuan.util.qiniu.QiniuSingleImageUploadListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by pengchao on 7/9/15.
 */
public class RegisterActivity extends Activity {

    public static final int REQUEST_IMAGE_CODE = 1;
    public static final int CROP_IMAGE_CODE = 2;

    @InjectView(R.id.register_avatar)ImageView avatarImageView;
    @InjectView(R.id.register_avatar_random)Button randomButton;
    @InjectView(R.id.register_avatar_select)Button selectButton;
    @InjectView(R.id.register_nick_name)EditText nickNameEditText;
    @InjectView(R.id.register_birth)EditText birthEditText;
    @InjectView(R.id.register_gender_radiogroup)RadioGroup genderRadiogroup;
    @InjectView(R.id.register_invitation_layout)LinearLayout invitationLayout;
    @InjectView(R.id.register_invitation_code_edittext)EditText invitationCodeEditText;
    @InjectView(R.id.register_invitation_description)TextView invitationDescriptionTextView;

    private AlertDialog progressDialog;


    private String hobby;
    private QQOpenInfo qqOpenInfo;
    private QQUserInfo qqUserInfo;
    private AppConfigDTO appConfigDTO;

    private UserRegisterRequest userRequestParam;
    private ReferenceAvatarDTO referenceAvatarDTO;


    private String localAvatarPath;
    private ImageLoader imageLoader;

    private Boolean isUseUploadAvatar = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        userRequestParam = new UserRegisterRequest();
        userRequestParam.setGender("male");

        ButterKnife.inject(this);

        imageLoader = ImageLoader.getInstance();

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

            userRequestParam.setQqOpenId(qqOpenInfo.getOpenid());
        }

        appConfigDTO = MyApplication.getContext().getAppInfomation().getCurrentHobbyInfo().getAppConfig();

        nickNameEditText.setText(qqUserInfo.getNickname());

        genderRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                if (radioButtonId == R.id.register_gender_male) {
                    userRequestParam.setGender("male");
                } else if (radioButtonId == R.id.register_gender_female) {
                    userRequestParam.setGender("female");
                }

            }
        });


        if(appConfigDTO != null){
            if(!appConfigDTO.getOpenInvitation()){
                invitationLayout.setVisibility(View.GONE);
                invitationDescriptionTextView.setText(Html.fromHtml("<u>" + appConfigDTO.getInvitationDesTitle() + "</u>"));
            }
        }

    }

    private void submit(){
        progressDialog = new SpotsDialog(this,R.style.ProgressDialogUpdating);
        progressDialog.show();

        if(isUseUploadAvatar){
            uploadAvatar();
        }else {
            requestRegister();
        }
    }

    private void refreshAvatarView(){
        String url = referenceAvatarDTO.getUrl() + "!AndroidProfileAvatar";

        imageLoader.displayImage(url, avatarImageView, ImageLoaderConfig.getAvatarOption(this));
    }



    private boolean buildUserIofoParam(){
        userRequestParam.setBirth(birthEditText.getText().toString());
        userRequestParam.setName(nickNameEditText.getText().toString());
        //userRequestParam.setId(16L);

        return true;
    }

    private boolean checkParam(){
        int birth = Integer.parseInt(birthEditText.getText().toString());
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        String error = "";
        if(birth < (currentYear - 99) || birth > currentYear){
            Toast.makeText(RegisterActivity.this, getString(R.string.birth_err),Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!Verification.checkNickName(nickNameEditText.getText().toString(), error)){
            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
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

        MyRequest<UserDTO> myRequest = new MyRequest(Request.Method.POST,url,UserDTO.class, userRequestParam,
                new Response.Listener<MyResponse<UserDTO>>() {
                    @Override
                    public void onResponse(MyResponse<UserDTO> response) {
                        if(response.getStatus() == MyResponse.status_ok){
                            Toast.makeText(RegisterActivity.this, R.string.register_success, Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();

                            UserDTO newUserDTO = response.getContent();
                            Mine mine = MyApplication.getContext().getMine();
                            mine.setUserInfo(newUserDTO);
                            mine.WriteSerializationToLocal(RegisterActivity.this);

                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                            startActivity(intent);

                            finish();
                        }else if(response.getStatus() == MyResponse.status_error){
                            ErrorInfo errorInfo = response.getErrorInfo();
                            MyErrorHelper.showErrorToast(RegisterActivity.this, errorInfo);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        MyVolleyErrorHelper.showError(RegisterActivity.this,error);
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
                            userRequestParam.setAvatar(referenceAvatarDTO.getUrl());
                            isUseUploadAvatar = false;
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

    private void uploadAvatar(){
        QiniuSingleImageUpload singleImageUpload = new QiniuSingleImageUpload(this);
        singleImageUpload.upload(localAvatarPath, UploadPrefixName.AVATAR, new QiniuSingleImageUploadListener() {
            @Override
            public void onUploading(double percent) {

            }

            @Override
            public void onUploadComplete(String url) {
                userRequestParam.setAvatar(url);
                MyLog.d("RegisterActivity", "imageUrl=" + url);
                requestRegister();
            }

            @Override
            public void onError(String error) {

            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the result list of select image paths
                    List<String> pathList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    // do your logic ....
                    if(pathList.size() > 0){
                        String path = pathList.get(0);
                        Intent intent = new Intent(RegisterActivity.this, CropAvatarActivity.class);
                        intent.putExtra("imagePath", path);
                        startActivityForResult(intent,CROP_IMAGE_CODE);
                    }

                }
                break;
            case CROP_IMAGE_CODE:
                if (resultCode == RESULT_OK) {

                    String filePath = data.getStringExtra("filePath");
                    if(filePath != null){
                        localAvatarPath = filePath;
                        isUseUploadAvatar = true;
                        ImageLoaderConfig.getAvatarOption(this);
                        imageLoader.displayImage("file://" + filePath, avatarImageView, ImageLoaderConfig.getAvatarOption(this));
                    }

                }
                break;
        }
    }

    @OnClick(R.id.register_submit)void onRegister(){
        submit();
    }

    @OnClick(R.id.register_avatar_random)void onRandomClick(){
        requestReferenceAvatar();
    }
    @OnClick(R.id.register_avatar_select)void onSelectClick(){
        Intent intent = new Intent(this, MultiImageSelectorActivity.class);

        // whether show camera
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);

        startActivityForResult(intent, REQUEST_IMAGE_CODE);
    }

    @OnClick(R.id.register_invitation_description)void onInvitationDesClick(){
        Intent intent = new Intent(RegisterActivity.this, WebActivity.class);
        intent.putExtra("url", appConfigDTO.getInvitationDesUrl());
        startActivity(intent);
    }


}
