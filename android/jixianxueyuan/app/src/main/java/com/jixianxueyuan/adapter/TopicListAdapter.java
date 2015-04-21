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

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by pengchao on 2015/4/12.
 */
public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    Context context;

    List<TopicDTO> topicDTOList;

    public TopicListAdapter(Context context)
    {
        this.context = context;
        topicDTOList = new ArrayList<TopicDTO>();
    }

    public void addDatas(List<TopicDTO> topicDTOs)
    {
        for(TopicDTO dtoItem : topicDTOs)
        {
            topicDTOList.add(dtoItem);
        }

        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.topic_list_item, null);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleTextView.setText(topicDTOList.get(position).getTitle());

        holder.timeTextView.setText(topicDTOList.get(position).getCreateTime());
        //holder.nameTextView.setText(topicDTOList.get(position).getCreateUser().getName());

    }

    @Override
    public int getItemCount() {
        return topicDTOList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        @InjectView(R.id.topic_list_item_title)
        TextView titleTextView;

        @InjectView(R.id.topic_list_item_name)
        TextView nameTextView;

        @InjectView(R.id.topic_list_item_time)
        TextView timeTextView;

        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }
}
