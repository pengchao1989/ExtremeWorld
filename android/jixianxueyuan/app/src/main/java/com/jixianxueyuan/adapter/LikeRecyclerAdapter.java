package com.jixianxueyuan.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.UserHomeActivity;
import com.jixianxueyuan.config.QiniuImageStyle;
import com.jixianxueyuan.config.UmengEventId;
import com.jixianxueyuan.dto.LikeDTO;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.util.Util;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pengchao on 17-3-20.
 */

public class LikeRecyclerAdapter extends RecyclerView.Adapter<LikeRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<LikeDTO> likeDTOList = new ArrayList<LikeDTO>();

    public LikeRecyclerAdapter(Context context, List<LikeDTO> likeDTOs){
        this.context = context;
        if (likeDTOs != null && likeDTOs.size() > 0){
            likeDTOList.addAll(likeDTOs);
        }
    }

    public void addToHead(LikeDTO likeDTO){
        if (likeDTOList != null){
            likeDTOList.add(0,likeDTO);
            notifyDataSetChanged();
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.small_like_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final UserMinDTO userMinDTO = this.likeDTOList.get(position).getUser();
        String avatarUrl = userMinDTO.getAvatar();
        if(Util.isOurServerImage(avatarUrl)){
            avatarUrl += QiniuImageStyle.LIST_AVATAR;
        }
        if (!TextUtils.isEmpty(avatarUrl)){
            Uri uri = Uri.parse(avatarUrl);
            holder.avatarImageView.setImageURI(uri);
        }else {
            Uri uri = Uri.EMPTY;
            holder.avatarImageView.setImageURI(uri);
        }

        holder.avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(context, UmengEventId.TOPIC_DETAIL_LIKE_ITEM_CLICK);
                Intent intent = new Intent(context, UserHomeActivity.class);
                intent.putExtra(UserHomeActivity.INTENT_USER_MIN, userMinDTO);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return likeDTOList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @BindView(R.id.small_avatar)
        SimpleDraweeView avatarImageView;
    }
}
