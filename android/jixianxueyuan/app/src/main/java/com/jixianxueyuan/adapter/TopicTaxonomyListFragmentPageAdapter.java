package com.jixianxueyuan.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.dto.HobbyDTO;
import com.jixianxueyuan.dto.TaxonomyDTO;
import com.jixianxueyuan.fragment.TopicListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengchao on 6/29/15.
 */
public class TopicTaxonomyListFragmentPageAdapter extends FragmentPagerAdapter {

    Context mContext;
    MyApplication myApplication;

    FragmentManager fm;

    HobbyDTO hobbyDTO;

    String topicType;

    List<TaxonomyDTO> taxonomyDTOList;

    public TopicTaxonomyListFragmentPageAdapter(FragmentManager fm, Context c, String topicType) {
        super(fm);
        mContext = c;
        this.fm = fm;
        this.topicType = topicType;

        myApplication = (MyApplication) MyApplication.getContext();

        hobbyDTO = myApplication.getBaseInfoDTO().getHobbyDTOList().get(0);

        analysisTaxonomy();
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
        HobbyDTO hobbyDTO = myApplication.getBaseInfoDTO().getHobbyDTOList().get(0);

        taxonomyDTOList = new ArrayList<TaxonomyDTO>();

        for (TaxonomyDTO taxonomyDTO : hobbyDTO.getTaxonomys())
        {
            if(topicType.equals(taxonomyDTO.getType()))
            {
                taxonomyDTOList.add(taxonomyDTO);
            }

        }

    }
}
