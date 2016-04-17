package com.jixianxueyuan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.jixianxueyuan.R;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.dto.ExhibitionDTO;
import com.jixianxueyuan.util.Util;
import com.jixianxueyuan.widget.AdvancedWebView;
import com.jixianxueyuan.widget.MyActionBar;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;

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
    @InjectView(R.id.web_view)AdvancedWebView webView;
    @InjectView(R.id.invite_web_button)
    Button inviteButton;

    private MyApplication application;
    private Mine mine;
    ExhibitionDTO exhibitionDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_web_activity);
        ButterKnife.inject(this);

        actionBar.setTitle("邀请好友");

        application = (MyApplication) this.getApplicationContext();
        mine = application.getMine();

        exhibitionDTO = (ExhibitionDTO) getIntent().getSerializableExtra(INTENT_EXHIBITION);
        if (exhibitionDTO == null){
            finish();
        }
    }

    @OnClick(R.id.invite_web_button)void onClick(){
        share();
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
                .withText(inviteMessage)
                .withTargetUrl(url)
                .open();
    }

    public static void startActivity(Context context, ExhibitionDTO exhibitionDTO){
        Intent intent = new Intent(context, InviteWebActivity.class);
        intent.putExtra(INTENT_EXHIBITION, exhibitionDTO);
        context.startActivity(intent);
    }
}
