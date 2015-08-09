package com.jixianxueyuan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.RemindOfReolyListAdapter;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.RemindDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.MyActionBar;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 8/9/15.
 */
public class RemindOfReplyActivity extends Activity {

    MyApplication myApplication;
    Mine mine;

    @InjectView(R.id.remind_reply_actionbar)
    MyActionBar actionBar;
    @InjectView(R.id.remind_reply_list_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.remind_reply_listview)
    ListView listView;

    RemindOfReolyListAdapter adapter;

    int currentPage = 0;
    int totalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.remind_reply_activity);

        ButterKnife.inject(this);

        myApplication = (MyApplication) this.getApplication();
        mine = myApplication.getMine();

        adapter = new RemindOfReolyListAdapter(this);
        listView.setAdapter(adapter);

        refreshTopicList();
    }

    private void refreshTopicList()
    {
        currentPage = 0;

        requestRemindReplyList();
    }

    private void getNextPage(){
        if(currentPage < totalPage ){
            requestRemindReplyList();
        }else {
            Toast.makeText(this, R.string.not_more, Toast.LENGTH_SHORT).show();
        }
    }


    private void requestRemindReplyList(){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.remind() + mine.getUserInfo().getId() + "?page=" + (currentPage + 1);
        MyLog.d("RemindOfReplyActivity", "request url=" + url);

        MyPageRequest<RemindDTO> myPageRequest = new MyPageRequest<RemindDTO>(Request.Method.GET, url, RemindDTO.class,
                new Response.Listener<MyResponse<MyPage<RemindDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<RemindDTO>> response) {

                        if(response.getStatus() == MyResponse.status_ok){

                            MyLog.d("RemindOfReplyActivity","onResponse");
                            MyPage myPage = response.getContent();
                            List<RemindDTO> remindDTOList = myPage.getContents();
                            adapter.addData(remindDTOList);

                            totalPage = myPage.getTotalPages();
                            currentPage = myPage.getCurPage() + 1;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(myPageRequest);
    }


}
