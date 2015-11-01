package com.jixianxueyuan.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.jixianxueyuan.R;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.fragment.CourseListFragment;
import com.jixianxueyuan.fragment.DiscoveryFragment;
import com.jixianxueyuan.fragment.MineFragment;
import com.jixianxueyuan.fragment.TopicListFragment;
import com.jixianxueyuan.fragment.VideoListFragment;

/**
 * Created by pengchao on 2015/4/12.
 */
public class HomeFragmentAdapter extends FragmentPagerAdapter {

    Context mContext;

    FragmentManager fm;


    public HomeFragmentAdapter(FragmentManager fm, Context c) {
        super(fm);

        mContext = c;
        this.fm = fm;
    }

    //getItem 实现中不需要FragmentManager来进行管理，直接new就可以了，每个Item只会get一次
    //fragment onCreate会执行一次，fragment会一直驻在内存，view会销毁，下次进入视窗会调用onCreateView
    @Override
    public Fragment getItem(int position) {

        Log.d("HomeFragmentAdapter","getItem position=" + position);

        Fragment fragment = null;
        switch (position)
        {
            case 0:
                Bundle bundle = new Bundle();
                bundle.putString("topicType", TopicType.ALL);
                fragment = new TopicListFragment();
                fragment.setArguments(bundle);
                break;

            case 1:
                fragment = new DiscoveryFragment();
                break;

            case 2:
                fragment = new MineFragment();
                break;
            case 3:
                fragment = new MineFragment();
                break;

            case 4:
                fragment = new MineFragment();
                break;
        }


        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getResources().getString(R.string.topic);
            case 1:
                return mContext.getResources().getString(R.string.discovery);
            case 2:
                return mContext.getResources().getString(R.string.mine);


        }
        return null;
    }
}
