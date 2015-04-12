package com.jixianxueyuan.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.HomeFragmentAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeActivity extends FragmentActivity {

    @InjectView(R.id.activity_home_fragment_viewpager)
    ViewPager viewPager;

    HomeFragmentAdapter fragmentAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.inject(this);

        fragmentAdapter = new HomeFragmentAdapter(this.getSupportFragmentManager(),this);
        viewPager.setAdapter(fragmentAdapter);
    }

}

