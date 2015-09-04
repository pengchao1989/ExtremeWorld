package com.jixianxueyuan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.jixianxueyuan.dto.ErrorInfo;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.UserDTO;
import com.jixianxueyuan.dto.request.UserUpdateRequestDTO;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.widget.MyActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by pengchao on 9/2/15.
 */
public class ProfileEditActivity extends Activity {

    public static final int REQUEST_IMAGE_CODE = 1;
    public static final int CROP_IMAGE_CODE = 2;


    public static final String GENDER_MALE = "male";
    public static final String GENDER_FEMALE = "female";

    @InjectView(R.id.profile_edit_actionbar)MyActionBar myActionBar;
    @InjectView(R.id.profile_edit_avatar)ImageView avatarImageView;
    @InjectView(R.id.profile_edit_nickname)EditText nickNameEditText;
    @InjectView(R.id.profile_edit_gender)TextView genderTextView;
    @InjectView(R.id.profile_edit_signature)EditText signatureTextView;

    private AlertDialog progressDialog;


    UserDTO userDTO;
    UserUpdateRequestDTO userUpdateRequestParam;
    String localAvatarPath;

    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_activity);

        ButterKnife.inject(this);

        userDTO = MyApplication.getContext().getMine().getUserInfo();
        userUpdateRequestParam = new UserUpdateRequestDTO();
        userUpdateRequestParam.setId(userDTO.getId());
        userUpdateRequestParam.setAvatar(userDTO.getAvatar());
        userUpdateRequestParam.setName(userDTO.getName());
        userUpdateRequestParam.setGender(userDTO.getGender());
        userUpdateRequestParam.setSignature(userDTO.getSignature());



        initView();
    }

    private void initView(){
        imageLoader = ImageLoader.getInstance();
        String avatarUrl = userDTO.getAvatar() + "!AndroidProfileAvatar";
        imageLoader.displayImage(avatarUrl, avatarImageView, ImageLoaderConfig.getAvatarOption(this));

        nickNameEditText.setText(userDTO.getName());
        signatureTextView.setText(userDTO.getSignature());
        if(userDTO.getGender().equals("male")){
            genderTextView.setText(getString(R.string.male));
        }else if(userDTO.getGender().equals("female")){
            genderTextView.setText(getString(R.string.female));
        }else {
            genderTextView.setText(getString(R.string.unknown));
        }

        myActionBar.setActionOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit(){
        buildParam();

        if(checkParam()){

            requestSaveProfile();
        }
    }

    private boolean checkParam(){

        String error = "";
        if(!Verification.checkNickName(userUpdateRequestParam.getName(),error)){
            Toast.makeText(ProfileEditActivity.this, error, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void buildParam(){
        userUpdateRequestParam.setName(nickNameEditText.getText().toString());
        userUpdateRequestParam.setSignature(signatureTextView.getText().toString());
    }

    private void requestSaveProfile(){

        progressDialog = new SpotsDialog(this,R.style.ProgressDialogUpdating);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.profile_update();

        MyRequest<UserDTO> myRequest = new MyRequest<UserDTO>(Request.Method.POST, url, UserDTO.class, userUpdateRequestParam,
                new Response.Listener<MyResponse<UserDTO>>() {
                    @Override
                    public void onResponse(MyResponse<UserDTO> response) {

                        progressDialog.dismiss();

                        if (response.getStatus() == MyResponse.status_ok){
                            UserDTO newUserDTO = response.getContent();
                            Mine mine = MyApplication.getContext().getMine();
                            mine.setUserInfo(newUserDTO);
                            mine.WriteSerializationToLocal(ProfileEditActivity.this);
                            finish();
                            Toast.makeText(ProfileEditActivity.this, getString(R.string.success),Toast.LENGTH_SHORT).show();

                        }else if(response.getStatus() == MyResponse.status_error){
                            ErrorInfo errorInfo = response.getErrorInfo();
                            MyErrorHelper.showErrorToast(ProfileEditActivity.this, errorInfo);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MyVolleyErrorHelper.showError(ProfileEditActivity.this,error);
            }
        });

        queue.add(myRequest);
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
                        Intent intent = new Intent(ProfileEditActivity.this, CropAvatarActivity.class);
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
                        ImageLoaderConfig.getAvatarOption(this);
                        imageLoader.displayImage("file://" + filePath, avatarImageView, ImageLoaderConfig.getAvatarOption(this));
                    }
                }
                break;
        }
    }

    @OnClick(R.id.profile_edit_avatar)void onAvatarClick(){
        Intent intent = new Intent(this, MultiImageSelectorActivity.class);

        // whether show camera
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);

        startActivityForResult(intent, REQUEST_IMAGE_CODE);
    }

    @OnClick(R.id.profile_edit_gender)void onGenderClick(){

    }

}
