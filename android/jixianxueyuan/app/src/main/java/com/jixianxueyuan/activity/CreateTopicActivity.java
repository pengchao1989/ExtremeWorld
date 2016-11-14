package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.duanqu.qupai.android.app.QupaiServiceImpl;
import com.duanqu.qupai.editor.EditorResult;
import com.duanqu.qupai.engine.session.MovieExportOptions;
import com.duanqu.qupai.engine.session.VideoSessionCreateInfo;
import com.duanqu.qupai.recorder.EditorCreateInfo;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.CreateActivityImageListAdapter;
import com.jixianxueyuan.adapter.TaxonomySpinnerAdapter;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.Contant;
import com.jixianxueyuan.commons.FileUtils;
import com.jixianxueyuan.config.HobbyType;
import com.jixianxueyuan.config.MediaType;
import com.jixianxueyuan.config.StaticResourceConfig;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.dto.CourseMinDTO;
import com.jixianxueyuan.dto.HobbyDTO;
import com.jixianxueyuan.dto.MediaDTO;
import com.jixianxueyuan.dto.MediaWrapDTO;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.dto.TaxonomyDTO;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.dto.VideoDetailDTO;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.http.MyVolleyErrorHelper;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.BitmapUtils;
import com.jixianxueyuan.util.DiskCachePath;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.qiniu.QiniuMultiImageUpload;
import com.jixianxueyuan.util.qiniu.QiniuMultiImageUploadListener;
import com.jixianxueyuan.util.qiniu.QiniuVideoUpload;
import com.jixianxueyuan.util.qiniu.QiniuVideoUploadListener;
import com.jixianxueyuan.util.qiniu.VideoUploadResult;
import com.jixianxueyuan.widget.MyActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import net.bither.util.NativeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by pengchao on 5/24/15.
 */
public class CreateTopicActivity extends Activity implements CreateActivityImageListAdapter.OnImageOperatorListener {

    public static final String tag = CreateTopicActivity.class.getSimpleName();

    public static final int REQUEST_IMAGE_CODE = 1;
    public static final int REQUEST_VIDEO_CODE = 2;

    @BindView(R.id.create_topic_actionbar)MyActionBar myActionBar;
    @BindView(R.id.create_topic_title_layout)LinearLayout mTaxonomySpinnerLayout;
    @BindView(R.id.create_topic_taxonomy_spinner)Spinner taxonomySpinner;
    @BindView(R.id.create_topic_title)EditText titleEditText;
    @BindView(R.id.create_topic_content_edittext)
    EditText contentEditText;
    @BindView(R.id.create_topic_video_thumble_layout)
    RelativeLayout videoThumbleLayout;
    @BindView(R.id.create_topic_image_list_view)
    RecyclerView recyclerView;
    @BindView(R.id.create_topic_video_thumble_imageview)
    ImageView videoThumbleImage;

    @BindView(R.id.create_topic_upload_progress_layout)
    RelativeLayout progressLayout;
    @BindView(R.id.create_topic_upload_progress_view)
    ProgressBar uploadProgress;
    @BindView(R.id.create_topic_upload_progress_textview)
    TextView progressTextView;


    String topicType = null;
    Long topicTaxonomyId = null;
    String topicTaxonomyName = null;
    Long courseId = null;
    String courseType = null;
    String videoPath = null;
    String thumblePath = null;


    List<String> localImagePathList = null;
    LinkedHashMap<String,String> serverImagePathMap = null;

    List<String> localVideoPathList = null;
    LinkedHashMap<String,VideoUploadResult> serverVideoPathMap = null;

    List<TaxonomyDTO> taxonomyDTOList;
    TaxonomySpinnerAdapter taxonomySpinnerAdapter;


    TopicDTO topicDTO;
    VideoDetailDTO videoDetailDTO;

    CreateActivityImageListAdapter imageListAdapter;

    //video
    private String videoUrl;
    private final EditorCreateInfo _CreateInfo = new EditorCreateInfo();

    boolean isUploadedImage = false;
    boolean isUploadedVideo = false;

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
                    progressTextView.setText("正在压缩图片第  " + progressData.getInt("index") + "张图片")  ;
                    break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceStated)
    {
        super.onCreate(savedInstanceStated);
        setContentView(R.layout.create_topic_activity);

        ButterKnife.bind(this);

        myActionBar.setActionOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitControling();
            }
        });

        imageListAdapter = new CreateActivityImageListAdapter(this);
        imageListAdapter.setImageDeleteListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(imageListAdapter);

        initExtra();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initExtra()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null)
        {
            topicType = bundle.getString(TopicType.TYPE);
            topicTaxonomyId = bundle.getLong(TopicType.TOPIC_TAXONOMY_ID);
            topicTaxonomyName = bundle.getString(TopicType.TOPIC_TAXONOMY_NAME);

            switch (topicType)
            {
                case TopicType.MOOD:
                    myActionBar.setTitle("发布心情");
                    break;
                case TopicType.DISCUSS:
                    myActionBar.setTitle("发布讨论");
                    initTaxonomySpinner();
                    break;
                case TopicType.S_VIDEO:
                    myActionBar.setTitle("发布短视频");
                    startVideoRecord();
                    break;
                case TopicType.NEWS:
                    myActionBar.setTitle("发布新闻");
                case TopicType.COURSE:
                    myActionBar.setTitle("发布教学");
                    courseId = intent.getLongExtra("courseId",0);
                    courseType = intent.getStringExtra("courseType");
                    break;
            }
        }
    }

    private void initTaxonomySpinner(){
        mTaxonomySpinnerLayout.setVisibility(View.VISIBLE);
        taxonomySpinner.setVisibility(View.VISIBLE);
        taxonomyDTOList = MyApplication.getContext().getAppInfomation().getCurrentHobbyTaxonomyListOfType(topicType);

        taxonomySpinnerAdapter = new TaxonomySpinnerAdapter(this);
        taxonomySpinnerAdapter.setTaxonomyDTOList(taxonomyDTOList);
        taxonomySpinner.setAdapter(taxonomySpinnerAdapter);

        taxonomySpinner.setSelection(taxonomySpinnerAdapter.getTaxonomyIndex(topicTaxonomyId));
        taxonomySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                topicTaxonomyId = taxonomySpinnerAdapter.getItemId(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //提交数据流程控制函数
    private void submitControling()
    {

        showUploadProgressView();

        localImagePathList = imageListAdapter.getImagePathList();

        //若不存在视频但存在图片，则走图片上传链
        if(localImagePathList != null && localImagePathList.size() > 0)
        {
            uploadImage();
        }
        //若存在视频则走视频上传链
        else if(localVideoPathList != null && localVideoPathList.size() > 0)
        {
            submitVideo();
        }
        //若既不存在视频也不存在图片，则直接上传内容
        else
        {
            submitContent();
        }


    }

    private void submitVideo()
    {
        QiniuVideoUpload qiniuVideoUpload = new QiniuVideoUpload(this);
        qiniuVideoUpload.upload(localVideoPathList, new QiniuVideoUploadListener() {
            @Override
            public void onUploading(int index, String key, double percent) {
                updateProgressView(2, index, percent);
            }

            @Override
            public void onUploadFailed() {

            }

            @Override
            public void onUploadComplete(LinkedHashMap<String, VideoUploadResult> result) {
                VideoUploadResult videoUploadResult = result.get(videoPath);
                videoDetailDTO = new VideoDetailDTO();
                videoDetailDTO.setVideoSource(videoUploadResult.getUrl());
                videoDetailDTO.setThumbnail(videoUploadResult.getThumbnailUrl());

                isUploadedVideo = true;
                if(localImagePathList != null && localImagePathList.size() > 0 && isUploadedImage == false){
                    uploadImage();
                }else {
                    submitContent();
                }
            }

            @Override
            public void onUploadCancelled() {

            }
        });
    }

    private void uploadImage()
    {

        //压缩图片
        localImagePathList = compressImage(localImagePathList);

        QiniuMultiImageUpload qiNiuPictureUpload = new QiniuMultiImageUpload(this);
        qiNiuPictureUpload.upload(localImagePathList, new QiniuMultiImageUploadListener() {

            @Override
            public void onUploading(int index, String key, double percent) {
                updateProgressView(1, index, percent);
            }

            @Override
            public void onUploadFailed() {

            }

            @Override
            public void onUploadComplete(LinkedHashMap<String, String> result) {
                serverImagePathMap = result;
                if (serverImagePathMap != null) {
                    isUploadedImage = true;
                    if (videoPath != null && isUploadedVideo == false) {
                        submitVideo();
                    } else {
                        submitContent();
                    }

                } else {

                }
            }

            @Override
            public void onUploadCancelled() {

            }
        });
    }

    private List<String> compressImage(List<String> filePathList){
        List<String> compressImageFilePath = new ArrayList<String>();
        int index = 0;
        for (String filePath : filePathList){
            updateProgressView(3, index, 0.0);
            File saveFile = new File(DiskCachePath.getDiskCacheDir(CreateTopicActivity.this, "compressCache"), "compress_" + System.currentTimeMillis() + ".jpg");
            Bitmap bitmap = BitmapUtils.getBitmap(filePath);
            NativeUtil.compressBitmap(bitmap, saveFile.getAbsolutePath());
            compressImageFilePath.add(saveFile.getAbsolutePath());
            index++;
        }
        return compressImageFilePath;
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
                        hideUploadProgressView();
                        MyLog.d(tag, "onErrorResponse" + error.toString());
                        MyVolleyErrorHelper.showError(CreateTopicActivity.this, error);
                    }
                });

        queue.add(myRequest);
    }


    private void buildTopicParam()
    {
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
        topicDTO.setContent(contentEditText.getText().toString());
        switch(topicType)
        {
            case TopicType.MOOD:
                topicDTO.setType(TopicType.MOOD);
                topicDTO.setTitle(contentEditText.getText().toString());
                topicDTO.setContent("");
                break;
            case TopicType.DISCUSS:
                topicDTO.setType(TopicType.DISCUSS);
                break;
            case TopicType.VIDEO:
                topicDTO.setContent("");
                break;
            case TopicType.S_VIDEO:
                topicDTO.setType(TopicType.S_VIDEO);
                topicDTO.setTitle(contentEditText.getText().toString());
                topicDTO.setType(TopicType.S_VIDEO);
                break;
            case TopicType.NEWS:
                topicDTO.setType(TopicType.NEWS);
                break;
            case TopicType.COURSE:
                topicDTO.setType(TopicType.COURSE);
                topicDTO.setMagicType(courseType);

                CourseMinDTO courseMinDTO = new CourseMinDTO();
                courseMinDTO.setId(courseId);
                topicDTO.setCourse(courseMinDTO);
                break;
        }

        //taxonomy
        if(topicTaxonomyId != null && topicTaxonomyId != 0L){
            TaxonomyDTO taxonomyDTO = new TaxonomyDTO();
            taxonomyDTO.setId(topicTaxonomyId);
            topicDTO.setTaxonomy(taxonomyDTO);
        }

        //image
        MediaWrapDTO mediaWrapDTO = new MediaWrapDTO();
        List<MediaDTO> mediaDTOList = new ArrayList<MediaDTO>();
        if(serverImagePathMap != null){
            Iterator iter = serverImagePathMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();

                String key = (String) entry.getKey();
                String url = StaticResourceConfig.IMG_DOMAIN + (String) entry.getValue();
                MediaDTO mediaDTO = new MediaDTO();
                mediaDTO.setType(MediaType.IMAGE);
                mediaDTO.setPath(url);
                mediaDTOList.add(mediaDTO);
            }
            mediaWrapDTO.setMedias(mediaDTOList);
            topicDTO.setMediaWrap(mediaWrapDTO);
        }


        //video
        if(videoDetailDTO != null){
            topicDTO.setVideoDetail(videoDetailDTO);
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

    public void addImage()
    {
        Intent intent = new Intent(this, MultiImageSelectorActivity.class);

        // whether show camera
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

        // max select image amount
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);

        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);

        startActivityForResult(intent, REQUEST_IMAGE_CODE);

    }


    private void startVideoRecord(){
        /**
         * 压缩参数，可以自由调节
         */
        MovieExportOptions movie_options = new MovieExportOptions.Builder()
                .setVideoProfile("high")
                .setVideoBitrate(Contant.DEFAULT_BITRATE)
                .setVideoPreset(Contant.DEFAULT_VIDEO_Preset).setVideoRateCRF(Contant.DEFAULT_VIDEO_RATE_CRF)
                .setOutputVideoLevel(Contant.DEFAULT_VIDEO_LEVEL)
                .setOutputVideoTune(Contant.DEFAULT_VIDEO_TUNE)
                .configureMuxer(Contant.DEFAULT_VIDEO_MOV_FLAGS_KEY, Contant.DEFAULT_VIDEO_MOV_FLAGS_VALUE)
                .build();

        /**
         * 界面参数
         */
        VideoSessionCreateInfo create_info = new VideoSessionCreateInfo.Builder()
                .setOutputDurationLimit(15)//最大时长
                .setOutputDurationMin(3)//最短时长
                .setMovieExportOptions(movie_options)
                .setWaterMarkPath(Contant.WATER_MARK_PATH)
                .setWaterMarkPosition(1)
                .setBeautyProgress(0)
                .setBeautySkinOn(false)
                .setCameraFacing(Camera.CameraInfo.CAMERA_FACING_BACK)
                .setVideoSize(360, 640)
                .setCaptureHeight(getResources().getDimension(R.dimen.qupai_recorder_capture_height_size))
                .setBeautySkinViewOn(false)
                .setFlashLightOn(false)
                .setTimelineTimeIndicator(true)
                .build();

        _CreateInfo.setSessionCreateInfo(create_info);
        _CreateInfo.setNextIntent(null);
        _CreateInfo.setOutputThumbnailSize(360, 640);//输出图片宽高
        videoPath = FileUtils.newOutgoingFilePath(this);
        _CreateInfo.setOutputVideoPath(videoPath);//输出视频路径
        _CreateInfo.setOutputThumbnailPath(videoPath + ".png");//输出图片路径

        QupaiServiceImpl qupaiService= new QupaiServiceImpl.Builder()
                .setEditorCreateInfo(_CreateInfo).build();
        qupaiService.showRecordPage(this, REQUEST_VIDEO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the result list of select image paths
                    List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    // do your logic ....
                    for (String p : path) {
                        MyLog.d(tag, "imgPath=" + p);

                    }
                    imageListAdapter.setImagePathList(path);
                }
                break;
            case REQUEST_VIDEO_CODE:
                if (resultCode == RESULT_OK) {

                    Toast.makeText(CreateTopicActivity.this, "视频录制成功", Toast.LENGTH_LONG).show();

                    EditorResult result = new EditorResult(data);
                    videoPath = result.getPath();
                    String []thumbailArray = result.getThumbnail();
                    if(thumbailArray.length > 2){
                        thumblePath = thumbailArray[2];
                    }
                    localVideoPathList = new ArrayList<String>();
                    localVideoPathList.add(videoPath);

                    showVideoThumble();
                }
                break;
        }
    }

    private void showVideoThumble(){

        videoThumbleLayout.setVisibility(View.VISIBLE);


        ImageLoader imageLoader = ImageLoader.getInstance();
        //Uri uri = Uri.fromFile(new File(thumblePath));
        imageLoader.displayImage("file://" + thumblePath, videoThumbleImage);
    }

    private void hideVideoThumble(){
        videoThumbleLayout.setVisibility(View.VISIBLE);
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

    @Override
    public void onDelete(int size) {

    }

    @Override
    public void onAdd() {
        addImage();
    }


}
