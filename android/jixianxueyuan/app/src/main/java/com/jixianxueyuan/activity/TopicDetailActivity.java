package com.jixianxueyuan.activity;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.server.StaticResourceConfig;
import com.jixianxueyuan.util.AnalyzeContent;
import com.jixianxueyuan.util.DateTimeFormatter;
import com.jixianxueyuan.util.MyLog;
import com.nostra13.universalimageloader.core.ImageLoader;



import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 5/22/15.
 */

public class TopicDetailActivity extends Activity
{

}
/*



public class TopicDetailActivity extends FragmentActivity implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener{

    public final static String tag = TopicDetailActivity.class.getSimpleName();

    @InjectView(R.id.topic_detail_listview)ListView listView;
    @InjectView(R.id.reply_widget_edittext)EmojiconEditText emojiconEditText;
    @InjectView(R.id.emojicons)FrameLayout emojiconsLayout;

    int currentPage = 1;
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

    EmojiconsFragment emojiconsFragment;
    boolean isCurEmoji = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.topic_detail_activity);

        ButterKnife.inject(this);

        initTopicHeadView();
        initFooterView();

        adapter = new TopicDetailListAdapter(this);
        listView.setAdapter(adapter);




        initReplyWidget();

        requestReplyList();

    }

    private void initReplyWidget()
    {
        Button emojiButton = (Button) findViewById(R.id.reply_widget_more_button);
        emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = TopicDetailActivity.this.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                if(!isCurEmoji)
                {
                    emojiconsLayout.setVisibility(View.VISIBLE);

                    if(emojiconsFragment == null)
                    {
                        emojiconsFragment = new EmojiconsFragment();
                        ft.replace(R.id.emojicons, emojiconsFragment);
                    }
                    else
                    {
                        ft.show(emojiconsFragment);
                    }

                    isCurEmoji = true;
                }
                else
                {
                    ft.hide(emojiconsFragment);
                    emojiconsLayout.setVisibility(View.GONE);
                    isCurEmoji = false;
                }

                ft.commit();

            }
        });


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
*/
/*								String emoji = "\ue32d \ue32d \ue32d";
								String content = "ÎÒÊÇÄÚÈÝ hello";*//*

                String temp = contentFragmentList.get(n).mText ;
                textView.setText(Html.fromHtml(temp));
            }

        }

        listView.addHeaderView(headView);
    }

    private void doHideFootView()
    {
        if(totalPage <= 1)
        {
            footerView.setVisibility(View.GONE);
        }
        else if(currentPage >= totalPage)
        {
            loadMoreButton.setText(R.string.not_more, TextView.BufferType.NORMAL);
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

    @Override
    public void onEmojiconBackspaceClicked(View view) {
        EmojiconsFragment.backspace(emojiconEditText);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(emojiconEditText, emojicon);
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
*/
