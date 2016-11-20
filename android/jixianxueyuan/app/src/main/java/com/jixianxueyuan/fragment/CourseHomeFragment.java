package com.jixianxueyuan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.BaseActivity;
import com.jixianxueyuan.activity.CourseDetailActivity;
import com.jixianxueyuan.activity.CourseListActivity;
import com.jixianxueyuan.adapter.CourseListAdapter;
import com.jixianxueyuan.adapter.CourseTaxonomyListAdapter;
import com.jixianxueyuan.dto.CourseMinDTO;
import com.jixianxueyuan.dto.CourseTaxonomysResponseDTO;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.ACache;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 3/1/16.
 */
public class CourseHomeFragment extends Fragment {

    @BindView(R.id.course_home_taxonomy_list_view)ListView taxonomyListView;
    @BindView(R.id.course_home_course_list_view)ListView courseListView;

    CourseTaxonomyListAdapter taxonomyListAdapter;
    CourseListAdapter courseListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_home_activity, container, false);
        ButterKnife.bind(this, view);

        taxonomyListAdapter = new CourseTaxonomyListAdapter(this.getContext());
        taxonomyListView.setAdapter(taxonomyListAdapter);

        courseListAdapter = new CourseListAdapter(this.getActivity());
        courseListView.setAdapter(courseListAdapter);


        initCourseList();

        return view;
    }

    @OnItemClick(R.id.course_home_taxonomy_list_view)void OnItemClicked(int position){
        //CourseListActivity.startActivity(this.getContext(), taxonomyListAdapter.getItem(position));
        courseListAdapter.setDatas(taxonomyListAdapter.getItem(position).getCourses());
        taxonomyListAdapter.setCurrentSelected(position);
    }

    @OnItemClick(R.id.course_home_course_list_view)void onCourseItemClick(int position){
        CourseMinDTO courseMinDTO = courseListAdapter.getItem(position);
        CourseDetailActivity.startActivity(this.getContext(), courseMinDTO);
    }

    private void initCourseList(){
        ACache aCache = ACache.get(this.getContext());
        String url = ServerMethod.courseTaxonomy();
        CourseTaxonomysResponseDTO courseTaxonomysResponseDTO= (CourseTaxonomysResponseDTO) aCache.getAsObject(url);
        if(courseTaxonomysResponseDTO != null){
            taxonomyListAdapter.setData(courseTaxonomysResponseDTO.getCourseTaxonomyList());
        }else {
            requestCourseList();
        }

        if (taxonomyListAdapter.getCount() > 0){
            courseListAdapter.addDatas(taxonomyListAdapter.getItem(0).getCourses());
        }
    }


    private void requestCourseList()
    {
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        final String url = ServerMethod.courseTaxonomy();

        MyRequest<CourseTaxonomysResponseDTO> myRequest = new MyRequest<CourseTaxonomysResponseDTO>(Request.Method.GET,url,CourseTaxonomysResponseDTO.class,
                new Response.Listener<MyResponse<CourseTaxonomysResponseDTO>>() {
                    @Override
                    public void onResponse(MyResponse<CourseTaxonomysResponseDTO> response) {

                        if(response.getContent() != null) {

                            taxonomyListAdapter.setData(response.getContent().getCourseTaxonomyList());

                            if (taxonomyListAdapter.getCount() > 0){
                                courseListAdapter.addDatas(taxonomyListAdapter.getItem(0).getCourses());
                            }

                            ACache aCache = ACache.get(CourseHomeFragment.this.getContext());
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
