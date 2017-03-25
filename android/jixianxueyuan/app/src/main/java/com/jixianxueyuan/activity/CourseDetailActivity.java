package com.jixianxueyuan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jixianxueyuan.R;
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
import com.jixianxueyuan.widget.ClickLoadMoreView;
import com.jixianxueyuan.widget.MyActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 7/2/15.
 */
public class CourseDetailActivity extends BaseActivity {

    private static final String INTENT_COURSE_MIN_DTO = "INTENT_COURSE_MIN_DTO";

    @BindView(R.id.course_detail_actionbar)
    MyActionBar myActionBar;
    @BindView(R.id.course_detail_listview)
    ListView listView;

    TopicListAdapter adapter;
    private int currentPage = 0;
    private int totalPage = 0;

    View headView;
    View footerView;
    HeadViewHolder headViewHolder;
    private ClickLoadMoreView loadMoreButton;

    private CourseMinDTO courseMinDTO;
    private CourseDto courseDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.course_detail_activity);
        ButterKnife.bind(this);

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
        myActionBar.setActionOnClickListener(new MyActionBar.MyActionBarListener() {
            @Override
            public void onFirstActionClicked() {

            }

            @Override
            public void onSecondActionClicked() {

            }
        });


        headView = LayoutInflater.from(this).inflate(R.layout.course_detail_head_view, null);

        headViewHolder = new HeadViewHolder(headView);

        headViewHolder.titleTextView.setText(courseMinDTO.getName());
        headViewHolder.userNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetailActivity.this, UserHomeActivity.class);
                intent.putExtra(UserHomeActivity.INTENT_USER_MIN, courseDto.getUser());
                startActivity(intent);
            }
        });

        headViewHolder.challengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetailActivity.this, CreateVideoActivity.class);
                intent.putExtra(TopicType.TYPE, TopicType.CHALLENGE);
                intent.putExtra(CreateVideoActivity.INTENT_COURSE_MIN_DTO, courseMinDTO);
                startActivity(intent);
            }
        });

        listView.addHeaderView(headView);

    }

    private void initFooterView()
    {

        loadMoreButton = new ClickLoadMoreView(this);
        loadMoreButton.setVisibility(View.GONE);
        loadMoreButton.setClickLoadMoreViewListener(new ClickLoadMoreView.ClickLoadMoreViewListener() {
            @Override
            public void runLoad() {
                getNextPage();
            }
        });
        listView.addFooterView(loadMoreButton);
    }

    private void getNextPage()
    {
        if(currentPage < totalPage)
        {
            requestTopicList();
        }
        else
        {
            Toast.makeText(this, R.string.not_more, Toast.LENGTH_SHORT).show();
        }

    }

    private void doHideFootView()
    {
        if(totalPage > 1)
        {
            if(loadMoreButton.isLoading() == true)
            {
                loadMoreButton.onFinish();
            }

            if(currentPage >= totalPage)
            {
                loadMoreButton.setOver();
            }
        }
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
                        requestExplainList();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(myRequest);
    }

    private void requestExplainList() {

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ServerMethod.topic() + "?type=course&magicType=explain&courseId=" + courseMinDTO.getId() + "&sortType=agree";

        MyPageRequest<TopicDTO> myPageRequest = new MyPageRequest<TopicDTO>(Request.Method.GET, url, TopicDTO.class,
                new Response.Listener<MyResponse<MyPage<TopicDTO>>>() {

                    @Override
                    public void onResponse(MyResponse<MyPage<TopicDTO>> response) {

                        if (response.getStatus() == MyResponse.status_ok) {
                            MyPage<TopicDTO> myPage = response.getContent();
                            adapter.addDatas(myPage.getContents());
                            requestTopicList();
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

    private void requestTopicList() {

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ServerMethod.topic() + "?type=course&magicType=sb&courseId=" + courseMinDTO.getId() + "&sortType=agree&page=" + (currentPage + 1);

        MyPageRequest<TopicDTO> myPageRequest = new MyPageRequest<TopicDTO>(Request.Method.GET, url, TopicDTO.class,
                new Response.Listener<MyResponse<MyPage<TopicDTO>>>() {

                    @Override
                    public void onResponse(MyResponse<MyPage<TopicDTO>> response) {

                        if (response.getStatus() == MyResponse.status_ok) {
                            MyPage<TopicDTO> myPage = response.getContent();
                            adapter.addDatas(myPage.getContents());

                            totalPage = response.getContent().getTotalPages();
                            currentPage++;

                            doHideFootView();
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
        @BindView(R.id.course_detail_head_title)
        TextView titleTextView;
        @BindView(R.id.course_detail_head_content)
        TextView contentTextView;
        @BindView(R.id.course_detail_head_user_name)
        TextView userNameTextView;
        @BindView(R.id.course_detail_head_modify_time)
        TextView modifyTimeTextView;
        @BindView(R.id.course_detail_head_challenge)
        Button challengeButton;

        public HeadViewHolder(View headView) {
            ButterKnife.bind(this, headView);
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
