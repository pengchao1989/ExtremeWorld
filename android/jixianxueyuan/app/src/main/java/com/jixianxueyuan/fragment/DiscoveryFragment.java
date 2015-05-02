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
import com.jixianxueyuan.activity.CourseTaxonomyActivity;
import com.jixianxueyuan.adapter.DiscoveryLIstAdapter;

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

    DiscoveryLIstAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        adapter = new DiscoveryLIstAdapter(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.discovery_fragment, container, false);

        ButterKnife.inject(this, view);

        listView.setAdapter(adapter);

        Resources resources = this.getResources();
        String[] discoveryNames =resources.getStringArray(R.array.discovery_names);

        TypedArray ar = resources.obtainTypedArray(R.array.discovery_images);
        int len = ar.length();
        int[] discoveryImages = new int[len];

        List<DiscoveryLIstAdapter.DiscoveryItem> discoveryItemList = new ArrayList<DiscoveryLIstAdapter.DiscoveryItem>();
        for(int i = 0; i < len; i++)
        {
            DiscoveryLIstAdapter.DiscoveryItem item = new DiscoveryLIstAdapter.DiscoveryItem();
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
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        intent = new Intent(DiscoveryFragment.this.getActivity(), CourseTaxonomyActivity.class);
                        break;
                    case 4:
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
