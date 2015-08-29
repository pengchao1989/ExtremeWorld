package com.jixianxueyuan.activity;

import android.app.Activity;
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
import com.jixianxueyuan.dto.CourseDto;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.DateTimeFormatter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 7/2/15.
 */
public class CourseDetailActivity extends Activity {

    @InjectView(R.id.course_detail_listview)ListView listView;

    TopicListAdapter adapter;

    View headView;
    View footerView;
    HeadViewHolder headViewHolder;

    Long courseId;
    String courseName;
    CourseDto courseDto;

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

    private void getBundle()
    {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle.containsKey("courseId"))
        {
            courseId = bundle.getLong("courseId");
        }
        if(bundle.containsKey("courseName"))
        {
            courseName = bundle.getString("courseName");
        }
    }

    private void initHeadView()
    {
        headView = LayoutInflater.from(this).inflate(R.layout.course_detail_head_view, null);

        headViewHolder = new HeadViewHolder(headView);

        headViewHolder.titleTextView.setText(courseName);

        listView.addHeaderView(headView);

    }

    private void initFooterView()
    {
        footerView = LayoutInflater.from(this).inflate(R.layout.list_footer_textview, null);
        TextView textView = (TextView) footerView.findViewById(R.id.list_footer_textview);
        textView.setText("进入官网www.jixianxueyuan.com上传你的教学");
        listView.addFooterView(footerView);
    }

    private void requestCourseDetail()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.course() + courseId;

        MyRequest<CourseDto> myRequest = new MyRequest<CourseDto>(Request.Method.GET,url, CourseDto.class,
                new Response.Listener<MyResponse<CourseDto>>(){

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

    private void requestTopicList()
    {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ServerMethod.course_explain() + courseId;

        MyPageRequest<TopicDTO> myPageRequest = new MyPageRequest<TopicDTO>(Request.Method.GET,url, TopicDTO.class,
                new Response.Listener<MyResponse<MyPage<TopicDTO>>>(){

                    @Override
                    public void onResponse(MyResponse<MyPage<TopicDTO>> response) {

                        if(response.getStatus() == MyResponse.status_ok)
                        {
                            MyPage<TopicDTO> myPage = response.getContent();
                            adapter.addDatas(myPage.getContents());
                        }
                        else
                        {

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


    public class HeadViewHolder
    {
        @InjectView(R.id.course_detail_head_title)TextView titleTextView;
        @InjectView(R.id.course_detail_head_content)TextView contentTextView;
        @InjectView(R.id.course_detail_head_user_name)TextView userNameTextView;
        @InjectView(R.id.course_detail_head_modify_time)TextView modifyTimeTextView;

        public HeadViewHolder(View headView)
        {
            ButterKnife.inject(this, headView);
        }
    }

    @OnItemClick(R.id.course_detail_listview)void onItemClick(int position){
        if(position == 0){
            return;
        }
        Intent intent = new Intent(CourseDetailActivity.this, TopicDetailActivity.class);
        intent.putExtra("topic", adapter.getItem(position - 1));
        startActivity(intent);
    }

    @OnClick(R.id.course_detail_head_user_name)void onUserClick(){
        Intent intent = new Intent(CourseDetailActivity.this, UserHomeActivity.class);
        intent.putExtra("userMinDTO", courseDto.getUser());
        startActivity(intent);
    }

}
