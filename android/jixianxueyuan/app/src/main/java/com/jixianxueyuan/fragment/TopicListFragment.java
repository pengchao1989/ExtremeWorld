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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.DiscussionDetailActivity;
import com.jixianxueyuan.activity.MoodDetailActivity;
import com.jixianxueyuan.activity.VideoDetailActivity;
import com.jixianxueyuan.adapter.TopicListAdapter;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.server.ServerMethod;
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

    public static final String TAG = TopicListFragment.class.getSimpleName();

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

        Log.d("TopicListFragment","onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        Log.d("TopicListFragment","onCreateView");

        View view = inflater.inflate(R.layout.topic_list_fragment, container, false);

        ButterKnife.inject(this,view);

        listView.setAdapter(adapter);


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
                intent = new Intent(this.getActivity(), MoodDetailActivity.class);
                intent.putExtra("topicId", topicDTO.getId());
                break;
            case "discuss":
                intent = new Intent(this.getActivity(), DiscussionDetailActivity.class);
                intent.putExtra("topicId", topicDTO.getId());
                break;
            case "video":
                intent = new Intent(this.getActivity(), VideoDetailActivity.class);
                intent.putExtra("videoId",topicDTO.getId());
                break;
        }

        if(intent != null)
        {
            startActivity(intent);
        }
    }

    private void refreshTopicList()
    {
        requestTopicList();
    }

    private void requestTopicList()
    {

        RequestQueue queue = Volley.newRequestQueue(this.getActivity());
        String url = ServerMethod.topic;

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Gson gson = new Gson();
                        List<TopicDTO> topicDTOs = gson.fromJson(response, new TypeToken<List<TopicDTO>>(){}.getType());
                        if(topicDTOs != null)
                        {
                            adapter.addDatas(topicDTOs);

                            isRefreshData = true;
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
