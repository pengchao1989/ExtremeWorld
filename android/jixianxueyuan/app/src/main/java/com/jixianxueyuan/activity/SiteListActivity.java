package com.jixianxueyuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.SiteListAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.SiteDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.widget.MyActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pengchao on 10/30/15.
 */
public class SiteListActivity extends BaseActivity {

    private static final int REQUEST_CODE_NEW = 1;

    @BindView(R.id.site_list_activity_actionbar)MyActionBar actionBar;
    @BindView(R.id.site_listview)
    ListView listView;
    @BindView(R.id.site_list_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private SiteListAdapter adapter;

    private int currentPage = 0;
    private int totalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_list_activity);
        ButterKnife.bind(this);

        initView();

        refreshSiteList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == BaseActivity.RESULT_OK){
            if (requestCode == REQUEST_CODE_NEW){
                refreshSiteList();
            }
        }
    }

    private void initView(){

        actionBar.setTitle(getString(R.string.site));
        actionBar.setActionOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SiteListActivity.this, SiteCreateActivity.class);
                startActivityForResult(intent,REQUEST_CODE_NEW);
            }
        });

        adapter = new SiteListAdapter(this);
        listView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshSiteList();
            }
        });

    }

    private void refreshSiteList()
    {
        currentPage = 0;

        requestSiteList();
    }

    private void requestSiteList(){
        swipeRefreshLayout.setRefreshing(true);

        String url = ServerMethod.site();

        MyPageRequest<SiteDTO> myPageRequest = new MyPageRequest<SiteDTO>(Request.Method.GET, url, SiteDTO.class,
                new Response.Listener<MyResponse<MyPage<SiteDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<SiteDTO>> response) {
                        if(response.getStatus() == MyResponse.status_ok){
                            MyPage<SiteDTO> myPage = response.getContent();
                            adapter.setData(myPage.getContents());
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

        MyApplication.getContext().getRequestQueue().add(myPageRequest);
    }
}
