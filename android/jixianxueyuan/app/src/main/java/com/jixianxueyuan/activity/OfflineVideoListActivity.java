package com.jixianxueyuan.activity;

import android.os.Bundle;

import com.jixianxueyuan.R;

import butterknife.ButterKnife;

/**
 * Created by pengchao on 17-3-25.
 */

public class OfflineVideoListActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offlie_video_list_activity);
        ButterKnife.bind(this);
    }



}
