package com.jixianxueyuan.activity;

import android.app.Activity;
import android.os.Bundle;

import com.jixianxueyuan.R;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 4/25/15.
 */
public class DiscussionDetailActivity extends Activity{

    @InjectView(R.id.discussion_detail_swipeback_layout)
    SwipeBackLayout swipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.discussion_detail_activity);

        ButterKnife.inject(this);

        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
    }
}
