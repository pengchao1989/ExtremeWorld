package com.jixianxueyuan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.jixianxueyuan.activity.TopicDetailActivity;
import com.jixianxueyuan.adapter.TopicListAdapter;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.record.ui.record.MediaRecorderActivity;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.LoadMoreView;
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
    @InjectView(R.id.top_list_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    /*以下两个参数用于定义topic范围，从arg传递过来*/
    String topicType;
    String topicTaxonomyId;

    LoadMoreView loadMoreView;
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

        ButterKnife.inject(this,view);

        Bundle bundle = getArguments();
        if(bundle.containsKey("topicType"))
        {
            topicType = bundle.getString("topicType");
        }
        if(bundle.containsKey("topicTaxonomyId"))
        {
            topicTaxonomyId = bundle.getString("topicTaxonomyId");
        }


        loadMoreView = new LoadMoreView(this.getActivity());
        loadMoreView.setLoadMoreViewListener(new LoadMoreView.LoadMoreViewListener() {
            @Override
            public void runLoad() {
                getNextPage();
            }
        });
        listView.addFooterView(loadMoreView);

        listView.setAdapter(adapter);

        floatingActionButton.attachToListView(listView);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                floatingActionButton.hide();
                showAddLayout();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshTopicList();
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
                intent.setAction(TopicType.MOOD);
                break;
            case "discuss":
                intent = new Intent(this.getActivity(), TopicDetailActivity.class);
                intent.setAction(TopicType.MOOD);
                break;
            case "video":
            case "s_video":
                intent = new Intent(this.getActivity(), TopicDetailActivity.class);
                intent.setAction(TopicType.VIDEO);
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
        intent.setAction(TopicType.MOOD);
        startActivity(intent);
    }
    @OnClick(R.id.topic_list_fragment_add_mood)void onCreateMood()
    {

    }
    @OnClick(R.id.topic_list_fragment_add_short_video)void onCreateShortVideo()
    {
        Intent intent = new Intent(this.getActivity(), MediaRecorderActivity.class);
        startActivity(intent);
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
            if(loadMoreView.isLoading() == true)
            {
                loadMoreView.onFinish();
            }

            if(currentPage >= totalPage)
            {
                loadMoreView.setOver();
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
                            swipeRefreshLayout.setRefreshing(false);

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
