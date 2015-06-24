package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jixianxueyuan.R;
import com.jixianxueyuan.dto.UploadToken;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyHttpClient;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.RoundProgressBarWidthNumber;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pengchao on 6/24/15.
 */
public class CreateShortVideoActivity extends Activity {

    @InjectView(R.id.create_short_video_submit)Button submitButton;
    @InjectView(R.id.create_short_video_thumb)ImageView thumbImageView;
    @InjectView(R.id.create_short_video_progress)
    RoundProgressBarWidthNumber roundProgressBarWidthNumber;

    String path = null;
    UploadToken uploadToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_short_video_activity);

        ButterKnife.inject(this);

        path = getIntent().getStringExtra("path");

        Bitmap bitmap = getVideoThumbnail(path, 512, 512, MediaStore.Images.Thumbnails.MINI_KIND);
        thumbImageView.setImageBitmap(bitmap);

    }

    private void requestToken()
    {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(ServerMethod.uploadToken, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                uploadToken = gson.fromJson(response, UploadToken.class);

                MyLog.d("CreateShortVideoActivity", "path=" + path);

                UploadManager uploadManager = new UploadManager();

                uploadManager.put(path, null, uploadToken.getUptoken(),
                        new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                Log.i("qiniu", info.path);

                                Toast.makeText(CreateShortVideoActivity.this,"上传成功", Toast.LENGTH_LONG).show();
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


    @OnClick(R.id.create_short_video_submit)void onSubmit()
    {
        roundProgressBarWidthNumber.setVisibility(View.VISIBLE);
        requestToken();
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
