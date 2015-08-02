package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jixianxueyuan.R;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.config.HobbyType;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.dto.HobbyDTO;
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
import com.jixianxueyuan.util.qiniu.QiNiuImageUpload;
import com.jixianxueyuan.util.qiniu.QiNiuImageUploadListener;
import com.jixianxueyuan.util.qiniu.QiNiuVideoUpload;
import com.jixianxueyuan.util.qiniu.QiNiuVideoUploadListener;
import com.jixianxueyuan.util.qiniu.VideoUploadResult;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by pengchao on 5/24/15.
 */
public class CreateTopicActivity extends Activity implements NewEditWidgetListener {

    public static final String tag = CreateTopicActivity.class.getSimpleName();


    @InjectView(R.id.create_topic_actionbar)MyActionBar myActionBar;
    @InjectView(R.id.create_topic_title)EditText titleEditText;
    @InjectView(R.id.create_edit_widget_layout)
    LinearLayout editWidgetLayout;
    @InjectView(R.id.create_topic_image_gridview)
    NoScorllBarGridView imageGridView;
    @InjectView(R.id.create_short_video_progress)
    RoundProgressBarWidthNumber roundProgressBarWidthNumber;

    NewEditWidget contentEditWidget;

    String topicType = null;
    String topicTaxonomyId = null;
    String topicTaxonomyName = null;
    String videoPath = null;

    List<String> localImagePathList = null;
    LinkedHashMap<String,String> serverImagePathMap = null;

    List<String> localVideoPathList = null;
    LinkedHashMap<String,VideoUploadResult> serverVideoPathMap = null;


    TopicDTO topicDTO;
    VideoDetailDTO videoDetailDTO;

    @Override
    public void onCreate(Bundle savedInstanceStated)
    {
        super.onCreate(savedInstanceStated);
        setContentView(R.layout.create_topic_activity);

        ButterKnife.inject(this);

        initExtra();

        myActionBar.setActionOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitControling();
            }
        });

        contentEditWidget = new NewEditWidget(this, editWidgetLayout);

        contentEditWidget.setNewEditWidgetListener(this);

    }

    private void initExtra()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null)
        {
            topicType = bundle.getString(TopicType.STRING);
            topicTaxonomyId = bundle.getString("topicTaxonomyId");
            topicTaxonomyName = bundle.getString("topicTaxonomyName");

            if(topicType.equals(TopicType.S_VIDEO))
            {
                videoPath = bundle.getString("videoPath");
            }

            switch (topicType)
            {
                case TopicType.MOOD:
                    myActionBar.setTitle("发布动态");
                    break;
                case TopicType.DISCUSS:
                    myActionBar.setTitle("发布讨论");
                    break;
                case TopicType.S_VIDEO:
                    myActionBar.setTitle("发布短视频");
                    break;
                case TopicType.NEWS:
                    myActionBar.setTitle("发布新闻");
                    break;
            }

        }

    }

    //提交数据流程控制函数
    private void submitControling()
    {

        localImagePathList = contentEditWidget.getLocalImagePathList();

        //若存在视频则走视频上传链
        if(videoPath != null)
        {
            submitVideo();
        }
        //若不存在视频但存在图片，则走图片上传链
        else if(localImagePathList != null && localImagePathList.size() > 0)
        {
            submitImage();
        }
        //若既不存在视频也不存在图片，则直接上传内容
        else
        {
            submitContent();
        }


    }

    private void submitVideo()
    {
        QiNiuVideoUpload qiNiuVideoUpload = new QiNiuVideoUpload(this);
        qiNiuVideoUpload.upload(localVideoPathList, new QiNiuVideoUploadListener() {
            @Override
            public void onUploading() {

            }

            @Override
            public void onUploadFailed() {

            }

            @Override
            public void onUploadComplete(LinkedHashMap<String, VideoUploadResult> result) {

            }

            @Override
            public void onUploadCancelled() {

            }
        });
    }

    private void submitImage()
    {
        QiNiuImageUpload qiNiuPictureUpload = new QiNiuImageUpload(this);
        qiNiuPictureUpload.upload(localImagePathList, new QiNiuImageUploadListener() {
            @Override
            public void onUploading() {

            }

            @Override
            public void onUploadFailed() {

            }

            @Override
            public void onUploadComplete(LinkedHashMap<String, String> result) {
                serverImagePathMap = result;
                if(serverImagePathMap != null){
                    submitContent();
                }else {

                }
            }

            @Override
            public void onUploadCancelled() {

            }
        });
    }

    private void submitContent()
    {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ServerMethod.topic();

        buildTopicParam();

        MyRequest<TopicDTO> myRequest = new MyRequest(Request.Method.POST,url,TopicDTO.class, topicDTO,
                new Response.Listener<MyResponse<TopicDTO>>() {
                    @Override
                    public void onResponse(MyResponse<TopicDTO> response) {

                        if(response.getStatus() == MyResponse.status_ok)
                        {
                            Toast.makeText(CreateTopicActivity.this, R.string.add_topic_success, Toast.LENGTH_LONG).show();
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

        queue.add(myRequest);
    }



    private void buildTopicParam()
    {
        topicDTO = new TopicDTO();
        topicDTO.setTitle(titleEditText.getText().toString());

        if(localImagePathList!= null && localImagePathList.size() > 0)
        {
            String content = analyzeContent(contentEditWidget.getHtml(), null);
            topicDTO.setContent(content);

        }
        else
        {
            topicDTO.setContent(contentEditWidget.getText());
        }


        UserMinDTO userMinDTO = new UserMinDTO();
        userMinDTO.setId(MyApplication.getContext().getMine().getUserInfo().getId());
        topicDTO.setUser(userMinDTO);
        List<HobbyDTO> hobbys = new ArrayList<HobbyDTO>();
        HobbyDTO hobbyDTO = new HobbyDTO();
        Long hobbyId = HobbyType.getHobbyId(MyApplication.getContext().getCurrentHobby());
        hobbyDTO.setId(hobbyId);
        hobbys.add(hobbyDTO);
        topicDTO.setHobbys(hobbys);

        switch(topicType)
        {
            case TopicType.MOOD:
                topicDTO.setType(TopicType.MOOD);
                break;
            case TopicType.DISCUSS:
                topicDTO.setType(TopicType.DISCUSS);
                break;
            case TopicType.VIDEO:
                topicDTO.setVideoDetail(videoDetailDTO);
                topicDTO.setType(TopicType.S_VIDEO);
                break;
            case TopicType.S_VIDEO:
                topicDTO.setVideoDetail(videoDetailDTO);
                topicDTO.setType(TopicType.S_VIDEO);
                break;
            case TopicType.NEWS:
                topicDTO.setType(TopicType.NEWS);
                break;
        }

    }

    private String analyzeContent(String str, Map<String,String> words)
    {

        MyLog.d("CreateTopicActivity", "analyze before contetn=" + str);

        //除了img外的标签全部删掉
/*        String regex = "<(?!img)[^>]+>";
        str=str.replaceAll(regex,"").trim();
        MyLog.d("CreateTopicActivity","analyzeContent replace=" + str);

        String gbkString = str.trim();
        MyLog.d("CreateTopicActivity","analyzeContent gbkstring=" + gbkString);*/

        Iterator iter = serverImagePathMap.entrySet().iterator();
        while (iter.hasNext())
        {
            Map.Entry entry = (Map.Entry) iter.next();

            String key = (String) entry.getKey();
            String val = StaticResourceConfig.IMG_DOMAIN + (String) entry.getValue();

            str = str.replace(  key , val);
        }

        MyLog.d("CreateTopicActivity", "resultString=" + str);
        return str;


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
                    contentEditWidget.insertImg(p);
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
