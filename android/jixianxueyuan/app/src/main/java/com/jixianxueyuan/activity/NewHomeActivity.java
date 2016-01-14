package com.jixianxueyuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.jixianxueyuan.R;
import com.jixianxueyuan.commons.UpdateManager;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.config.UmengEventId;
import com.jixianxueyuan.fragment.DiscoveryFragment;
import com.jixianxueyuan.fragment.DynamicHomeFragment;
import com.jixianxueyuan.fragment.MarketFragment;
import com.jixianxueyuan.fragment.MineFragment;
import com.jixianxueyuan.fragment.TopicListFragment;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 10/31/15.
 */
public class NewHomeActivity extends FragmentActivity implements View.OnClickListener {

    @InjectView(R.id.bottom_sheet)BottomSheetLayout bottomSheetLayout;
    @InjectView(R.id.tab_new_layout)RelativeLayout newLayout;
    @InjectView(R.id.tab_dynamic_layout)RelativeLayout trendsLayout;
    @InjectView(R.id.tab_discover_layout)RelativeLayout discoverLayout;
    @InjectView(R.id.tab_mine_layout)RelativeLayout mineLayout;
    @InjectView(R.id.tab_market_layout)RelativeLayout marketLayout;

    @InjectView(R.id.tab_new_image)ImageView newImageView;
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

        UpdateManager updateManager = new UpdateManager(this);
        updateManager.checkUpdateInfo(false);

        MobclickAgent.setDebugMode(true);
    }

    private void initView(){
        bottomSheetLayout.setPeekOnDismiss(true);

        trendsLayout.setOnClickListener(this);
        discoverLayout.setOnClickListener(this);
        mineLayout.setOnClickListener(this);
        marketLayout.setOnClickListener(this);

        newLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                Intent intent = new Intent(NewHomeActivity.this, CreateTopicPreActivity.class);
                startActivity(intent);*/
                showMenuSheet(MenuSheetView.MenuType.GRID);
                MobclickAgent.onEvent(NewHomeActivity.this, UmengEventId.TAB_CREATE_CLICK);
            }
        });
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

            case R.id.tab_new_layout:

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

    private void showMenuSheet(final MenuSheetView.MenuType menuType) {
        MenuSheetView menuSheetView =
                new MenuSheetView(NewHomeActivity.this, menuType, "Create...", new MenuSheetView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = new Intent(NewHomeActivity.this, CreateTopicActivity.class);
                        switch (item.getItemId()){
                            case R.id.menu_create_mood:
                                intent.putExtra(TopicType.TYPE, TopicType.MOOD);
                                break;
                            case R.id.menu_create_discuss:
                                intent.putExtra(TopicType.TYPE, TopicType.DISCUSS);
                                break;
                            case R.id.menu_create_short_video:
                                intent.putExtra(TopicType.TYPE, TopicType.S_VIDEO);
                                break;
                        }
                        startActivity(intent);
                        MobclickAgent.onEvent(NewHomeActivity.this, UmengEventId.HOME_CREATE_ITEM_CLICK, intent.getStringExtra(TopicType.TYPE));

                        if (bottomSheetLayout.isSheetShowing()) {
                            bottomSheetLayout.dismissSheet();
                        }
                        return true;
                    }
                });
        menuSheetView.inflateMenu(R.menu.create);
        bottomSheetLayout.showWithSheetView(menuSheetView);
    }

}
