package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.server.StaticResourceConfig;
import com.jixianxueyuan.util.AnalyzeContent;
import com.jixianxueyuan.util.DateTimeFormatter;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 5/22/15.
 */
public class TopicDetailActivity extends Activity{

    @InjectView(R.id.topic_detail_swipeback_layout)
    SwipeBackLayout swipeBackLayout;
    @InjectView(R.id.topic_detail_title)TextView titleTextView;
    @InjectView(R.id.topic_detail_content)TextView contentTextView;
    @InjectView(R.id.topic_detail_name)TextView nameTextView;
    @InjectView(R.id.topic_detail_time)TextView timeTextView;
    @InjectView(R.id.topic_detail_avatar)ImageView avatarImageView;
    //@InjectView(R.id.topic_detail_content_webview)WebView contentWebView;

    String title;
    String content;
    String name;
    String avatar;
    String createTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.topic_detail_activity);

        ButterKnife.inject(this);

        Intent intent = this.getIntent();

        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        name = intent.getStringExtra("name");
        createTime = intent.getStringExtra("createTime");
        avatar = intent.getStringExtra("avatar");


        titleTextView.setText(title);
        nameTextView.setText(name);
        String timeAgo = DateTimeFormatter.getTimeAgo(this, createTime);
        timeTextView.setText(timeAgo);

        String url = StaticResourceConfig.IMG_DOMAIN + avatar + "!androidListAvatar";
        ImageLoader.getInstance().displayImage(url, avatarImageView);

        contentTextView.setText(content);

        //contentWebView.loadData(content, "text/html", "utf-8");
        //contentWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);

        List<AnalyzeContent.ContentFragment> contentFragmentList = new LinkedList<AnalyzeContent.ContentFragment>();
        contentFragmentList = AnalyzeContent.analyzeContent(content);


        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
    }
}
