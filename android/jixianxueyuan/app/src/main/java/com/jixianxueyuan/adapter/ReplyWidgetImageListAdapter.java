package com.jixianxueyuan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jixianxueyuan.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pengchao on 8/22/15.
 */
public class ReplyWidgetImageListAdapter extends RecyclerView.Adapter<ReplyWidgetImageListAdapter.ViewHolder> {

    private ArrayList<String> imagePathList;
    OnImageDeleteListener imageDeleteListener;

    public ReplyWidgetImageListAdapter(OnImageDeleteListener listener){
        imagePathList = new ArrayList<String>();
        imageDeleteListener = listener;
    }

    public void setImagePathList(List<String> imagePathList){
        if (null == imagePathList){
            this.imagePathList = new ArrayList<String>();
        }
        this.imagePathList.clear();
        this.imagePathList.addAll(imagePathList);
        this.notifyDataSetChanged();
    }

    public ArrayList<String> getImagePathList(){
        return imagePathList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_widget_image_list_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        holder.mImageView = (ImageView) v.findViewById(R.id.reply_widget_image_list_item_image);
        holder.mDeleteView = (ImageView) v.findViewById(R.id.reply_widget_image_list_item_delete);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage("file://" + imagePathList.get(position), holder.mImageView);

        holder.mDeleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePathList.remove(position);
                ReplyWidgetImageListAdapter.this.notifyDataSetChanged();
                if(null != ReplyWidgetImageListAdapter.this.imageDeleteListener){
                    ReplyWidgetImageListAdapter.this.imageDeleteListener.onDelete(ReplyWidgetImageListAdapter.this.imagePathList.size());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagePathList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        ImageView mDeleteView;

        // TODO Auto-generated method stub
        public ViewHolder(View v) {
            super(v);
        }

    }

    public static interface OnImageDeleteListener{
        public void onDelete(int size);
    }

}
