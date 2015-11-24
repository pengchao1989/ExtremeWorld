package com.jixianxueyuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.dto.CourseCatalogueDTO;
import com.jixianxueyuan.dto.CourseMinDTO;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengchao on 2015/4/12.
 */
public class CourseListAdapter extends BaseExpandableListAdapter {

    Context context;

    List<CourseCatalogueDTO> courseCatalogueDTOList;

    public CourseListAdapter(Context context){
        this.context = context;
        courseCatalogueDTOList = new ArrayList<CourseCatalogueDTO>();

    }

    public void addDatas(List<CourseCatalogueDTO> courseTaxonomyDTOs)
    {
        for(CourseCatalogueDTO courseTaxonomyDTO:courseTaxonomyDTOs)
        {
            courseCatalogueDTOList.add(courseTaxonomyDTO);
        }

        this.notifyDataSetChanged();

    }

    @Override
    public int getGroupCount() {
        return courseCatalogueDTOList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return courseCatalogueDTOList.get(groupPosition).getCourses().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return courseCatalogueDTOList.get(groupPosition);
    }

    @Override
    public CourseMinDTO getChild(int groupPosition, int childPosition) {
        return courseCatalogueDTOList.get(groupPosition).getCourses().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return courseCatalogueDTOList.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return courseCatalogueDTOList.get(groupPosition).getCourses().get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.course_list_taxonomy_item,null);

        TextView nameTextView = (TextView) convertView.findViewById(R.id.course_list_taxonomy_item_name);

        nameTextView.setText(courseCatalogueDTOList.get(groupPosition).getName());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.course_list_item,null);

        TextView nameTextView = (TextView) convertView.findViewById(R.id.course_list_item_name);

        nameTextView.setText(courseCatalogueDTOList.get(groupPosition).getCourses().get(childPosition).getName());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }


}
