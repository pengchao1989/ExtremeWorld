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
import com.jixianxueyuan.config.ImageLoaderConfig;
import com.jixianxueyuan.dto.biz.CategoryDTO;
import com.jixianxueyuan.util.ACache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by pengchao on 10/17/15.
 */
public class CategoryGridAdapter extends BaseAdapter{

    private final int EXTRA_COUNT = 1;
    public static final int VIEW_TYPE_CATEGORY = 0x10001;
    public static final int VIEW_TYPE_ORDER = 0x10002;

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

    public int getCategoryCount(){
        return categoryDTOList.size();
    }

    @Override
    public int getCount() {
        return categoryDTOList.size() + EXTRA_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if(position < categoryDTOList.size()){
            return VIEW_TYPE_CATEGORY;
        }else {
            return VIEW_TYPE_ORDER;
        }
    }

    @Override
    public CategoryDTO getItem(int position) {
        if(position < categoryDTOList.size()){
            return categoryDTOList.get(position);
        }else {
            return null;
        }

    }

    @Override
    public long getItemId(int position) {
        if(position < categoryDTOList.size()){
            return categoryDTOList.get(position).getId();
        }else {
            return -1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if(getItemViewType(position) == VIEW_TYPE_CATEGORY){
            if(/*convertView == null*/true){
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


        }else if(getItemViewType(position) == VIEW_TYPE_ORDER){
            convertView = LayoutInflater.from(context).inflate(R.layout.category_grid_item_used,null);
        }



        return convertView;
    }

    static class ViewHolder{
        @BindView(R.id.category_grid_item_name)TextView nameTextView;
        @BindView(R.id.category_grid_item_icon) ImageView iconImageView;

        public ViewHolder(View itemView){
            ButterKnife.bind(this, itemView);
        }
    }
}
