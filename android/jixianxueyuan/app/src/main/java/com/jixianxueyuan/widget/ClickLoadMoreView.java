package com.jixianxueyuan.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jixianxueyuan.R;

/**
 * Created by pengchao on 6/27/15.
 */
public class ClickLoadMoreView extends RelativeLayout {

    Context context;

    Button loadButton = null;
    ProgressBar progressBar = null;


    boolean isLoading = true;
    boolean isOver = false;


    ClickLoadMoreViewListener clickLoadMoreViewListener = null;

    public ClickLoadMoreView(Context context)
    {
        super(context);
        this.context = context;
        initView();
    }

    public ClickLoadMoreView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView()
    {
        View.inflate(context, R.layout.click_loadmore, this);
        loadButton = (Button) findViewById(R.id.click_loadmore_button);
        loadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLoading && isOver==false)
                {
                    if(clickLoadMoreViewListener != null){
                        clickLoadMoreViewListener.runLoad();
                    }

                    ClickLoadMoreView.this.loadButton.setVisibility(View.GONE);
                    ClickLoadMoreView.this.progressBar.setVisibility(View.VISIBLE);
                    isLoading = true;
                }
                else if(isOver == true)
                {
                    Toast.makeText(context, "没有更多了", Toast.LENGTH_SHORT).show();
                }
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.click_loadmore_progress);
    }

    public boolean isLoading()
    {
        return isLoading;
    }

    public void setOver()
    {
        loadButton.setText(R.string.not_more, TextView.BufferType.NORMAL);
        isOver = true;
    }

    public void onFinish()
    {
        if(ClickLoadMoreView.this.getVisibility() != VISIBLE)
        {
            ClickLoadMoreView.this.setVisibility(VISIBLE);
        }

        isLoading = false;
        ClickLoadMoreView.this.loadButton.setVisibility(View.VISIBLE);
        ClickLoadMoreView.this.progressBar.setVisibility(View.GONE);
    }

    public void setClickLoadMoreViewListener(ClickLoadMoreViewListener clickLoadMoreViewListener)
    {
        this.clickLoadMoreViewListener = clickLoadMoreViewListener;
    }

    public interface ClickLoadMoreViewListener
    {
        void runLoad();
    }

}
