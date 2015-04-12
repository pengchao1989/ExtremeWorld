package com.jixianxueyuan.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.jixianxueyuan.adapter.TopicListAdapter;
import com.jixianxueyuan.dto.TopicDTO;
import com.jixianxueyuan.server.ServerMethod;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 2015/4/12.
 */
public class TopicListFragment extends Fragment {

    @InjectView(R.id.topic_list_fragment_listview)
    ListView listView;

    TopicListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        adapter = new TopicListAdapter(this.getActivity());
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.topic_list_fragment, container, false);

        ButterKnife.inject(this,view);

        listView.setAdapter(adapter);

        requestTopicList();

        return view;
    }

    private void requestTopicList()
    {
        RequestQueue queue = Volley.newRequestQueue(this.getActivity());
        String url = ServerMethod.topic;

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Gson gson = new Gson();
                        List<TopicDTO> topicDTOs = gson.fromJson(response, new TypeToken<List<TopicDTO>>(){}.getType());
                        if(topicDTOs != null)
                        {
                            adapter.addDatas(topicDTOs);
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