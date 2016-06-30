package com.jixianxueyuan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.CourseListAdapter;
import com.jixianxueyuan.dto.CourseMinDTO;
import com.jixianxueyuan.dto.CourseTaxonomyDTO;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 3/2/16.
 */
public class CourseListActivity extends BaseActivity{


    public static final String TAG = CourseListActivity.class.getSimpleName();
    public static final String INTENT_COURSE_LIST = "INTENT_COURSE_LIST";

    @BindView(R.id.course_list_fragment_listview)
    ListView listView;

    private CourseListAdapter adapter;

    private CourseTaxonomyDTO courseTaxonomyDTO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.course_list_fragment);

        ButterKnife.bind(this);


        Bundle bundle = this.getIntent().getExtras();
        if(bundle.containsKey(INTENT_COURSE_LIST)){
            courseTaxonomyDTO = (CourseTaxonomyDTO) bundle.getSerializable(INTENT_COURSE_LIST);
        }else {
            return;
        }

        adapter = new CourseListAdapter(this);
        adapter.addDatas(courseTaxonomyDTO.getCourses());

        listView.setAdapter(adapter);

        Log.d("CourseListFragment", "onCreate");
    }


    @OnItemClick(R.id.course_list_fragment_listview)void onCourseItemClick(int position){
        CourseMinDTO courseMinDTO = adapter.getItem(position);
        CourseDetailActivity.startActivity(this, courseMinDTO);
    }

    public static void startActivity(Context context, CourseTaxonomyDTO courseTaxonomyDTO){
        Intent intent = new Intent(context, CourseListActivity.class);
        intent.putExtra(INTENT_COURSE_LIST, courseTaxonomyDTO);
        context.startActivity(intent);
    }
}
