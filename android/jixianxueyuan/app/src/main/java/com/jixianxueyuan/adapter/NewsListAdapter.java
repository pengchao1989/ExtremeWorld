package com.jixianxueyuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.config.ImageLoaderConfig;
import com.jixianxueyuan.dto.MediaDTO;
import com.jixianxueyuan.dto.MediaWrapDTO;
import com.jixianxueyuan.dto.TopicDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pengchao on 11/8/15.
 */
public class NewsListAdapter extends BaseAdapter {

    Context context;

    List<TopicDTO> topicDTOList;

    private DisplayImageOptions options;


    public NewsListAdapter(Context context){
        this.context = context;

        topicDTOList = new ArrayList<TopicDTO>();
    }

    public void refresh(List<TopicDTO> list)
    {
        topicDTOList.clear();
        topicDTOList.addAll(list);

        this.notifyDataSetChanged();
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
    public int getCount() {
        return topicDTOList.size();
    }

    @Override
    public TopicDTO getItem(int position) {
        return topicDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return topicDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.news_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TopicDTO topicDTO = topicDTOList.get(position);
        viewHolder.titleTextView.setText(topicDTO.getTitle());
        viewHolder.contentTextView.setText(topicDTO.getContent());
        viewHolder.viewCountTextView.setText(topicDTO.getViewCount() + context.getString(R.string.view));
        viewHolder.replyCountTextView.setText(topicDTO.getReplyCount() + context.getString(R.string.reply));
        MediaWrapDTO mediaWrapDTO = topicDTO.getMediaWrap();
        String url = "";
        if(mediaWrapDTO != null){
            if(mediaWrapDTO.getMedias().size() > 0){
                MediaDTO mediaDto = mediaWrapDTO.getMedias().get(0);
                url =  mediaDto.getPath()  + "!AndroidListItem";
            }
        }
        ImageLoader.getInstance().displayImage(url, viewHolder.frontImageView, ImageLoaderConfig.getImageOption(context));


        return convertView;
    }

    public static class ViewHolder{

        @BindView(R.id.news_list_item_title)
        TextView titleTextView;

        @BindView(R.id.news_list_item_content)
        TextView contentTextView;

        @BindView(R.id.news_list_item_agree_textview)
        TextView viewCountTextView;

        @BindView(R.id.news_list_item_reply_textview)
        TextView replyCountTextView;

        @BindView(R.id.news_list_item_front_image)
        ImageView frontImageView;


        public ViewHolder(View itemView){

            ButterKnife.bind(this, itemView);
        }
    }
}
