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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.TopicDetailActivity;
import com.jixianxueyuan.adapter.TopicListAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.MyErrorHelper;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.AutoLoadMoreView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 12/4/15.
 */
public class SimpleTopicListFragment extends Fragment {

    public static final String tag = SimpleTopicListFragment.class.getSimpleName();
    public static final String INTENT_IS_FIRST = "INTENT_IS_FIRST";
    public static final String INTENT_IS_FINE = "INTENT_IS_FINE";

    @InjectView(R.id.simple_top_list_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.simple_topic_list_view)
    ListView listView;

    /*以下两个参数用于定义topic范围，从arg传递过来*/
    private String topicType = TopicType.ALL;
    private Long topicTaxonomyId;

    private AutoLoadMoreView autoLoadMoreView;
    private int currentPage = 0;
    private int totalPage = 0;
    private boolean isFine = false;

    TopicListAdapter adapter;
    boolean isFirst = false;
    boolean isInitData = false;
    boolean isInitView = false;
    boolean isRequesting = false;
    boolean isRefreshData = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TopicListAdapter(this.getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.simple_topic_list_fragment, container, false);
        ButterKnife.inject(this, view);

        swipeRefreshLayout.setColorSchemeResources(R.color.primary);

        initBundle();
        initFooterView();
        listView.setAdapter(adapter);

        initListener();

        isInitView = true;
        return view;
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if (bundle.containsKey(TopicType.TYPE)) {
            topicType = bundle.getString(TopicType.TYPE);
        }
        if (bundle.containsKey(TopicType.TOPIC_TAXONOMY_ID)) {
            topicTaxonomyId = bundle.getLong(TopicType.TOPIC_TAXONOMY_ID);
        }
        if(bundle.containsKey(INTENT_IS_FINE)){
            isFine = bundle.getBoolean(INTENT_IS_FINE);
        }
        if(bundle.containsKey(INTENT_IS_FIRST)){
            isFirst = bundle.getBoolean(INTENT_IS_FIRST);
        }
    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshTopicList();
            }
        });

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
    }

    private void initFooterView(){
        autoLoadMoreView = new AutoLoadMoreView(this.getActivity());
        listView.addFooterView(autoLoadMoreView);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isRefreshData && isFirst) {
            refreshTopicList();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser && isInitView && !isFirst){
            if(!isInitData){
                isInitData = true;
                MyLog.d(tag, "setUserVisibleHint");
                refreshTopicList();
            }
        }
    }

    @OnItemClick(R.id.simple_topic_list_view)
    void onItemClicked(int position) {

        if (position >= adapter.getCount()) {
            return;
        }

        TopicDTO topicDTO = adapter.getItem(position);

        Intent intent = null;
        switch (topicDTO.getType()) {
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
            default:
                Toast.makeText(this.getContext(),R.string.app_version_is_low, Toast.LENGTH_SHORT).show();

        }

        if (intent != null) {
            intent.putExtra(TopicDetailActivity.INTENT_TOPIC, topicDTO);
            startActivity(intent);
        }
    }


    private void refreshTopicList() {
        currentPage = 0;

        requestTopicList();
    }

    private void getNextPage() {
        if (currentPage < totalPage) {
            requestTopicList();
        }
    }

    private void doHideFootView() {
        if ((totalPage > 0 && currentPage >= totalPage) || totalPage == 0 ) {
            autoLoadMoreView.setOver();
        }else {
            autoLoadMoreView.reset();
        }
    }

    private void requestTopicList() {
        if (isRequesting) {
            return;
        }

        swipeRefreshLayout.setRefreshing(true);

        String url = null;

        if (isFine) {
            url = ServerMethod.topic_fine() + "?page=" + (currentPage + 1);
        } else {
            switch (topicType) {
                case TopicType.ALL:
                    url = ServerMethod.topic() + "?page=" + (currentPage + 1);
                    break;
                case TopicType.DISCUSS:
                case TopicType.NEWS:
                case TopicType.VIDEO:
                case TopicType.S_VIDEO:
                    url = ServerMethod.topic() + "?type=" + topicType + "&taxonomyId=" + topicTaxonomyId + "&page=" + (currentPage + 1);
                    break;
            }
        }


        MyLog.d(tag, "request url=" + url);
        if (url == null) {
            return;
        }

        MyPageRequest<TopicDTO> myPageRequest = new MyPageRequest(Request.Method.GET, url, TopicDTO.class,
                new Response.Listener<MyResponse<MyPage<TopicDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<TopicDTO>> response) {

                        if (response.getStatus() == MyResponse.status_ok) {
                            MyPage page = response.getContent();
                            List<TopicDTO> topicDTOs = page.getContents();
                            if (currentPage == 0) {
                                adapter.refresh(topicDTOs);
                            } else {
                                adapter.addDatas(topicDTOs);
                            }

                            isRefreshData = true;

                            totalPage = page.getTotalPages();
                            currentPage = page.getCurPage() + 1;
                            doHideFootView();
                            swipeRefreshLayout.setRefreshing(false);

                        }else {
                            MyErrorHelper.showErrorToast(SimpleTopicListFragment.this.getActivity(), response.getError());
                        }
                        isRequesting = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isRequesting = false;
                        swipeRefreshLayout.setRefreshing(false);
                        MyVolleyErrorHelper.showError(SimpleTopicListFragment.this.getActivity(), error);
                    }
                });

        isRequesting = true;
        MyApplication.getContext().getRequestQueue().add(myPageRequest);
    }

}
