package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.NearFriendListAdapter;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;

import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 7/3/15.
 */
public class NearFriendActivity extends Activity  {

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    double latitude;
    double longitude;

    @InjectView(R.id.near_friend_listview)ListView listView;

    NearFriendListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.near_friend_activity);

        ButterKnife.inject(this);

        adapter = new NearFriendListAdapter(this);
        listView.setAdapter(adapter);

        refresh();
    }

    private void refresh()
    {
        if(mLocationClient == null)
        {
            initLocation();
        }

        if(mLocationClient.isStarted() == false)
        {
            mLocationClient.start();
        }
    }


    private void requestData()
    {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ServerMethod.near_friend + "?userId=1" + "&latitude=" + latitude + "&longitude=" + longitude + "&page=1";
        MyLog.d("NearFriendActivity", "request=" + url);

        MyPageRequest<UserMinDTO> myPageRequest = new MyPageRequest<UserMinDTO>(Request.Method.GET,url, UserMinDTO.class,
                new Response.Listener<MyResponse<MyPage<UserMinDTO>>>(){

                    @Override
                    public void onResponse(MyResponse<MyPage<UserMinDTO>> response) {

                        adapter.refreshData(response.getContent().getContents());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

                );

        queue.add(myPageRequest);

    }

    private void initLocation()
    {
        mLocationClient = new LocationClient(getApplicationContext());// 声明 LocationClient 类
        mLocationClient.registerLocationListener( myListener);// 注册监听函数
        mLocationClient.start();


        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度 , 默认值 gcj02
        //option.setScanSpan(5000);// 设置发起定位请求的间隔时间为 5000ms
        option.disableCache(true);// 禁止启用缓存定位
        option.setPoiNumber(5); // 最多返回 POI 个数
        option.setPoiDistance(1000); //poi 查询距离
        option.setPoiExtraInfo(false); // 是否需要 POI 的电话和地址等详细信息
        mLocationClient.setLocOption(option);

        if (mLocationClient != null && mLocationClient.isStarted())
            mLocationClient.requestLocation();
        else
            MyLog.d("LocSDK3", "locClient is null or not started");

    }





    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location)
        {
            if (location == null)
            return ;
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());
            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());




        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
        }

            mLocationClient.stop();

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            //请求数据
            requestData();
            MyLog.d("Location",sb.toString());
        }
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null){
                return ;
            }
            StringBuffer sb = new StringBuffer(256);
            sb.append("Poi time : ");
            sb.append(poiLocation.getTime());
            sb.append("\nerror code : ");
            sb.append(poiLocation.getLocType());
            sb.append("\nlatitude : ");
            sb.append(poiLocation.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(poiLocation.getLongitude());
            sb.append("\nradius : ");
            sb.append(poiLocation.getRadius());
            if (poiLocation.getLocType() ==
                    BDLocation.TypeNetWorkLocation){
                sb.append("\naddr : ");
                sb.append(poiLocation.getAddrStr());
            }
            if(poiLocation.hasPoi()){
                sb.append("\nPoi:");
                sb.append(poiLocation.getPoi());
            }else{
                sb.append("noPoi information");
            }

            mLocationClient.stop();

            latitude = poiLocation.getLatitude();
            longitude = poiLocation.getLongitude();

            //请求数据
            requestData();
            MyLog.d("POI Location", sb.toString());
        }
    }

}
