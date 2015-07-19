package com.jixianxueyuan.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.dto.UserMinDTO;
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
    List<UserMinDTO> userMinDTOList;

    public NearFriendListAdapter(Context context)
    {
        this.context = context;
        userMinDTOList = new ArrayList<UserMinDTO>();
    }

    public void refreshData(List<UserMinDTO> list )
    {
        userMinDTOList.clear();

        userMinDTOList.addAll(list);

        this.notifyDataSetChanged();
    }

    public void addDatas(List<UserMinDTO> list)
    {
        userMinDTOList.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return userMinDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return userMinDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return userMinDTOList.get(position).getId();
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

        UserMinDTO userMinDTO = userMinDTOList.get(position);

        String avatarUrl = userMinDTO.getAvatar();
        if(Util.isOurServerImage(avatarUrl)){
            avatarUrl += "!androidListAvatar";
        }

        ImageLoader.getInstance().displayImage(avatarUrl, viewHolder.avatarImageView);
        viewHolder.nameTextView.setText(userMinDTO.getName());
        viewHolder.distanceTextView.setText(Util.meterToString(userMinDTO.getDistance()));


        return convertView;
    }

    public static class ViewHolder
    {
        @InjectView(R.id.near_friend_list_item_avatar)ImageView avatarImageView;
        @InjectView(R.id.near_friend_list_item_name)TextView nameTextView;
        @InjectView(R.id.near_friend_list_item_distance)TextView distanceTextView;

        public ViewHolder(View itemView)
        {
            ButterKnife.inject(this, itemView);
        }
    }
}
