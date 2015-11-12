package com.jixianxueyuan.activity;

import android.content.Intent;
import android.os.Bundle;

import com.jixianxueyuan.R;
import com.jixianxueyuan.config.TopicType;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by pengchao on 11/11/15.
 */
public class CreateTopicPreActivity extends SwipeBackActivity {
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_topic_pre_activity);

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_ALL);

        ButterKnife.inject(this);
    }

    @OnClick(R.id.create_topic_pre_cancel)void onCancel(){
        finish();
    }

    @OnClick(R.id.create_topic_pre_discuss)void onDiscuss(){
        Intent intent = new Intent(this, CreateTopicActivity.class);
        intent.putExtra(TopicType.TYPE, TopicType.DISCUSS);
        startActivity(intent);
    }
    @OnClick(R.id.create_topic_pre_mood)void onMood(){
        Intent intent = new Intent(this, CreateTopicActivity.class);
        intent.putExtra(TopicType.TYPE, TopicType.MOOD);
        startActivity(intent);
    }
    @OnClick(R.id.create_topic_pre_short_video)void onShortVideo(){
        Intent intent = new Intent(this, CreateTopicActivity.class);
        intent.putExtra(TopicType.TYPE, TopicType.S_VIDEO);
        startActivity(intent);
    }


}
