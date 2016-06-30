package com.jixianxueyuan.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.CourseDetailActivity;
import com.jixianxueyuan.adapter.CourseListAdapter;
import com.jixianxueyuan.dto.CourseMinDTO;
import com.jixianxueyuan.dto.CourseTaxonomyDTO;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 2015/4/12.
 */
public class CourseListFragment extends Fragment{

    public static final String TAG = CourseListFragment.class.getSimpleName();
    public static final String INTENT_COURSE_LIST = "INTENT_COURSE_LIST";

    @BindView(R.id.course_list_fragment_listview)
    ListView listView;

    private CourseListAdapter adapter;

    private CourseTaxonomyDTO courseTaxonomyDTO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle.containsKey(INTENT_COURSE_LIST)){
            courseTaxonomyDTO = (CourseTaxonomyDTO) bundle.getSerializable(INTENT_COURSE_LIST);
        }else {
            return;
        }

        adapter = new CourseListAdapter(this.getActivity());
        adapter.addDatas(courseTaxonomyDTO.getCourses());
        Log.d("CourseListFragment", "onCreate");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        Log.d("CourseListFragment", "onCreateView");

        View view = inflater.inflate(R.layout.course_list_fragment, container, false);
        ButterKnife.bind(this, view);

        listView.setAdapter(adapter);

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
    }

    @OnItemClick(R.id.course_list_fragment_listview)void onCourseItemClick(int position){
        CourseMinDTO courseMinDTO = adapter.getItem(position);
        CourseDetailActivity.startActivity(CourseListFragment.this.getContext(), courseMinDTO);
    }
}
