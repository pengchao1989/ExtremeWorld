package com.jixianxueyuan.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.VideoListAdapter;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.dto.VideoDTO;
import com.jixianxueyuan.server.ServerMethod;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 2015/4/12.
 */
public class VideoListFragment extends Fragment{

    public static final String TAG = VideoListFragment.class.getSimpleName();



    @InjectView(R.id.video_list_fragment_listview)
    ListView listView;

    VideoListAdapter adapter;

    boolean isRefreshData = false;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        adapter = new VideoListAdapter(this.getActivity());
        Log.d("VideoListFragment", "onCreate");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        Log.d("VideoListFragment", "onCreateView");

        View view = inflater.inflate(R.layout.video_list_fragment, container, false);

        ButterKnife.inject(this, view);

        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(!isRefreshData)
        {
            refreshVideoList();
        }
    }

    private void refreshVideoList()
    {
        requestVideoList();
    }

    private void requestVideoList()
    {
        RequestQueue queue = Volley.newRequestQueue(this.getActivity());
        String url = ServerMethod.video;

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Gson gson = new Gson();
                        List<VideoDTO> topicDTOs = gson.fromJson(response, new TypeToken<List<VideoDTO>>(){}.getType());
                        if(topicDTOs != null)
                        {
                            adapter.addDatas(topicDTOs);
                            isRefreshData = true;
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

}
