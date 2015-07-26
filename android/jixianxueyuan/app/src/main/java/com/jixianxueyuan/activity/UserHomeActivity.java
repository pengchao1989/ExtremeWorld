package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.TopicListOfUserAdapter;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.widget.LoadMoreView;
import com.jixianxueyuan.widget.MyActionBar;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 5/22/15.
 */
public class UserHomeActivity extends Activity {

    @InjectView(R.id.user_home_actionbar)MyActionBar actionBar;
    @InjectView(R.id.user_home_listview)ListView listView;

    TopicListOfUserAdapter adapter;

    UserMinDTO userMinDTO;

    LoadMoreView loadMoreView;
    int currentPage = 0;
    int totalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_activity);

        ButterKnife.inject(this);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle.containsKey("userMinDTO")){
            userMinDTO = (UserMinDTO) bundle.getSerializable("userMinDTO");
        }
        else {
            finish();
        }



        actionBar.setTitle(userMinDTO.getName());

        View headView = LayoutInflater.from(this).inflate(R.layout.user_home_head_view, null);
        listView.addHeaderView(headView);

        adapter = new TopicListOfUserAdapter(this);

        listView.setAdapter(adapter);


        requestTopicList();
    }


    private void requestTopicList(){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.topic_user()+userMinDTO.getId();

        MyPageRequest<TopicDTO> stringRequest = new MyPageRequest(Request.Method.GET,url,TopicDTO.class,
                new Response.Listener<MyResponse<MyPage<TopicDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<TopicDTO>> response) {


                        if(response.getStatus() == MyResponse.status_ok)
                        {
                            MyPage page = response.getContent();
                            List<TopicDTO> topicDTOs = page.getContents();
                            if(currentPage == 0)
                            {
                                adapter.refresh(topicDTOs);
                            }
                            else
                            {
                                adapter.addDatas(topicDTOs);
                            }

                            totalPage = page.getTotalPages();
                            currentPage = page.getCurPage() + 1;

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

    @OnItemClick(R.id.user_home_listview)void onItemCLick(int position){
        TopicDTO topicDTO = (TopicDTO) adapter.getItem(position-1);
        Intent intent = new Intent(this, TopicDetailActivity.class);
        intent.putExtra("topic", topicDTO);
        startActivity(intent);
    }

}
