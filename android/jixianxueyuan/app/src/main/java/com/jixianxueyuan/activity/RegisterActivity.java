package com.jixianxueyuan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.jixianxueyuan.dto.Error;
import com.jixianxueyuan.dto.HobbyDTO;
import com.jixianxueyuan.dto.InviteDTO;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.UserDTO;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.dto.qq.QQOpenInfo;
import com.jixianxueyuan.dto.qq.QQUserInfo;
import com.jixianxueyuan.dto.request.ReferenceAvatarDTO;
import com.jixianxueyuan.dto.request.UserRegisterRequest;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.DiskCachePath;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.StringUtils;
import com.jixianxueyuan.util.Util;
import com.jixianxueyuan.util.qiniu.QiniuSingleImageUpload;
import com.jixianxueyuan.util.qiniu.QiniuSingleImageUploadListener;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    private static final String tag = "RegisterActivity";
    public static final String REGISTER_TYPE = "registerType";
    public static final String REGISTER_TYPE_QQ = "qq";
    public static final String REGISTER_TYPE_PHONE = "phone";

    public static final int REQUEST_IMAGE_CODE = 1;
    public static final int CROP_IMAGE_CODE = 2;

    @InjectView(R.id.register_avatar)ImageView avatarImageView;
    @InjectView(R.id.register_avatar_select)Button selectButton;
    @InjectView(R.id.register_nick_name)EditText nickNameEditText;
    @InjectView(R.id.register_birth)EditText birthEditText;
    @InjectView(R.id.register_gender_radiogroup)RadioGroup genderRadiogroup;
    @InjectView(R.id.register_invitation_layout)LinearLayout invitationLayout;
    @InjectView(R.id.register_inviting_layout)LinearLayout invitingLayout;
    @InjectView(R.id.register_invitation_code_edittext)EditText invitationCodeEditText;
    @InjectView(R.id.register_inviting_name)TextView invitingNameTextView;
    @InjectView(R.id.register_invitation_description)TextView invitationDescriptionTextView;
    @InjectView(R.id.register_verification_code_layout)LinearLayout verCodeLayout;
    @InjectView(R.id.register_verification_code_edittext)EditText verCodeEditText;
    @InjectView(R.id.register_verification_code_retry_button) TextView verCodeRetryButton;
    @InjectView(R.id.register_password_layout)LinearLayout passwordLayout;
    @InjectView(R.id.register_password_edittext)EditText passwordEditText;


    private AlertDialog progressDialog;
    private TimeCount timeCount;


    private String hobby;
    private String registerType; //qq phone
    private QQOpenInfo qqOpenInfo;
    private QQUserInfo qqUserInfo;
    private String phoneNumber;
    private String invitationCode;
    private String verCode;
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

        //requestReferenceAvatar();
    }

    private void initView(){

        hobby = Util.getApplicationMetaString(this, "HOBBY");
        MyLog.d(this.getClass().getSimpleName(), "hobby=" + hobby);

        Bundle bundle = this.getIntent().getExtras();
        registerType = bundle.getString("registerType");

        if(registerType.equals(REGISTER_TYPE_QQ)){
            if(bundle.containsKey("qqOpenInfo") && bundle.containsKey("qqUserInfo") ){
                qqOpenInfo = (QQOpenInfo) bundle.getSerializable("qqOpenInfo");
                qqUserInfo = (QQUserInfo) bundle.getSerializable("qqUserInfo");

                userRequestParam.setQqOpenId(qqOpenInfo.getOpenid());

                invitationLayout.setVisibility(View.VISIBLE);
                nickNameEditText.setText(qqUserInfo.getNickname());
                downLoadQQAvatar();
            }
        }else if(registerType.equals(REGISTER_TYPE_PHONE)){
            passwordLayout.setVisibility(View.VISIBLE);
            verCodeLayout.setVisibility(View.VISIBLE);
            phoneNumber = bundle.getString("phoneNumber");
            userRequestParam.setPhone(phoneNumber);
            timeCount = new TimeCount(60000, 1000);
        }

        HobbyDTO currentHobbyDTO = MyApplication.getContext().getAppInfomation().getCurrentHobbyInfo();
        if(currentHobbyDTO != null){
            appConfigDTO = currentHobbyDTO.getAppConfig();
        }


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

        if(registerType.equals(REGISTER_TYPE_QQ)){
            if(appConfigDTO != null){
                if(!appConfigDTO.getOpenInvitation()){
                    invitationLayout.setVisibility(View.GONE);

                }else {
                    invitationDescriptionTextView.setText(Html.fromHtml("<u>" + appConfigDTO.getInvitationDesTitle() + "</u>"));
                }
            }
        }else if(registerType.equals(REGISTER_TYPE_PHONE)){
            requestInvite();
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
        if(registerType.equals(REGISTER_TYPE_PHONE)){
            userRequestParam.setPhone(phoneNumber);
            userRequestParam.setPassword(passwordEditText.getText().toString());
            verCode = verCodeEditText.getText().toString();
        }
        //userRequestParam.setId(16L);

        return true;
    }

    private boolean checkParam(){
        int birth = Integer.parseInt(birthEditText.getText().toString());
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        String error = "";
        if(birth < (currentYear - 99) || birth > currentYear){
            Toast.makeText(RegisterActivity.this, getString(R.string.err_birth),Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!Verification.checkNickName(nickNameEditText.getText().toString(), error)){
            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
            return false;
        }


        if(registerType.equals(REGISTER_TYPE_PHONE)){

            if(StringUtils.isBlank(passwordEditText.getText().toString())){
                Toast.makeText(RegisterActivity.this, getString(R.string.err_password_empty),Toast.LENGTH_SHORT).show();
                return false;
            }

            if(StringUtils.isBlank(verCodeEditText.getText().toString())){
                Toast.makeText(RegisterActivity.this, getString(R.string.err_verification_code),Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private void requestRegister(){

        if(!checkParam()){
            return;
        }
        buildUserIofoParam();



        RequestQueue queue = Volley.newRequestQueue(this);
        String url = null;
        if(registerType.equals(REGISTER_TYPE_QQ)){
            url = ServerMethod.account_qq_register() + "?invitationCode=" + invitationCode;
        }else if(registerType.equals(REGISTER_TYPE_PHONE)){
            url = ServerMethod.account_phone_register() + "?verCode=" + verCode + "&invitationCode=" + invitationCode;
        }else {
            return;
        }

        MyRequest<UserDTO> myRequest = new MyRequest(Request.Method.POST,url,UserDTO.class, userRequestParam,
                new Response.Listener<MyResponse<UserDTO>>() {
                    @Override
                    public void onResponse(MyResponse<UserDTO> response) {
                        if(response.getStatus() == MyResponse.status_ok){
                            Toast.makeText(RegisterActivity.this, R.string.register_success, Toast.LENGTH_LONG).show();


                            UserDTO newUserDTO = response.getContent();
                            Mine mine = MyApplication.getContext().getMine();
                            mine.setUserInfo(newUserDTO);
                            mine.WriteSerializationToLocal(RegisterActivity.this);

                            Intent intent = new Intent(RegisterActivity.this, NewHomeActivity.class);
                            startActivity(intent);

                            finish();
                        }else if(response.getStatus() == MyResponse.status_error){
                            Error error = response.getError();
                            MyErrorHelper.showErrorToast(RegisterActivity.this, error);

                        }
                        progressDialog.dismiss();
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

    private void requestInvite(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.invite() + "?phone=" + phoneNumber;

        MyRequest<InviteDTO> myRequest = new MyRequest<InviteDTO>(Request.Method.GET, url, InviteDTO.class,
                new Response.Listener<MyResponse<InviteDTO>>() {
                    @Override
                    public void onResponse(MyResponse<InviteDTO> response) {
                        if(response.getStatus() == MyResponse.status_ok){

                            UserMinDTO invitingUser = response.getContent().getInviteUser();
                            if(invitingUser != null){
                                invitingLayout.setVisibility(View.VISIBLE);
                                invitingNameTextView.setText(invitingUser.getName());
                            }


                        }else if(response.getStatus() == MyResponse.status_error){
                            MyErrorHelper.showErrorToast(RegisterActivity.this, response.getError());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(myRequest);
    }

    private void downLoadQQAvatar(){
        if(qqUserInfo != null){
            final ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.loadImage(qqUserInfo.getFigureurl_qq_2(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                    if(bitmap == null){
                        avatarImageView.setImageResource(R.mipmap.avatar_def);
                        return;
                    }
                    //show
                    avatarImageView.setImageBitmap(bitmap);

                    isUseUploadAvatar = true;

                    //save
                    File f = new File(DiskCachePath.getDiskCacheDir(RegisterActivity.this, "avatarCache"), "qq_avatar");
                    localAvatarPath = DiskCachePath.getDiskCacheDir(RegisterActivity.this, "avatarCache") + "/qq_avatar";
                    if (f.exists()) {
                        f.delete();
                    }
                    try {
                        FileOutputStream out = new FileOutputStream(f);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                        out.flush();
                        out.close();
                        MyLog.i("Regis", "已经保存");
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }
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

    @OnClick(R.id.register_verification_code_retry_button)void onVerCodeRetryClick(){
        timeCount.start();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            verCodeRetryButton.setText(getString(R.string.re_acquisition));
            verCodeRetryButton.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            verCodeRetryButton.setClickable(false);
            verCodeRetryButton.setText( getString(R.string.re_acquisition) + millisUntilFinished / 1000 + "s");
        }
    }

}
