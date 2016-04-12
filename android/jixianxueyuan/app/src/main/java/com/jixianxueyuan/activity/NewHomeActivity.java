package com.jixianxueyuan.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.jixianxueyuan.R;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.im.IMManager;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.config.UmengEventId;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.request.LocationDTO;
import com.jixianxueyuan.fragment.DiscoveryFragment;
import com.jixianxueyuan.fragment.DynamicHomeFragment;
import com.jixianxueyuan.fragment.MarketFragment;
import com.jixianxueyuan.fragment.MineFragment;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.location.LocationManager;
import com.jixianxueyuan.location.MyLocation;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.ShareUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 10/31/15.
 */
public class NewHomeActivity extends FragmentActivity implements View.OnClickListener {

    public static final String tag = NewHomeActivity.class.getSimpleName();

    @InjectView(R.id.bottom_sheet)BottomSheetLayout bottomSheetLayout;
    @InjectView(R.id.tab_dynamic_layout)RelativeLayout trendsLayout;
    @InjectView(R.id.tab_discover_layout)RelativeLayout discoverLayout;
    @InjectView(R.id.tab_mine_layout)RelativeLayout mineLayout;
    @InjectView(R.id.tab_market_layout)RelativeLayout marketLayout;

    @InjectView(R.id.tab_trends_image)ImageView trendsImageView;
    @InjectView(R.id.tab_discover_image)ImageView discoverImageView;
    @InjectView(R.id.tab_mine_image)ImageView mineImageView;
    @InjectView(R.id.tab_market_image)ImageView marketImageView;

    @InjectView(R.id.tab_trends_text)TextView trendsTextView;
    @InjectView(R.id.tab_discover_text)TextView discoverTextView;
    @InjectView(R.id.tab_mine_text)TextView mineTextView;
    @InjectView(R.id.tab_market_text)TextView marketTextView;

    private FragmentManager fragmentManager;
    private DynamicHomeFragment topicListFragment;
    private DiscoveryFragment discoveryFragment;
    private MineFragment mineFragment;
    private MarketFragment marketFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_home_activity);

        ButterKnife.inject(this);

        fragmentManager = getSupportFragmentManager();

        initView();

        setChioceItem(0);

        MobclickAgent.setDebugMode(true);

        UmengUpdateAgent.update(this);

        location();

        loginIM();
    }

    private void initView(){
        bottomSheetLayout.setPeekOnDismiss(true);

        trendsLayout.setOnClickListener(this);
        discoverLayout.setOnClickListener(this);
        mineLayout.setOnClickListener(this);
        marketLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tab_dynamic_layout:
                setChioceItem(0);
/*                Intent intent = new Intent(NewHomeActivity.this, CreateTopicPreActivity.class);
                startActivity(intent);*/
                break;

            case R.id.tab_discover_layout:
                setChioceItem(1);
                MobclickAgent.onEvent(NewHomeActivity.this, UmengEventId.TAB_DISCOVERY_CLICK);
                break;

            case R.id.tab_mine_layout:
                setChioceItem(3);
                MobclickAgent.onEvent(NewHomeActivity.this, UmengEventId.TAB_MINE_CLICK);
                break;

            case R.id.tab_market_layout:
                setChioceItem(4);
                MobclickAgent.onEvent(NewHomeActivity.this, UmengEventId.TAB_STORE_CLICK);
                break;
        }
    }

    public void setChioceItem(int index)
    {
        //重置选项+隐藏所有Fragment
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        clearChioce();
        hideFragments(transaction);
        switch (index) {
            case 0:
                trendsImageView.setImageResource(R.mipmap.bottombar_message2);
                trendsTextView.setTextColor(getResources().getColor(R.color.primary));
                if (topicListFragment == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    topicListFragment = new DynamicHomeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("topicType", TopicType.ALL);
                    topicListFragment.setArguments(bundle);
                    transaction.add(R.id.content, topicListFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(topicListFragment);
                }
                break;

            case 1:
                discoverImageView.setImageResource(R.mipmap.bottombar_find2);
                discoverTextView.setTextColor(getResources().getColor(R.color.primary));
                if (discoveryFragment == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    discoveryFragment = new DiscoveryFragment();
                    transaction.add(R.id.content, discoveryFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(discoveryFragment);
                }
                break;
            case 2:
                break;

            case 3:
                mineImageView.setImageResource(R.mipmap.bottombar_mine_2);
                mineTextView.setTextColor(getResources().getColor(R.color.primary));
                if (mineFragment == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    mineFragment = new MineFragment();
                    transaction.add(R.id.content, mineFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mineFragment);
                }
                break;
            case 4:
                marketImageView.setImageResource(R.mipmap.bottombar_shop2);
                marketTextView.setTextColor(getResources().getColor(R.color.primary));
                if(marketFragment == null){
                    marketFragment = new MarketFragment();
                    transaction.add(R.id.content, marketFragment);
                }else {
                    transaction.show(marketFragment);
                }
                break;
        }
        transaction.commit();
    }


    //隐藏所有的Fragment,避免fragment混乱
    private void hideFragments(FragmentTransaction transaction) {
        if (topicListFragment != null) {
            transaction.hide(topicListFragment);
        }
        if (discoveryFragment != null) {
            transaction.hide(discoveryFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
        if(marketFragment != null){
            transaction.hide(marketFragment);
        }
    }
    //定义一个重置所有选项的方法
    public void clearChioce()
    {
        trendsImageView.setImageResource(R.mipmap.bottombar_message);
        trendsTextView.setTextColor(getResources().getColor(R.color.secondary_text));

        discoverImageView.setImageResource(R.mipmap.bottombar_find);
        discoverTextView.setTextColor(getResources().getColor(R.color.secondary_text));

        mineImageView.setImageResource(R.mipmap.bottombar_mine);
        mineTextView.setTextColor(getResources().getColor(R.color.secondary_text));

        marketImageView.setImageResource(R.mipmap.bottombar_shop);
        marketTextView.setTextColor(getResources().getColor(R.color.secondary_text));
    }



    public void showShareMenuSheet(final MenuSheetView.MenuType menuType) {
        MenuSheetView menuSheetView =
                new MenuSheetView(NewHomeActivity.this, menuType, "邀请3个好友升级为永久会员...", new MenuSheetView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        ShareUtils.ShareItem shareItem = null;

                        switch (item.getItemId()){
                            case R.id.menu_share_qq:
                                shareItem = new ShareUtils.ShareItem("QQ", R.drawable.umeng_socialize_qq_on,
                                        "com.tencent.mobileqq.activity.JumpActivity","com.tencent.mobileqq");
                                break;
                            case R.id.menu_share_kongjian:
                                shareItem = new ShareUtils.ShareItem("空间", R.drawable.umeng_socialize_qzone_on,
                                        "com.qzone.ui.operation.QZonePublishMoodActivity","com.qzone");
                                break;
                        }


                        if(shareItem != null){
                            ShareUtils.share(NewHomeActivity.this,"title", "text", "", shareItem);
                        }




                        if (bottomSheetLayout.isSheetShowing()) {
                            bottomSheetLayout.dismissSheet();
                        }
                        return true;
                    }
                });
        menuSheetView.inflateMenu(R.menu.share);
        bottomSheetLayout.showWithSheetView(menuSheetView);
    }

        private void location(){
        new CountDownTimer(3000, 3000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                final LocationManager locationManager = LocationManager.getInstance();
                locationManager.setLocationListener(new LocationManager.LocationListener() {
                    @Override
                    public void onSuccess(MyLocation location) {
                        MyLog.e(tag, "address=" + location.getAddress() +
                        " lat=" + location.getLatitude() +
                        " lng=" + location.getLongitude());

                        requestPublishLocation(location);

                        locationManager.stop();
                    }

                    @Override
                    public void onError(int errCode, String err) {

                    }
                });
                locationManager.start();
            }
        }.start();
    }

    private void requestPublishLocation(MyLocation myLocation){
        if (myLocation != null){
            String url = ServerMethod.publish_location();

            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setLatitude(myLocation.getLatitude());
            locationDTO.setLongitude(myLocation.getLongitude());
            locationDTO.setAddress(myLocation.getAddress());

            MyRequest<Void> myRequest = new MyRequest<Void>(Request.Method.POST, url, Void.class, locationDTO, new Response.Listener<MyResponse<Void>>() {
                @Override
                public void onResponse(MyResponse<Void> response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            MyApplication.getContext().getRequestQueue().add(myRequest);
        }
    }

    private void loginIM(){

        new CountDownTimer(5000, 5000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                IMManager.getInstance().Login(new IMManager.LoginResultListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(String err) {
                        Toast.makeText(NewHomeActivity.this, err, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }.start();


    }
}
