package com.jixianxueyuan.widget.fulltoast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jixianxueyuan.R;
import com.jixianxueyuan.util.ScreenUtils;


/**
 * Created by pengchao on 2016-09-07.
 */
public class FullToast extends Toast {
    private TextView mTitle;
    private TextView mContent;
    private TextView mTips;

    public FullToast(Context context) {
        super(context);
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflate.inflate(R.layout.full_toast_layout, null);

        mTitle = (TextView) contentView.findViewById(R.id.title);
        mContent = (TextView) contentView.findViewById(R.id.content);
        mTips = (TextView) contentView.findViewById(R.id.tips);

        setView(contentView);

        //保留底部50%的距离  0.5 = 100% -50%
        int[] screenSize = ScreenUtils.getScreenSize(context);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        contentView.measure(w, h);
        int yOffset = (int) (screenSize[1] * 0.5 - contentView.getMeasuredHeight() - ScreenUtils.getStatusBarHeight(context));
        setGravity(Gravity.TOP, 0, yOffset);
    }

    public void toast(String title, String content, String tips){
        mTitle.setText(title);
        mContent.setText(content);
        mTips.setText(tips);
        try {
            this.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
