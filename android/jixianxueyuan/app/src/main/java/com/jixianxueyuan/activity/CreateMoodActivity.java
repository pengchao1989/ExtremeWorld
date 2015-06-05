package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jixianxueyuan.R;
import com.jixianxueyuan.dto.HobbyDTO;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.NewEditWidget;
import com.jixianxueyuan.widget.NewEditWidgetListener;
import com.jixianxueyuan.widget.NoScorllBarGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by pengchao on 5/24/15.
 */
public class CreateMoodActivity extends Activity implements NewEditWidgetListener {

    public static final String tag = CreateMoodActivity.class.getSimpleName();


    @InjectView(R.id.create_edit_widget_layout)
    LinearLayout editWidgetLayout;
    @InjectView(R.id.create_mood_image_gridview)
    NoScorllBarGridView imageGridView;

    NewEditWidget newEditWidget;



    TopicDTO topicDTO;

    @Override
    public void onCreate(Bundle savedInstanceStated)
    {
        super.onCreate(savedInstanceStated);
        setContentView(R.layout.create_mood_activity);

        ButterKnife.inject(this);


        newEditWidget = new NewEditWidget(this, editWidgetLayout);

        newEditWidget.setNewEditWidgetListener(this);

    }

    @OnClick(R.id.create_mood_submit)void onSubmit()
    {
        submit();
    }

    private void submit()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.topic;

        buildTopicParam();

        MyRequest<TopicDTO> stringRequest = new MyRequest(Request.Method.POST,url,TopicDTO.class, topicDTO,
                new Response.Listener<MyResponse<TopicDTO>>() {
                    @Override
                    public void onResponse(MyResponse<TopicDTO> response) {

                        if(response.getStatus() == MyResponse.status_ok)
                        {
                            Toast.makeText(CreateMoodActivity.this, R.string.add_topic_success, Toast.LENGTH_LONG).show();
                            finish();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyLog.d(tag, "onErrorResponse" + error.toString());
                    }
                });

        queue.add(stringRequest);
    }

    private void buildTopicParam()
    {
        topicDTO = new TopicDTO();
        topicDTO.setTitle(newEditWidget.getText());
        topicDTO.setContent(newEditWidget.getText());
        topicDTO.setType("mood");
        UserMinDTO userMinDTO = new UserMinDTO();
        userMinDTO.setId(1L);
        topicDTO.setUser(userMinDTO);
        List<HobbyDTO> hobbys = new ArrayList<HobbyDTO>();
        HobbyDTO hobbyDTO = new HobbyDTO();
        hobbyDTO.setId(1L);
        hobbys.add(hobbyDTO);
        topicDTO.setHobbys(hobbys);
    }

    @Override
    public void onImage()
    {
        Intent intent = new Intent(this, MultiImageSelectorActivity.class);

// whether show camera
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

// max select image amount
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);

// select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);

        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                // Get the result list of select image paths
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // do your logic ....
                for(String p : path)
                {
                    MyLog.d(tag, "path=" + p);
                }
            }
        }
    }
}
