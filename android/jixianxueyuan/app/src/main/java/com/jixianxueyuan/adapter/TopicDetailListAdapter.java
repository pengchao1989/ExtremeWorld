package com.jixianxueyuan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.dto.ReplyDTO;
import com.jixianxueyuan.server.StaticResourceConfig;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yumfee.emoji.EmojiconTextView;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 5/23/15.
 */
public class TopicDetailListAdapter extends BaseAdapter {

    Context context;
    LinkedList<ReplyDTO> replyDTOs;

    public TopicDetailListAdapter(Context context)
    {
        this.context = context;
        replyDTOs = new LinkedList<ReplyDTO>();
    }

    public void addNextPageData(List<ReplyDTO> replyDTOs)
    {
        this.replyDTOs.addAll(replyDTOs);
        this.notifyDataSetChanged();
    }

    public void addNew(ReplyDTO replyDTO)
    {
        this.replyDTOs.add(replyDTO);
        this.notifyDataSetChanged();
    }

    public void adFrontPageData()
    {

    }

    @Override
    public int getCount() {
        return replyDTOs.size();
    }

    @Override
    public Object getItem(int position) {
        return replyDTOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return replyDTOs.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null)
        {
            convertView = LinearLayout.inflate(context, R.layout.topic_detail_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }



        ReplyDTO replyDTO = replyDTOs.get(position);

        viewHolder.nameTextView.setText(replyDTO.getUser().getName());
        viewHolder.timeTextView.setText(replyDTO.getCreateTime());
        viewHolder.replyContentTextView.setText(replyDTO.getContent());


        String url = StaticResourceConfig.IMG_DOMAIN + replyDTO.getUser().getAvatar() + "!androidListAvatar";
        ImageLoader.getInstance().displayImage(url, viewHolder.avatarImageView);

        return convertView;
    }


    public static class ViewHolder
    {
        @InjectView(R.id.user_head_avatar)ImageView avatarImageView;
        @InjectView(R.id.user_head_name)TextView nameTextView;
        @InjectView(R.id.user_head_time)TextView timeTextView;
        @InjectView(R.id.topic_detail_list_item_reply_content)EmojiconTextView replyContentTextView;

        public ViewHolder(View itemView)
        {
            ButterKnife.inject(this, itemView);
        }
    }

}
