package com.jixianxueyuan.activity.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.BaseActivity;
import com.jixianxueyuan.activity.CropAvatarActivity;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.MyErrorHelper;
import com.jixianxueyuan.config.ImageLoaderConfig;
import com.jixianxueyuan.config.ProfileAttributeName;
import com.jixianxueyuan.config.UploadPrefixName;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.UserDTO;
import com.jixianxueyuan.dto.request.UserAttributeRequestDTO;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.ImageUriParseUtil;
import com.jixianxueyuan.util.qiniu.QiniuSingleImageUpload;
import com.jixianxueyuan.util.qiniu.QiniuSingleImageUploadListener;
import com.jixianxueyuan.widget.MyActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by pengchao on 9/2/15.
 */
public class ProfileEditActivity extends BaseActivity {

    public static final int REQUEST_IMAGE_CODE = 1;
    public static final int CROP_IMAGE_CODE = 2;

    private static final int REQUEST_CODE_GENDER = 0x1001;
    private static final int REQUEST_CODE_SIGNATURE = 0x1002;



    public static final String GENDER_MALE = "male";
    public static final String GENDER_FEMALE = "female";

    @BindView(R.id.profile_edit_actionbar)MyActionBar myActionBar;
    @BindView(R.id.profile_edit_avatar)SimpleDraweeView avatarImageView;
    @BindView(R.id.profile_edit_nickname)TextView nickNameEditText;
    @BindView(R.id.profile_edit_gender)TextView genderTextView;
    @BindView(R.id.profile_edit_signature)TextView signatureTextView;

    private AlertDialog progressDialog;


    private UserDTO userDTO;
    private String localAvatarPath;


    private QiniuSingleImageUpload qiniuSingleImageUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_activity);

        ButterKnife.bind(this);

        userDTO = MyApplication.getContext().getMine().getUserInfo();


        initView();
    }

    private void initView(){
        myActionBar.setTitle(getString(R.string.edit_profile));
        String avatarUrl = userDTO.getAvatar() + "!AndroidProfileAvatar";
        avatarImageView.setImageURI(ImageUriParseUtil.parse(avatarUrl));

        nickNameEditText.setText(userDTO.getName());
        signatureTextView.setText(userDTO.getSignature());
        if(userDTO.getGender().equals("male")){
            genderTextView.setText(getString(R.string.male));
        }else if(userDTO.getGender().equals("female")){
            genderTextView.setText(getString(R.string.female));
        }else {
            genderTextView.setText(getString(R.string.unknown));
        }

    }

    private void uploadUserAvatar(){

        progressDialog = new SpotsDialog(this,R.style.ProgressDialogWait);
        progressDialog.show();

        if(qiniuSingleImageUpload == null){
            qiniuSingleImageUpload = new QiniuSingleImageUpload(this);
        }
        qiniuSingleImageUpload.upload(localAvatarPath, UploadPrefixName.AVATAR, new QiniuSingleImageUploadListener() {
            @Override
            public void onUploading(double percent) {

            }

            @Override
            public void onUploadComplete(String url) {
                progressDialog.dismiss();
                //更新后台信息
                requestUpdateUserAvatar(url);
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                Toast.makeText(ProfileEditActivity.this, R.string.err, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestUpdateUserAvatar(String imageUrl){

        String url = ServerMethod.profile_update_attribute();

        UserAttributeRequestDTO userAttributeRequestDTO = new UserAttributeRequestDTO();
        userAttributeRequestDTO.setAttributeName(ProfileAttributeName.AVATAR);
        userAttributeRequestDTO.setAttributeValue(imageUrl);

        MyRequest<UserDTO> myRequest = new MyRequest<UserDTO>(Request.Method.POST, url, UserDTO.class, userAttributeRequestDTO,
                new Response.Listener<MyResponse<UserDTO>>() {
                    @Override
                    public void onResponse(MyResponse<UserDTO> response) {
                        progressDialog.dismiss();
                        if(response.getStatus() == MyResponse.status_ok){
                            MyApplication.getContext().getMine().setUserInfo(response.getContent());
                            MyApplication.getContext().getMine().WriteSerializationToLocal(ProfileEditActivity.this);

                            //显示图片
                            avatarImageView.setImageURI(ImageUriParseUtil.parse("file://" + localAvatarPath));


                            Toast.makeText(ProfileEditActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                        }else {
                            MyErrorHelper.showErrorToast(ProfileEditActivity.this, response.getError());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MyVolleyErrorHelper.showError(ProfileEditActivity.this, error);
            }
        });

        MyApplication.getContext().getRequestQueue().add(myRequest);
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
                        uploadUserAvatar();
                    }
                }
                break;
            case REQUEST_CODE_SIGNATURE:
                if (resultCode == RESULT_OK) {
                    userDTO.setSignature("");
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

    @OnClick(R.id.profile_edit_gender_layout)void onGenderClick(){

    }

    @OnClick(R.id.profile_edit_signature_layout)void onSignatureClick(){
        Intent intent = new Intent(this, ModifyProfileAttrEditTextActivity.class);
        intent.putExtra(ModifyProfileAttrEditTextActivity.INTENT_TITLE, getString(R.string.signature));
        intent.putExtra(ModifyProfileAttrEditTextActivity.INTENT_HINT, getString(R.string.please_enter));
        intent.putExtra(ModifyProfileAttrEditTextActivity.INTENT_ATTRIBUTE_KEY, ProfileAttributeName.SIGNATURE);
        intent.putExtra(ModifyProfileAttrEditTextActivity.INTENT_ATTRIBUTE_VALUE, userDTO.getSignature());
        startActivityForResult(intent, REQUEST_CODE_SIGNATURE);
    }



}
