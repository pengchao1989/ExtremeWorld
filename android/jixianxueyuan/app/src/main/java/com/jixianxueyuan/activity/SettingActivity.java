package com.jixianxueyuan.activity;

import android.app.Activity;
import android.os.Bundle;

import com.jixianxueyuan.R;

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

    }

    @OnClick(R.id.setting_activity_notify)void onNotifyClick(){

    }

    @OnClick(R.id.setting_activity_feedback)void onFeedbackClick(){

    }

    @OnClick(R.id.setting_activity_clean_cache)void onCleanCacheClick(){

    }
}
