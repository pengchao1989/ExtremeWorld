package com.jixianxueyuan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jixianxueyuan.R;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.config.HobbyType;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.dto.HobbyDTO;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TaxonomyDTO;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.widget.MyActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 3/24/16.
 */
public class CreateNewsActivity extends BaseActivity {

    @InjectView(R.id.create_news_actionbar)
    MyActionBar myActionBar;
    @InjectView(R.id.create_news_title)
    EditText titleEditText;
    @InjectView(R.id.create_news_url)
    EditText urlEditText;


    private TopicDTO topicDTO;
    private long topicTaxonomyId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_news_activity);

        getIntentParams();

        ButterKnife.inject(this);

        myActionBar.setActionOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkParams()){
                    submitContent();
                }
            }
        });
    }

    private void getIntentParams(){
        topicTaxonomyId = getIntent().getLongExtra(TopicType.TOPIC_TAXONOMY_ID, 0);
    }

    private boolean checkParams(){
        if(TextUtils.isEmpty(titleEditText.getText().toString())){
            Toast.makeText(this, R.string.title_is_empty, Toast.LENGTH_LONG).show();
            return false;
        }

        if(TextUtils.isEmpty(urlEditText.getText().toString())){
            Toast.makeText(this, R.string.url_is_empty, Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

    private void buildTopicParam(){
        topicDTO = new TopicDTO();

        UserMinDTO userMinDTO = new UserMinDTO();
        userMinDTO.setId(MyApplication.getContext().getMine().getUserInfo().getId());
        topicDTO.setUser(userMinDTO);

        List<HobbyDTO> hobbys = new ArrayList<HobbyDTO>();
        HobbyDTO hobbyDTO = new HobbyDTO();
        Long hobbyId = HobbyType.getHobbyId(MyApplication.getContext().getAppInfomation().getCurrentHobbyStamp());
        hobbyDTO.setId(hobbyId);
        hobbys.add(hobbyDTO);
        topicDTO.setHobbys(hobbys);

        //taxonomy
        if(topicTaxonomyId != 0L){
            TaxonomyDTO taxonomyDTO = new TaxonomyDTO();
            taxonomyDTO.setId(topicTaxonomyId);
            topicDTO.setTaxonomy(taxonomyDTO);
        }


        topicDTO.setTitle(titleEditText.getText().toString());
        topicDTO.setUrl(urlEditText.getText().toString());

        topicDTO.setType(TopicType.NEWS);
    }


    private void submitContent() {
        String url = ServerMethod.topic();

        buildTopicParam();

        MyRequest<TopicDTO> myRequest = new MyRequest(Request.Method.POST,url,TopicDTO.class, topicDTO,
                new Response.Listener<MyResponse<TopicDTO>>() {
                    @Override
                    public void onResponse(MyResponse<TopicDTO> response) {

                        if(response.getStatus() == MyResponse.status_ok) {
                            Toast.makeText(CreateNewsActivity.this, R.string.add_topic_success, Toast.LENGTH_LONG).show();
                            finish();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        MyVolleyErrorHelper.showError(CreateNewsActivity.this, error);
                    }
                });

        MyApplication.getContext().getRequestQueue().add(myRequest);
    }

    public static void startActivity(Context context, long taxonomyId){
        Intent intent = new Intent(context, CreateNewsActivity.class);
        intent.putExtra(TopicType.TOPIC_TAXONOMY_ID, taxonomyId);
        context.startActivity(intent);
    }
}
