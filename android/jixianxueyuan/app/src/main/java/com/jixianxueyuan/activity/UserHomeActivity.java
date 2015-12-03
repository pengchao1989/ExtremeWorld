package com.jixianxueyuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.jixianxueyuan.dto.UserDTO;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.ClickLoadMoreView;
import com.jixianxueyuan.widget.MyActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 5/22/15.
 */
public class UserHomeActivity extends BaseActivity {

    public static final String INTENT_USER_MIN = "INTENT_USER_MIN";
    public static final String INTENT_USER = "INTENT_USER";

    @InjectView(R.id.user_home_actionbar)MyActionBar actionBar;
    @InjectView(R.id.user_home_listview)ListView listView;

    TopicListOfUserAdapter adapter;

    UserMinDTO userMinDTO;
    UserDTO userDTO;

    ClickLoadMoreView clickLoadMoreView;
    TextView signatureTextView;


    int currentPage = 0;
    int totalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_activity);

        ButterKnife.inject(this);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle.containsKey(INTENT_USER_MIN)){
            userMinDTO = (UserMinDTO) bundle.getSerializable(INTENT_USER_MIN);
            actionBar.setTitle(userMinDTO.getName());
        }
        else if(bundle.containsKey(INTENT_USER)){
            userDTO = (UserDTO) bundle.getSerializable(INTENT_USER);
            actionBar.setTitle(userDTO.getName());
        }
        else {
            finish();
        }


        initHeadView();

        adapter = new TopicListOfUserAdapter(this);
        listView.setAdapter(adapter);
        if(userDTO != null){
            setUserDetailView();
        }else {
            requestUserInfo();
        }
        requestTopicList();
    }

    private void initHeadView(){
        View headView = LayoutInflater.from(this).inflate(R.layout.user_home_head_view, null);
        signatureTextView = (TextView) headView.findViewById(R.id.user_home_head_signature);
        ImageView avatarImageView = (ImageView) headView.findViewById(R.id.user_home_head_avatar);

        String avatarUrl = userMinDTO != null?userMinDTO.getAvatar() + "!AndroidProfileAvatar"
                :userDTO.getAvatar() + "!AndroidProfileAvatar";
        MyLog.d("UserHomeActivity", "avatar=" + avatarUrl);
        ImageLoader.getInstance().displayImage(avatarUrl, avatarImageView);
        listView.addHeaderView(headView);
    }

    private void setUserDetailView(){
        signatureTextView.setText(userDTO.getSignature());
    }

    private void requestUserInfo(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.user()+userMinDTO.getId();

        MyRequest<UserDTO> myRequest = new MyRequest<UserDTO>(Request.Method.GET, url, UserDTO.class,
                new Response.Listener<MyResponse<UserDTO>>() {
                    @Override
                    public void onResponse(MyResponse<UserDTO> response) {

                        if(response.getStatus() == MyResponse.status_ok){
                            userDTO = response.getContent();
                            setUserDetailView();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(myRequest);
    }

    private void requestTopicList(){

        RequestQueue queue = Volley.newRequestQueue(this);
        Long userId = userMinDTO != null ? userMinDTO.getId() : userDTO.getId();
        String url = ServerMethod.topic_user()+userId;

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
        if(position == 0){

        }else {
            TopicDTO topicDTO = (TopicDTO) adapter.getItem(position-1);
            Intent intent = new Intent(this, TopicDetailActivity.class);
            intent.putExtra("topic", topicDTO);
            startActivity(intent);
        }

    }

}
