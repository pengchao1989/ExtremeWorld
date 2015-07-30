package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.jixianxueyuan.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 4/25/15.
 */
public class DiscussionDetailActivity extends Activity{

    @InjectView(R.id.discussion_detail_title)TextView titleTextView;
    @InjectView(R.id.discussion_detail_content)TextView contentTextView;

    String title;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.discussion_detail_activity);

        ButterKnife.inject(this);

        Intent intent = this.getIntent();

        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");

        titleTextView.setText(title);
        contentTextView.setText(content);
    }
}
