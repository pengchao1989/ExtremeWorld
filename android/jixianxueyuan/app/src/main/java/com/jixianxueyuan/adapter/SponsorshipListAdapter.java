package com.jixianxueyuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jixianxueyuan.R;
import com.jixianxueyuan.config.QiniuImageStyle;
import com.jixianxueyuan.dto.SponsorshipDTO;
import com.jixianxueyuan.util.ImageUriParseUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by pengchao on 12/3/15.
 */
public class SponsorshipListAdapter extends BaseAdapter {

    private Context context;
    private List<SponsorshipDTO> sponsorshipDTOList;

    private DisplayImageOptions options;

    public SponsorshipListAdapter(Context context){
        this.context = context;
        sponsorshipDTOList = new ArrayList<SponsorshipDTO>();

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20)).build();
    }

    public void setData(List<SponsorshipDTO> sponsorshipDTOs){
        sponsorshipDTOList.clear();;
        sponsorshipDTOList.addAll(sponsorshipDTOs);
        this.notifyDataSetChanged();
    }

    public void addData(List<SponsorshipDTO> sponsorshipDTOs){
        sponsorshipDTOList.addAll(sponsorshipDTOs);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return sponsorshipDTOList.size();
    }

    @Override
    public SponsorshipDTO getItem(int position) {
        return sponsorshipDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return sponsorshipDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.sponsorship_list_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SponsorshipDTO sponsorshipDTO = sponsorshipDTOList.get(position);
        if(sponsorshipDTO!= null){
            if(sponsorshipDTO.getUser() != null){
                String avatarUrl = sponsorshipDTO.getUser().getAvatar() + QiniuImageStyle.LIST_AVATAR;
                viewHolder.avatarImageView.setImageURI(ImageUriParseUtil.parse(avatarUrl));

                viewHolder.nameTextView.setText(sponsorshipDTO.getUser().getName());
            }
        }
        viewHolder.sumTextView.setText(String.format("%.2f", sponsorshipDTO.getSum()) + context.getString(R.string.money_unit));
        viewHolder.messageTextView.setText(sponsorshipDTO.getMessage());

        return convertView;
    }

    public static class ViewHolder{

        @BindView(R.id.sponsorship_list_item_avatar)
        SimpleDraweeView avatarImageView;
        @BindView(R.id.sponsorship_list_item_name)
        TextView nameTextView;
        @BindView(R.id.sponsorship_list_item_sum)
        TextView sumTextView;
        @BindView(R.id.sponsorship_list_item_message)
        TextView messageTextView;


        public ViewHolder(View itemView){
            ButterKnife.bind(this, itemView);
        }
    }
}
