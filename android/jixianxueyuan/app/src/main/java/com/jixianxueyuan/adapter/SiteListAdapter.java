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
import com.jixianxueyuan.dto.SiteDTO;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 10/30/15.
 */
public class SiteListAdapter extends BaseAdapter {

    private Context context;
    private List<SiteDTO> siteDTOList;

    public SiteListAdapter(Context context){
        this.context = context;
        siteDTOList = new ArrayList<SiteDTO>();
    }

    public void setData(List<SiteDTO> data){
        siteDTOList.clear();
        siteDTOList.addAll(data);
        this.notifyDataSetChanged();
    }

    public void addData(List<SiteDTO> data){
        siteDTOList.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return siteDTOList.size();
    }

    @Override
    public SiteDTO getItem(int position) {
        return siteDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return siteDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.site_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SiteDTO siteDTO = siteDTOList.get(position);
        viewHolder.nameTextView.setText(siteDTO.getName());
        viewHolder.addressTextView.setText(siteDTO.getAddress());

        String imageUrl = siteDTO.getFrontImg() + "!AndroidListItemLarge";
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imageUrl, viewHolder.frontImageView, ImageLoaderConfig.getImageOption(context));

        return convertView;
    }

    static class ViewHolder{
        @InjectView(R.id.site_list_item_front_imageview)ImageView frontImageView;
        @InjectView(R.id.site_list_item_name_textview)TextView nameTextView;
        @InjectView(R.id.site_list_item_address_textview)TextView addressTextView;

        public ViewHolder(View itemView){
            ButterKnife.inject(this, itemView);
        }
    }
}
