package com.jixianxueyuan.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.dto.CourseTaxonomyDTO;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by pengchao on 3/1/16.
 */
public class CourseTaxonomyListAdapter extends BaseAdapter{

    private Context context;
    private List<CourseTaxonomyDTO> courseTaxonomyDTOList;

    public CourseTaxonomyListAdapter(Context context){
        this.context = context;
        courseTaxonomyDTOList = new ArrayList<CourseTaxonomyDTO>();
    }

    public void setData(List<CourseTaxonomyDTO> courseTaxonomyDTOs){
        if(courseTaxonomyDTOs == null){
            return;
        }
        courseTaxonomyDTOList.clear();
        courseTaxonomyDTOList.addAll(courseTaxonomyDTOs);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return courseTaxonomyDTOList.size();
    }

    @Override
    public CourseTaxonomyDTO getItem(int position) {
        return courseTaxonomyDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return courseTaxonomyDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.course_taxonomy_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Resources resources = context.getResources();
        int[] courseTaxonomyColors =resources.getIntArray(R.array.course_taxonomy_color_array);
        viewHolder.containerLayout.setBackgroundColor(courseTaxonomyColors[position%(courseTaxonomyColors.length)]);
        viewHolder.nameTextView.setText(courseTaxonomyDTOList.get(position).getName());
        viewHolder.desTextView.setText(courseTaxonomyDTOList.get(position).getDes());
        return convertView;
    }

    static class ViewHolder{
        @InjectView(R.id.course_taxonomy_list_item_container)
        LinearLayout containerLayout;
        @InjectView(R.id.course_taxonomy_list_item_name)
        TextView nameTextView;
        @InjectView(R.id.course_taxonomy_list_item_des)
        TextView desTextView;

        public ViewHolder(View itemView){
            ButterKnife.inject(this, itemView);
        }
    }

}
