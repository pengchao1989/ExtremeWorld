package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.cchannel.plugin.CloudPushService;
import com.alibaba.sdk.android.AlibabaSDK;
import com.jixianxueyuan.MainActivity;
import com.jixianxueyuan.R;
import com.jixianxueyuan.app.MyApplication;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pengchao on 6/2/15.
 */
public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.setting_activity_logout)void onLogoutClick(){
        MyApplication.getContext().getMine().loginOut(this);
        CloudPushService cloudPushService = AlibabaSDK.getService(CloudPushService.class);
        cloudPushService.logout();
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.setting_activity_notify)void onNotifyClick(){

    }

    @OnClick(R.id.setting_activity_feedback)void onFeedbackClick(){

    }

    @OnClick(R.id.setting_activity_clean_cache)void onCleanCacheClick(){

    }
}
