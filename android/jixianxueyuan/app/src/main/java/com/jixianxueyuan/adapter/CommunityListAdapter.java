package com.jixianxueyuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jixianxueyuan.R;
import com.jixianxueyuan.dto.CommunityDTO;
import com.jixianxueyuan.util.ImageUriParseUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by pengchao on 10/30/15.
 */
public class CommunityListAdapter extends BaseAdapter {

    private Context context;
    private List<CommunityDTO> communityDTOList;

    public CommunityListAdapter(Context context){
        this.context = context;
        communityDTOList = new ArrayList<CommunityDTO>();
    }

    public void setData(List<CommunityDTO> data){
        communityDTOList.clear();
        communityDTOList.addAll(data);
        this.notifyDataSetChanged();
    }

    public void addData(List<CommunityDTO> data){
        communityDTOList.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return communityDTOList.size();
    }

    @Override
    public CommunityDTO getItem(int position) {
        return communityDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return communityDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.community_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CommunityDTO communityDTO = communityDTOList.get(position);
        viewHolder.nameTextView.setText(communityDTO.getName());
        viewHolder.addressTextView.setText(communityDTO.getAddress());

        String imageUrl = communityDTO.getFrontImg() + "!AndroidListItemLarge";
        viewHolder.frontImageView.setImageURI(ImageUriParseUtil.parse(imageUrl));

        return convertView;
    }

    static class ViewHolder{
        @BindView(R.id.community_list_item_front_imageview)SimpleDraweeView frontImageView;
        @BindView(R.id.community_list_item_name_textview)TextView nameTextView;
        @BindView(R.id.community_list_item_address_textview)TextView addressTextView;

        public ViewHolder(View itemView){
            ButterKnife.bind(this, itemView);
        }
    }
}
