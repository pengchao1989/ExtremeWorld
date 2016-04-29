package com.jixianxueyuan.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.jixianxueyuan.R;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.config.HobbyType;
import com.jixianxueyuan.util.BitmapUtils;
import com.jixianxueyuan.util.BuildQRCode;
import com.jixianxueyuan.util.DiskCachePath;
import com.jixianxueyuan.util.QRCodeUtil;
import com.jixianxueyuan.util.ShareUtils;
import com.jixianxueyuan.util.Util;
import com.jixianxueyuan.widget.AdvancedWebView;
import com.jixianxueyuan.widget.MyActionBar;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;

import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pengchao on 4/17/16.
 */
public class InviteWebActivity extends BaseActivity implements AdvancedWebView.Listener, IWXAPIEventHandler{

    public static final String INTENT_EXHIBITION = "INTENT_EXHIBITION";



    @InjectView(R.id.web_actionbar)
    MyActionBar actionBar;
    @InjectView(R.id.bottom_sheet)BottomSheetLayout bottomSheetLayout;
    @InjectView(R.id.web_view)AdvancedWebView webView;
    @InjectView(R.id.invite_web_button)
    Button inviteButton;

    private MyApplication application;
    private Mine mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_web_activity);
        ButterKnife.inject(this);

        actionBar.setTitle("邀请好友");

        application = (MyApplication) this.getApplicationContext();
        mine = application.getMine();

        String url  = "http://www.jixianxueyuan.com/other/invite_board?userid="
                + mine.getUserInfo().getId()
                +"&hobby=" + Util.getApplicationMetaString(this, "HOBBY");
                ;
        webView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @OnClick(R.id.invite_web_button)void onClick(){
        showShareMenu();
    }

    public void share(){

        String inviteMessage = mine.getUserInfo().getName()
                + "邀请你加入"
                + Util.getApplicationMetaString(this, "HOBBY");

        String url =
                "http://www.jixianxueyuan.com/skateboard/invite2"
                        + "?inviteid="
                        + mine.getUserInfo().getId();


        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE
                        ,SHARE_MEDIA.SINA,
                        SHARE_MEDIA.DOUBAN
                };

        new ShareAction(this).setDisplayList(displaylist)
                .withTitle(inviteMessage)
                .withText(inviteMessage)
                .withTargetUrl(url)
                .open();
    }

    //share

    private void showShareMenu(){

        final String url =
                "http://www.jixianxueyuan.com/skateboard/invite2"
                        + "?inviteid="
                        + mine.getUserInfo().getId();


        Bitmap qrcodeBitmap = null;
        try {
            qrcodeBitmap = BuildQRCode.encodeAsBitmap(url, BarcodeFormat.QR_CODE, 360, 360);

        } catch (WriterException e) {
            e.printStackTrace();
        }

 /*       String filePath = DiskCachePath.getDiskCacheDir(this, "my_invite_qrcode.jpg").getAbsolutePath();
        boolean success = QRCodeUtil.createQRImage(url.trim(), 360, 360,
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo),
                filePath);

        Bitmap qrcodeBitmap = null;
        if (success){
            qrcodeBitmap = BitmapFactory.decodeFile(filePath);
        }*/

        final UMImage image = new UMImage(InviteWebActivity.this, qrcodeBitmap);


        final String inviteMessage = mine.getUserInfo().getName()
                + " 邀请你加入"
                + this.getResources().getText(R.string.app_name) + "APP,点击链接立即加入";



        MenuSheetView menuSheetView =
                new MenuSheetView(this, MenuSheetView.MenuType.GRID, "邀请好友...", new MenuSheetView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        ShareUtils.ShareItem shareItem = null;

                        switch (item.getItemId()){
                            case R.id.menu_share_qq:
                                new ShareAction(InviteWebActivity.this).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                                        .withTitle(inviteMessage)
                                        .withTargetUrl(url)
                                        .share();
                                break;
                            case R.id.menu_share_kongjian:
                                new ShareAction(InviteWebActivity.this).setPlatform(SHARE_MEDIA.QZONE).setCallback(umShareListener)
                                        .withTitle(inviteMessage)
                                        .withMedia(image)
                                        .withTargetUrl(url)
                                        .share();

                                break;
                            case R.id.menu_share_weixin:
                                shareToWeiXin(url, inviteMessage, false);
                                break;
                            case R.id.menu_share_weixin_circle:
                                shareToWeiXin(url, inviteMessage, true);
                                break;
                            case R.id.menu_share_weibo:
                                new ShareAction(InviteWebActivity.this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
                                        .withText(inviteMessage)
                                        .withMedia(image)
                                        .withTargetUrl(url)
                                        .share();
                                break;
                        }


                        if (bottomSheetLayout.isSheetShowing()) {
                            bottomSheetLayout.dismissSheet();
                        }
                        return true;
                    }
                });
        menuSheetView.inflateMenu(R.menu.share);
        bottomSheetLayout.showWithSheetView(menuSheetView);
    }

    private void shareToWeiXin(String url, String inviteMessage, boolean isTimeLine) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = url;
        WXMediaMessage mediaMessage = new WXMediaMessage(webpageObject);
        mediaMessage.title = "邀请链接";
        mediaMessage.description = inviteMessage;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        mediaMessage.thumbData = BitmapUtils.bitmap2Bytes(bitmap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = UUID.randomUUID().toString();
        req.message = mediaMessage;
        req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        IWXAPI api = WXAPIFactory.createWXAPI(InviteWebActivity.this, "wxdb326de61ab1adb6", true);
        api.handleIntent(getIntent(), this);
        api.sendReq(req);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(InviteWebActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(InviteWebActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(InviteWebActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    public static void startActivity(Context context){
        Intent intent = new Intent(context, InviteWebActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Toast.makeText(this, "非常感谢你的分享", Toast.LENGTH_LONG).show();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Toast.makeText(this, "取消了分享", Toast.LENGTH_LONG).show();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                break;
            default:
                break;
        }
    }
}
