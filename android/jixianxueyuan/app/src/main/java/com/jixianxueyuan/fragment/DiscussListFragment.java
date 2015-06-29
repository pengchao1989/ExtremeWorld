package com.jixianxueyuan.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jixianxueyuan.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 6/29/15.
 */
public class DiscussListFragment extends Fragment {


    @InjectView(R.id.discuss_list_fragment_listview)ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.discuss_list_fragment, null);

        ButterKnife.inject(this, view);

        return view;
    }
}
