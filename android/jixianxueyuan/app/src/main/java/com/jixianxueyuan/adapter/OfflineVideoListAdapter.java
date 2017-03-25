package com.jixianxueyuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.commons.downloader.TopicDownloaderManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pengchao on 17-3-26.
 */

public class OfflineVideoListAdapter extends BaseAdapter{

    private Context context;
    private List<TopicDownloaderManager.VideoCacheFile> videoCacheFileList;

    public OfflineVideoListAdapter(Context context, List<TopicDownloaderManager.VideoCacheFile> videoCacheFiles){
        this.context = context;
        this.videoCacheFileList = videoCacheFiles;
    }


    @Override
    public int getCount() {
        return videoCacheFileList == null ? 0 : videoCacheFileList.size();
    }

    @Override
    public TopicDownloaderManager.VideoCacheFile getItem(int i) {
        return videoCacheFileList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.offline_video_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TopicDownloaderManager.VideoCacheFile videoCacheFile = (TopicDownloaderManager.VideoCacheFile) getItem(position);

        viewHolder.titleTextView.setText(videoCacheFile.getFileName());

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.offline_video_item_title)TextView titleTextView;

        public ViewHolder(View itemView){
            ButterKnife.bind(this, itemView);
        }
    }
}
