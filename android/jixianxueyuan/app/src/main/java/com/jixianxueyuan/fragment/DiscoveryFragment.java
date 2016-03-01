package com.jixianxueyuan.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.CourseHomeActivity;
import com.jixianxueyuan.activity.CourseOldHomeActivity;
import com.jixianxueyuan.activity.NearFriendActivity;
import com.jixianxueyuan.activity.NewHomeActivity;
import com.jixianxueyuan.activity.SiteListActivity;
import com.jixianxueyuan.activity.TopicTaxonomyHomeActivity;
import com.jixianxueyuan.adapter.DiscoveryListAdapter;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.config.UmengEventId;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 4/20/15.
 */
public class DiscoveryFragment extends Fragment {

    public static final String tag = DiscoveryFragment.class.getSimpleName();

    @InjectView(R.id.discovery_fragment_listview)ListView listView;

    DiscoveryListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        adapter = new DiscoveryListAdapter(this.getActivity());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.discovery_fragment, container, false);

        ButterKnife.inject(this, view);

        listView.setAdapter(adapter);

        Resources resources = this.getResources();
        String[] discoveryNames =resources.getStringArray(R.array.discovery_names);

        TypedArray ar = resources.obtainTypedArray(R.array.discovery_images);
        int len = ar.length();
        int[] discoveryImages = new int[len];

        List<DiscoveryListAdapter.DiscoveryItem> discoveryItemList = new ArrayList<DiscoveryListAdapter.DiscoveryItem>();
        for(int i = 0; i < len; i++)
        {
            DiscoveryListAdapter.DiscoveryItem item = new DiscoveryListAdapter.DiscoveryItem();
            item.setName(discoveryNames[i]);
            item.setImageResourceId(ar.getResourceId(i,0));
            discoveryItemList.add(item);
        }

        adapter.setDatas(discoveryItemList);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = null;
                switch (position)
                {
                    case 0:
                        MobclickAgent.onEvent(DiscoveryFragment.this.getContext(), UmengEventId.DISCOVERY_NEWS_CLICK);
                        intent = new Intent(DiscoveryFragment.this.getActivity(), TopicTaxonomyHomeActivity.class);
                        intent.putExtra(TopicType.TYPE, TopicType.NEWS);
                        break;
                    case 1:
                        MobclickAgent.onEvent(DiscoveryFragment.this.getContext(), UmengEventId.DISCOVERY_VIDEO_CLICK);
                        intent = new Intent(DiscoveryFragment.this.getActivity(), TopicTaxonomyHomeActivity.class);
                        intent.putExtra(TopicType.TYPE, TopicType.VIDEO);
                        break;
                    case 2:
                        MobclickAgent.onEvent(DiscoveryFragment.this.getContext(), UmengEventId.DISCOVERY_COURSE_CLICK);
                        intent = new Intent(DiscoveryFragment.this.getActivity(), CourseHomeActivity.class);
                        break;

/*                    case 4:
                        intent = new Intent(DiscoveryFragment.this.getActivity(), TopicTaxonomyHomeActivity.class);
                        intent.putExtra(TopicType.TYPE, TopicType.S_VIDEO);
                        break;*/

                    case 3:
                        MobclickAgent.onEvent(DiscoveryFragment.this.getContext(), UmengEventId.DISCOVERY_SITE_CLICK);
                        intent = new Intent(DiscoveryFragment.this.getActivity(), SiteListActivity.class);
                        break;
                    case 4:
                        MobclickAgent.onEvent(DiscoveryFragment.this.getContext(), UmengEventId.DISCOVERY_NEAR_FRIEND_CLICK);
                        intent = new Intent(DiscoveryFragment.this.getActivity(), NearFriendActivity.class);
                        break;
                    case 5:
                        intent = new Intent(DiscoveryFragment.this.getActivity(), NewHomeActivity.class);
                        //intent = new Intent(DiscoveryFragment.this.getActivity(), MarketHomeActivity.class);
                        break;
                }

                if(intent != null)
                {
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }


    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }
}
