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
import com.jixianxueyuan.dto.biz.CategoryDTO;
import com.nostra13.universalimageloader.core.ImageLoader;

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

        CategoryDTO categoryDTO = categoryDTOList.get(position);
        viewHolder.nameTextView.setText(categoryDTO.getName());
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(categoryDTO.getIcon(), viewHolder.iconImageView, ImageLoaderConfig.getImageOption(context));

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
