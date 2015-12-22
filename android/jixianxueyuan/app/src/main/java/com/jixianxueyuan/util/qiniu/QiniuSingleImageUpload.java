package com.jixianxueyuan.util.qiniu;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jixianxueyuan.config.StaticResourceConfig;
import com.jixianxueyuan.config.UploadPrefixName;
import com.jixianxueyuan.dto.UploadToken;
import com.jixianxueyuan.server.ServerMethod;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.StringUtils;
import com.jixianxueyuan.util.Util;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

/**
 * Created by pengchao on 9/4/15.
 */
public class QiniuSingleImageUpload {

    Context context;
    private boolean isModify = false;
    UploadToken pictureUploadToken;
    String mPrefixKey = "";
    String mFileKey;
    String imagePath;
    QiniuSingleImageUploadListener listener;

    public QiniuSingleImageUpload(Context context){
        this.context = context;
    }

    public void modify(String imagePath, UploadPrefixName prefixKey, String fileKey, QiniuSingleImageUploadListener listener){
        this.mPrefixKey = prefixKey.getPrefixName();
        this.mFileKey = fileKey;
        this.imagePath = imagePath;
        this.listener = listener;
        isModify = true;

        requestPictureToken();
    }

    public void upload(String imagePath, UploadPrefixName prefixKey, QiniuSingleImageUploadListener listener){
        this.mPrefixKey = prefixKey.getPrefixName();
        this.imagePath = imagePath;
        this.listener = listener;

        requestPictureToken();
    }

    private void requestPictureToken() {

        RequestQueue queue = Volley.newRequestQueue(context);

        String tokenUrl;
        if(isModify){
            String key = mPrefixKey + mFileKey;
            tokenUrl = ServerMethod.imageModifyToken + "?key=" + key;
        }else {
            tokenUrl = ServerMethod.imgUploadToken;
        }
        StringRequest stringRequest = new StringRequest(tokenUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                pictureUploadToken = gson.fromJson(response, UploadToken.class);

                if (pictureUploadToken != null) {
                    upLoadImage();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(listener != null){
                            listener.onError(error.getMessage());
                        }
                    }
                });

        queue.add(stringRequest);
    }

    private void upLoadImage(){
        UploadManager uploadManager = new UploadManager();
        if(StringUtils.isBlank(mPrefixKey)){
            mPrefixKey = "";
        }
        if(TextUtils.isEmpty(mFileKey)){
            mFileKey = Util.getDateKey();
        }

        uploadManager.put(imagePath, mPrefixKey + mFileKey, pictureUploadToken.getUptoken(), new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {

                        MyLog.d("", "key=" + key);
                        if(info.isOK()){
                            if(listener != null){

                                listener.onUploadComplete(StaticResourceConfig.IMG_DOMAIN+key);
                            }
                        }else {
                            listener.onError(info.error);
                        }



                    }
                },
                new UploadOptions(null,null,false,new UpProgressHandler() {
                    @Override
                    public void progress(String key, double percent) {
                        if(listener != null){
                            listener.onUploading(percent);
                        }
                    }
                },null));
    }
}
