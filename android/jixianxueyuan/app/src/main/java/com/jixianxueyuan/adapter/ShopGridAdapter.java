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
import com.jixianxueyuan.config.ImageLoaderConfig;
import com.jixianxueyuan.dto.biz.ShopDTO;
import com.jixianxueyuan.util.ImageUriParseUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by pengchao on 10/17/15.
 */
public class ShopGridAdapter extends BaseAdapter {

    private Context context;
    private List<ShopDTO> shopDTOList;

    public ShopGridAdapter(Context context){
        this.context = context;
        shopDTOList = new LinkedList<ShopDTO>();
    }

    public void setShopDTOList(List<ShopDTO> shopDTOs){
        shopDTOList.clear();
        shopDTOList.addAll(shopDTOs);
        this.notifyDataSetChanged();
    }

    public void addShopDTOLIst(List<ShopDTO> shopDTOs){
        shopDTOList.addAll(shopDTOs);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return shopDTOList.size();
    }

    @Override
    public ShopDTO getItem(int position) {
        return shopDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (position < shopDTOList.size()){
            return shopDTOList.get(position).getId();
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.shop_grid_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ShopDTO shopDTO = shopDTOList.get(position);

        viewHolder.nameTextView.setText(shopDTO.getName());
        viewHolder.signatureTextView.setText(shopDTO.getSignature());
        String imageUrl = shopDTO.getCover() + "!AndroidGridItem";
        viewHolder.iconImageView.setImageURI(ImageUriParseUtil.parse(imageUrl));

        return convertView;
    }

    static class ViewHolder{
        @BindView(R.id.shop_gird_item_icon)SimpleDraweeView iconImageView;
        @BindView(R.id.shop_gird_item_name)TextView nameTextView;
        @BindView(R.id.shop_gird_item_signature)TextView signatureTextView;

        public ViewHolder(View itemView){
            ButterKnife.bind(this,itemView);
        }
    }
}
