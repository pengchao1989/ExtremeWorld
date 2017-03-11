package com.jixianxueyuan.activity.admin;

import android.content.Intent;
import android.os.Bundle;

import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pengchao on 17-3-10.
 */

public class AdminHomeActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_activity);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.topic_manage)void onTopicManageClick(){
        Intent intent = new Intent(this, AdminTopicListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.connect_manage)void onConnectManageClick(){
        Intent intent = new Intent(this, ConnectManageActivity.class);
        startActivity(intent);
    }

}
