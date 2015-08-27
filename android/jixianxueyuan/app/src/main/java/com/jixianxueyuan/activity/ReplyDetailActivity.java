package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.SubReplylListAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.ReplyDTO;
import com.jixianxueyuan.dto.SubReplyDTO;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.dto.request.SubReplyRequest;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.widget.ReplyWidget;
import com.jixianxueyuan.widget.ReplyWidgetListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.nereo.multi_image_selector.bean.Image;

/**
 * Created by pengchao on 8/22/15.
 */
public class ReplyDetailActivity extends Activity implements ReplyWidgetListener {


    @InjectView(R.id.reply_detail_listview)ListView listView;
    @InjectView(R.id.reply_widget_layout)LinearLayout contentLayout;
    ReplyWidget replyWidget;

    ReplyDTO replyDTO;

    SubReplylListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply_detail_activity);
        ButterKnife.inject(this);

        getIntentData();

        initHeadView();

        adapter = new SubReplylListAdapter(this);
        listView.setAdapter(adapter);

        replyWidget = new ReplyWidget(this, contentLayout);
        replyWidget.setReplyWidgetListener(this);

        requestSubReplyList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        replyWidget.onActivityResult(requestCode, resultCode, data);
    }

    private void getIntentData(){
        Intent intent = this.getIntent();
        replyDTO = (ReplyDTO) intent.getSerializableExtra("reply");
    }

    private void initHeadView(){
        View headView = LayoutInflater.from(this).inflate(R.layout.reply_detail_head_view,null);
        ImageView avatarImageView = (ImageView) headView.findViewById(R.id.user_head_avatar);
        TextView nameTextView = (TextView) headView.findViewById(R.id.user_head_name);
        TextView timeTextView = (TextView) headView.findViewById(R.id.user_head_time);
        TextView contentTextView = (TextView) headView.findViewById(R.id.reply_detail_content);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(replyDTO.getUser().getAvatar() + "!androidListAvatar", avatarImageView);
        nameTextView.setText(replyDTO.getUser().getName());
        timeTextView.setText(replyDTO.getCreateTime());
        contentTextView.setText(replyDTO.getContent());

        listView.addHeaderView(headView);

    }

    private void requestSubReplyList(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.sub_reply() + "?replyId=" + replyDTO.getId() + "&page=1";

        MyPageRequest<SubReplyDTO> myPageRequest = new MyPageRequest<SubReplyDTO>(Request.Method.GET, url, SubReplyDTO.class,
                new Response.Listener<MyResponse<MyPage<SubReplyDTO>>>() {
                    @Override
                    public void onResponse(MyResponse<MyPage<SubReplyDTO>> response) {
                        if(response.getStatus() == MyResponse.status_ok){

                            MyPage<SubReplyDTO> myPage = response.getContent();

                            adapter.addNextPageData(myPage.getContents());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(myPageRequest);
    }

    private void submitSubReply(String content){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.sub_reply();

        SubReplyRequest subReplyRequest = buildSubReplyDTO("1234567");

        MyRequest<SubReplyDTO> myRequest = new MyRequest<SubReplyDTO>(Request.Method.POST, url,
                SubReplyDTO.class,subReplyRequest,
                new Response.Listener<MyResponse<SubReplyDTO>>() {
                    @Override
                    public void onResponse(MyResponse<SubReplyDTO> response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(myRequest);
    }

    private SubReplyRequest buildSubReplyDTO(String subReplyString){
        SubReplyRequest subReplyRequest = new SubReplyRequest();
        subReplyRequest.setContent(subReplyString);

        UserMinDTO userMinDTO = new UserMinDTO();
        userMinDTO.setId(MyApplication.getContext().getMine().getUserInfo().getId());

        subReplyRequest.setReply(replyDTO);
        return subReplyRequest;

    }

    @Override
    public void onCommit(String text) {

    }
}
