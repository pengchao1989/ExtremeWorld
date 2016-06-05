package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.jixianxueyuan.R;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.location.LocationManager;
import com.jixianxueyuan.location.MyLocation;
import com.jixianxueyuan.util.MyLog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pengchao on 5/29/16.
 */
public class AddressPicketActivity extends Activity implements LocationSource, AMapLocationListener,
        GeocodeSearch.OnGeocodeSearchListener{

    @InjectView(R.id.map)MapView mapView;
    @InjectView(R.id.address_picket_marker_image)
    ImageView markerImage;
    @InjectView(R.id.address_picket_add_text_view)
    TextView addressTextView;
    @InjectView(R.id.address_picket_complete_button)
    Button completeButton;

    AMap aMap;
    GeocodeSearch geocoderSearch;

    private MyLocation myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_picket_activity);
        ButterKnife.inject(this);

        mapView.onCreate(savedInstanceState);
        initMap();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void initMap(){
        aMap = mapView.getMap();
        MyLocation myLocation = LocationManager.getLastLocation();
        if (myLocation != null){
            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.changeLatLng(latLng);
            aMap.moveCamera(cameraUpdate);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        }


        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

                //1.得到marker的屏幕坐标
                int bottomY = markerImage.getBottom();
                int width = markerImage.getRight() - markerImage.getLeft();
                int bottomX = markerImage.getLeft() + width / 2;

                //2.屏幕坐标转经纬度
                Point screenPoint = new Point(bottomX, bottomY);
                LatLng latLng = aMap.getProjection().fromScreenLocation(screenPoint);

                //3.获取地址
                LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);

                geocoderSearch = new GeocodeSearch(AddressPicketActivity.this);
                geocoderSearch.setOnGeocodeSearchListener(AddressPicketActivity.this);
                //latLonPoint参数表示一个Latlng，第二参数表示范围多少米，GeocodeSearch.AMAP表示是国测局坐标系还是GPS原生坐标系
                RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
                geocoderSearch.getFromLocationAsyn(query);
            }
        });
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null){
            addressTextView.setText(regeocodeResult.getRegeocodeAddress().getFormatAddress());
            myLocation = new MyLocation();
            myLocation.setAddress(regeocodeResult.getRegeocodeAddress().getFormatAddress());
            myLocation.setLatitude(regeocodeResult.getRegeocodeQuery().getPoint().getLatitude());
            myLocation.setLongitude(regeocodeResult.getRegeocodeQuery().getPoint().getLongitude());
        }

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                MyLog.d("AddressPicketActivity", "lat=" + aMapLocation.getLatitude() + "Long=" + aMapLocation.getLongitude());
                LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.changeLatLng(latLng);
                aMap.moveCamera(cameraUpdate);
            } else {
                Toast.makeText(AddressPicketActivity.this, R.string.location_failed,Toast.LENGTH_LONG).show();
            }

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }

    @OnClick(R.id.address_picket_complete_button)void onClick(){
        if (myLocation != null){
            Intent intent = new Intent();
            intent.putExtra("location", myLocation);
            this.setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}
