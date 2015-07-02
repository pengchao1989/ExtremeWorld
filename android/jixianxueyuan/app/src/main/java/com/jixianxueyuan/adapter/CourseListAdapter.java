package com.jixianxueyuan.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.dto.CourseTaxonomyDTO;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengchao on 2015/4/12.
 */
public class CourseListAdapter extends BaseExpandableListAdapter {

    Context context;

    List<CourseTaxonomyDTO> courseTaxonomyDTOList;

    public CourseListAdapter(Context context){
        this.context = context;
        courseTaxonomyDTOList = new ArrayList<CourseTaxonomyDTO>();

    }

    public void addDatas(List<CourseTaxonomyDTO> courseTaxonomyDTOs)
    {
        for(CourseTaxonomyDTO courseTaxonomyDTO:courseTaxonomyDTOs)
        {
            courseTaxonomyDTOList.add(courseTaxonomyDTO);
        }

        this.notifyDataSetChanged();

    }

    @Override
    public int getGroupCount() {
        return courseTaxonomyDTOList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return courseTaxonomyDTOList.get(groupPosition).getCourses().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return courseTaxonomyDTOList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return courseTaxonomyDTOList.get(groupPosition).getCourses().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return courseTaxonomyDTOList.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return courseTaxonomyDTOList.get(groupPosition).getCourses().get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.course_list_taxonomy_item,null);

        TextView nameTextView = (TextView) convertView.findViewById(R.id.course_list_taxonomy_item_name);

        nameTextView.setText(courseTaxonomyDTOList.get(groupPosition).getName());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.course_list_item,null);

        TextView nameTextView = (TextView) convertView.findViewById(R.id.course_list_item_name);

        nameTextView.setText(courseTaxonomyDTOList.get(groupPosition).getCourses().get(childPosition).getName());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}
