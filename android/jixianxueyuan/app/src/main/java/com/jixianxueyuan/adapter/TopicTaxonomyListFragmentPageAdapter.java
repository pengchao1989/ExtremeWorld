package com.jixianxueyuan.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jixianxueyuan.R;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.dto.HobbyDTO;
import com.jixianxueyuan.dto.TaxonomyDTO;
import com.jixianxueyuan.fragment.TopicListFragment;

import java.util.ArrayList;
import java.util.List;

import me.amiee.nicetab.NiceTabLayout;

/**
 * Created by pengchao on 6/29/15.
 */
public class TopicTaxonomyListFragmentPageAdapter extends FragmentPagerAdapter implements NiceTabLayout.IconTabProvider{

    Context mContext;
    MyApplication myApplication;

    FragmentManager fm;

    HobbyDTO hobbyDTO;

    String topicType;

    List<TaxonomyDTO> taxonomyDTOList;

    private List<SamplePagerItem> mTabs = new ArrayList<>();

    public TopicTaxonomyListFragmentPageAdapter(FragmentManager fm, Context c, String topicType) {
        super(fm);
        mContext = c;
        this.fm = fm;
        this.topicType = topicType;

        myApplication = (MyApplication) MyApplication.getContext();

        hobbyDTO = myApplication.getHandshakeDTO().getHobbyDTOList().get(0);

        analysisTaxonomy();

        initTabs();
    }

    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putString(TopicType.STRING, topicType);
        bundle.putString("topicTaxonomyId", String.valueOf(taxonomyDTOList.get(position).getId()));
        TopicListFragment fragment = new TopicListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return taxonomyDTOList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return taxonomyDTOList.get(position).getName();
    }

    private void analysisTaxonomy()
    {
        HobbyDTO hobbyDTO = myApplication.getHandshakeDTO().getHobbyDTOList().get(0);

        taxonomyDTOList = new ArrayList<TaxonomyDTO>();

        for (TaxonomyDTO taxonomyDTO : hobbyDTO.getTaxonomys())
        {
            if(topicType.equals(taxonomyDTO.getType()))
            {
                taxonomyDTOList.add(taxonomyDTO);
            }

        }

    }

    @Override
    public int getPageIconResId(int position) {
        return mTabs.get(position).getAndroidIconResId();
    }

    public List<SamplePagerItem> getmTabs() {
        return mTabs;
    }

    private void initTabs(){
        for(TaxonomyDTO taxonomyDTO : taxonomyDTOList){
            mTabs.add(new SamplePagerItem(
                    taxonomyDTO.getName(), // Title
                    R.drawable.ic_home, // Icon
                    R.drawable.ic_home_a, // Icon
                    mContext.getResources().getColor(R.color.blue), // Indicator color
                    mContext.getResources().getColor(R.color.blue) // Divider color
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
            mTitle = title;
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
