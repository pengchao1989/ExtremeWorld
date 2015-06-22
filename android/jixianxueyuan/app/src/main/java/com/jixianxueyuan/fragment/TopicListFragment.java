package com.jixianxueyuan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.jixianxueyuan.activity.CreateMoodActivity;
import com.jixianxueyuan.activity.ShortVideoDetailActivity;
import com.jixianxueyuan.activity.TopicDetailActivity;
import com.jixianxueyuan.adapter.TopicListAdapter;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 2015/4/12.
 */
public class TopicListFragment extends Fragment {

    public static final String tag = TopicListFragment.class.getSimpleName();

    @InjectView(R.id.topic_list_fragment_listview)
    ListView listView;
    @InjectView(R.id.topic_list_fragment_fab)
    FloatingActionButton floatingActionButton;
    @InjectView(R.id.topic_list_fragment_add_layout)
    FrameLayout addLayout;
    @InjectView(R.id.topic_list_fragment_add_button_layout)
    LinearLayout addButtonLayout;
    @InjectView(R.id.topic_list_fragment_add_blank_view)
    View addBlankView;

    View footerView;
    Button loadMoreButton;
    int currentPage = 0;
    int totalPage = 0;

    TopicListAdapter adapter;

    boolean isRefreshData = false;


    private TranslateAnimation showAddPanl;
    private TranslateAnimation hideAddPanl;
    private AlphaAnimation showBlandPanlAnimation;
    private AlphaAnimation hideBlankPanlAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        adapter = new TopicListAdapter(this.getActivity());

        Log.d(tag,"onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        Log.d(tag,"onCreateView");

        View view = inflater.inflate(R.layout.topic_list_fragment, container, false);
        footerView = inflater.inflate(R.layout.loadmore, null,false);

        ButterKnife.inject(this,view);
        //ButterKnife.inject(this, footerView);

        loadMoreButton = (Button) footerView.findViewById(R.id.loadmore_button);
        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNextPage();
            }
        });
        listView.addFooterView(footerView);

        listView.setAdapter(adapter);


        //


        floatingActionButton.attachToListView(listView);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                floatingActionButton.hide();
                showAddLayout();
            }
        });


        initTranslateAnimation();


        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(!isRefreshData)
        {
            refreshTopicList();
        }
    }

    @OnClick(R.id.topic_list_fragment_add_blank_view)void cancelAdd()
    {
        hideAddLayout();
    }

    @OnItemClick(R.id.topic_list_fragment_listview) void onItemClicked(int position)
    {
        TopicDTO topicDTO = (TopicDTO) adapter.getItem(position);

        Intent intent = null;
        switch (topicDTO.getType())
        {
            case "mood":
                intent = new Intent(this.getActivity(), TopicDetailActivity.class);
                break;
            case "discuss":
                intent = new Intent(this.getActivity(), TopicDetailActivity.class);
                break;
            case "video":
                intent = new Intent(this.getActivity(), TopicDetailActivity.class);
                break;
        }

        if(intent != null)
        {
            intent.putExtra("topic", topicDTO);

            if(topicDTO.getVideoDetail() != null)
            {
                if(topicDTO.getVideoDetail().getVideoSource()!= null)
                {
                    intent.putExtra("videoPath", topicDTO.getVideoDetail().getVideoSource());
                }
            }

            MyLog.d(tag, "topicId=" + topicDTO.getId());
            intent.putExtra("topicId", topicDTO.getId());
            intent.putExtra("title", topicDTO.getTitle());
            intent.putExtra("content", topicDTO.getContent());
            intent.putExtra("createTime", topicDTO.getCreateTime());
            intent.putExtra("name", topicDTO.getUser().getName());
            intent.putExtra("avatar", topicDTO.getUser().getAvatar());
            startActivity(intent);
        }
    }

    @OnClick(R.id.topic_list_fragment_add_discuss)void onCreateDiscuss()
    {
        Intent intent = new Intent(this.getActivity(), CreateMoodActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.topic_list_fragment_add_mood)void onCreateMood()
    {

    }
    @OnClick(R.id.topic_list_fragment_add_short_video)void onCreateShortVideo()
    {

    }

    private void refreshTopicList()
    {
        currentPage = 0;

        requestTopicList();
    }

    private void getNextPage()
    {

        if(currentPage < totalPage )
        {
            requestTopicList();
        }
        else
        {
            Toast.makeText(this.getActivity(), "没了", Toast.LENGTH_SHORT).show();
        }

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

    private void requestTopicList()
    {

        RequestQueue queue = Volley.newRequestQueue(this.getActivity());
        String url = ServerMethod.topic + "?page=" + (currentPage + 1);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //MyLog.d(tag, "response=" + response);

                        Gson gson = new Gson();

                        MyResponse<MyPage<TopicDTO>> myResponse = gson.fromJson(response,new TypeToken<MyResponse<MyPage<TopicDTO>>>(){}.getType());


                        if(myResponse.getStatus() == MyResponse.status_ok)
                        {
                            MyPage page = myResponse.getContent();
                            List<TopicDTO> topicDTOs = page.getContents();
                            adapter.addDatas(topicDTOs);

                            isRefreshData = true;

                            totalPage = page.getTotalPages();
                            currentPage = page.getCurPage() + 1;
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

    private void showAddLayout()
    {
        addButtonLayout.startAnimation(showAddPanl);
        addBlankView.startAnimation(showBlandPanlAnimation);
        addButtonLayout.setVisibility(View.VISIBLE);
        addLayout.setVisibility(View.VISIBLE);

    }

    private void hideAddLayout()
    {
        addButtonLayout.startAnimation(hideAddPanl);
        addBlankView.startAnimation(hideBlankPanlAnimation);
    }

    private void initTranslateAnimation()
    {
        hideAddPanl = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_PARENT,  1.0f);
        hideAddPanl.setDuration(200);

        showAddPanl = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,  0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        showAddPanl.setDuration(200);

        showBlandPanlAnimation = new AlphaAnimation(0, 1);
        showBlandPanlAnimation.setDuration(200);


        hideBlankPanlAnimation = new AlphaAnimation(1,0);
        hideBlankPanlAnimation.setDuration(200);
        hideBlankPanlAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                addButtonLayout.setVisibility(View.GONE);
                addLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}
