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
import com.jixianxueyuan.dto.biz.GoodsDTO;
import com.jixianxueyuan.util.ImageUriParseUtil;
import com.jixianxueyuan.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by pengchao on 10/18/15.
 */
public class GoodsListAdapter extends BaseAdapter {

    private Context context;
    private List<GoodsDTO> goodsDTOList;

    public GoodsListAdapter(Context context){
        this.context = context;
        goodsDTOList = new ArrayList<GoodsDTO>();
    }

    public void setData(List<GoodsDTO> goodsDTOs){
        goodsDTOList.clear();
        goodsDTOList.addAll(goodsDTOs);
        this.notifyDataSetChanged();
    }

    public void addData(List<GoodsDTO> goodsDTOs){
        goodsDTOList.addAll(goodsDTOs);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return goodsDTOList.size();
    }

    @Override
    public GoodsDTO getItem(int position) {
        return goodsDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return goodsDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.goods_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        GoodsDTO goodsDTO = goodsDTOList.get(position);
        viewHolder.titleTextView.setText(goodsDTO.getName());

        String imageUrl = "";

        if(Util.isOurServerImage(goodsDTO.getCover())){
            imageUrl = goodsDTO.getCover() + "!AndroidGridItem";
        }else{
            imageUrl = goodsDTO.getCover();
        }
        viewHolder.imageView.setImageURI(ImageUriParseUtil.parse(imageUrl));

        return convertView;
    }

    static class ViewHolder{
        @BindView(R.id.goods_list_item_image)SimpleDraweeView imageView;
        @BindView(R.id.goods_list_item_title)TextView titleTextView;

        public ViewHolder(View itemView){
            ButterKnife.bind(this, itemView);
        }
    }
}
