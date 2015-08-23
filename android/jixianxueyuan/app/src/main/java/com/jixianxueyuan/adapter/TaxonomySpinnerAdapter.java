package com.jixianxueyuan.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.dto.TaxonomyDTO;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 8/23/15.
 */
public class TaxonomySpinnerAdapter extends BaseAdapter{

    Context context;
    List<TaxonomyDTO> taxonomyDTOList;

    public TaxonomySpinnerAdapter(Context context){
        this.context = context;
        taxonomyDTOList = new ArrayList<TaxonomyDTO>();
    }

    public void setTaxonomyDTOList(List<TaxonomyDTO> taxonomyDTOs){
        taxonomyDTOList.clear();
        taxonomyDTOList.addAll(taxonomyDTOs);
    }

    public int getTaxonomyIndex(Long taxonomyId){
        int index = 0;
        for (TaxonomyDTO taxonomyDTO : taxonomyDTOList){
            if(taxonomyDTO.getId() == taxonomyId){
                break;
            }
            index++;
        }
        if(index >= taxonomyDTOList.size()){
            index = 0;
        }
        return index;
    }

    @Override
    public int getCount() {
        return taxonomyDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return taxonomyDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return taxonomyDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(null == convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.taxonomy_spinner_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nameTextView.setText(taxonomyDTOList.get(position).getName());

        return convertView;
    }

    public static class ViewHolder{

        @InjectView(R.id.taxonomy_spinner_item_name)TextView nameTextView;
        public ViewHolder(View itemView)
        {
            ButterKnife.inject(this, itemView);
        }
    }
}