package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jixianxueyuan.R;
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
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.GridViewWithHeaderAndFooter;
import com.jixianxueyuan.widget.NoScorllBarGridView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 10/17/15.
 */
public class MarketHomeActivity extends BaseActivity {

    public static final String tag = MarketHomeActivity.class.getSimpleName();


    @InjectView(R.id.market_home_activity_gridview)GridViewWithHeaderAndFooter gridView;

    private CategoryGridAdapter categoryGridAdapter;
    private ShopGridAdapter shopGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_home_activity);

        ButterKnife.inject(this);

        initHeadView();

        shopGridAdapter = new ShopGridAdapter(this);
        gridView.setAdapter(shopGridAdapter);


        requestCategoryList();
    }

    private void initHeadView(){
        View headerView = LayoutInflater.from(this).inflate(R.layout.market_home_activity_head,null);
        NoScorllBarGridView categoryGridView = (NoScorllBarGridView) headerView.findViewById(R.id.market_home_activity_head_gridview);

        categoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MarketHomeActivity.this, GoodsListActivity.class);
                intent.putExtra(GoodsListActivity.SOURCE_TYPE, GoodsListActivity.SOURCE_TYPE_CATEGORY);
                intent.putExtra(GoodsListActivity.SOURCE_TYPE_CATEGORY, categoryGridAdapter.getItem(position));
                startActivity(intent);
            }
        });

        categoryGridAdapter = new CategoryGridAdapter(this);
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
        Intent intent = new Intent(MarketHomeActivity.this, GoodsListActivity.class);
        intent.putExtra(GoodsListActivity.SOURCE_TYPE, GoodsListActivity.SOURCE_TYPE_SHOP);
        intent.putExtra(GoodsListActivity.SOURCE_TYPE_SHOP, shopGridAdapter.getItem(position));
        startActivity(intent);
    }

}
