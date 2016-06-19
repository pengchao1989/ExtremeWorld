package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tedcoder.wkvideoplayer.view.MediaController;
import com.android.tedcoder.wkvideoplayer.view.SuperVideoPlayer;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jakewharton.disklrucache.DiskLruCache;
import com.jixianxueyuan.R;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.FileUtils;
import com.jixianxueyuan.config.HobbyType;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.dto.CourseMinDTO;
import com.jixianxueyuan.dto.HobbyDTO;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.dto.VideoDetailDTO;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.BitmapUtils;
import com.jixianxueyuan.util.DiskCachePath;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.Util;
import com.jixianxueyuan.util.qiniu.QiniuVideoUpload;
import com.jixianxueyuan.util.qiniu.QiniuVideoUploadListener;
import com.jixianxueyuan.util.qiniu.VideoUploadResult;
import com.jixianxueyuan.widget.MyActionBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pengchao on 3/13/16.
 * 投稿（视频）页面
 */
public class CreateVideoActivity extends BaseActivity {

    public static final String INTENT_COURSE_MIN_DTO = "INTENT_COURSE_MIN_DTO";

    private static final String tag = "CreateVideoActivity";
    private static final int FILE_SELECT_CODE = 0x1;

    @InjectView(R.id.create_video_guide)
    TextView guideTextView;
    @InjectView(R.id.create_video_actionbar)
    MyActionBar myActionBar;
    @InjectView(R.id.create_video_title)
    EditText titleEditText;
    @InjectView(R.id.create_video_description)
    EditText descriptionEditText;
    @InjectView(R.id.create_video_select)
    ImageButton selectButton;
    @InjectView(R.id.create_video_upload_progress_layout)
    RelativeLayout progressLayout;
    @InjectView(R.id.create_video_upload_progress_view)
    ProgressBar uploadProgress;
    @InjectView(R.id.create_video_upload_progress_textview)
    TextView progressTextView;
    @InjectView(R.id.create_video_video_layout)
    FrameLayout videoLayout;
    @InjectView(R.id.create_video_video_cover_image)
    ImageView videoCoverImageView;
    @InjectView(R.id.create_video_video_play_btn)
    ImageView videoPlayButton;
    @InjectView(R.id.videoview)
    SuperVideoPlayer videoView;

    private String topicType;
    private CourseMinDTO courseMinDTO;


    private String localVideoPath;
    private TopicDTO topicDTO;
    private VideoDetailDTO videoDetailDTO;

    public static final int HANDLER_UPDATE_PROGRESS = 0x1;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle progressData = msg.getData();
            switch (progressData.getInt("type")){
                case 1:
                    progressTextView.setText("正在上传第" + progressData.getInt("index") + "张图片  " + String.format("%.1f",progressData.getDouble("percent") * 100) + "%")  ;
                    break;
                case 2:
                    progressTextView.setText("正在上传视频  " + String.format("%.1f",progressData.getDouble("percent") * 100) + "%")  ;
                    break;
                case 3:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_video_activity);
        getIntentParams();
        ButterKnife.inject(this);

        if (TopicType.CHALLENGE.equals(topicType)){
            if (courseMinDTO != null){
                String guideString = String.format(getString(R.string.please_upload_your_xx_video), courseMinDTO.getName());
                guideTextView.setText(guideString);
            }
            myActionBar.setTitle(getString(R.string.challenge));

        }

        myActionBar.setActionOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkParams()){
                    submitVideo(localVideoPath);
                }
            }
        });

    }

    private void getIntentParams(){
        topicType = getIntent().getStringExtra(TopicType.TYPE);
        if (TopicType.CHALLENGE.equals(topicType)){
            courseMinDTO = (CourseMinDTO) getIntent().getSerializableExtra(INTENT_COURSE_MIN_DTO);
        }
    }

    @OnClick(R.id.create_video_select)void onSelectButtonClick(){
        showFileChooser();
    }

    @OnClick(R.id.create_video_video_play_btn)void onPlayButtonClick() {
        playVideo();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.please_select_mp4_file)),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, R.string.please_install_file_browser, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == Activity.RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            localVideoPath = FileUtils.getVideoRealPathFromURI(this, uri);

            if(TextUtils.isEmpty(localVideoPath)){
                Toast.makeText(this, R.string.err_video_not_found, Toast.LENGTH_SHORT).show();
            }else {
                showVideoView();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showVideoView(){
        videoLayout.setVisibility(View.VISIBLE);
        Bitmap bitmap = BitmapUtils.getVideoThumbnail(localVideoPath);
        videoCoverImageView.setImageBitmap(bitmap);
    }

    private void playVideo() {
        videoPlayButton.setVisibility(View.GONE);
        videoCoverImageView.setVisibility(View.GONE);
        videoView.setVideoPlayCallback(mVideoPlayCallback);
        MyLog.d(tag, "videoPath=" + localVideoPath);

        videoView.setVisibility(View.VISIBLE);

        videoView.loadLocalVideo(localVideoPath);
        videoView.requestFocus();
    }

    private SuperVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new SuperVideoPlayer.VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {

        }

        @Override
        public void onSwitchPageType() {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                videoView.setPageType(MediaController.PageType.SHRINK);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                videoView.setPageType(MediaController.PageType.EXPAND);
            }
        }

        @Override
        public void onPlayFinish() {

        }
    };

    private boolean checkParams(){
        if(TextUtils.isEmpty(localVideoPath)){
            Toast.makeText(this, R.string.video_is_empty, Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.isEmpty(titleEditText.getText().toString())){
            Toast.makeText(this, R.string.title_is_empty, Toast.LENGTH_LONG).show();
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

        topicDTO.setTitle(titleEditText.getText().toString());
        topicDTO.setContent(descriptionEditText.getText().toString());

        topicDTO.setType(topicType);
        if (TopicType.CHALLENGE.equals(topicType)){
            topicDTO.setCourse(courseMinDTO);
        }

        //video
        if(videoDetailDTO != null){
            topicDTO.setVideoDetail(videoDetailDTO);
        }
    }
    private void submitContent() {
        String url = ServerMethod.topic();

        buildTopicParam();

        MyRequest<TopicDTO> myRequest = new MyRequest(Request.Method.POST,url,TopicDTO.class, topicDTO,
                new Response.Listener<MyResponse<TopicDTO>>() {
                    @Override
                    public void onResponse(MyResponse<TopicDTO> response) {

                        if(response.getStatus() == MyResponse.status_ok) {
                            Toast.makeText(CreateVideoActivity.this, R.string.add_topic_success, Toast.LENGTH_LONG).show();
                            finish();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideUploadProgressView();
                        MyVolleyErrorHelper.showError(CreateVideoActivity.this, error);
                    }
                });

        MyApplication.getContext().getRequestQueue().add(myRequest);
    }


    private void submitVideo(final String localVideoPath) {

        showUploadProgressView();


        final List<String> localVideoPathList = new ArrayList<String>();
        localVideoPathList.add(localVideoPath);
        QiniuVideoUpload qiniuVideoUpload = new QiniuVideoUpload(this);
        qiniuVideoUpload.upload(localVideoPathList, new QiniuVideoUploadListener() {
            @Override
            public void onUploading(int index, String key, double percent) {
                updateProgressView(2, index, percent);
            }

            @Override
            public void onUploadFailed() {
                hideUploadProgressView();
                Toast.makeText(CreateVideoActivity.this, R.string.err, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onUploadComplete(LinkedHashMap<String, VideoUploadResult> result) {
                VideoUploadResult videoUploadResult = result.get(localVideoPath);
                videoDetailDTO = new VideoDetailDTO();
                videoDetailDTO.setVideoSource(videoUploadResult.getUrl());
                videoDetailDTO.setThumbnail(videoUploadResult.getThumbnailUrl());
                submitContent();
            }

            @Override
            public void onUploadCancelled() {

            }
        });
    }

    private void showUploadProgressView(){
        progressLayout.setVisibility(View.VISIBLE);

    }

    private void hideUploadProgressView(){
        progressLayout.setVisibility(View.GONE);
    }

    private void updateProgressView(int type, int index, double percent){

        Message msg = new Message();
        msg.what = HANDLER_UPDATE_PROGRESS;
        Bundle progressData = new Bundle();
        progressData.putInt("type", type);
        progressData.putInt("index", index+1);
        progressData.putDouble("percent", percent);
        msg.setData(progressData);
        handler.sendMessage(msg);

    }
}
