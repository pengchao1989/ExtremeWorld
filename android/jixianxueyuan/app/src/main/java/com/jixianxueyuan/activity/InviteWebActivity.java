package com.jixianxueyuan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.jixianxueyuan.R;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.config.HobbyType;
import com.jixianxueyuan.config.QiniuImageStyle;
import com.jixianxueyuan.dto.ExhibitionDTO;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.dto.VideoDetailDTO;
import com.jixianxueyuan.util.ShareUtils;
import com.jixianxueyuan.util.Util;
import com.jixianxueyuan.widget.AdvancedWebView;
import com.jixianxueyuan.widget.MyActionBar;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pengchao on 4/17/16.
 */
public class InviteWebActivity extends BaseActivity {

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

        final UMImage image = new UMImage(InviteWebActivity.this, "http://download.jixianxueyuan.com/barcode_skateboard_download.png");


        final String inviteMessage = mine.getUserInfo().getName()
                + "邀请你加入"
                + Util.getApplicationMetaString(this, "HOBBY");

        final String url =
                "http://www.jixianxueyuan.com/skateboard/invite2"
                        + "?inviteid="
                        + mine.getUserInfo().getId();

        MenuSheetView menuSheetView =
                new MenuSheetView(this, MenuSheetView.MenuType.GRID, "邀请好友...", new MenuSheetView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        ShareUtils.ShareItem shareItem = null;

                        switch (item.getItemId()){
                            case R.id.menu_share_qq:
                                new ShareAction(InviteWebActivity.this).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                                        .withText(inviteMessage)
                                        .withMedia(image)
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
                            case R.id.menu_share_weibo:
                                new ShareAction(InviteWebActivity.this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
                                        .withText(inviteMessage)
                                        .withMedia(image)
                                        .withTargetUrl(url)
                                        .share();
                                break;
                            case R.id.menu_share_weixin:
                                new ShareAction(InviteWebActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                                        .withText(inviteMessage)
                                        .withMedia(image)
                                        .withTargetUrl(url)
                                        .share();
                                break;
                            case R.id.menu_share_weixin_circle:
                                new ShareAction(InviteWebActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
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
}
