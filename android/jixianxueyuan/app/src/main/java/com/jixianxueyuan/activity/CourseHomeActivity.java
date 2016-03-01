package com.jixianxueyuan.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.CourseTaxonomyListAdapter;
import com.jixianxueyuan.dto.CourseTaxonomysResponseDTO;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.ACache;


import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 3/1/16.
 */
public class CourseHomeActivity extends BaseActivity {

    @InjectView(R.id.course_home_taxonomy_list_view)ListView listView;

    CourseTaxonomyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.course_home_activity);

        ButterKnife.inject(this);

        adapter = new CourseTaxonomyListAdapter(this);
        listView.setAdapter(adapter);

        initCourseList();
    }

    @OnItemClick(R.id.course_home_taxonomy_list_view)void OnItemClicked(int position){
        CourseListActivity.startActivity(this, adapter.getItem(position));
    }

    private void initCourseList(){
        ACache aCache = ACache.get(this);
        String url = ServerMethod.courseTaxonomy();
        CourseTaxonomysResponseDTO courseTaxonomysResponseDTO= (CourseTaxonomysResponseDTO) aCache.getAsObject(url);
        if(courseTaxonomysResponseDTO != null){
            adapter.setData(courseTaxonomysResponseDTO.getCourseTaxonomyList());
        }else {
            requestCourseList();
        }
    }


    private void requestCourseList()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = ServerMethod.courseTaxonomy();

        MyRequest<CourseTaxonomysResponseDTO> myRequest = new MyRequest<CourseTaxonomysResponseDTO>(Request.Method.GET,url,CourseTaxonomysResponseDTO.class,
                new Response.Listener<MyResponse<CourseTaxonomysResponseDTO>>() {
                    @Override
                    public void onResponse(MyResponse<CourseTaxonomysResponseDTO> response) {

                        if(response.getContent() != null) {

                            adapter.setData(response.getContent().getCourseTaxonomyList());

                            ACache aCache = ACache.get(CourseHomeActivity.this);
                            aCache.put(url, response.getContent(), ACache.TIME_DAY);
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
