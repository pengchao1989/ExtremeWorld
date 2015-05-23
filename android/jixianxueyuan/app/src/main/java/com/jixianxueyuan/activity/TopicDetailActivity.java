package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.TopicDetailListAdapter;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.ReplyDTO;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.server.StaticResourceConfig;
import com.jixianxueyuan.util.AnalyzeContent;
import com.jixianxueyuan.util.DateTimeFormatter;
import com.jixianxueyuan.util.MyLog;
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

    public final static String tag = TopicDetailActivity.class.getSimpleName();

    @InjectView(R.id.topic_detail_swipeback_layout)
    SwipeBackLayout swipeBackLayout;


    @InjectView(R.id.topic_detail_listview)ListView listView;

    int currentPage = 1;
    TopicDetailListAdapter adapter;

    Long topicId;
    String title;
    String content;
    String name;
    String avatar;
    String createTime;

    View headView;
    HeadViewHolder headViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.topic_detail_activity);

        ButterKnife.inject(this);

        initTopicHeadView();


        listView.addHeaderView(headView);

        adapter = new TopicDetailListAdapter(this);
        listView.setAdapter(adapter);

        requestReplyList();


        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
    }

    private void initTopicHeadView()
    {
        headView = LayoutInflater.from(this).inflate(R.layout.topic_detail_head_view, null);

        headViewHolder = new HeadViewHolder(headView);

        Intent intent = this.getIntent();

        topicId = intent.getLongExtra("topicId", 0);
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        name = intent.getStringExtra("name");
        createTime = intent.getStringExtra("createTime");
        avatar = intent.getStringExtra("avatar");


        headViewHolder.titleTextView.setText(title);
        headViewHolder.nameTextView.setText(name);
        String timeAgo = DateTimeFormatter.getTimeAgo(this, createTime);
        headViewHolder.timeTextView.setText(timeAgo);

        String url = StaticResourceConfig.IMG_DOMAIN + avatar + "!androidListAvatar";
        ImageLoader.getInstance().displayImage(url, headViewHolder.avatarImageView);

        List<AnalyzeContent.ContentFragment> contentFragmentList = new LinkedList<AnalyzeContent.ContentFragment>();
        contentFragmentList = AnalyzeContent.analyzeContent(content);

        for(int n=0; n != contentFragmentList.size() ; n++ )
        {
            if(contentFragmentList.get(n).mType == AnalyzeContent.ContentFragment.IMG_URL_TYPE)
            {
                ImageView imageviwe = new ImageView(this);
                imageviwe.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageviwe.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                headViewHolder.contentLayout.addView(imageviwe);

                ImageLoader.getInstance().displayImage(contentFragmentList.get(n).mText, imageviwe);
            }
            else if(contentFragmentList.get(n).mType == AnalyzeContent.ContentFragment.TEXT_TYPE)
            {
                TextView textView = new TextView(this);
                textView.setTextSize(20);
                textView.setMovementMethod(LinkMovementMethod.getInstance());

                headViewHolder.contentLayout.addView(textView);
/*								String emoji = "\ue32d \ue32d \ue32d";
								String content = "ÎÒÊÇÄÚÈÝ hello";*/
                String temp = contentFragmentList.get(n).mText ;
                textView.setText(Html.fromHtml(temp));
            }

        }
    }


    private void requestReplyList()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.reply + "?topicId="+ topicId + "&page=" + currentPage ;
        MyLog.d(tag, "request=" + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        MyLog.d(tag,"response=" + response);

                        Gson gson = new Gson();

                        MyResponse<MyPage<ReplyDTO>> myResponse = gson.fromJson(response,new TypeToken<MyResponse<MyPage<ReplyDTO>>>(){}.getType());


                        if(myResponse.getStatus() == ServerMethod.status_ok)
                        {
                            MyPage page = myResponse.getContent();
                            List<ReplyDTO> topicDTOs = page.getContents();
                            adapter.addNextPageData(topicDTOs);

                            currentPage++;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(stringRequest);
    }

    public class HeadViewHolder
    {
        @InjectView(R.id.topic_detail_title)TextView titleTextView;
        @InjectView(R.id.user_head_name)TextView nameTextView;
        @InjectView(R.id.user_head_time)TextView timeTextView;
        @InjectView(R.id.user_head_avatar)ImageView avatarImageView;
        @InjectView(R.id.topic_detail_content_container)LinearLayout contentLayout;

        public HeadViewHolder(View headView)
        {
            ButterKnife.inject(this, headView);
        }
    }
}
