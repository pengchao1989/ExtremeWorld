package com.jixianxueyuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.dto.VideoDTO;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 2015/4/12.
 */
public class VideoListAdapter extends BaseAdapter {

    Context context;
    List<VideoDTO> videoDTOList;

    public VideoListAdapter(Context context){
        this.context = context;

        videoDTOList = new ArrayList<VideoDTO>();
    }

    public void addDatas(List<VideoDTO> videoDTOs){
        for(VideoDTO videoDTOItem : videoDTOs)
        {
            videoDTOList.add(videoDTOItem);
        }

        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return videoDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return videoDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return videoDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.video_list_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.titleTextView.setText(videoDTOList.get(position).getTitle());

        return convertView;
    }

    static class ViewHolder{

        @InjectView(R.id.video_list_item_title)
        TextView titleTextView;

        public ViewHolder(View view){
            ButterKnife.inject(this, view);
        }
    }
}
