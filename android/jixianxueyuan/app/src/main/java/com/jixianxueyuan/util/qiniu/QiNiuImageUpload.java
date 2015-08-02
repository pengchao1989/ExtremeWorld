package com.jixianxueyuan.util.qiniu;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jixianxueyuan.dto.UploadToken;
import com.jixianxueyuan.server.ServerMethod;
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
 * Created by pengchao on 8/1/15.
 */
public class QiNiuImageUpload {

    Context context;
    UploadToken pictureUploadToken = null;
    QiNiuImageUploadListener listener = null;
    List<String> imagePathList;
    LinkedHashMap<String,String> serverImagePathMap;

    int imagePathUploadIndex = 0;

    public QiNiuImageUpload(Context context){
        this.context = context;
    }

    public void upload(List<String> imagePath, QiNiuImageUploadListener listener){
        this.listener = listener;
        this.imagePathList = imagePath;
        serverImagePathMap = new LinkedHashMap<String,String>();
        requestPictureToken();
    }

    private void requestPictureToken() {

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(ServerMethod.imgUploadToken, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                pictureUploadToken = gson.fromJson(response, UploadToken.class);

                MyLog.d("CreateTopicActivity", "pictureUploadToken=" + pictureUploadToken);

                if(pictureUploadToken != null){
                    upLoadImage();
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

    private void upLoadImage(){
        UploadManager uploadManager = new UploadManager();

        MyLog.d("CreateTopicActivity","upLoadImage path index=" + imagePathUploadIndex);

        uploadManager.put(imagePathList.get(imagePathUploadIndex), Util.getDateKey(), pictureUploadToken.getUptoken(),
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {

                        MyLog.d("", "key=" + key);


                        serverImagePathMap.put(imagePathList.get(imagePathUploadIndex), key);

                        if(imagePathUploadIndex < imagePathList.size() - 1)
                        {
                            imagePathUploadIndex++;
                            upLoadImage();
                        }
                        else
                        {
                            //全部上传完成
                            if(listener != null){
                                listener.onUploadComplete(serverImagePathMap);
                            }
                        }

                    }
                },
                new UploadOptions(null,null,false,new UpProgressHandler() {
                    @Override
                    public void progress(String key, double percent) {

                    }
                },null)
        );
    }

}
