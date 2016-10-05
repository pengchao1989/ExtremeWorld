package com.jixianxueyuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.dto.TopicDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengchao on 16-10-5.
 */

public class TopicRecyclerAdapter extends RecyclerView.Adapter<TopicRecyclerAdapter.TopicViewHolder> {

    private Context mContent;
    private List<TopicDTO> mTopicList;

    public TopicRecyclerAdapter(Context context){
        mContent = context;
        mTopicList = new ArrayList<TopicDTO>();
    }

    public void refreshData(List<TopicDTO> topicDTOs){
        mTopicList.clear();
        mTopicList.addAll(topicDTOs);
    }

    public void addData(List<TopicDTO> topicDTOs){

    }

    public TopicDTO getItem(int position){
        return position >= 0 && position < mTopicList.size() ? mTopicList.get(position) : null;
    }


    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TopicViewHolder topicViewHolder = new TopicViewHolder(LayoutInflater.from(mContent).inflate(R.layout.topic_list_item, parent, false));

        return topicViewHolder;
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, int position) {
        TopicDTO topicDTO = mTopicList.get(position);
        holder.title.setText(topicDTO.getTitle());
    }

    @Override
    public int getItemCount() {
        return mTopicList.size();
    }



    class TopicViewHolder extends RecyclerView.ViewHolder{

        TextView title;


        public TopicViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.topic_list_item_title);
        }
    }
}
