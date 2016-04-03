package com.jixianxueyuan.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.jixianxueyuan.R;

/**
 * Created by pengchao on 4/3/16.
 */
public class CustomMenuItemAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Menu menu;

    public CustomMenuItemAdapter(Activity activity, int menuResId) {
        layoutInflater = LayoutInflater.from(activity);
        this.menu = (new PopupMenu(activity, (View)null)).getMenu();
        activity.getMenuInflater().inflate(menuResId, menu);
    }

    @Override
    public int getCount() {
        return menu.size();
    }

    @Override
    public MenuItem getItem(int position) {
        return menu.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return menu.getItem(position).getItemId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.custom_grid_menu_item_layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.topic_type_item_text);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.topic_type_item_image);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        MenuItem menuItem = menu.getItem(position);
        viewHolder.textView.setText(menuItem.getTitle());
        viewHolder.imageView.setImageDrawable(menuItem.getIcon());

        return view;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
