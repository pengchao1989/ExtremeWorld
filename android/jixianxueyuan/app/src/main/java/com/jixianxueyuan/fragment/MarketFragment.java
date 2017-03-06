package com.jixianxueyuan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.constants.AlibcConstants;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.AlibcTaokeParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcMyCartsPage;
import com.alibaba.baichuan.android.trade.page.AlibcMyOrdersPage;
import com.alibaba.baichuan.android.trade.page.AlibcShopPage;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.GoodsListActivity;
import com.jixianxueyuan.adapter.CategoryGridAdapter;
import com.jixianxueyuan.adapter.ShopGridAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.config.UmengEventId;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.biz.CategoryDTO;
import com.jixianxueyuan.dto.biz.MarketfoDTO;
import com.jixianxueyuan.dto.biz.ShopDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.widget.AutoLoadMoreView;
import com.jixianxueyuan.widget.GridViewWithHeaderAndFooter;
import com.jixianxueyuan.widget.NoScorllBarGridView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 11/1/15.
 */
public class MarketFragment extends Fragment {

    @BindView(R.id.market_home_activity_gridview)GridViewWithHeaderAndFooter gridView;

    private CategoryGridAdapter categoryGridAdapter;
    private ShopGridAdapter shopGridAdapter;

    NoScorllBarGridView categoryGridView;

    private AutoLoadMoreView autoLoadMoreView;
    private int currentPage = 0;
    private int totalPage = 0;

    private int orderType = 0;//订单页面参数，仅在H5方式下有效
    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    private Map<String, String> exParams;//yhhpass参数

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_home_activity, container, false);

        ButterKnife.bind(this, view);

        initHeadView();

        shopGridAdapter = new ShopGridAdapter(MarketFragment.this.getActivity());
        gridView.setAdapter(shopGridAdapter);


        requestCategoryList();

        initAliTrade();

        return view;
    }

    private void initAliTrade(){
        alibcShowParams = new AlibcShowParams(OpenType.Auto, false);

        exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
        exParams.put("skate group", "滑板圈");//自定义参数部分，可任意增删改
    }

    private void initHeadView() {
        View headerView = LayoutInflater.from(MarketFragment.this.getActivity()).inflate(R.layout.market_home_activity_head, null);
        NoScorllBarGridView categoryGridView = (NoScorllBarGridView) headerView.findViewById(R.id.market_home_activity_head_gridview);

        categoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (categoryGridAdapter.getItemViewType(position) == CategoryGridAdapter.VIEW_TYPE_CATEGORY) {
                    MobclickAgent.onEvent(MarketFragment.this.getContext(), UmengEventId.MARKET_CATEGORY_ITEM_CLICK, categoryGridAdapter.getItem(position).getName());
                    Intent intent = new Intent(MarketFragment.this.getActivity(), GoodsListActivity.class);
                    intent.putExtra(GoodsListActivity.SOURCE_TYPE, GoodsListActivity.SOURCE_TYPE_CATEGORY);
                    intent.putExtra(GoodsListActivity.SOURCE_TYPE_CATEGORY, categoryGridAdapter.getItem(position));
                    startActivity(intent);
                } else if (categoryGridAdapter.getItemViewType(position) == CategoryGridAdapter.VIEW_TYPE_ORDER) {

                }

            }
        });

        categoryGridAdapter = new CategoryGridAdapter(MarketFragment.this.getActivity());
        categoryGridView.setAdapter(categoryGridAdapter);

        headerView.findViewById(R.id.market_home_shopping_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlibcBasePage alibcBasePage = new AlibcMyOrdersPage(orderType, false);
                AlibcTrade.show(getActivity(), alibcBasePage, alibcShowParams, null, exParams, new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(TradeResult tradeResult) {

                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });

                MobclickAgent.onEvent(MarketFragment.this.getContext(), UmengEventId.MARKET_SHOPPING_CART_CLICK);
            }
        });

        headerView.findViewById(R.id.market_home_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlibcBasePage alibcBasePage = new AlibcMyCartsPage();
                AlibcTrade.show(getActivity(), alibcBasePage, alibcShowParams, null, exParams, new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(TradeResult tradeResult) {

                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });

                MobclickAgent.onEvent(MarketFragment.this.getContext(), UmengEventId.MARKET_ORDER_CLICK);
            }
        });

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
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


        gridView.addHeaderView(headerView);
        initFooterView();

    }

    private void initFooterView(){
        autoLoadMoreView = new AutoLoadMoreView(this.getActivity());
        gridView.addFooterView(autoLoadMoreView);
    }

    private void requestCategoryList(){
        String url = ServerMethod.biz_market();

        MyRequest<MarketfoDTO> myRequest = new MyRequest<MarketfoDTO>(Request.Method.GET, url, MarketfoDTO.class,
                new Response.Listener<MyResponse<MarketfoDTO>>() {
                    @Override
                    public void onResponse(MyResponse<MarketfoDTO> response) {
                        List<CategoryDTO> categoryDTOs = response.getContent().getCategoryList();
                        categoryGridAdapter.setCategoryList(categoryDTOs);

                        requestShopList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getContext().getRequestQueue().add(myRequest);
    }


    private void getNextPage(){
        if (currentPage < totalPage) {
            requestShopList();
        }
    }

    private void doHideFootView() {
        if (totalPage > 0 && currentPage >= totalPage) {
            autoLoadMoreView.setOver();
        }else {
            autoLoadMoreView.reset();
        }
    }

    private void requestShopList(){

        String url = ServerMethod.biz_shop() + "?page.size=14&page=" + (currentPage + 1);
        MyPageRequest<ShopDTO> myPageRequest = new MyPageRequest<ShopDTO>(Request.Method.GET, url, ShopDTO.class,
                new Response.Listener<MyResponse<MyPage<ShopDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<ShopDTO>> response) {

                        MyPage page = response.getContent();
                        shopGridAdapter.addShopDTOLIst(page.getContents());
                        totalPage = page.getTotalPages();
                        currentPage = page.getCurPage() + 1;
                        doHideFootView();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getContext().getRequestQueue().add(myPageRequest);
    }

    @OnItemClick(R.id.market_home_activity_gridview) void onShopClick(int position){

        Toast.makeText(this.getContext(), "籌建中，經請關注",Toast.LENGTH_LONG).show();


            MobclickAgent.onEvent(MarketFragment.this.getContext(), UmengEventId.MARKET_SHOP_ITEM_CLICK, shopGridAdapter.getItem(position).getName());
/*            Intent intent = new Intent(MarketFragment.this.getActivity(), GoodsListActivity.class);
            intent.putExtra(GoodsListActivity.SOURCE_TYPE, GoodsListActivity.SOURCE_TYPE_SHOP);
            intent.putExtra(GoodsListActivity.SOURCE_TYPE_SHOP, shopGridAdapter.getItem(position));
            startActivity(intent);*/
/*
        String shopId = shopGridAdapter.getItem(position).getTaobaoUrl();
        AlibcBasePage alibcBasePage;

        if (!TextUtils.isEmpty(shopId) ){
            alibcBasePage = new AlibcShopPage(shopId);
        } else {
            alibcBasePage = new AlibcShopPage(shopId.trim());
        }
        AlibcTaokeParams alibcTaokeParams = new AlibcTaokeParams("mm_111250070_0_0", "mm_111250070_0_0", null);
        AlibcTrade.show(getActivity(), alibcBasePage, alibcShowParams, alibcTaokeParams, exParams, new AlibcTradeCallback() {
            @Override
            public void onTradeSuccess(TradeResult tradeResult) {

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });*/

    }
}
