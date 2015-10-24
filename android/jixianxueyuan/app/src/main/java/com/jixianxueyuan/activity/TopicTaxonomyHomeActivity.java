package com.jixianxueyuan.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.TopicTaxonomyListFragmentPageAdapter;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.MyActionBar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.amiee.nicetab.NiceTabLayout;
import me.amiee.nicetab.NiceTabStrip;

/**
 * Created by pengchao on 6/29/15.
 */
public class TopicTaxonomyHomeActivity extends BaseActivity {

    @InjectView(R.id.discuss_home_actionbar)MyActionBar myActionBar;
    @InjectView(R.id.discuss_home_activity_pager)ViewPager viewPager;
    @InjectView(R.id.discuss_home_activity_pager_title_strip)NiceTabLayout niceTabLayout;

    TopicTaxonomyListFragmentPageAdapter pageAdapter;

    String topicType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.discuss_home_activity);

        ButterKnife.inject(this);

        Intent intent = this.getIntent();
        if(intent.hasExtra(TopicType.TYPE))
        {
            topicType = intent.getStringExtra(TopicType.TYPE);
            MyLog.d("TopicTaxonomyHomeActivity", "topicType=" + topicType);

            switch (topicType)
            {
                case TopicType.NEWS:
                    myActionBar.setTitle(this.getString(R.string.news));
                    niceTabLayout.setVisibility(View.GONE);
                    break;
                case TopicType.DISCUSS:
                    myActionBar.setTitle(this.getString(R.string.discuss));
                    break;
                case TopicType.S_VIDEO:
                    myActionBar.setTitle(this.getString(R.string.s_video));
                    break;

                case TopicType.VIDEO:
                    myActionBar.setTitle(this.getString(R.string.video));
                    break;
            }
        }
        else
        {
            finish();
        }


        pageAdapter = new TopicTaxonomyListFragmentPageAdapter(this.getSupportFragmentManager(), this, topicType);

        viewPager.setAdapter(pageAdapter);
        niceTabLayout.setViewPager(viewPager);

        niceTabLayout.setTabStripColorize(new NiceTabStrip.TabStripColorize() {

            @Override
            public int getIndicatorColor(int position) {
                return pageAdapter.getmTabs().get(position).getIndicatorColor();
            }

            @Override
            public int getDividerColor(int position) {
                return pageAdapter.getmTabs().get(position).getDividerColor();
            }
        });

        niceTabLayout.setTabColorize(new NiceTabLayout.TabColorize() {

            @Override
            public int getDefaultTabColor(int position) {
                return Color.WHITE;
            }

            @Override
            public int getSelectedTabColor(int position) {
                return pageAdapter.getmTabs().get(position).getIndicatorColor();
            }
        });

        niceTabLayout.setTabMode(NiceTabLayout.TabMode.TITLE_ONLY);

    }




}
