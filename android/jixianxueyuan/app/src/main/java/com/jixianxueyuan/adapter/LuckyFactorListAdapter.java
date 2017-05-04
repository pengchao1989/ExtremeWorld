package com.jixianxueyuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.config.QiniuImageStyle;
import com.jixianxueyuan.dto.UserMinDTO;
import com.jixianxueyuan.dto.lottery.LuckyFactorDTO;
import com.jixianxueyuan.util.DateUtils;
import com.jixianxueyuan.util.ImageUriParseUtil;
import com.jixianxueyuan.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pengchao on 17-5-2.
 */

public class LuckyFactorListAdapter extends BaseAdapter {

    private Context context;
    private List<LuckyFactorDTO> luckyFactorDTOList;

    public LuckyFactorListAdapter(Context context){
        this.context = context;
        luckyFactorDTOList = new ArrayList<LuckyFactorDTO>();
    }

    public void setDatas(List<LuckyFactorDTO> luckyFactorDTOs){
        luckyFactorDTOList.clear();
        luckyFactorDTOList.addAll(luckyFactorDTOs);
        notifyDataSetChanged();
    }

    public void addDatas(List<LuckyFactorDTO> luckyFactorDTOs){
        luckyFactorDTOList.addAll(luckyFactorDTOs);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return luckyFactorDTOList.size();
    }

    @Override
    public Object getItem(int i) {
        return luckyFactorDTOList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return luckyFactorDTOList.get(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.lucky_factor_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LuckyFactorDTO luckyFactorDTO = luckyFactorDTOList.get(position);

        viewHolder.nameTextView.setText(luckyFactorDTO.getUser().getName());

        UserMinDTO userDTO = luckyFactorDTO.getUser();
        if(userDTO.getGender() == null || userDTO.getGender().equals("male")){
            viewHolder.genderImageView.setImageResource(R.mipmap.ic_sex_male);
        }else if(userDTO.getGender().equals("female")){
            viewHolder.genderImageView.setImageResource(R.mipmap.ic_sex_female);
        }

        viewHolder.timeTextView.setText(DateUtils.format(luckyFactorDTO.getCreateTime(), DateUtils.FORMAT_ALL));
        viewHolder.numberTextView.setText(String.valueOf(luckyFactorDTO.getNumber()));

        String avatarUrl = userDTO.getAvatar();
        if(Util.isOurServerImage(avatarUrl)){
            avatarUrl += QiniuImageStyle.LIST_AVATAR;
        }
        viewHolder.avatarImageView.setImageURI(ImageUriParseUtil.parse(avatarUrl));

        return convertView;
    }

    public static class ViewHolder{

        @BindView(R.id.lucky_factor_list_item_avatar)
        ImageView avatarImageView;
        @BindView(R.id.lucky_factor_list_item_name)
        TextView nameTextView;
        @BindView(R.id.lucky_factor_list_item_gender)
        ImageView genderImageView;
        @BindView(R.id.lucky_factor_list_item_time)
        TextView timeTextView;
        @BindView(R.id.lucky_factor_list_item_number_textview)
        TextView numberTextView;

        public ViewHolder(View itemView){


            ButterKnife.bind(this, itemView);
        }
    }
}
