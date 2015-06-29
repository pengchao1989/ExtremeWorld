package com.jixianxueyuan.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jixianxueyuan.MyApplication;
import com.jixianxueyuan.fragment.DiscussListFragment;

/**
 * Created by pengchao on 6/29/15.
 */
public class DiscussListFragmentPageAdapter extends FragmentPagerAdapter {

    Context mContext;
    MyApplication myApplication;

    FragmentManager fm;

    public DiscussListFragmentPageAdapter(FragmentManager fm, Context c) {
        super(fm);
        mContext = c;
        this.fm = fm;

        myApplication = (MyApplication) MyApplication.getContext();
    }

    @Override
    public Fragment getItem(int position) {
        return new DiscussListFragment();
    }

    @Override
    public int getCount() {
        return myApplication.getBaseInfoDTO().getHobbyDTOList().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return myApplication.getBaseInfoDTO().getHobbyDTOList().get(position).getName();
    }
}
