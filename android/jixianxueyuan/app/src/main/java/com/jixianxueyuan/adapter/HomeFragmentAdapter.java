package com.jixianxueyuan.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jixianxueyuan.R;
import com.jixianxueyuan.fragment.CourseListFragment;
import com.jixianxueyuan.fragment.TopicListFragment;
import com.jixianxueyuan.fragment.VideoListFragment;

/**
 * Created by pengchao on 2015/4/12.
 */
public class HomeFragmentAdapter extends FragmentPagerAdapter {

    Context mContext;

    public HomeFragmentAdapter(FragmentManager fm, Context c) {
        super(fm);

        mContext = c;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        switch (position)
        {
            case 0:
                fragment = new TopicListFragment();

                break;

            case 1:
                fragment = new VideoListFragment();
                break;

            case 2:
                fragment = new CourseListFragment();
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
                return mContext.getResources().getString(R.string.video);
            case 2:
                return "教学";
        }
        return null;
    }
}
