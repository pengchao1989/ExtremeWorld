package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

/**
 * Created by pengchao on 6/24/15.
 */
public class CreateShortVideoActivity extends Activity {

    String path = null;
    UploadToken uploadToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_short_video_activity);


        path = getIntent().getStringExtra("path");

        requestToken();

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

                uploadManager.put(path, uploadToken.getMyParam(), uploadToken.getUptoken(),
                        new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                Log.i("qiniu", info.path);
                            }
                        }, null);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });

        queue.add(stringRequest);
    }






}
