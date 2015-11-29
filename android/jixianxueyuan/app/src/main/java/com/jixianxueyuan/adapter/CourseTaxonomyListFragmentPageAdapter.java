package com.jixianxueyuan.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jixianxueyuan.R;
import com.jixianxueyuan.dto.CourseTaxonomyDTO;
import com.jixianxueyuan.fragment.CourseListFragment;

import java.util.ArrayList;
import java.util.List;

import me.amiee.nicetab.NiceTabLayout;

/**
 * Created by pengchao on 11/24/15.
 */
public class CourseTaxonomyListFragmentPageAdapter extends FragmentPagerAdapter
        implements NiceTabLayout.IconTabProvider{

    private Context mContext;
    private FragmentManager fm;

    private List<CourseTaxonomyDTO> courseTaxonomyDTOList;

    private List<SamplePagerItem> mTabs = new ArrayList<>();

    public CourseTaxonomyListFragmentPageAdapter(FragmentManager fm, Context c) {
        super(fm);
        this.mContext = c;
        this.fm = fm;
        courseTaxonomyDTOList = new ArrayList<CourseTaxonomyDTO>();
    }

    public void setData(List<CourseTaxonomyDTO> courseTaxonomyDTOs){
        courseTaxonomyDTOList.clear();
        courseTaxonomyDTOList.addAll(courseTaxonomyDTOs);
        initTabs();
        this.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new CourseListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CourseListFragment.INTENT_COURSE_LIST, courseTaxonomyDTOList.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return courseTaxonomyDTOList.size();
    }

    @Override
    public int getPageIconResId(int position) {
        return mTabs.get(position).getAndroidIconResId();
    }

    public List<SamplePagerItem> getTabs() {
        return mTabs;
    }

    private void initTabs(){
        for(CourseTaxonomyDTO taxonomyDTO : courseTaxonomyDTOList){
            mTabs.add(new SamplePagerItem(
                    taxonomyDTO.getName(), // Title
                    R.drawable.ic_home, // Icon
                    R.drawable.ic_home_a, // Icon
                    mContext.getResources().getColor(R.color.white), // Indicator color
                    mContext.getResources().getColor(R.color.white) // Divider color
            ));
        }
    }

    public static class SamplePagerItem {
        private final CharSequence mTitle;
        private final int mIosIconResId;
        private final int mAndroidIconResId;
        private final int mIndicatorColor;
        private final int mDividerColor;

        SamplePagerItem(CharSequence title, int iosIconResId, int androidIconResId, int indicatorColor, int dividerColor) {
            mTitle = "title";
            mIosIconResId = iosIconResId;
            mAndroidIconResId = androidIconResId;
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
        }

        CharSequence getTitle() {
            return mTitle;
        }

        public int getIosIconResId() {
            return mIosIconResId;
        }

        public int getAndroidIconResId() {
            return mAndroidIconResId;
        }

        public int getIndicatorColor() {
            return mIndicatorColor;
        }

        public int getDividerColor() {
            return mDividerColor;
        }
    }
}
