package com.jixianxueyuan.widget;

import android.app.Instrumentation;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jixianxueyuan.R;

/**
 * Created by pengchao on 7/2/15.
 */
public class MyActionBar extends LinearLayout {

    Context context;

    TextView titleTextView;

    private OnClickListener actionOnClickListener;

    public MyActionBar(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.my_actionbar, this);
        titleTextView = (TextView) view.findViewById(R.id.page_head_title);
    }

    public MyActionBar(final Context context, AttributeSet attrs)
    {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.my_actionbar, this);

        titleTextView = (TextView) view.findViewById(R.id.page_head_title);
        LinearLayout backLayout = (LinearLayout) view.findViewById(R.id.page_head_back);
        LinearLayout actionLayout = (LinearLayout) view.findViewById(R.id.page_head_action);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyActionBar);

        String title = a.getString(R.styleable.MyActionBar_my_title);
        if(title != null)
        {
            titleTextView.setText(title);
        }

        int type = a.getInt(R.styleable.MyActionBar_type, 0);
        if(type == 1)
        {
            actionLayout.setVisibility(View.VISIBLE);
            actionLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(actionOnClickListener != null)
                    {
                        actionOnClickListener.onClick(v);
                    }

                }
            });
        }

        backLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run() {
                        try{
                            Instrumentation inst = new Instrumentation();
                            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                        }
                        catch (Exception e) {
                        }
                    }
                }.start();
            }
        });


    }

    public void setTitle(String title)
    {
        if(titleTextView != null)
        {
            titleTextView.setText(title);
        }
    }

    public void setActionOnClickListener(OnClickListener listener)
    {
        actionOnClickListener = listener;
    }
}
