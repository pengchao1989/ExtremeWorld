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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.CreateTopicActivity;
import com.jixianxueyuan.activity.TopicDetailActivity;
import com.jixianxueyuan.adapter.TopicListAdapter;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.LoadMoreView;

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
    @InjectView(R.id.top_list_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private ImageView switchButton;

    /*以下两个参数用于定义topic范围，从arg传递过来*/
    private String topicType = TopicType.ALL;
    private Long topicTaxonomyId;

    private LoadMoreView loadMoreView;
    private int currentPage = 0;
    private int totalPage = 0;
    private boolean isFine = false;


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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        Log.d(tag,"onCreateView");

        View view = inflater.inflate(R.layout.topic_list_fragment, container, false);

        ButterKnife.inject(this,view);

        Bundle bundle = getArguments();
        if(bundle.containsKey(TopicType.TYPE))
        {
            topicType = bundle.getString(TopicType.TYPE);
        }
        if(bundle.containsKey("topicTaxonomyId"))
        {
            topicTaxonomyId = bundle.getLong("topicTaxonomyId");
        }

        //head view
        View headView = LayoutInflater.from(this.getActivity()).inflate(R.layout.topic_list_head_view, null);
        switchButton = (ImageView) headView.findViewById(R.id.topic_list_head_switch);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFine();
            }
        });
        listView.addHeaderView(headView);

        loadMoreView = new LoadMoreView(this.getActivity());
        if(isRefreshData){
            loadMoreView.setVisibility(View.VISIBLE);
        }else{
            loadMoreView.setVisibility(View.GONE);
        }
        loadMoreView.setLoadMoreViewListener(new LoadMoreView.LoadMoreViewListener() {
            @Override
            public void runLoad() {
                getNextPage();
            }
        });
        listView.addFooterView(loadMoreView);

        listView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeResources(R.color.primary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshTopicList();
            }
        });

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

    @OnItemClick(R.id.topic_list_fragment_listview) void onItemClicked(int position)
    {
        if (position == 0){
            return;
        }
        TopicDTO topicDTO = adapter.getItem(position - 1);

        Intent intent = null;
        switch (topicDTO.getType())
        {
            case TopicType.MOOD:
            case TopicType.NEWS:
            case TopicType.COURSE:
                intent = new Intent(this.getActivity(), TopicDetailActivity.class);
                intent.putExtra(TopicType.TYPE, TopicType.MOOD);
                break;
            case TopicType.DISCUSS:
                intent = new Intent(this.getActivity(), TopicDetailActivity.class);
                intent.putExtra(TopicType.TYPE, TopicType.DISCUSS);
                break;
            case TopicType.VIDEO:
            case TopicType.S_VIDEO:
                intent = new Intent(this.getActivity(), TopicDetailActivity.class);
                intent.putExtra(TopicType.TYPE, TopicType.S_VIDEO);
                break;

        }

        if(intent != null)
        {
            intent.putExtra(TopicDetailActivity.INTENT_TOPIC, topicDTO);
            startActivity(intent);
        }
    }

    private void switchFine(){
        isFine = !isFine;
        if(isFine){
            switchButton.setImageResource(R.mipmap.ic_option);
        }else {
            switchButton.setImageResource(R.mipmap.ic_option_2);
        }
        refreshTopicList();
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
            Toast.makeText(this.getActivity(), R.string.not_more, Toast.LENGTH_SHORT).show();
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
        swipeRefreshLayout.setRefreshing(true);

        RequestQueue queue = Volley.newRequestQueue(this.getActivity());

        String url = null;

        if(isFine){
            url = ServerMethod.topic_fine() + "?page=" + (currentPage + 1);
        }else {
            switch (topicType)
            {
                case TopicType.ALL:
                    url = ServerMethod.topic() + "?page=" + (currentPage + 1);
                    break;
                case TopicType.DISCUSS:
                case TopicType.NEWS:
                    url = ServerMethod.topic() + "?type=" + topicType +  "&taxonomyId=" + topicTaxonomyId + "&page=" + (currentPage + 1);
                    break;
                case TopicType.S_VIDEO:
                    url = ServerMethod.topic() + "?type=" + topicType +  "&taxonomyId=" + topicTaxonomyId + "&page=" + (currentPage + 1);
                    break;
            }
        }


        MyLog.d(tag, "request url=" + url);
        if(url == null)
        {
            return;
        }

        MyPageRequest<TopicDTO> myPageRequest = new MyPageRequest(Request.Method.GET,url,TopicDTO.class,
                new Response.Listener<MyResponse<MyPage<TopicDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<TopicDTO>> response) {

                        if(response.getStatus() == MyResponse.status_ok)
                        {
                            MyPage page = response.getContent();
                            List<TopicDTO> topicDTOs = page.getContents();
                            if(currentPage == 0)
                            {
                                adapter.refresh(topicDTOs);
                            }
                            else
                            {
                                adapter.addDatas(topicDTOs);
                            }

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
                        //Toast.makeText(TopicListFragment.this.getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                        MyVolleyErrorHelper.showError(TopicListFragment.this.getActivity(), error);
                    }
                });

        queue.add(myPageRequest);
    }
}
