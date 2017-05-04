package com.jixianxueyuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.dto.CourseMinDTO;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by pengchao on 2015/4/12.
 */
public class CourseListAdapter extends BaseAdapter {

    Context context;

    List<CourseMinDTO> courseDTOList;

    public CourseListAdapter(Context context){
        this.context = context;
        courseDTOList = new ArrayList<CourseMinDTO>();

    }

    public void addDatas(List<CourseMinDTO> courseTaxonomyDTOs)
    {
        for(CourseMinDTO courseTaxonomyDTO:courseTaxonomyDTOs)
        {
            courseDTOList.add(courseTaxonomyDTO);
        }

        this.notifyDataSetChanged();

    }

    public void setDatas(List<CourseMinDTO> courseTaxonomyDTOs){
        courseDTOList.clear();
        courseDTOList.addAll(courseTaxonomyDTOs);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return courseDTOList.size();
    }

    @Override
    public CourseMinDTO getItem(int position) {
        return courseDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return courseDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.course_list_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CourseMinDTO courseMinDTO = courseDTOList.get(position);
        viewHolder.nameTextView.setText(courseMinDTO.getName() + "(" + courseMinDTO.getExplainCount() + ")");
        return convertView;
    }

    public static class ViewHolder{
        @BindView(R.id.course_list_item_name)
        TextView nameTextView;

        public ViewHolder(View itemView){
            ButterKnife.bind(this, itemView);
        }
    }

}
