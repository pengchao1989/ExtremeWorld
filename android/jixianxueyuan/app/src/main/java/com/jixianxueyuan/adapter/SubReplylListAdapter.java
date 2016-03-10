package com.jixianxueyuan.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jixianxueyuan.R;
import com.jixianxueyuan.dto.SubReplyDTO;
import com.jixianxueyuan.util.MyLog;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 8/26/15.
 */
public class SubReplylListAdapter extends BaseAdapter {

    Context context;
    List<SubReplyDTO> subReplyDTOList;

    public SubReplylListAdapter(Context context){
        this.context = context;
        subReplyDTOList = new LinkedList<SubReplyDTO>();
    }

    public void addData(SubReplyDTO subReplyDTO){
        subReplyDTOList.add(subReplyDTO);
        this.notifyDataSetChanged();
    }

    public void setData(List<SubReplyDTO> subReplyDTOs){
        if(subReplyDTOList != null){
            subReplyDTOList.clear();
            subReplyDTOList.addAll(subReplyDTOs);
            this.notifyDataSetChanged();
        }
    }

    public void addNextPageData(List<SubReplyDTO> subReplyDTOs){
        if(subReplyDTOList != null){
            subReplyDTOList.addAll(subReplyDTOs);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return subReplyDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return subReplyDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return subReplyDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LinearLayout.inflate(context,R.layout.subreply_list_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SubReplyDTO subReplyDTO = subReplyDTOList.get(position);
        String name = subReplyDTO.getUser().getName();
        String content = subReplyDTO.getContent();
        SpannableString ss=new SpannableString(name+":"+content);

        //ss.setSpan(new ForegroundColorSpan(Color.BLUE), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ClickableSpan(){

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(context.getResources().getColor(R.color.blue));       //设置文件颜色
                ds.setUnderlineText(true);      //设置下划线
            }

            @Override
            public void onClick(View widget) {
                Toast.makeText(context, "click", Toast.LENGTH_LONG).show();
                MyLog.d("TopicDetailListAdapter", "onCLick");
            }
        },0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.contentTextView.setText(ss);


        return convertView;
    }

    public static class ViewHolder{

        @InjectView(R.id.subreply_list_item_content)
        TextView contentTextView;


        public ViewHolder(View itemView){
            ButterKnife.inject(this,itemView);
        }
    }
}
