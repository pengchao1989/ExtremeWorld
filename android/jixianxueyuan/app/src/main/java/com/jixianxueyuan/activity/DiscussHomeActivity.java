package com.jixianxueyuan.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;

import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.DiscussListFragmentPageAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 6/29/15.
 */
public class DiscussHomeActivity extends FragmentActivity {

    @InjectView(R.id.discuss_home_activity_pager)ViewPager viewPager;
    @InjectView(R.id.discuss_home_activity_pager_title_strip)PagerTitleStrip pagerTitleStrip;

    DiscussListFragmentPageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.discuss_home_activity);

        ButterKnife.inject(this);


        pageAdapter = new DiscussListFragmentPageAdapter(this.getSupportFragmentManager(), this);

        viewPager.setAdapter(pageAdapter);


    }
}
