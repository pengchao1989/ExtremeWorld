package com.jixianxueyuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.config.lotteryPlan.LotteryPlanStatus;
import com.jixianxueyuan.dto.lottery.LotteryPlanDTO;
import com.jixianxueyuan.util.DateTimeFormatter;
import com.jixianxueyuan.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pengchao on 17-4-29.
 */

public class LotteryPlanListAdapter extends BaseAdapter {

    private Context context;
    private List<LotteryPlanDTO> lotteryPlanDTOList;

    public LotteryPlanListAdapter(Context context){
        this.context = context;
        lotteryPlanDTOList = new ArrayList<LotteryPlanDTO>();
    }

    public void setDatas(List<LotteryPlanDTO> lotteryPlanDTOs){
        if (lotteryPlanDTOList != null){
            lotteryPlanDTOList.clear();
        }else {
            lotteryPlanDTOList = new ArrayList<LotteryPlanDTO>();
        }
        lotteryPlanDTOList.addAll(lotteryPlanDTOs);
        notifyDataSetChanged();
    }

    public void addDatas(List<LotteryPlanDTO> lotteryPlanDTOs){
        if (lotteryPlanDTOList != null){
            lotteryPlanDTOList.addAll(lotteryPlanDTOs);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return lotteryPlanDTOList.size();
    }

    @Override
    public LotteryPlanDTO getItem(int i) {
        return lotteryPlanDTOList != null? lotteryPlanDTOList.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return lotteryPlanDTOList.get(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.lottery_plan_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LotteryPlanDTO lotteryPlanDTO = lotteryPlanDTOList.get(position);
        viewHolder.titleTextView.setText(lotteryPlanDTO.getTitle());
        viewHolder.openTimeTextView.setText(DateUtils.format(lotteryPlanDTO.getLotteryTime(), DateUtils.FORMAT_ALL));
        viewHolder.sponsorTextView.setText(lotteryPlanDTO.getSponsor());
        if (LotteryPlanStatus.COMPLETED.equals(lotteryPlanDTO.getStatus())){
            viewHolder.statusTextView.setTextColor(context.getResources().getColor(R.color.green));
            viewHolder.statusTextView.setText("已完成");
        }else if (LotteryPlanStatus.WAITING_LOTTERY.equals(lotteryPlanDTO.getStatus())){
            viewHolder.statusTextView.setTextColor(context.getResources().getColor(R.color.red));
            viewHolder.statusTextView.setText("进行中");
        }
        return convertView;
    }

    public static class ViewHolder{

        @BindView(R.id.lottery_plan_list_item_title_textview)TextView titleTextView;
        @BindView(R.id.lottery_plan_list_item_status_textview)TextView statusTextView;
        @BindView(R.id.lottery_plan_list_item_open_time_value)TextView openTimeTextView;
        @BindView(R.id.lottery_plan_list_item_sponsor)TextView sponsorTextView;

        public ViewHolder(View itemView)
        {
            ButterKnife.bind(this, itemView);
        }
    }
}
