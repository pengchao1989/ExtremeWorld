package com.jixianxueyuan.activity.admin;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.BaseActivity;
import com.jixianxueyuan.adapter.AdminTopicListAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.MyErrorHelper;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.AutoLoadMoreView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pengchao on 17-3-11.
 */

public class AdminTopicListActivity extends BaseActivity {

    private static final String tag = AdminTopicListActivity.class.getSimpleName();

    @BindView(R.id.admin_top_list_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.admin_topic_list_view)
    ListView listView;

    private AutoLoadMoreView autoLoadMoreView;
    private int currentPage = 0;
    private int totalPage = 0;

    AdminTopicListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_topic_list_activity);
        ButterKnife.bind(this);

        adapter = new AdminTopicListAdapter(this);
        listView.setAdapter(adapter);
        initFooterView();
        initListener();

        refreshTopicList();
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
        autoLoadMoreView = new AutoLoadMoreView(this);
        listView.addFooterView(autoLoadMoreView);
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

        swipeRefreshLayout.setRefreshing(true);

        String url = ServerMethod.topic() + "?page=" + (currentPage + 1);



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


                            totalPage = page.getTotalPages();
                            currentPage = page.getCurPage() + 1;
                            doHideFootView();
                            swipeRefreshLayout.setRefreshing(false);

                        }else {
                            MyErrorHelper.showErrorToast(AdminTopicListActivity.this, response.getError());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        MyVolleyErrorHelper.showError(AdminTopicListActivity.this, error);
                    }
                });

        MyApplication.getContext().getRequestQueue().add(myPageRequest);
    }

}
