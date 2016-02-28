package com.jixianxueyuan.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.CollectionListActivity;
import com.jixianxueyuan.activity.CropBgActivity;
import com.jixianxueyuan.activity.NewHomeActivity;
import com.jixianxueyuan.activity.profile.ProfileEditActivity;
import com.jixianxueyuan.activity.RemindListActivity;
import com.jixianxueyuan.activity.SettingActivity;
import com.jixianxueyuan.activity.SponsorshipActivity;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.MyErrorHelper;
import com.jixianxueyuan.config.ImageLoaderConfig;
import com.jixianxueyuan.config.ProfileAttributeName;
import com.jixianxueyuan.config.QiniuImageStyle;
import com.jixianxueyuan.config.UmengEventId;
import com.jixianxueyuan.config.UploadPrefixName;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.UserDTO;
import com.jixianxueyuan.dto.request.UserAttributeRequestDTO;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.Util;
import com.jixianxueyuan.util.qiniu.QiniuSingleImageUpload;
import com.jixianxueyuan.util.qiniu.QiniuSingleImageUploadListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import dmax.dialog.SpotsDialog;
import me.drakeet.materialdialog.MaterialDialog;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by pengchao on 4/20/15.
 */
public class MineFragment extends Fragment {

    public static final String tag = MineFragment.class.getSimpleName();

    public static final int REQUEST_IMAGE_CODE = 1;
    public static final int CROP_IMAGE_CODE = 2;

    @InjectView(R.id.mine_fragment_head_image_view)
    ImageView headImageView;
    @InjectView(R.id.mine_avatar_imageview)
    ImageView avatarImageView;
    @InjectView(R.id.mine_fragment_signature)
    TextView signatureTextView;

    private NewHomeActivity activity;

    private AlertDialog progressDialog;

    private MyApplication application;

    private Mine mine;

    private String bgImageFilePath;
    private QiniuSingleImageUpload qiniuSingleImageUpload;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        application = (MyApplication) this.getActivity().getApplicationContext();
        mine = application.getMine();
        activity = (NewHomeActivity) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.mine_fragment, container, false);

        ButterKnife.inject(this, view);

        ImageLoader.getInstance().displayImage(mine.getUserInfo().getAvatar(), avatarImageView, ImageLoaderConfig.getAvatarOption(this.getActivity()));
        ImageLoader.getInstance().displayImage(mine.getUserInfo().getBg() + QiniuImageStyle.COVER, headImageView, ImageLoaderConfig.getHeadOption(this.getActivity()));

        signatureTextView.setText(mine.getUserInfo().getSignature());

        return view;

    }

    @Override
    public void onResume()
    {
        super.onResume();
    }


    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @OnClick(R.id.mine_fragment_remind_reply) void remindReplyOnClick(){
        MobclickAgent.onEvent(MineFragment.this.getContext(), UmengEventId.MINE_REPLY_CLICK);
        Intent intent = new Intent(this.getActivity(), RemindListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.mine_fragment_collection)void collectionOnClick(){
        MobclickAgent.onEvent(MineFragment.this.getContext(), UmengEventId.MINE_COLLECTION_CLICK);
        Intent intent = new Intent(this.getActivity(), CollectionListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.mine_fragment_setting)void onSettingClick(){
        MobclickAgent.onEvent(MineFragment.this.getContext(), UmengEventId.MINE_SETTING_CLICK);
        Intent intent = new Intent(this.getActivity(), SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.mine_avatar_imageview)void onAvatarOnClick(){
        MobclickAgent.onEvent(MineFragment.this.getContext(), UmengEventId.MINE_AVATAR_CLICK);
        Intent intent = new Intent(this.getActivity(), ProfileEditActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.mine_fragment_sponsorship)void onSponsorship(){
        MobclickAgent.onEvent(MineFragment.this.getContext(), UmengEventId.MINE_SPONSORSHIP_CLICK);
        Intent intent = new Intent(this.getActivity(), SponsorshipActivity.class);
        startActivity(intent);
    }

    @OnLongClick(R.id.mine_fragment_head_image_view)boolean onHeadLongClick(){
/*        new SweetAlertDialog(this.getContext())
                .setTitleText("更新封面")
                .setContentText("YES！")
                .show();*/
        final MaterialDialog mMaterialDialog = new MaterialDialog(MineFragment.this.getContext());
        mMaterialDialog.setTitle("更新封面？");
        mMaterialDialog.setMessage("点击OK上传你自定义封面");
        mMaterialDialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
                showImageSelectActivity();

            }
        });
        mMaterialDialog.setNegativeButton("CANCEL", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        mMaterialDialog.show();

        return true;
    }

    @OnClick(R.id.mine_fragment_share)void onShareClick(){
        //TODO 将url进行配置
        String inviteMessage = mine.getUserInfo().getName()
                + "邀请你加入"
                + Util.getApplicationMetaString(MineFragment.this.getContext(),"HOBBY")
                + "http://www.jixianxueyuan.com/skateboard/invite2"
                + "?inviteid="
                + mine.getUserInfo().getId();

        activity.showShareMenuSheet(MenuSheetView.MenuType.GRID);

    }

    private void showImageSelectActivity(){
        Intent intent = new Intent(MineFragment.this.getActivity(), MultiImageSelectorActivity.class);
        // whether show camera
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);

        startActivityForResult(intent, REQUEST_IMAGE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    // Get the result list of select image paths
                    List<String> pathList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    // do your logic ....
                    if(pathList.size() > 0){
                        String path = pathList.get(0);
                            Intent intent = new Intent(MineFragment.this.getActivity(), CropBgActivity.class);
                            intent.putExtra("imagePath", path);
                            startActivityForResult(intent,CROP_IMAGE_CODE);
                        }
                    }
                break;
            case CROP_IMAGE_CODE:
                if (resultCode == Activity.RESULT_OK) {

                    String filePath = data.getStringExtra("filePath");
                    if(filePath != null){
                        bgImageFilePath = filePath;
                        uploadUserBg();
                    }
                }

                break;
        }
    }

    private void uploadUserBg(){

        progressDialog = new SpotsDialog(MineFragment.this.getContext(),R.style.ProgressDialogUpdating);
        progressDialog.show();

        if(qiniuSingleImageUpload == null){
            qiniuSingleImageUpload = new QiniuSingleImageUpload(MineFragment.this.getContext());
        }
        qiniuSingleImageUpload.upload(bgImageFilePath, UploadPrefixName.COVER, new QiniuSingleImageUploadListener() {
            @Override
            public void onUploading(double percent) {

            }

            @Override
            public void onUploadComplete(String url) {
                progressDialog.dismiss();
                //更新后台信息
                requestUpdateUserBackground(url);
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                Toast.makeText(MineFragment.this.getContext(), R.string.err, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestUpdateUserBackground(String imageUrl){

        String url = ServerMethod.profile_update_attribute();

        UserAttributeRequestDTO userAttributeRequestDTO = new UserAttributeRequestDTO();
        userAttributeRequestDTO.setAttributeName(ProfileAttributeName.BACKGROUND);
        userAttributeRequestDTO.setAttributeValue(imageUrl);

        MyRequest<UserDTO> myRequest = new MyRequest<UserDTO>(Request.Method.POST, url, UserDTO.class, userAttributeRequestDTO,
                new Response.Listener<MyResponse<UserDTO>>() {
                    @Override
                    public void onResponse(MyResponse<UserDTO> response) {
                        progressDialog.dismiss();
                        if(response.getStatus() == MyResponse.status_ok){
                            MyApplication.getContext().getMine().setUserInfo(response.getContent());
                            MyApplication.getContext().getMine().WriteSerializationToLocal(MineFragment.this.getContext());

                            //显示图片
                            ImageLoader imageLoader = ImageLoader.getInstance();
                            imageLoader.displayImage("file://" + bgImageFilePath, headImageView, ImageLoaderConfig.getHeadOption(MineFragment.this.getContext()));

                            Toast.makeText(MineFragment.this.getContext(), R.string.success, Toast.LENGTH_SHORT).show();
                        }else {
                            MyErrorHelper.showErrorToast(MineFragment.this.getContext(), response.getError());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MyVolleyErrorHelper.showError(MineFragment.this.getContext(), error);
            }
        });

        MyApplication.getContext().getRequestQueue().add(myRequest);
    }

}
