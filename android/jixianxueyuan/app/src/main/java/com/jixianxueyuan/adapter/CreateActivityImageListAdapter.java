package com.jixianxueyuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jixianxueyuan.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengchao on 8/22/15.
 */
public class CreateActivityImageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private Context context;
    private ArrayList<String> imagePathList;
    private OnImageOperatorListener imageDeleteListener;

    public CreateActivityImageListAdapter(Context context){
        this.context = context;
        imagePathList = new ArrayList<String>();

    }

    public void setImageDeleteListener(OnImageOperatorListener listener){
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_widget_image_list_item, parent, false);
            ItemViewHolder holder = new ItemViewHolder(v);
            holder.mImageView = (ImageView) v.findViewById(R.id.reply_widget_image_list_item_image);
            holder.mDeleteView = (ImageView) v.findViewById(R.id.reply_widget_image_list_item_delete);
            return holder;
        }else if (viewType == TYPE_FOOTER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_image_item, parent, false);
            FooterViewHolder footerViewHolder = new FooterViewHolder(v);
            footerViewHolder.mImageView = (ImageView)v.findViewById(R.id.add_image_item_action);
            return footerViewHolder;
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof ItemViewHolder){

            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage("file://" + imagePathList.get(position), itemViewHolder.mImageView);

            itemViewHolder.mDeleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imagePathList.remove(position);
                    CreateActivityImageListAdapter.this.notifyDataSetChanged();
                    if (null != CreateActivityImageListAdapter.this.imageDeleteListener) {
                        CreateActivityImageListAdapter.this.imageDeleteListener.onDelete(CreateActivityImageListAdapter.this.imagePathList.size());
                    }
                }
            });
        }else if (holder instanceof  FooterViewHolder){
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != CreateActivityImageListAdapter.this.imageDeleteListener) {
                        CreateActivityImageListAdapter.this.imageDeleteListener.onAdd();
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return imagePathList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position)){
            return TYPE_FOOTER;
        }

        return TYPE_ITEM;
    }

    private boolean isPositionFooter(int position) {

        return position == getItemCount() - 1;
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        ImageView mDeleteView;

        // TODO Auto-generated method stub
        public ItemViewHolder(View v) {
            super(v);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static interface OnImageOperatorListener {
        public void onDelete(int size);
        public void onAdd();
    }

}
