package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.jixianxueyuan.dto.CourseTaxonomysResponseDTO;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 4/30/15.
 */
public class CourseTaxonomyActivity extends BaseActivity{

    @InjectView(R.id.course_taxonomy_activity_expandablelistview)
    ExpandableListView expandableListView;

    CourseListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d("CourseTaxonomyActivity", "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_taxonomy_activity);

        ButterKnife.inject(this);

        adapter = new CourseListAdapter(this);
        expandableListView.setAdapter(adapter);


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Intent intent = new Intent(CourseTaxonomyActivity.this, CourseDetailActivity.class);
                intent.putExtra("courseId", adapter.getChildId(groupPosition, childPosition));
                intent.putExtra("courseName", adapter.getChild(groupPosition, childPosition).getName() );
                startActivity(intent);
                return false;
            }
        });

        requestCourseList();

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
        //requestCourseList();
    }

    private void requestCourseList()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.courseTaxonomy();

        MyRequest<CourseTaxonomysResponseDTO> myRequest = new MyRequest<CourseTaxonomysResponseDTO>(Request.Method.GET,url,CourseTaxonomysResponseDTO.class,
                new Response.Listener<MyResponse<CourseTaxonomysResponseDTO>>() {
                    @Override
                    public void onResponse(MyResponse<CourseTaxonomysResponseDTO> response) {


                        Gson gson = new Gson();

                        if(response.getContent() != null)
                        {
                            adapter.addDatas(response.getContent().getCourseTaxonomyList().get(0).getCourseCatalogues());

                            for(int i = 0; i != adapter.getGroupCount(); i++)
                            {
                                expandableListView.expandGroup(i);
                            }
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

}
