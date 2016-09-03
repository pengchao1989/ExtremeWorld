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
import com.jixianxueyuan.config.QiniuImageStyle;
import com.jixianxueyuan.dto.UserScoreDTO;
import com.jixianxueyuan.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pengchao on 7/7/16.
 */
public class RankingListAdapter extends BaseAdapter {

    private Context context;
    private List<UserScoreDTO> userScoreDTOList;

    public RankingListAdapter(Context context) {
        this.context = context;
        userScoreDTOList = new ArrayList<UserScoreDTO>();
    }

    public void setData(List<UserScoreDTO> userScoreDTOs) {
        userScoreDTOList.clear();
        userScoreDTOList.addAll(userScoreDTOs);
        notifyDataSetChanged();
    }

    public void addData(List<UserScoreDTO> userScoreDTOs) {
        userScoreDTOList.addAll(userScoreDTOs);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return userScoreDTOList.size();
    }

    @Override
    public UserScoreDTO getItem(int position) {
        if (position < userScoreDTOList.size()){
            return userScoreDTOList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ranking_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == 0){
            viewHolder.rankingIndexImageView.setVisibility(View.VISIBLE);
            viewHolder.rankingIndexTextView.setVisibility(View.GONE);
            viewHolder.rankingIndexImageView.setImageResource(R.mipmap.icon_ranking_no1);
            viewHolder.rankingIndexTextView.setText("");

        }else if (position == 1){
            viewHolder.rankingIndexImageView.setVisibility(View.VISIBLE);
            viewHolder.rankingIndexTextView.setVisibility(View.GONE);
            viewHolder.rankingIndexImageView.setImageResource(R.mipmap.icon_ranking_no2);
            viewHolder.rankingIndexTextView.setText("");
        }else if(position == 2){
            viewHolder.rankingIndexImageView.setVisibility(View.VISIBLE);
            viewHolder.rankingIndexTextView.setVisibility(View.GONE);
            viewHolder.rankingIndexImageView.setImageResource(R.mipmap.icon_ranking_no3);
            viewHolder.rankingIndexTextView.setText("");
        }else {
            viewHolder.rankingIndexImageView.setVisibility(View.GONE);
            viewHolder.rankingIndexTextView.setVisibility(View.VISIBLE);
            viewHolder.rankingIndexTextView.setText(position + 1 +  "");
        }

        String avatarUrl = userScoreDTOList.get(position).getUser().getAvatar();
        UserScoreDTO userScoreDTO = userScoreDTOList.get(position);
        if (userScoreDTO != null){
            if(Util.isOurServerImage(avatarUrl)){
                avatarUrl += QiniuImageStyle.LIST_AVATAR;
            }
            viewHolder.userAvatarImageView.setImageURI(avatarUrl);

            String scoreText = context.getString(R.string.score) + String.format("%.2f", userScoreDTO.getScore());
            viewHolder.scoreTextTextView.setText(scoreText);
            viewHolder.userNameTextView.setText(userScoreDTO.getUser().getName());
        }


        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.user_avatar_image_view)
        SimpleDraweeView userAvatarImageView;
        @BindView(R.id.user_name_text_view)
        TextView userNameTextView;
        @BindView(R.id.score_text_text_view)
        TextView scoreTextTextView;
        @BindView(R.id.ranking_index_image_view)
        ImageView rankingIndexImageView;
        @BindView(R.id.ranking_index_text_view)
        TextView rankingIndexTextView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
