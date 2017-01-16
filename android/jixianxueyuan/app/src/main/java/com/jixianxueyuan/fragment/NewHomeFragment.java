package com.jixianxueyuan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.constants.AlibcConstants;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.AlibcTaokeParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.google.gson.Gson;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.InviteWebActivity;
import com.jixianxueyuan.activity.NearFriendActivity;
import com.jixianxueyuan.activity.RankingListActivity;
import com.jixianxueyuan.activity.SiteListActivity;
import com.jixianxueyuan.activity.SponsorshipActivity;
import com.jixianxueyuan.activity.TopicDetailActivity;
import com.jixianxueyuan.activity.WebActivity;
import com.jixianxueyuan.adapter.TopicListAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.MyErrorHelper;
import com.jixianxueyuan.config.ExhibitionAction;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.dto.ExhibitionDTO;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.ACache;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.AutoLoadMoreView;
import com.jixianxueyuan.widget.ExhibitionItemHolderView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 12/4/15.
 */
public class NewHomeFragment extends Fragment {

    public static final String tag = NewHomeFragment.class.getSimpleName();
    public static final String INTENT_IS_FIRST = "INTENT_IS_FIRST";
    public static final String INTENT_IS_FINE = "INTENT_IS_FINE";

    @BindView(R.id.simple_top_list_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.simple_topic_list_view)
    ListView listView;

    ConvenientBanner mConvenientBanner;

    private List<ExhibitionDTO> exhibitionDTOList;

    /*以下两个参数用于定义topic范围，从arg传递过来*/
    private String topicType = TopicType.ALL;
    private Long topicTaxonomyId;

    private AutoLoadMoreView autoLoadMoreView;
    private int currentPage = 0;
    private int totalPage = 0;
    private boolean isFine = false;

    TopicListAdapter adapter;
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
        ButterKnife.bind(this, view);

        swipeRefreshLayout.setColorSchemeResources(R.color.primary);

        initBundle();
        initHeaderView();
        initFooterView();
        listView.setAdapter(adapter);

        initListener();
        initExhibitionList();
        refreshTopicList();

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

        mConvenientBanner.setOnItemClickListener(new com.bigkoo.convenientbanner.listener.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startExhibitionDetail(position);
            }
        });
    }

    private void initHeaderView(){
        View headerView = LayoutInflater.from(this.getActivity()).inflate(R.layout.new_home_header, null);
        mConvenientBanner = (ConvenientBanner) headerView.findViewById(R.id.convenientBanner);
        listView.addHeaderView(headerView);

        headerView.findViewById(R.id.home_header_rank_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewHomeFragment.this.getContext(), RankingListActivity.class);
                NewHomeFragment.this.getActivity().startActivity(intent);
            }
        });

        headerView.findViewById(R.id.home_header_near_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewHomeFragment.this.getContext(), NearFriendActivity.class);
                NewHomeFragment.this.getActivity().startActivity(intent);
            }
        });

        headerView.findViewById(R.id.home_header_site).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewHomeFragment.this.getContext(), SiteListActivity.class);
                NewHomeFragment.this.getActivity().startActivity(intent);
            }
        });

        headerView.findViewById(R.id.home_header_sponsorship).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewHomeFragment.this.getContext(), SponsorshipActivity.class);
                NewHomeFragment.this.getActivity().startActivity(intent);
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
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

    }

    @OnItemClick(R.id.simple_topic_list_view)
    void onItemClicked(int position) {

        if (position >= adapter.getCount() || position == 0) {
            return;
        }

        TopicDTO topicDTO = adapter.getItem(position - 1);

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
            case TopicType.CHALLENGE:
                intent = new Intent(this.getActivity(), TopicDetailActivity.class);
                intent.putExtra(TopicType.TYPE, TopicType.CHALLENGE);
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
                            MyErrorHelper.showErrorToast(NewHomeFragment.this.getActivity(), response.getError());
                        }
                        isRequesting = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isRequesting = false;
                        swipeRefreshLayout.setRefreshing(false);
                        MyVolleyErrorHelper.showError(NewHomeFragment.this.getActivity(), error);
                    }
                });

        isRequesting = true;
        MyApplication.getContext().getRequestQueue().add(myPageRequest);
    }

    private void initExhibitionList(){
        ACache aCache = ACache.get(this.getContext());
        String url = ServerMethod.exhibition();
        MyPage<ExhibitionDTO> exhibitionDTOMyPage = (MyPage<ExhibitionDTO>) aCache.getAsObject(url);
        if (exhibitionDTOMyPage != null){
            MyLog.e("NewHomeFragment", "Load cache success");
            exhibitionDTOList = exhibitionDTOMyPage.getContents();
        }
        if (exhibitionDTOList != null){
            updateExhibitionHeadVIew();
        }else {
            requestExhibitionList();
        }
    }

    private void requestExhibitionList(){

        final String url = ServerMethod.exhibition();

        MyPageRequest<ExhibitionDTO> myPageRequest = new MyPageRequest<ExhibitionDTO>(Request.Method.GET, url, ExhibitionDTO.class,
                new Response.Listener<MyResponse<MyPage<ExhibitionDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<ExhibitionDTO>> response) {
                        if (response.getStatus() == MyResponse.status_ok){
                            if (response.getContent() != null){
                                NewHomeFragment.this.exhibitionDTOList = response.getContent().getContents();
                                updateExhibitionHeadVIew();

                                //cache
                                ACache aCache = ACache.get(NewHomeFragment.this.getContext());
                                aCache.put(url, response.getContent(), ACache.TIME_HOUR);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getContext().getRequestQueue().add(myPageRequest);
    }

    private void updateExhibitionHeadVIew(){
        mConvenientBanner.setPages(new CBViewHolderCreator<ExhibitionItemHolderView>() {
            @Override
            public ExhibitionItemHolderView createHolder() {
                return new ExhibitionItemHolderView();
            }
        }, exhibitionDTOList)
                .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
    }

    private void startExhibitionDetail(int position){
        if (position < exhibitionDTOList.size()){
            ExhibitionDTO exhibitionDTO = exhibitionDTOList.get(position);
            if (ExhibitionAction.OPEN_TOPIC.equals(exhibitionDTO.getAction())){
                String topicJson = exhibitionDTO.getData();
                Gson gson = new Gson();
                if (!TextUtils.isEmpty(topicJson)){
                    TopicDTO topicDTO = gson.fromJson(topicJson, TopicDTO.class);
                    if (!TextUtils.isEmpty(topicDTO.getUrl())){
                        WebActivity.startActivity(this.getActivity(), topicDTO.getTitle(), topicDTO.getUrl());
                    }else {
                        Intent intent = new Intent(NewHomeFragment.this.getActivity(), TopicDetailActivity.class);
                        intent.putExtra(TopicDetailActivity.INTENT_TOPIC, topicDTO);
                        startActivity(intent);
                    }
                }
            }else if (ExhibitionAction.OPEN_URL.equals(exhibitionDTO.getAction())){
                WebActivity.startActivity(this.getActivity(), exhibitionDTO.getTitle(), exhibitionDTO.getData());
            }
            else if (ExhibitionAction.OPEN_TAOBAO_PRODUCT.equals(exhibitionDTO.getAction())){
                AlibcDetailPage alibcDetailPage = new AlibcDetailPage(exhibitionDTO.getData());
                AlibcShowParams alibcShowParams = new AlibcShowParams(OpenType.H5, false);
                AlibcTaokeParams alibcTaokeParams = new AlibcTaokeParams("mm_111250070_0_0", "mm_111250070_0_0", null);
                Map<String, String> exParams = new HashMap<>();
                exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
                exParams.put("skate group", "滑板圈");//自定义参数部分，可任意增删改


                AlibcTrade.show(getActivity(), alibcDetailPage, alibcShowParams, alibcTaokeParams, exParams, new AlibcTradeCallback() {

                    @Override
                    public void onTradeSuccess(TradeResult tradeResult) {
                        //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
                        Toast.makeText(NewHomeFragment.this.getContext(), "成功", Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
                    }
                });
            }else if(ExhibitionAction.INVITE_FRIEND.equals(exhibitionDTO.getAction())){

                InviteWebActivity.startActivity(this.getContext());
            }
            else {
                Toast.makeText(NewHomeFragment.this.getContext(), R.string.app_version_is_low, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
