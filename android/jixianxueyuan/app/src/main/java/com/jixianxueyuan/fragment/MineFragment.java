package com.jixianxueyuan.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.CollectionListActivity;
import com.jixianxueyuan.activity.CreditActivity;
import com.jixianxueyuan.activity.CropBgActivity;
import com.jixianxueyuan.activity.InviteWebActivity;
import com.jixianxueyuan.activity.LotteryPlanHomeActivity;
import com.jixianxueyuan.activity.NewHomeActivity;
import com.jixianxueyuan.activity.OfflineVideoListActivity;
import com.jixianxueyuan.activity.RemindListActivity;
import com.jixianxueyuan.activity.SettingActivity;
import com.jixianxueyuan.activity.SponsorshipActivity;
import com.jixianxueyuan.activity.WebActivity;
import com.jixianxueyuan.activity.admin.AdminHomeActivity;
import com.jixianxueyuan.activity.profile.ProfileEditActivity;
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
import com.jixianxueyuan.dto.lottery.LotteryPlanDTO;
import com.jixianxueyuan.dto.lottery.LotteryPlanDetailOfUserDTO;
import com.jixianxueyuan.dto.request.UserAttributeRequestDTO;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.ACache;
import com.jixianxueyuan.util.ImageUriParseUtil;
import com.jixianxueyuan.util.StringUtils;
import com.jixianxueyuan.util.qiniu.QiniuSingleImageUpload;
import com.jixianxueyuan.util.qiniu.QiniuSingleImageUploadListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
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

    private static final String CACHE_POINT_COUNT = "cache_point_count";
    private static final String CACHE_LUCKY_COUNT = "cache_lucky_count";

    @BindView(R.id.mine_fragment_head_image_view)
    SimpleDraweeView headImageView;
    @BindView(R.id.mine_avatar_imageview)
    SimpleDraweeView avatarImageView;
    @BindView(R.id.mine_fragment_signature)
    TextView signatureTextView;
    @BindView(R.id.mine_fragment_point_count)
    TextView pointCountTextView;
    @BindView(R.id.mine_fragment_admin)
    RelativeLayout adminLayout;
    @BindView(R.id.mine_fragment_lucky_count)
    TextView luckCountTextView;
    @BindView(R.id.mine_fragment_pick_luck_button)
    Button pickLuckButton;

    private NewHomeActivity activity;

    private AlertDialog progressDialog;

    private MyApplication application;

    private Mine mine;

    private String bgImageFilePath;
    private QiniuSingleImageUpload qiniuSingleImageUpload;


    private LotteryPlanDTO lotteryPlanDTO;
    private long myLuckFactorCount = 0;

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

        ButterKnife.bind(this, view);

        if (mine.getUserInfo().getId() == 32L || mine.getUserInfo().getId() == 1664){
            adminLayout.setVisibility(View.VISIBLE);
        }

        avatarImageView.setImageURI(ImageUriParseUtil.parse(mine.getUserInfo().getAvatar()));
        if (!StringUtils.isEmpty(mine.getUserInfo().getBg())){
            headImageView.setImageURI(ImageUriParseUtil.parse(mine.getUserInfo().getBg()));
        }else {
            headImageView.setImageURI(ImageUriParseUtil.parse("http://7u2nc3.com1.z0.glb.clouddn.com/default_head.jpg" + QiniuImageStyle.COVER));
        }


        signatureTextView.setText(mine.getUserInfo().getSignature());

        //show cache
        ACache aCache = ACache.get(MineFragment.this.getActivity());
        if (aCache != null){
            pointCountTextView.setText( "(" + String.valueOf(aCache.getAsString(CACHE_POINT_COUNT)) + "积分)");
            luckCountTextView.setText( "(" + String.valueOf(aCache.getAsString(CACHE_LUCKY_COUNT)) + "幸运豆)");
        }


        return view;

    }

    @Override
    public void onResume()
    {
        super.onResume();
        requestPointCount();
        requestLuckCount();
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

    @OnClick(R.id.mine_fragment_offline_video)void offlineVideoOnClick(){
        MobclickAgent.onEvent(MineFragment.this.getContext(), UmengEventId.MINE_OFFLINE_VIDEO_CLICK);
        Intent intent = new Intent(this.getActivity(), OfflineVideoListActivity.class);
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

    @OnClick(R.id.mine_fragment_head_image_view)void onHeadClick(){
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
    }

    @OnClick(R.id.mine_fragment_share)void onShareClick(){
        InviteWebActivity.startActivity(this.getContext());
    }

    @OnClick(R.id.mine_fragment_point)void onPointClick(){
        requestDuibaAutoLoginUrl();
    }

    @OnClick(R.id.mine_fragment_lottery_plan)void onLotteryPlanClick(){
        MobclickAgent.onEvent(MineFragment.this.getContext(), UmengEventId.MINE_LOTTERY_PLAN_CLICK);
        Intent intent = new Intent(this.getActivity(), LotteryPlanHomeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.mine_fragment_admin)void onAdminClick() {
        Intent intent = new Intent(this.getActivity(), AdminHomeActivity.class);
        startActivity(intent);
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

    private void startCreditActivity(String auto_login_url){
        Intent intent = new Intent();
        intent.setClass(MineFragment.this.getContext(), CreditActivity.class);
        intent.putExtra("navColor", "#E24542");    //配置导航条的背景颜色，请用#ffffff长格式。
        intent.putExtra("titleColor", "#ffffff");    //配置导航条标题的颜色，请用#ffffff长格式。
        intent.putExtra("url", auto_login_url);    //配置自动登陆地址，每次需服务端动态生成。
        startActivity(intent);

        CreditActivity.creditsListener = new CreditActivity.CreditsListener() {
            /**
             * 当点击分享按钮被点击
             * @param shareUrl 分享的地址
             * @param shareThumbnail 分享的缩略图
             * @param shareTitle 分享的标题
             * @param shareSubtitle 分享的副标题
             */
            public void onShareClick(WebView webView, String shareUrl, String shareThumbnail, String shareTitle, String shareSubtitle) {
                //当分享按钮被点击时，会调用此处代码。在这里处理分享的业务逻辑。
                new AlertDialog.Builder(webView.getContext())
                        .setTitle("分享信息")
                        .setItems(new String[] {"标题："+shareTitle,"副标题："+shareSubtitle,"缩略图地址："+shareThumbnail,"链接："+shareUrl}, null)
                        .setNegativeButton("确定", null)
                        .show();
            }

            /**
             * 当点击登录
             * @param webView 用于登录成功后返回到当前的webview并刷新。
             * @param currentUrl 当前页面的url
             */
            public void onLoginClick(WebView webView, String currentUrl) {
                //当未登录的用户点击去登录时，会调用此处代码。
                //为了用户登录后能回到之前未登录前的页面。
                //当用户登录成功后，需要重新动态生成一次自动登录url，需包含redirect参数，将currentUrl放入redirect参数。
                new AlertDialog.Builder(webView.getContext())
                        .setTitle("跳转登录")
                        .setMessage("跳转到登录页面？")
                        .setPositiveButton("是", null)
                        .setNegativeButton("否", null)
                        .show();
            }

            /**
             * 当点击“复制”按钮时，触发该方法，回调获取到券码code
             * @param webView webview对象。
             * @param code 复制的券码
             */
            public void onCopyCode(WebView webView, String code) {
                //当未登录的用户点击去登录时，会调用此处代码。
                new AlertDialog.Builder(webView.getContext())
                        .setTitle("复制券码")
                        .setMessage("已复制，券码为："+code)
                        .setPositiveButton("是", null)
                        .setNegativeButton("否", null)
                        .show();
            }

            /**
             * 积分商城返回首页刷新积分时，触发该方法。
             */
            public void onLocalRefresh(WebView mWebView, String credits) {
                //String credits为积分商城返回的最新积分，不保证准确。
                //触发更新本地积分，这里建议用ajax向自己服务器请求积分值，比较准确。
                pointCountTextView.setText("(" + credits + "积分)");
            }
        };
    }


    private void uploadUserBg(){

        showProgress();

        if(qiniuSingleImageUpload == null){
            qiniuSingleImageUpload = new QiniuSingleImageUpload(MineFragment.this.getContext());
        }
        qiniuSingleImageUpload.upload(bgImageFilePath, UploadPrefixName.COVER, new QiniuSingleImageUploadListener() {
            @Override
            public void onUploading(double percent) {

            }

            @Override
            public void onUploadComplete(String url) {
                hideProgress();
                //更新后台信息
                requestUpdateUserBackground(url);
            }

            @Override
            public void onError(String error) {
                hideProgress();
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
                        hideProgress();
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
                hideProgress();
                MyVolleyErrorHelper.showError(MineFragment.this.getContext(), error);
            }
        });

        MyApplication.getContext().getRequestQueue().add(myRequest);
    }

    private void requestDuibaAutoLoginUrl(){
        String url = ServerMethod.point_duiba_auto_login();
        MyRequest<String> myRequest = new MyRequest<String>(Request.Method.GET, url, String.class, null, new Response.Listener<MyResponse<String>>() {
            @Override
            public void onResponse(MyResponse<String> response) {
                if (!TextUtils.isEmpty(response.getContent())){
                    startCreditActivity(response.getContent());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getContext().getRequestQueue().add(myRequest);
    }

    private void requestPointCount(){
        String url = ServerMethod.point_count();
        MyRequest<Integer> myRequest = new MyRequest<Integer>(Request.Method.GET, url, Integer.class, null, new Response.Listener<MyResponse<Integer>>() {
            @Override
            public void onResponse(MyResponse<Integer> response) {
                pointCountTextView.setText( "(" + String.valueOf(response.getContent()) + "积分)");
                ACache.get(MineFragment.this.getActivity()).put(CACHE_POINT_COUNT, String.valueOf(response.getContent()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getContext().getRequestQueue().add(myRequest);
    }

    private void requestLuckCount(){
        String url = ServerMethod.ongoing_lucky_saturday();
        MyRequest<LotteryPlanDetailOfUserDTO> myRequest = new MyRequest<LotteryPlanDetailOfUserDTO>(Request.Method.GET, url, LotteryPlanDetailOfUserDTO.class, null, new Response.Listener<MyResponse<LotteryPlanDetailOfUserDTO>>() {
            @Override
            public void onResponse(MyResponse<LotteryPlanDetailOfUserDTO> response) {
                LotteryPlanDetailOfUserDTO lotteryPlanDetailOfUserDTO = response.getContent();
                if (lotteryPlanDetailOfUserDTO != null){
                    lotteryPlanDTO = lotteryPlanDetailOfUserDTO.getLotteryPlan();
                    myLuckFactorCount = lotteryPlanDetailOfUserDTO.getUserLuckyFactorCount();
                    luckCountTextView.setText( "(" + String.valueOf(myLuckFactorCount) + "幸运豆)");
                    ACache.get(MineFragment.this.getActivity()).put(CACHE_LUCKY_COUNT, String.valueOf(myLuckFactorCount));
                    if (lotteryPlanDetailOfUserDTO.isCanGetLuckyFactor() && lotteryPlanDTO != null){
                        pickLuckButton.setVisibility(View.VISIBLE);
                        pickLuckButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MobclickAgent.onEvent(MineFragment.this.getContext(), UmengEventId.MINE_LOTTERY_PICK_LUCKY_CLICK);
                                requestPickLuckyFactor();
                            }
                        });
                    }else {
                        pickLuckButton.setVisibility(View.GONE);
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getContext().getRequestQueue().add(myRequest);
    }

    private void requestPickLuckyFactor(){
        String url = ServerMethod.lottery_plan() + "/" + lotteryPlanDTO.getId() + "/pickLuckyFactor";
        MyRequest<Void> myRequest = new MyRequest<Void>(Request.Method.POST, url, Void.class, null, new Response.Listener<MyResponse<Void>>() {
            @Override
            public void onResponse(MyResponse<Void> response) {
                if (response.isOK()){
                    pickLuckButton.setVisibility(View.GONE);
                    luckCountTextView.setText( "(" + String.valueOf(++myLuckFactorCount) + "幸运豆)");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getContext().getRequestQueue().add(myRequest);
    }

    private void showProgress(){
        if (progressDialog == null){
            progressDialog = new SpotsDialog(MineFragment.this.getContext(),R.style.ProgressDialogWait);
        }
        progressDialog.show();
    }

    private void hideProgress(){
        if (progressDialog != null){
            progressDialog.hide();
        }
    }
}
