package com.jixianxueyuan.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.server.ServerMethod;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 2015/4/12.
 */
public class CourseListFragment extends Fragment{

    public static final String TAG = CourseListFragment.class.getSimpleName();

    @InjectView(R.id.course_list_fragment_expandablelistview)
    ExpandableListView expandableListView;

    CourseListAdapter adapter;

    boolean isRefreshData = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new CourseListAdapter(this.getActivity());
        Log.d("CourseListFragment", "onCreate");


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        Log.d("CourseListFragment", "onCreateView");

        View view = inflater.inflate(R.layout.course_list_fragment, container, false);
        ButterKnife.inject(this, view);

        expandableListView.setAdapter(adapter);

        return view;
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
        refreshCourseList();
    }

    private void refreshCourseList()
    {
        if(!isRefreshData)
        {
            requestCourseList();
        }
    }

    private void requestCourseList()
    {
        RequestQueue queue = Volley.newRequestQueue(this.getActivity());
        String url = ServerMethod.courseTaxonomy();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Gson gson = new Gson();
                        List<CourseTaxonomyDTO> courseTaxonomyDTOs = gson.fromJson(response, new TypeToken<List<CourseTaxonomyDTO>>(){}.getType());
                        if(courseTaxonomyDTOs != null)
                        {

                            adapter.addDatas(courseTaxonomyDTOs.get(1).getCourseCatalogues());

                            isRefreshData = true;
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
