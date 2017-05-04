package com.jixianxueyuan.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.LotteryPlanListAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.config.lotteryPlan.LotteryPlanStatus;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.lottery.LotteryPlanDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.widget.AutoLoadMoreView;
import com.jixianxueyuan.widget.MyActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 17-4-24.
 抽奖首页
 */

public class LotteryPlanHomeActivity extends BaseActivity {

    @BindView(R.id.lottery_plan_home_actionbar)
    MyActionBar myActionBar;
    @BindView(R.id.lottery_plan_list_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.lottery_plan_listview)
    ListView listView;

    LotteryPlanListAdapter lotteryPlanListAdapter;

    private AutoLoadMoreView autoLoadMoreView;
    private int currentPage = 0;
    private int totalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottery_plan_home_activity);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        lotteryPlanListAdapter = new LotteryPlanListAdapter(this);
        listView.setAdapter(lotteryPlanListAdapter);

        myActionBar.setTitle(getString(R.string.lucky_saturday));
        initFooterView();

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
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWaitingLotteryList();
            }
        });

        requestWaitingLotteryList();
    }

    @OnItemClick(R.id.lottery_plan_listview)void onItemClick(int position){
        LotteryPlanDTO lotteryPlanDTO = lotteryPlanListAdapter.getItem(position);
        LotteryPlanDetailActivity.startActivity(this, lotteryPlanDTO);
    }
    private void initFooterView(){
        autoLoadMoreView = new AutoLoadMoreView(this);
        listView.addFooterView(autoLoadMoreView);
    }

    private void refresh(){
        currentPage = 0;
        requestLotteryPlanList();
    }

    private void getNextPage(){
        if (currentPage < totalPage) {
            requestLotteryPlanList();
        }
    }

    private void doHideFootView() {
        if (totalPage > 0 && currentPage >= totalPage) {
            autoLoadMoreView.setOver();
        }else {
            autoLoadMoreView.reset();
        }
    }

    private void requestWaitingLotteryList(){
        String url = ServerMethod.lottery_plan() + "?page.size=20&page=" + 1 + "&status=" + LotteryPlanStatus.WAITING_LOTTERY;
        MyPageRequest<LotteryPlanDTO> myPageRequest = new MyPageRequest<LotteryPlanDTO>(Request.Method.GET, url, LotteryPlanDTO.class,
                new Response.Listener<MyResponse<MyPage<LotteryPlanDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<LotteryPlanDTO>> response) {
                        MyPage page = response.getContent();
                        lotteryPlanListAdapter.setDatas(page.getContents());
                        refresh();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getContext().getRequestQueue().add(myPageRequest);
    }

    private void requestLotteryPlanList(){
        String url = ServerMethod.lottery_plan() + "?page.size=20&page=" + (currentPage + 1) + "&status=" + LotteryPlanStatus.COMPLETED;
        MyPageRequest<LotteryPlanDTO> myPageRequest = new MyPageRequest<LotteryPlanDTO>(Request.Method.GET, url, LotteryPlanDTO.class,
                new Response.Listener<MyResponse<MyPage<LotteryPlanDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<LotteryPlanDTO>> response) {

                        MyPage page = response.getContent();
                        lotteryPlanListAdapter.addDatas(page.getContents());
                        totalPage = page.getTotalPages();
                        currentPage = page.getCurPage() + 1;

                        doHideFootView();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getContext().getRequestQueue().add(myPageRequest);
    }

}
