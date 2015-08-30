package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.cchannel.plugin.CloudPushService;
import com.alibaba.sdk.android.AlibabaSDK;
import com.jakewharton.disklrucache.DiskLruCache;
import com.jixianxueyuan.MainActivity;
import com.jixianxueyuan.R;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.util.DiskCachePath;
import com.jixianxueyuan.util.Util;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pengchao on 6/2/15.
 */
public class SettingActivity extends Activity {

    @InjectView(R.id.setting_activity_cache_size)TextView cacheSizeTextView;

    DiskLruCache diskLruCache = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        ButterKnife.inject(this);

        File cacheDir = DiskCachePath.getDiskCacheDir(this, "short_video");

        try {
            diskLruCache = DiskLruCache.open(cacheDir, Util.getAppVersion(this), 1, 30 * 1024 * 1024);

        } catch (IOException e) {
            e.printStackTrace();
        }

        showCacheSize(false);
    }

    private void showCacheSize(boolean isShowZero) {
        if(isShowZero){
            cacheSizeTextView.setText("(0.00MB)");
        }else {
            long cacheByte = diskLruCache.size();
            double cacheMB = cacheByte / (1024.0*1024.0);
            String cacheSizeString = "(" + String.format("%.2f", cacheMB) + "MB)";
            cacheSizeTextView.setText(cacheSizeString);
        }

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
        try {
            diskLruCache.delete();
            Toast.makeText(SettingActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
            showCacheSize(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
