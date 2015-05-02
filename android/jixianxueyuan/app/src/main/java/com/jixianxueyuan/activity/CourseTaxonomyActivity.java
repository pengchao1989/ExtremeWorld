package com.jixianxueyuan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.CourseListAdapter;
import com.jixianxueyuan.dto.CourseTaxonomyDTO;
import com.jixianxueyuan.server.ServerMethod;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 4/30/15.
 */
public class CourseTaxonomyActivity extends Activity{

    @InjectView(R.id.course_taxonomy_activity_expandablelistview)
    ExpandableListView expandableListView;
    @InjectView(R.id.course_taxonomy_activity_swipeback_layout)
    SwipeBackLayout swipeBackLayout;

    CourseListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d("CourseTaxonomyActivity", "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_taxonomy_activity);

        ButterKnife.inject(this);

        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        adapter = new CourseListAdapter(this);
        expandableListView.setAdapter(adapter);



    }
    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        requestCourseList();
    }

    private void requestCourseList()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.courseTaxonomy;

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("CourseTaxonomyActivity", "response=" + response);

                        Gson gson = new Gson();
                        List<CourseTaxonomyDTO> courseTaxonomyDTOs = gson.fromJson(response, new TypeToken<List<CourseTaxonomyDTO>>(){}.getType());
                        if(courseTaxonomyDTOs != null)
                        {
                            adapter.addDatas(courseTaxonomyDTOs);
                            adapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(stringRequest);
    }

}
