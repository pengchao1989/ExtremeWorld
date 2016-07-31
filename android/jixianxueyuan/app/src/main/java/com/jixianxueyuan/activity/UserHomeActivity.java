package com.jixianxueyuan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.TopicListOfUserAdapter;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.config.AppConstant;
import com.jixianxueyuan.config.ImageLoaderConfig;
import com.jixianxueyuan.config.QiniuImageStyle;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.dto.UserDTO;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.http.MyPageRequest;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.DateTimeFormatter;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.ClickLoadMoreView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by pengchao on 5/22/15.
 */
public class UserHomeActivity extends BaseActivity {

    public static final String INTENT_USER_MIN = "INTENT_USER_MIN";
    public static final String INTENT_USER = "INTENT_USER";

    //@BindView(R.id.user_home_actionbar)MyActionBar actionBar;
    @BindView(R.id.user_home_listview)ListView listView;
    private ImageView avatarImageView;
    private ImageView coverImageView;
    private TextView nameTextView;
    private TextView idTextView;
    private TextView regionTextView;
    private TextView signatureTextView;
    private Button sendMessageButton;


    private TopicListOfUserAdapter adapter;

    private UserMinDTO userMinDTO;
    private UserDTO userDTO;

    private ClickLoadMoreView clickLoadMoreView;



    int currentPage = 0;
    int totalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_activity);

        ButterKnife.bind(this);

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
        avatarImageView = (ImageView) headView.findViewById(R.id.user_home_head_avatar);
        coverImageView = (ImageView) headView.findViewById(R.id.user_home_head_cover);
        nameTextView = (TextView) headView.findViewById(R.id.user_home_head_name);
        idTextView = (TextView) headView.findViewById(R.id.user_home_head_id);
        regionTextView = (TextView)headView.findViewById(R.id.user_home_head_region);
        sendMessageButton = (Button) headView.findViewById(R.id.user_home_head_send_msg);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle.containsKey(INTENT_USER_MIN)){
            userMinDTO = (UserMinDTO) bundle.getSerializable(INTENT_USER_MIN);
            nameTextView.setText(userMinDTO.getName());
        }
        else if(bundle.containsKey(INTENT_USER)){
            userDTO = (UserDTO) bundle.getSerializable(INTENT_USER);
            nameTextView.setText(userDTO.getName());
        }
        else {
            finish();
        }

        String avatarUrl = userMinDTO != null?userMinDTO.getAvatar() + QiniuImageStyle.PROFILE_AVATAR
                :userDTO.getAvatar() + "!AndroidProfileAvatar";
        MyLog.d("UserHomeActivity", "avatar=" + avatarUrl);
        ImageLoader.getInstance().displayImage(avatarUrl, avatarImageView);

        showCover();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userDTO != null){

                    Mine mine = MyApplication.getContext().getMine();
                    String userId = mine.getUserInfo().getLoginName();

                    YWIMKit mIMKit = YWAPI.getIMKitInstance(userId, AppConstant.BAICHUAN_APP_KEY);
                    final String target = userDTO.getLoginName();;// 消息接收者ID
                    Intent intent = mIMKit.getChattingActivityIntent(target);
                    startActivity(intent);
                }
            }
        });

        listView.addHeaderView(headView);
    }

    private void showCover() {
        if(userDTO != null){
            String coverUrl = userDTO.getBg() + QiniuImageStyle.COVER;
            ImageLoader.getInstance().displayImage(coverUrl, coverImageView, ImageLoaderConfig.getHeadOption(this));
        }
    }

    private void setUserDetailView(){
        nameTextView.setText(userDTO.getName());

        StringBuffer idBuffer = new StringBuffer();
        idBuffer.append("Id:" + userDTO.getId());

        if (!TextUtils.isEmpty(userDTO.getBirth())){
            int age = DateTimeFormatter.getAge(userDTO.getBirth());
            idBuffer.append(" | Age:" + age);
        }
        idTextView.setText(idBuffer.toString());

        StringBuffer regionStringBuffer = new StringBuffer();
        if (!TextUtils.isEmpty(userDTO.getCountry())){
            regionStringBuffer.append(userDTO.getCountry());
        }
        if (!TextUtils.isEmpty(userDTO.getProvince())){
            String cityText = userDTO.getProvince().replace("省","");
            cityText = cityText.replace("自治区", "");
            regionStringBuffer.append("  " + cityText);
        }
        if (!TextUtils.isEmpty(userDTO.getCity())){
            String cityText = userDTO.getCity().replace("市","");
            regionStringBuffer.append("  " + cityText);
        }
        String regionText = regionStringBuffer.toString();
        if (regionText.length() == 0){
            regionTextView.setVisibility(View.GONE);
        }else {
            regionTextView.setVisibility(View.VISIBLE);
            regionTextView.setText(regionStringBuffer.toString());
        }


        StringBuffer signatureStringBuffer = new StringBuffer();
        if (!TextUtils.isEmpty(userDTO.getSignature())){
            signatureStringBuffer.append(userDTO.getSignature());
        }
        signatureTextView.setText(signatureStringBuffer.toString());

        showCover();
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

    public static void startActivity(Context context, UserMinDTO userMinDTO){
        Intent intent = new Intent(context, UserHomeActivity.class);
        intent.putExtra(INTENT_USER_MIN, userMinDTO);
        context.startActivity(intent);
    }

}
