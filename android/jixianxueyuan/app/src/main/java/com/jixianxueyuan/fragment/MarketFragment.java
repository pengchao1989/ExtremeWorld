package com.jixianxueyuan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.GoodsListActivity;
import com.jixianxueyuan.adapter.CategoryGridAdapter;
import com.jixianxueyuan.adapter.ShopGridAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.biz.CategoryDTO;
import com.jixianxueyuan.dto.biz.MarketfoDTO;
import com.jixianxueyuan.dto.biz.ShopDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.widget.GridViewWithHeaderAndFooter;
import com.jixianxueyuan.widget.NoScorllBarGridView;

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

    private void initHeadView(){
        View headerView = LayoutInflater.from(MarketFragment.this.getActivity()).inflate(R.layout.market_home_activity_head, null);
        NoScorllBarGridView categoryGridView = (NoScorllBarGridView) headerView.findViewById(R.id.market_home_activity_head_gridview);

        categoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MarketFragment.this.getActivity(), GoodsListActivity.class);
                intent.putExtra(GoodsListActivity.SOURCE_TYPE, GoodsListActivity.SOURCE_TYPE_CATEGORY);
                intent.putExtra(GoodsListActivity.SOURCE_TYPE_CATEGORY, categoryGridAdapter.getItem(position));
                startActivity(intent);
            }
        });

        categoryGridAdapter = new CategoryGridAdapter(MarketFragment.this.getActivity());
        categoryGridView.setAdapter(categoryGridAdapter);

        gridView.addHeaderView(headerView);

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

    private void requestShopList(){

        String url = ServerMethod.biz_shop();
        MyPageRequest<ShopDTO> myPageRequest = new MyPageRequest<ShopDTO>(Request.Method.GET, url, ShopDTO.class,
                new Response.Listener<MyResponse<MyPage<ShopDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<ShopDTO>> response) {
                        shopGridAdapter.setShopDTOList(response.getContent().getContents());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getContext().getRequestQueue().add(myPageRequest);
    }

    @OnItemClick(R.id.market_home_activity_gridview) void onShopClick(int position){
        Intent intent = new Intent(MarketFragment.this.getActivity(), GoodsListActivity.class);
        intent.putExtra(GoodsListActivity.SOURCE_TYPE, GoodsListActivity.SOURCE_TYPE_SHOP);
        intent.putExtra(GoodsListActivity.SOURCE_TYPE_SHOP, shopGridAdapter.getItem(position));
        startActivity(intent);
    }
}
