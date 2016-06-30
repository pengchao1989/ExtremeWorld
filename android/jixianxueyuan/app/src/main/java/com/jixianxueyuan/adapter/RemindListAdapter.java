package com.jixianxueyuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.config.QiniuImageStyle;
import com.jixianxueyuan.dto.RemindDTO;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pengchao on 8/9/15.
 */
public class RemindListAdapter extends BaseAdapter {

    private Context context;
    private List<RemindDTO> remindDTOList;

    public RemindListAdapter(Context context){

        this.context = context;
        remindDTOList = new LinkedList<RemindDTO>();
    }

    public void refreshData(List<RemindDTO> remindDTOs){
        if(remindDTOList == null){
            remindDTOList = new LinkedList<RemindDTO>();
        }
        remindDTOList.clear();
        remindDTOList.addAll(remindDTOs);
        this.notifyDataSetChanged();
    }

    public void addData(List<RemindDTO> remindDTOs){
        if(remindDTOList != null){
            remindDTOList.addAll(remindDTOs);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return remindDTOList.size();
    }

    @Override
    public RemindDTO getItem(int position) {
        return remindDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return remindDTOList.get(position).getTargetId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.remind_list_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RemindDTO remindDTO = remindDTOList.get(position);

        UserMinDTO speaker = remindDTO.getSpeaker();

        String avatarUrl = speaker.getAvatar();
        if(Util.isOurServerImage(avatarUrl)){
            avatarUrl += QiniuImageStyle.LIST_AVATAR;
        }
        ImageLoader.getInstance().displayImage(avatarUrl, viewHolder.avatarImageView);
        viewHolder.nameTextView.setText(speaker.getName());

        viewHolder.timeTextView.setText(remindDTO.getCreateTime());
        viewHolder.contentTextView.setText(remindDTO.getContent());
        viewHolder.targetTextView.setText(remindDTO.getTargetContent());

        return convertView;
    }

    public static class ViewHolder{

        @BindView(R.id.user_head_avatar)ImageView avatarImageView;
        @BindView(R.id.user_head_name)TextView nameTextView;
        @BindView(R.id.user_head_time)TextView timeTextView;
        @BindView(R.id.remind_list_item_content)
        TextView contentTextView;
        @BindView(R.id.remind_list_item_target_content)
        TextView targetTextView;

        public ViewHolder(View itemView){
            ButterKnife.bind(this, itemView);
        }
    }
}
