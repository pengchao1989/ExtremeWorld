package com.jixianxueyuan.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jixianxueyuan.MyApplication;
import com.jixianxueyuan.dto.HobbyDTO;
import com.jixianxueyuan.fragment.DiscussListFragment;
import com.jixianxueyuan.fragment.TopicListFragment;

/**
 * Created by pengchao on 6/29/15.
 */
public class DiscussListFragmentPageAdapter extends FragmentPagerAdapter {

    Context mContext;
    MyApplication myApplication;

    FragmentManager fm;

    HobbyDTO hobbyDTO;

    public DiscussListFragmentPageAdapter(FragmentManager fm, Context c) {
        super(fm);
        mContext = c;
        this.fm = fm;

        myApplication = (MyApplication) MyApplication.getContext();

        hobbyDTO = myApplication.getBaseInfoDTO().getHobbyDTOList().get(0);
    }

    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("topicType", "discuss");
        bundle.putString("topicTaxonomy", String.valueOf(hobbyDTO.getTaxonomys().get(position).getId()));
        TopicListFragment fragment = new TopicListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return hobbyDTO.getTaxonomys().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {



        return hobbyDTO.getTaxonomys().get(position).getName();
    }
}
