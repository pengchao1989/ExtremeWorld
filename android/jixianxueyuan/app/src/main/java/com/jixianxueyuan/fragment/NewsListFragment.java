package com.jixianxueyuan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.TopicDetailActivity;
import com.jixianxueyuan.adapter.NewsListAdapter;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 11/8/15.
 */
public class NewsListFragment extends Fragment {
    public static String tag = NewsListFragment.class.getSimpleName();

    @InjectView(R.id.news_list_fragment_listview)
    ListView listView;
    @InjectView(R.id.news_list_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;


    private final String topicType = TopicType.NEWS;
    private Long topicTaxonomyId;
    private int currentPage = 0;
    private int totalPage = 0;
    private boolean isRefreshData = false;

    private NewsListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NewsListAdapter(this.getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_list_fragment, container, false);

        ButterKnife.inject(this, view);

        Bundle bundle = getArguments();
        if(bundle.containsKey(TopicType.TOPIC_TAXONOMY_ID))
        {
            topicTaxonomyId = bundle.getLong(TopicType.TOPIC_TAXONOMY_ID);
        }

        listView.setAdapter(adapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //滚动到底部
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                        getNextPage();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.primary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTopicList();
            }
        });

        refreshTopicList();

        return view;
    }

    @OnItemClick(R.id.news_list_fragment_listview) void onItemClicked(int position){
        TopicDTO topicDTO = adapter.getItem(position);

        Intent intent = new Intent(this.getActivity(), TopicDetailActivity.class);
        intent.putExtra(TopicType.TYPE, TopicType.NEWS);
        intent.putExtra(TopicDetailActivity.INTENT_TOPIC, topicDTO);
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
            Toast.makeText(this.getActivity(), R.string.not_more, Toast.LENGTH_SHORT).show();
        }

    }

    private void requestTopicList()
    {
        swipeRefreshLayout.setRefreshing(true);

        RequestQueue queue = Volley.newRequestQueue(this.getActivity());

        String url = ServerMethod.topic() + "?type=" + topicType +  "&taxonomyId=" + topicTaxonomyId + "&page=" + (currentPage + 1);


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
                            swipeRefreshLayout.setRefreshing(false);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(TopicListFragment.this.getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                        MyVolleyErrorHelper.showError(NewsListFragment.this.getActivity(), error);
                    }
                });

        queue.add(myPageRequest);
    }

}
