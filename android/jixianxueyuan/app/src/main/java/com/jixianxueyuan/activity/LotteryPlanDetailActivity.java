package com.jixianxueyuan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.LuckyFactorListAdapter;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.lottery.LotteryPlanDTO;
import com.jixianxueyuan.dto.lottery.LotteryPlanDetailOfUserDTO;
import com.jixianxueyuan.dto.lottery.LuckyFactorDTO;
import com.jixianxueyuan.dto.lottery.PrizeAllocationDTO;
import com.jixianxueyuan.dto.lottery.WinningRecordDTO;
import com.jixianxueyuan.fragment.MineFragment;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.ACache;
import com.jixianxueyuan.widget.AutoLoadMoreView;
import com.jixianxueyuan.widget.MyActionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pengchao on 17-5-2.
 */

public class LotteryPlanDetailActivity extends BaseActivity {

    public static final String LOTTERY_PLAN = "lottery_plan";

    @BindView(R.id.lottery_plan_detail_actionbar)
    MyActionBar myActionBar;
    @BindView(R.id.lucky_factor_list_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.lucky_factor_listview)
    ListView listView;

    private LotteryPlanDTO lotteryPlanDTO;
    private LotteryPlanDetailOfUserDTO lotteryPlanDetailOfUserDTO;

    private AutoLoadMoreView autoLoadMoreView;
    private int currentPage = 0;
    private int totalPage = 0;

    private LuckyFactorListAdapter luckyFactorListAdapter;

    private HeaderViewHolder headerViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottery_plan_detail_activity);
        ButterKnife.bind(this);

        init();
    }

    private void init(){

        lotteryPlanDTO = (LotteryPlanDTO) getIntent().getSerializableExtra(LOTTERY_PLAN);
        if (lotteryPlanDTO == null){
            return;
        }

        myActionBar.setTitle(lotteryPlanDTO.getTitle());

        initHeaderView();
        initFooterView();

        luckyFactorListAdapter = new LuckyFactorListAdapter(this);
        listView.setAdapter(luckyFactorListAdapter);

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
                requestLotteryOfMe();
            }
        });

        requestLotteryOfMe();
    }

    private void initHeaderView(){
        View headerView = LayoutInflater.from(this).inflate(R.layout.lottery_plan_detail_header_view, null);
        headerViewHolder = new HeaderViewHolder(headerView);
        listView.addHeaderView(headerView);
    }

    private void initFooterView(){
        autoLoadMoreView = new AutoLoadMoreView(this);
        listView.addFooterView(autoLoadMoreView);
    }

    private void refreshHeaderView(LotteryPlanDetailOfUserDTO lotteryPlanDetailOfUserDTO){

        LotteryPlanDTO lotteryPlanDTO = lotteryPlanDetailOfUserDTO.getLotteryPlan();
        if (lotteryPlanDTO != null){
            headerViewHolder.titleTextView.setText(lotteryPlanDTO.getTitle());
            headerViewHolder.desTextView.setText(lotteryPlanDTO.getDes());
            headerViewHolder.spnosorTextView.setText(lotteryPlanDTO.getSponsor());
            headerViewHolder.sponsorDesTextView.setText(lotteryPlanDTO.getSponsorDes());
        }

        //奖品列表
        List<PrizeAllocationDTO> prizeAllocationDTOList = lotteryPlanDetailOfUserDTO.getPrizeAllocationList();
        if (prizeAllocationDTOList != null){
            StringBuilder prizeStringBuilder = new StringBuilder();
            for (PrizeAllocationDTO prizeAllocationDTO : prizeAllocationDTOList){
                prizeStringBuilder.append(prizeAllocationDTO.getPrize().getName());
                prizeStringBuilder.append("x1 、 ");
            }
            String prizeString = prizeStringBuilder.toString();

            if (prizeString.length() > 0){
                prizeString = prizeString.substring(0, prizeString.length() - 2);
            }

            headerViewHolder.prizeTextView.setText(prizeString);
        }

        headerViewHolder.luckyCountTextView.setText(String.valueOf(lotteryPlanDetailOfUserDTO.getTotalLuckyFactorCount()));
        headerViewHolder.myLuckyCountTextView.setText(String.valueOf(lotteryPlanDetailOfUserDTO.getUserLuckyFactorCount()));
        headerViewHolder.myWinningTextView.setText(String.format("%.4f", lotteryPlanDetailOfUserDTO.getUserWinningProbability() * 100) + "%");

        //获奖记录
        List<WinningRecordDTO> winningRecordDTOList = lotteryPlanDetailOfUserDTO.getWinningRecordList();
        if (winningRecordDTOList != null && winningRecordDTOList.size() > 0){
            StringBuilder winningRecordStringBuilder = new StringBuilder();
            for (WinningRecordDTO winningRecordDTO : winningRecordDTOList){
                winningRecordStringBuilder.append(winningRecordDTO.getUser().getName());
                winningRecordStringBuilder.append(" 获得 ");
                winningRecordStringBuilder.append(winningRecordDTO.getPrizeAllocation().getPrize().getName());
                winningRecordStringBuilder.append("\n");
            }

            headerViewHolder.winningRecordTextView.setText(winningRecordStringBuilder.toString());
        }else {
            headerViewHolder.winningRecordTextView.setText("暂未开奖");
        }

    }

    private void refresh(){
        currentPage = 0;
        requestLuckyFactorPlanList();
    }

    private void getNextPage(){
        if (currentPage < totalPage) {
            requestLuckyFactorPlanList();
        }
    }

    private void doHideFootView() {
        if (totalPage > 0 && currentPage >= totalPage) {
            autoLoadMoreView.setOver();
        }else {
            autoLoadMoreView.reset();
        }
    }

    private void requestLuckyFactorPlanList(){
        String url = ServerMethod.lucky_factor() + "?&page=" + (currentPage + 1) + "&lotteryPlanId=" + lotteryPlanDTO.getId();

        MyPageRequest<LuckyFactorDTO> myPageRequest = new MyPageRequest<LuckyFactorDTO>(Request.Method.GET, url, LuckyFactorDTO.class,
                new Response.Listener<MyResponse<MyPage<LuckyFactorDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<LuckyFactorDTO>> response) {

                        MyPage page = response.getContent();
                        if (currentPage == 0){
                            luckyFactorListAdapter.setDatas(page.getContents());
                        }else {
                            luckyFactorListAdapter.addDatas(page.getContents());
                        }

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

    private void requestLotteryOfMe(){
        String url = ServerMethod.lottery_plan_detail_of_user() + lotteryPlanDTO.getId();
        MyRequest<LotteryPlanDetailOfUserDTO> myRequest = new MyRequest<LotteryPlanDetailOfUserDTO>(Request.Method.GET, url, LotteryPlanDetailOfUserDTO.class, null, new Response.Listener<MyResponse<LotteryPlanDetailOfUserDTO>>() {
            @Override
            public void onResponse(MyResponse<LotteryPlanDetailOfUserDTO> response) {
                lotteryPlanDetailOfUserDTO = response.getContent();
                refreshHeaderView(lotteryPlanDetailOfUserDTO);
                refresh();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getContext().getRequestQueue().add(myRequest);
    }

    public static class HeaderViewHolder{


        @BindView(R.id.lottery_plan_detail_title)TextView titleTextView;
        @BindView(R.id.lottery_plan_detail_total_lucky_count)TextView luckyCountTextView;
        @BindView(R.id.lottery_plan_detail_my_lucky_factor_count)TextView myLuckyCountTextView;
        @BindView(R.id.lottery_plan_detail_my_winning_rate)TextView myWinningTextView;
        @BindView(R.id.lottery_plan_detail_des)TextView desTextView;
        @BindView(R.id.lottery_plan_detail_prize)TextView prizeTextView;
        @BindView(R.id.lottery_plan_detail_sponsor)TextView spnosorTextView;
        @BindView(R.id.lottery_plan_detail_sponsor_des)TextView sponsorDesTextView;
        @BindView(R.id.lottery_plan_detail_winning_record)TextView winningRecordTextView;

        public HeaderViewHolder(View headView)
        {
            ButterKnife.bind(this, headView);
        }
    }
    public static void startActivity(Context context, LotteryPlanDTO lotteryPlanDTO){
        Intent intent = new Intent(context, LotteryPlanDetailActivity.class);
        intent.putExtra(LOTTERY_PLAN, lotteryPlanDTO);
        context.startActivity(intent);
    }
}
