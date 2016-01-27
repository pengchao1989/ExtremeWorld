package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.CourseDetailListAdapter;
import com.jixianxueyuan.adapter.TopicDetailListAdapter;
import com.jixianxueyuan.adapter.TopicListAdapter;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.dto.CourseDto;
import com.jixianxueyuan.dto.CourseMinDTO;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.DateTimeFormatter;
import com.jixianxueyuan.widget.MyActionBar;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 7/2/15.
 */
public class CourseDetailActivity extends BaseActivity {

    private static final String INTENT_COURSE_MIN_DTO = "INTENT_COURSE_MIN_DTO";

    @InjectView(R.id.course_detail_actionbar)
    MyActionBar myActionBar;
    @InjectView(R.id.course_detail_listview)
    ListView listView;

    TopicListAdapter adapter;

    View headView;
    View footerView;
    HeadViewHolder headViewHolder;

    private CourseMinDTO courseMinDTO;
    private CourseDto courseDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.course_detail_activity);
        ButterKnife.inject(this);

        getBundle();

        initHeadView();
        initFooterView();

        adapter = new TopicListAdapter(this);
        listView.setAdapter(adapter);

        requestCourseDetail();
    }

    private void getBundle() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle.containsKey(INTENT_COURSE_MIN_DTO)) {
            courseMinDTO = (CourseMinDTO) bundle.getSerializable(INTENT_COURSE_MIN_DTO);
        } else {
            finish();
        }
    }

    private void initHeadView() {
        myActionBar.setActionOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        headView = LayoutInflater.from(this).inflate(R.layout.course_detail_head_view, null);

        headViewHolder = new HeadViewHolder(headView);

        headViewHolder.titleTextView.setText(courseMinDTO.getName());
        headViewHolder.userNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetailActivity.this, UserHomeActivity.class);
                intent.putExtra("userMinDTO", courseDto.getUser());
                startActivity(intent);
            }
        });

        listView.addHeaderView(headView);

    }

    private void initFooterView() {
        footerView = LayoutInflater.from(this).inflate(R.layout.list_footer_button, null);
        Button button = (Button) footerView.findViewById(R.id.list_footer_button);
        button.setText("上传教学");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetailActivity.this, CreateTopicActivity.class);
                intent.putExtra(TopicType.TYPE, TopicType.COURSE);
                intent.putExtra("courseId", courseDto.getId());
                intent.putExtra("courseType", "explain");
                startActivity(intent);
            }
        });
        listView.addFooterView(footerView);
    }

    private void requestCourseDetail() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.course() + courseMinDTO.getId();

        MyRequest<CourseDto> myRequest = new MyRequest<CourseDto>(Request.Method.GET, url, CourseDto.class,
                new Response.Listener<MyResponse<CourseDto>>() {

                    @Override
                    public void onResponse(MyResponse<CourseDto> response) {

                        courseDto = response.getContent();
                        headViewHolder.contentTextView.setText(courseDto.getContent());
                        headViewHolder.userNameTextView.setText(courseDto.getUser().getName());

                        //String timeAgo = DateTimeFormatter.getTimeAgo(CourseDetailActivity.this, courseDto.getModifyTime());
                        headViewHolder.modifyTimeTextView.setText("于" + courseDto.getModifyTime() + "编辑");

                        //请求用户示例教程
                        requestTopicList();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(myRequest);
    }

    private void requestTopicList() {

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ServerMethod.topic() + "?type=course&magicType=sb&courseId=" + courseMinDTO.getId() + "&sortType=agree";

        MyPageRequest<TopicDTO> myPageRequest = new MyPageRequest<TopicDTO>(Request.Method.GET, url, TopicDTO.class,
                new Response.Listener<MyResponse<MyPage<TopicDTO>>>() {

                    @Override
                    public void onResponse(MyResponse<MyPage<TopicDTO>> response) {

                        if (response.getStatus() == MyResponse.status_ok) {
                            MyPage<TopicDTO> myPage = response.getContent();
                            adapter.addDatas(myPage.getContents());
                        } else {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }
        );

        queue.add(myPageRequest);
    }


    public class HeadViewHolder {
        @InjectView(R.id.course_detail_head_title)
        TextView titleTextView;
        @InjectView(R.id.course_detail_head_content)
        TextView contentTextView;
        @InjectView(R.id.course_detail_head_user_name)
        TextView userNameTextView;
        @InjectView(R.id.course_detail_head_modify_time)
        TextView modifyTimeTextView;

        public HeadViewHolder(View headView) {
            ButterKnife.inject(this, headView);
        }
    }

    @OnItemClick(R.id.course_detail_listview)
    void onItemClick(int position) {
        if (position == 0) {
            return;
        }
        Intent intent = new Intent(CourseDetailActivity.this, TopicDetailActivity.class);
        intent.putExtra("topic", adapter.getItem(position - 1));
        startActivity(intent);
    }

    public static void startActivity(Context context, CourseMinDTO courseMinDTO) {
        Intent intent = new Intent(context, CourseDetailActivity.class);
        intent.putExtra(INTENT_COURSE_MIN_DTO, courseMinDTO);
        context.startActivity(intent);
    }

}
