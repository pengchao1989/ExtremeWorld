package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.dto.HobbyDTO;
import com.jixianxueyuan.dto.MyPage;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.dto.UploadToken;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.dto.VideoDetailDTO;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.server.StaticResourceConfig;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.Util;
import com.jixianxueyuan.widget.MyActionBar;
import com.jixianxueyuan.widget.NewEditWidget;
import com.jixianxueyuan.widget.NewEditWidgetListener;
import com.jixianxueyuan.widget.NoScorllBarGridView;
import com.jixianxueyuan.widget.RoundProgressBarWidthNumber;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

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


    @InjectView(R.id.create_mood_actionbar)MyActionBar myActionBar;
    @InjectView(R.id.create_edit_widget_layout)
    LinearLayout editWidgetLayout;
    @InjectView(R.id.create_mood_image_gridview)
    NoScorllBarGridView imageGridView;
    @InjectView(R.id.create_short_video_progress)
    RoundProgressBarWidthNumber roundProgressBarWidthNumber;

    NewEditWidget newEditWidget;

    String action = null;
    String videoPath = null;
    UploadToken uploadToken = null;

    TopicDTO topicDTO;
    VideoDetailDTO videoDetailDTO;

    @Override
    public void onCreate(Bundle savedInstanceStated)
    {
        super.onCreate(savedInstanceStated);
        setContentView(R.layout.create_mood_activity);

        ButterKnife.inject(this);

        Intent intent = getIntent();
        action = intent.getAction();
        if(action == null)
        {

            finish();
            return;
        }
        else
        {
            if (action.equals(TopicType.MOOD))
            {
                myActionBar.setTitle("发布动态");
            }
            else if(action.equals(TopicType.VIDEO))
            {
                videoPath = intent.getStringExtra("path");
                myActionBar.setTitle("发布短视频");
            }
        }


        newEditWidget = new NewEditWidget(this, editWidgetLayout);

        newEditWidget.setNewEditWidgetListener(this);

    }

    @OnClick(R.id.create_mood_submit)void onSubmit()
    {
        if(action.equals(TopicType.MOOD))
        {
            submit();
        }
        else if(action.equals(TopicType.VIDEO))
        {
            requestToken();
        }

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

    private void requestToken()
    {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(ServerMethod.uploadToken, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                uploadToken = gson.fromJson(response, UploadToken.class);

                MyLog.d("CreateShortVideoActivity", "path=" + videoPath);

                UploadManager uploadManager = new UploadManager();

                uploadManager.put(videoPath, Util.getUUID(), uploadToken.getUptoken(),
                        new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                MyLog.i("qiniu", info.path);
                                MyLog.i("qiniu", key);

                                videoDetailDTO = new VideoDetailDTO();
                                videoDetailDTO.setVideoSource(StaticResourceConfig.VIDEO_DOMAIN + key);
                                videoDetailDTO.setThumbnail(StaticResourceConfig.VIDEO_DOMAIN + uploadToken.getMyParam());


                                Toast.makeText(CreateMoodActivity.this,"视频上传成功", Toast.LENGTH_LONG).show();

                                submit();
                            }
                        }, new UploadOptions(null, null, false,
                                new UpProgressHandler(){
                                    public void progress(String key, double percent)
                                    {

                                        Log.i("qiniu", key + ": " + percent);
                                        roundProgressBarWidthNumber.setProgress( (int)(percent * 100) );
                                    }
                                }, null));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
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


        if(action.equals(TopicType.DISCUSS))
        {
            topicDTO.setType(TopicType.DISCUSS);
        }
        else if(action.equals(TopicType.VIDEO))
        {
            topicDTO.setVideoDetail(videoDetailDTO);
            topicDTO.setType(TopicType.S_VIDEO);
        }
        else if(action.equals(TopicType.NEWS))
        {

        }


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
                    MyLog.d(tag, "videoPath=" + p);
                }
            }
        }
    }




    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     * @param videoPath 视频的路径
     * @param width 指定输出视频缩略图的宽度
     * @param height 指定输出视频缩略图的高度度
     * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                     int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        System.out.println("w"+bitmap.getWidth());
        System.out.println("h"+bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
