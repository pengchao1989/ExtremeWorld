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

    public MyActionBar(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.my_actionbar, this);
    }

    public MyActionBar(final Context context, AttributeSet attrs)
    {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.my_actionbar, this);

        TextView titleTextView = (TextView) view.findViewById(R.id.page_head_title);
        LinearLayout backLayout = (LinearLayout) view.findViewById(R.id.page_head_back);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyActionBar);

        String title = a.getString(R.styleable.MyActionBar_my_title);
        if(title != null)
        {
            titleTextView.setText(title);
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

}
