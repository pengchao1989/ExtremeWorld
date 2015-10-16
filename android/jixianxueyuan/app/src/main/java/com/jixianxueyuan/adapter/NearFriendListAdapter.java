package com.jixianxueyuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.dto.UserDTO;
import com.jixianxueyuan.util.DateTimeFormatter;
import com.jixianxueyuan.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 7/3/15.
 */
public class NearFriendListAdapter extends BaseAdapter {

    Context context;
    List<UserDTO> userDTOList;

    public NearFriendListAdapter(Context context)
    {
        this.context = context;
        userDTOList = new ArrayList<UserDTO>();
    }

    public void refreshData(List<UserDTO> list )
    {
        userDTOList.clear();

        userDTOList.addAll(list);

        this.notifyDataSetChanged();
    }

    public void addDatas(List<UserDTO> list)
    {
        userDTOList.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return userDTOList.size();
    }

    @Override
    public UserDTO getItem(int position) {
        return userDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return userDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.near_friend_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        UserDTO userDTO = userDTOList.get(position);

        String avatarUrl = userDTO.getAvatar();
        if(Util.isOurServerImage(avatarUrl)){
            avatarUrl += "!androidListAvatar";
        }

        ImageLoader.getInstance().displayImage(avatarUrl, viewHolder.avatarImageView);
        viewHolder.nameTextView.setText(userDTO.getName());
        viewHolder.distanceTextView.setText(Util.meterToString(userDTO.getDistance()));
        String timeAgo = DateTimeFormatter.getTimeAgo(context, userDTO.getGeoModifyTime());
        if(timeAgo != null){
            viewHolder.timeAgoTextView.setText(timeAgo);
        }

        if(userDTO.getGender() == null || userDTO.getGender().equals("male")){
            viewHolder.genderImageView.setImageResource(R.mipmap.ic_sex_male);
        }else if(userDTO.getGender().equals("female")){
            viewHolder.genderImageView.setImageResource(R.mipmap.ic_sex_female);
        }

        viewHolder.signatureTextView.setText(userDTO.getSignature());

        return convertView;
    }

    public static class ViewHolder
    {
        @InjectView(R.id.near_friend_list_item_avatar)ImageView avatarImageView;
        @InjectView(R.id.near_friend_list_item_name)TextView nameTextView;
        @InjectView(R.id.near_friend_list_item_distance)TextView distanceTextView;
        @InjectView(R.id.near_friend_list_item_gender)ImageView genderImageView;
        @InjectView(R.id.near_friend_list_item_time)TextView timeAgoTextView;
        @InjectView(R.id.near_friend_list_item_signature)TextView signatureTextView;

        public ViewHolder(View itemView)
        {
            ButterKnife.inject(this, itemView);
        }
    }
}
