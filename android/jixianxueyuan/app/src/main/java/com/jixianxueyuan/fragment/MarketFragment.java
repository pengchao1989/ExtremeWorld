package com.jixianxueyuan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.trade.TradeService;
import com.alibaba.sdk.android.trade.callback.TradeProcessCallback;
import com.alibaba.sdk.android.trade.model.TradeResult;
import com.alibaba.sdk.android.trade.page.MyCartsPage;
import com.alibaba.sdk.android.trade.page.MyOrdersPage;
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

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 11/1/15.
 */
public class MarketFragment extends Fragment {

    @InjectView(R.id.market_home_activity_gridview)GridViewWithHeaderAndFooter gridView;

    private CategoryGridAdapter categoryGridAdapter;
    private ShopGridAdapter shopGridAdapter;

    NoScorllBarGridView categoryGridView;

    private AutoLoadMoreView autoLoadMoreView;
    private int currentPage = 0;
    private int totalPage = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_home_activity, container, false);

        ButterKnife.inject(this, view);

        initHeadView();

        shopGridAdapter = new ShopGridAdapter(MarketFragment.this.getActivity());
        gridView.setAdapter(shopGridAdapter);


        requestCategoryList();

        return view;
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
                TradeService tradeService = AlibabaSDK.getService(TradeService.class);
                MyCartsPage myCartsPage = new MyCartsPage();
                tradeService.show(myCartsPage, null, MarketFragment.this.getActivity(), null, new TradeProcessCallback() {

                    @Override
                    public void onFailure(int code, String msg) {


                    }

                    @Override
                    public void onPaySuccess(TradeResult tradeResult) {


                    }
                });
                MobclickAgent.onEvent(MarketFragment.this.getContext(), UmengEventId.MARKET_SHOPPING_CART_CLICK);
            }
        });

        headerView.findViewById(R.id.market_home_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TradeService tradeService = AlibabaSDK.getService(TradeService.class);
                MyOrdersPage myOrdersPage = new MyOrdersPage(0, false);
                tradeService.show(myOrdersPage, null, MarketFragment.this.getActivity(), null, new TradeProcessCallback() {

                    @Override
                    public void onFailure(int code, String msg) {

                    }

                    @Override
                    public void onPaySuccess(TradeResult tradeResult) {

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

            MobclickAgent.onEvent(MarketFragment.this.getContext(), UmengEventId.MARKET_SHOP_ITEM_CLICK, shopGridAdapter.getItem(position).getName());
            Intent intent = new Intent(MarketFragment.this.getActivity(), GoodsListActivity.class);
            intent.putExtra(GoodsListActivity.SOURCE_TYPE, GoodsListActivity.SOURCE_TYPE_SHOP);
            intent.putExtra(GoodsListActivity.SOURCE_TYPE_SHOP, shopGridAdapter.getItem(position));
            startActivity(intent);
    }
}
