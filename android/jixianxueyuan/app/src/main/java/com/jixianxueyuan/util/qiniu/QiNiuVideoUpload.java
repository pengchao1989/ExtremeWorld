package com.jixianxueyuan.util.qiniu;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jixianxueyuan.dto.UploadToken;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.server.StaticResourceConfig;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.Util;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by pengchao on 8/2/15.
 */
public class QiNiuVideoUpload {

    Context context;


    UploadToken videoUploadToken;
    List<String> videoPath;
    int videoPathUploadIndex = 0;

    QiNiuVideoUploadListener listener;

    LinkedHashMap<String, VideoUploadResult> resultVideoPath;

    public QiNiuVideoUpload(Context context){
        this.context = context;
    }

    public void upload(List<String> videoPath, QiNiuVideoUploadListener listener){
        this.videoPath = videoPath;
        this.listener = listener;
        resultVideoPath = new LinkedHashMap<String, VideoUploadResult>();
        requestVideoToken();
    }

    private void requestVideoToken(){
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(ServerMethod.videoUploadToken, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                videoUploadToken = gson.fromJson(response, UploadToken.class);

                upLoadVideo();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });

        queue.add(stringRequest);
    }

    private void upLoadVideo(){
        UploadManager uploadManager = new UploadManager();

        uploadManager.put(videoPath.get(videoPathUploadIndex), Util.getUUID(), videoUploadToken.getUptoken(),
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        MyLog.i("qiniu", info.path);
                        MyLog.i("qiniu", key);

                        VideoUploadResult videoUploadResult = new VideoUploadResult();
                        videoUploadResult.setUrl(StaticResourceConfig.VIDEO_DOMAIN + key);
                        videoUploadResult.setThumbnailUrl(StaticResourceConfig.VIDEO_DOMAIN + videoUploadToken.getMyParam());

                        resultVideoPath.put(videoPath.get(videoPathUploadIndex),videoUploadResult );

                        if(videoPathUploadIndex < videoPath.size() -1){
                            videoPathUploadIndex++;
                            upLoadVideo();
                        }else {
                            //全部上传完成
                            if(listener != null){
                                listener.onUploadComplete(resultVideoPath);
                            }
                        }
                    }
                }, new UploadOptions(null, null, false,
                        new UpProgressHandler(){
                            public void progress(String key, double percent)
                            {

                                Log.i("qiniu", key + ": " + percent);
                            }
                        }, null));
    }
}
