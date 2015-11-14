package com.jixianxueyuan.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jixianxueyuan.R;

/**
 * Created by pengchao on 11/14/15.
 */
public class AutoLoadMoreView extends RelativeLayout {

    private Context context;
    private boolean isLoading = true;
    private boolean isOver = false;
    private TextView tipsTextView;
    private ProgressBar progressBar;


    public AutoLoadMoreView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public AutoLoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView(){
        View.inflate(context, R.layout.auto_load_more_view, this);
        tipsTextView = (TextView) findViewById(R.id.auto_load_more_text);
        progressBar = (ProgressBar) findViewById(R.id.auto_load_more_progress);
    }

    public boolean isLoading()
    {
        return isLoading;
    }

    public void reset(){
        tipsTextView.setText(R.string.loading);
        progressBar.setVisibility(VISIBLE);
    }

    public void setOver()
    {
        tipsTextView.setText(R.string.not_more);
        progressBar.setVisibility(GONE);
        isOver = true;
    }
}
