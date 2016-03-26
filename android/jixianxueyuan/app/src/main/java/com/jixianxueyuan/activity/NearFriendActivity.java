package com.jixianxueyuan.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.NearFriendListAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.MyErrorHelper;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.UserDTO;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.location.LocationManager;
import com.jixianxueyuan.location.MyLocation;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.ClickLoadMoreView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dmax.dialog.SpotsDialog;

/**
 * Created by pengchao on 7/3/15.
 */
public class NearFriendActivity extends BaseActivity  {


    double latitude;
    double longitude;

    @InjectView(R.id.near_friend_listview)ListView listView;
    @InjectView(R.id.near_friend_swiperefresh)SwipeRefreshLayout swipeRefreshLayout;
    private AlertDialog progressDialog;
    private ClickLoadMoreView clickLoadMoreView;

    private NearFriendListAdapter adapter;

    int currentPage = 0;
    int totalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.near_friend_activity);

        ButterKnife.inject(this);

        initView();


        refresh();
    }

    private void initView()
    {
        adapter = new NearFriendListAdapter(this);


        swipeRefreshLayout.setColorSchemeResources(R.color.blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refresh();
            }
        });

        clickLoadMoreView = new ClickLoadMoreView(this);
        clickLoadMoreView.setVisibility(View.GONE);
        clickLoadMoreView.setClickLoadMoreViewListener(new ClickLoadMoreView.ClickLoadMoreViewListener() {
            @Override
            public void runLoad() {
                nextPage();
            }
        });

        listView.addFooterView(clickLoadMoreView);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDTO userDTO = adapter.getItem(position);

                UserMinDTO userMinDTO = new UserMinDTO();
                userMinDTO.setId(userDTO.getId());
                userMinDTO.setAvatar(userDTO.getAvatar());
                userMinDTO.setGender(userDTO.getGender());
                userMinDTO.setName(userDTO.getName());
                Intent intent = new Intent(NearFriendActivity.this, UserHomeActivity.class);
                intent.putExtra(UserHomeActivity.INTENT_USER_MIN, userMinDTO);
                startActivity(intent);

            }
        });

    }

    private void refresh() {

        currentPage = 0;

        startLocation();

        LocationManager.getInstance().stop();
    }

    private void nextPage()
    {
        if(currentPage < totalPage ) {
            requestData();
        }
        else {
            Toast.makeText(this, "没了", Toast.LENGTH_SHORT).show();
        }
    }

    private void doHideFootView() {

        if(totalPage > 1) {

            if(clickLoadMoreView.isLoading() == true) {
                clickLoadMoreView.onFinish();
            }

            if(currentPage >= totalPage) {
                clickLoadMoreView.setOver();
            }
        }
    }


    private void requestData()
    {

        swipeRefreshLayout.setRefreshing(true);

        Long userId = MyApplication.getContext().getMine().getUserInfo().getId();
        String url = ServerMethod.near_friend() + "?userId=" + userId + "&latitude=" + latitude + "&longitude=" + longitude + "&page=" + (currentPage + 1);
        MyLog.d("NearFriendActivity", "request=" + url);

        MyPageRequest<UserDTO> myPageRequest = new MyPageRequest<UserDTO>(Request.Method.GET,url, UserDTO.class,
                new Response.Listener<MyResponse<MyPage<UserDTO>>>(){

                    @Override
                    public void onResponse(MyResponse<MyPage<UserDTO>> response) {

                        if(response.getStatus() == MyResponse.status_ok){
                            if(currentPage == 0)
                            {
                                adapter.refreshData(response.getContent().getContents());
                            }
                            else
                            {
                                adapter.addDatas(response.getContent().getContents());
                            }

                            swipeRefreshLayout.setRefreshing(false);

                            totalPage = response.getContent().getTotalPages();
                            currentPage = response.getContent().getCurPage() + 1;
                            doHideFootView();
                        }else if(response.getStatus() == MyResponse.status_error){
                            MyErrorHelper.showErrorToast(NearFriendActivity.this,response.getError());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        MyVolleyErrorHelper.showError(NearFriendActivity.this, error);
                    }
                });

        MyApplication.getContext().getRequestQueue().add(myPageRequest);
    }

    private void startLocation(){
        showLocationProgress();

        LocationManager locationManager = LocationManager.getInstance();
        locationManager.setLocationListener(new LocationManager.LocationListener() {
            @Override
            public void onSuccess(MyLocation location) {
                hideLocationProgress();
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                //请求数据
                requestData();
            }

            @Override
            public void onError(int errCode, String err) {
                hideLocationProgress();
                Toast.makeText(NearFriendActivity.this, R.string.location_failed, Toast.LENGTH_LONG).show();
            }
        });


    }

    private void showLocationProgress(){
        progressDialog = new SpotsDialog(this,R.style.ProgressDialogWaitLocation);
        progressDialog.setTitle(getString(R.string.wait_location));
        progressDialog.show();
    }

    private void hideLocationProgress(){
        progressDialog.dismiss();
    }


}
