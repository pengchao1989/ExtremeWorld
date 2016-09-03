package com.jixianxueyuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jixianxueyuan.R;
import com.jixianxueyuan.config.QiniuImageStyle;
import com.jixianxueyuan.dto.UserDTO;
import com.jixianxueyuan.util.DateTimeFormatter;
import com.jixianxueyuan.util.ImageUriParseUtil;
import com.jixianxueyuan.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

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
            avatarUrl += QiniuImageStyle.LIST_AVATAR;
        }
        viewHolder.avatarImageView.setImageURI(ImageUriParseUtil.parse(avatarUrl));
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
        @BindView(R.id.near_friend_list_item_avatar)SimpleDraweeView avatarImageView;
        @BindView(R.id.near_friend_list_item_name)TextView nameTextView;
        @BindView(R.id.near_friend_list_item_distance)TextView distanceTextView;
        @BindView(R.id.near_friend_list_item_gender)ImageView genderImageView;
        @BindView(R.id.near_friend_list_item_time)TextView timeAgoTextView;
        @BindView(R.id.near_friend_list_item_signature)TextView signatureTextView;

        public ViewHolder(View itemView)
        {
            ButterKnife.bind(this, itemView);
        }
    }
}
