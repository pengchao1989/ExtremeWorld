package com.jixianxueyuan.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.CourseTaxonomyListFragmentPageAdapter;
import com.jixianxueyuan.dto.CourseTaxonomysResponseDTO;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.amiee.nicetab.NiceTabLayout;
import me.amiee.nicetab.NiceTabStrip;

/**
 * Created by pengchao on 4/30/15.
 */
public class CourseHomeActivity extends BaseActivity{

    @InjectView(R.id.course_home_activity_pager)ViewPager viewPager;
    @InjectView(R.id.course_home_activity_pager_title_strip)NiceTabLayout niceTabLayout;

    CourseTaxonomyListFragmentPageAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("CourseHomeActivity", "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_home_activity);

        ButterKnife.inject(this);

        mAdapter = new CourseTaxonomyListFragmentPageAdapter(getSupportFragmentManager(), this);

        requestCourseList();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void requestCourseList()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.courseTaxonomy();

        MyRequest<CourseTaxonomysResponseDTO> myRequest = new MyRequest<CourseTaxonomysResponseDTO>(Request.Method.GET,url,CourseTaxonomysResponseDTO.class,
                new Response.Listener<MyResponse<CourseTaxonomysResponseDTO>>() {
                    @Override
                    public void onResponse(MyResponse<CourseTaxonomysResponseDTO> response) {

                        if(response.getContent() != null) {

                            viewPager.setAdapter(mAdapter);
                            mAdapter.setData(response.getContent().getCourseTaxonomyList());

                            setTabs();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                });

        queue.add(myRequest);
    }

    private void setTabs() {
        niceTabLayout.setViewPager(viewPager);
        niceTabLayout.setTabStripColorize(new NiceTabStrip.TabStripColorize() {

            @Override
            public int getIndicatorColor(int position) {
                return mAdapter.getTabs().get(position).getIndicatorColor();
            }

            @Override
            public int getDividerColor(int position) {
                return mAdapter.getTabs().get(position).getDividerColor();
            }
        });
        niceTabLayout.setTabColorize(new NiceTabLayout.TabColorize() {

            @Override
            public int getDefaultTabColor(int position) {
                return Color.WHITE;
            }

            @Override
            public int getSelectedTabColor(int position) {
                return mAdapter.getTabs().get(position).getIndicatorColor();
            }
        });

        niceTabLayout.setTabMode(NiceTabLayout.TabMode.TITLE_ONLY);
    }

}
