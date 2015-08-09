package com.jixianxueyuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jixianxueyuan.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 4/20/15.
 */
public class DiscoveryListAdapter extends BaseAdapter {

    Context context;
    List<DiscoveryItem> discoryItems;

    public DiscoveryListAdapter(Context context){
        this.context = context;
        discoryItems = new ArrayList<DiscoveryItem>();

    }

    public void setDatas(List<DiscoveryItem> discoveryNames)
    {
        discoryItems.clear();
        for(DiscoveryItem item : discoveryNames)
        {
            discoryItems.add(item);
        }

        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return discoryItems.size();
    }

    @Override
    public Object getItem(int position) {
        return discoryItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.discovery_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nameTextView.setText(discoryItems.get(position).getName());
        viewHolder.imageView.setImageResource(discoryItems.get(position).getImageResourceId());

        return convertView;
    }

    static class ViewHolder{

        @InjectView(R.id.discovery_list_item_name)TextView nameTextView;
        @InjectView(R.id.discovery_list_item_image)ImageView imageView;

        public ViewHolder(View itemView)
        {
            ButterKnife.inject(this, itemView);
        }
    }

    public static class DiscoveryItem{
        String  name;
        int imageResourceId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getImageResourceId() {
            return imageResourceId;
        }

        public void setImageResourceId(int imageResourceId) {
            this.imageResourceId = imageResourceId;
        }
    }
}
