package com.jixianxueyuan.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TabHost;

import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.HomeFragmentAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class HomeActivity extends ActionBarActivity implements MaterialTabListener {

    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.tabHost) MaterialTabHost tabHost;
    @InjectView(R.id.activity_home_fragment_viewpager)ViewPager viewPager;

    HomeFragmentAdapter fragmentAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.inject(this);

        this.setSupportActionBar(toolbar);


        fragmentAdapter = new HomeFragmentAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);

            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < fragmentAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(fragmentAdapter.getPageTitle(i))
                            .setTabListener(this)
            );

        }
    }


    @Override
    public void onTabSelected(MaterialTab materialTab) {
        viewPager.setCurrentItem(materialTab.getPosition());

    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }
}

