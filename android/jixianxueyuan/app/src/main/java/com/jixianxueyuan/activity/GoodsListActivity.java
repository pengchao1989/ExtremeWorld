package com.jixianxueyuan.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.GoodsListAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.biz.CategoryDTO;
import com.jixianxueyuan.dto.biz.GoodsDTO;
import com.jixianxueyuan.dto.biz.ShopDTO;
import com.jixianxueyuan.fragment.DynamicHomeFragment;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.widget.GridViewWithHeaderAndFooter;
import com.jixianxueyuan.widget.MyActionBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 10/18/15.
 */
public class GoodsListActivity extends BaseActivity {


    public static final String SOURCE_TYPE = "SOURCE_TYPE";
    public static final String SOURCE_TYPE_CATEGORY = "SOURCE_TYPE_CATEGORY";
    public static final String SOURCE_TYPE_SHOP = "SOURCE_TYPE_SHOP";

    @BindView(R.id.goods_list_activity_actionbar)MyActionBar actionBar;
    @BindView(R.id.goods_list_activity_gridview)GridViewWithHeaderAndFooter gridView;

    private String sourceType;
    private ShopDTO shopDTO;
    private CategoryDTO categoryDTO;

    private GoodsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_list_activity);
        ButterKnife.bind(this);

        intentData();
        initView();

        adapter = new GoodsListAdapter(this);
        gridView.setAdapter(adapter);

        refreshGoodsList();
    }

    private void intentData(){
        Intent intent = this.getIntent();
        sourceType = intent.getStringExtra(SOURCE_TYPE);
        if(SOURCE_TYPE_SHOP.equals(sourceType)){
            shopDTO = (ShopDTO) intent.getSerializableExtra(SOURCE_TYPE_SHOP);
        }else if(SOURCE_TYPE_CATEGORY.equals(sourceType)){
            categoryDTO = (CategoryDTO) intent.getSerializableExtra(SOURCE_TYPE_CATEGORY);
        }else {
            finish();
        }

        if(shopDTO == null && categoryDTO == null){
            finish();
        }
    }

    private void initView(){
        switch (sourceType){
            case SOURCE_TYPE_CATEGORY:
                actionBar.setTitle(categoryDTO.getName());
                break;
            case SOURCE_TYPE_SHOP:
                actionBar.setTitle(shopDTO.getName());
                break;
        }

    }


    private void refreshGoodsList(){
        requestGoodsList();
    }

    private void loadMoreGoodsList(){

    }

    private void requestGoodsList(){

        String url = null;

        switch (sourceType){
            case SOURCE_TYPE_CATEGORY:
                url = ServerMethod.goods_of_category() + categoryDTO.getId();
                break;

            case SOURCE_TYPE_SHOP:
                url = ServerMethod.goods_of_shop() + shopDTO.getId();
                break;
        }

        MyPageRequest<GoodsDTO> myPageRequest = new MyPageRequest<GoodsDTO>(Request.Method.GET, url, GoodsDTO.class,
                new Response.Listener<MyResponse<MyPage<GoodsDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<GoodsDTO>> response) {
                        if(response.getStatus() == MyResponse.status_ok){
                            adapter.setData(response.getContent().getContents());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getContext().getRequestQueue().add(myPageRequest);
    }

    @OnItemClick(R.id.goods_list_activity_gridview)void onItemClick(int position){

        GoodsDTO goodsDTO = adapter.getItem(position);

        AlibcDetailPage alibcDetailPage = new AlibcDetailPage(String.valueOf(goodsDTO.getTaobaoId()));
        AlibcShowParams alibcShowParams = new AlibcShowParams(OpenType.H5, false);
        AlibcTaokeParams alibcTaokeParams = new AlibcTaokeParams("mm_111250070_0_0", "mm_111250070_0_0", null);
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
        exParams.put("skate group", "滑板圈");//自定义参数部分，可任意增删改


        AlibcTrade.show(this, alibcDetailPage, alibcShowParams, alibcTaokeParams, exParams, new AlibcTradeCallback() {

            @Override
            public void onTradeSuccess(TradeResult tradeResult) {
                //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
                Toast.makeText(GoodsListActivity.this, "成功", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onFailure(int code, String msg) {
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
            }
        });
    }
}
