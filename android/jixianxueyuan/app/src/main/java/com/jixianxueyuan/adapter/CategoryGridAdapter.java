package com.jixianxueyuan.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.config.ImageLoaderConfig;
import com.jixianxueyuan.dto.biz.CategoryDTO;
import com.jixianxueyuan.util.ACache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 10/17/15.
 */
public class CategoryGridAdapter extends BaseAdapter{

    private Context context;
    private List<CategoryDTO> categoryDTOList;

    public CategoryGridAdapter(Context context){
        this.context = context;
        categoryDTOList = new ArrayList<CategoryDTO>();
    }

    public void setCategoryList(List<CategoryDTO> categoryDTOs){
        categoryDTOList.clear();
        categoryDTOList.addAll(categoryDTOs);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return categoryDTOList.size();
    }

    @Override
    public CategoryDTO getItem(int position) {
        return categoryDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return categoryDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.category_grid_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final CategoryDTO categoryDTO = categoryDTOList.get(position);
        viewHolder.nameTextView.setText(categoryDTO.getName());
        final ACache mCache = ACache.get(context);
        Bitmap bitmap = mCache.getAsBitmap(categoryDTO.getIcon());
        if(bitmap != null){
            viewHolder.iconImageView.setImageBitmap(bitmap);
        }else {
            ImageLoader imageLoader = ImageLoader.getInstance();
            final ViewHolder finalViewHolder = viewHolder;
            imageLoader.loadImage(categoryDTO.getIcon(), ImageLoaderConfig.getImageOption(context), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    finalViewHolder.iconImageView.setImageBitmap(loadedImage);
                    mCache.put(categoryDTO.getIcon(), loadedImage, ACache.TIME_DAY * 7);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }



        //imageLoader.displayImage(categoryDTO.getIcon(), viewHolder.iconImageView, ImageLoaderConfig.getImageOption(context));

        return convertView;
    }

    static class ViewHolder{
        @InjectView(R.id.category_grid_item_name)TextView nameTextView;
        @InjectView(R.id.category_grid_item_icon) ImageView iconImageView;

        public ViewHolder(View itemView){
            ButterKnife.inject(this, itemView);
        }
    }
}
