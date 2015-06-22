package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.dto.request.ReplyRequest;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.server.StaticResourceConfig;
import com.jixianxueyuan.util.AnalyzeContent;
import com.jixianxueyuan.util.DateTimeFormatter;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.ReplyWidget;
import com.jixianxueyuan.widget.ReplyWidgetListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yumfee.emoji.EmojiconEditText;
import com.yumfee.emoji.EmojiconTextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 5/22/15.
 */
public class TopicDetailActivity extends Activity implements ReplyWidgetListener {

    public final static String tag = TopicDetailActivity.class.getSimpleName();

    @InjectView(R.id.topic_detail_listview)ListView listView;
    @InjectView(R.id.reply_widget_layout)LinearLayout contentLayout;

    int currentPage = 0;
    int totalPage = 0;
    TopicDetailListAdapter adapter;

    Long topicId;
    String title;
    String content;
    String name;
    String avatar;
    String createTime;

    View headView;
    HeadViewHolder headViewHolder;
    View footerView;
    Button loadMoreButton;

    ReplyWidget replyWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.topic_detail_activity);

        ButterKnife.inject(this);

        initTopicHeadView();
        initFooterView();

        adapter = new TopicDetailListAdapter(this);
        listView.setAdapter(adapter);

        replyWidget = new ReplyWidget(this, contentLayout);
        replyWidget.setReplyWidgetListener(this);


        requestReplyList();

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

        String url =  avatar + "!androidListAvatar";
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
                EmojiconTextView textView = new EmojiconTextView(this);
                textView.setTextSize(20);
                textView.setEmojiconSize(48);
                textView.setMovementMethod(LinkMovementMethod.getInstance());

                headViewHolder.contentLayout.addView(textView);
                String temp = contentFragmentList.get(n).mText ;
                textView.setText(Html.fromHtml(temp));
            }

        }

        listView.addHeaderView(headView);
    }

    private void doHideFootView()
    {
        if(totalPage > 1)
        {
            if(footerView.getVisibility() != View.VISIBLE)
            {
                footerView.setVisibility(View.VISIBLE);
            }

            if(currentPage >= totalPage)
            {
                loadMoreButton.setText(R.string.not_more, TextView.BufferType.NORMAL);
            }
        }
    }

    private void initFooterView()
    {
        footerView = LayoutInflater.from(this).inflate(R.layout.loadmore, null, false);

        loadMoreButton = (Button) footerView.findViewById(R.id.loadmore_button);
        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNextPage();
            }
        });
        listView.addFooterView(footerView);
    }

    private void getNextPage()
    {
        if(currentPage < totalPage)
        {
            requestReplyList();
        }
        else
        {
            Toast.makeText(this,"没了", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isLastPage()
    {
        if(currentPage == totalPage)
        {
            return true;
        }

        return false;
    }

    private void requestReplyList()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.reply + "?topicId="+ topicId + "&page=" + (currentPage + 1) ;
        MyLog.d(tag, "request=" + url);

        MyPageRequest<ReplyDTO> stringRequest = new MyPageRequest<ReplyDTO>(Request.Method.GET,url,ReplyDTO.class,
                new Response.Listener<MyResponse<MyPage<ReplyDTO>>>()
                {
                    @Override
                    public void onResponse(MyResponse<MyPage<ReplyDTO>> response) {

                        MyLog.d(tag,"response=" + response);

                        if(response.getStatus() == MyResponse.status_ok)
                        {
                            MyPage<ReplyDTO> page = response.getContent();
                            List<ReplyDTO> topicDTOs = page.getContents();
                            adapter.addNextPageData(topicDTOs);

                            totalPage = page.getTotalPages();
                            currentPage++;

                            doHideFootView();
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

    private void submitReply(String replyContent)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.reply;

        ReplyRequest replyRequest = buildReplyDTO(replyContent);


        MyRequest<ReplyDTO> stringRequest = new MyRequest<ReplyDTO>(Request.Method.POST,url,ReplyDTO.class, replyRequest,
                new Response.Listener<MyResponse<ReplyDTO>>() {
                    @Override
                    public void onResponse(MyResponse<ReplyDTO> response) {
                        MyLog.d(tag, "onResponse=" + response.toString());

                        if(response.getStatus() == MyResponse.status_ok)
                        {
                            //若是在最后一页则 成功后将回复更新到view上
                            if(isLastPage())
                            {
                                ReplyDTO replyDTO = response.getContent();
                                adapter.addNew(replyDTO);
                            }

                            replyWidget.clean();
                            Toast.makeText(TopicDetailActivity.this, R.string.reply_success,Toast.LENGTH_LONG).show();

                            View view = getWindow().peekDecorView();
                            if (view != null) {
                                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyLog.d(tag, "onErrorResponse" + error.toString());
                    }
                });

        queue.add(stringRequest);
    }

    private ReplyRequest buildReplyDTO(String replyString)
    {
        ReplyRequest replyDTO = new ReplyRequest();
        replyDTO.setContent(replyString);

        UserMinDTO userMinDTO = new UserMinDTO();
        userMinDTO.setId(1L);
        replyDTO.setUser(userMinDTO);

        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setId(topicId);
        replyDTO.setTopic(topicDTO);

        return replyDTO;
    }


    @Override
    public void onCommit(String text) {
        submitReply(text);
    }

    public class HeadViewHolder
    {
        @InjectView(R.id.topic_detail_title)EmojiconTextView titleTextView;
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
