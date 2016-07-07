package com.jixianxueyuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.RankingListAdapter;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.UserScoreDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.AutoLoadMoreView;
import com.jixianxueyuan.widget.MyActionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 7/7/16.
 */
public class RankingListActivity extends BaseActivity {

    @BindView(R.id.remind_list_actionbar)
    MyActionBar remindListActionbar;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;

    private AutoLoadMoreView autoLoadMoreView;

    private RankingListAdapter rankingListAdapter;

    int currentPage = 0;
    int totalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_list_activity);
        ButterKnife.bind(this);

        rankingListAdapter = new RankingListAdapter(this);
        listview.setAdapter(rankingListAdapter);

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        initFootView();
        swipereFreshLayout.setColorSchemeResources(R.color.primary);
        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshRankingList();
            }
        });


        refreshRankingList();
    }

    private void initFootView(){
        autoLoadMoreView = new AutoLoadMoreView(this);
        listview.addFooterView(autoLoadMoreView);
    }

    private void doHideFootView()
    {
        if ((totalPage > 0 && currentPage >= totalPage) || totalPage == 0) {
            autoLoadMoreView.setOver();
        }else {
            autoLoadMoreView.reset();
        }
    }

    private void refreshRankingList()
    {
        currentPage = 0;

        requestRanklingList();
    }

    private void getNextPage(){
        if(currentPage < totalPage ){
            requestRanklingList();
        }else {
            Toast.makeText(this, R.string.not_more, Toast.LENGTH_SHORT).show();
        }
    }

    private void requestRanklingList(){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.ranking_list() + "?page=" + (currentPage + 1);
        MyLog.d("RemindListActivity", "request url=" + url);

        MyPageRequest<UserScoreDTO> myPageRequest = new MyPageRequest<UserScoreDTO>(Request.Method.GET, url, UserScoreDTO.class,
                new Response.Listener<MyResponse<MyPage<UserScoreDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<UserScoreDTO>> response) {

                        if(response.getStatus() == MyResponse.status_ok){

                            MyLog.d("RemindListActivity","onResponse");
                            MyPage myPage = response.getContent();
                            List<UserScoreDTO> userScoreList = myPage.getContents();
                            if(currentPage == 0){
                                rankingListAdapter.setData(userScoreList);
                            }else {
                                rankingListAdapter.addData(userScoreList);
                            }


                            totalPage = myPage.getTotalPages();
                            currentPage = myPage.getCurPage() + 1;

                            doHideFootView();
                            swipereFreshLayout.setRefreshing(false);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyVolleyErrorHelper.showError(RankingListActivity.this, error);
            }
        });

        queue.add(myPageRequest);
    }

    @OnItemClick(R.id.listview)void onItemClick(int position){
        UserScoreDTO userScoreDTO = rankingListAdapter.getItem(position);
        if (userScoreDTO != null){
            Intent intent = new Intent(RankingListActivity.this, UserHomeActivity.class);
            intent.putExtra(UserHomeActivity.INTENT_USER_MIN, userScoreDTO.getUser());
            startActivity(intent);
        }
    }
}
