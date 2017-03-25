package com.jixianxueyuan.widget;

import android.app.Instrumentation;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jixianxueyuan.R;

/**
 * Created by pengchao on 7/2/15.
 */
public class MyActionBar extends LinearLayout {

    Context context;

    TextView titleTextView;
    TextView actionTextView;
    ImageButton actionImageButton;
    ImageButton actionSecondImageButton;

    private MyActionBarListener actionBarListener;

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
        actionTextView = (TextView) view.findViewById(R.id.page_head_action_text);
        actionImageButton = (ImageButton) view.findViewById(R.id.page_head_action_image);
        actionSecondImageButton = (ImageButton) view.findViewById(R.id.page_head_action_second_image);
        LinearLayout backLayout = (LinearLayout) view.findViewById(R.id.page_head_back);
        FrameLayout actionLayout = (FrameLayout) view.findViewById(R.id.page_head_action);
        FrameLayout actionSecondLayout = (FrameLayout) view.findViewById(R.id.page_head_action_second);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyActionBar);

        String title = a.getString(R.styleable.MyActionBar_action_title);
        String actionText = a.getString(R.styleable.MyActionBar_action_text);
        final Drawable actionDrawable = a.getDrawable(R.styleable.MyActionBar_action_image);
        Drawable actionSecondDrawable = a.getDrawable(R.styleable.MyActionBar_action_second_image);
        if(title != null){
            titleTextView.setText(title);
        }
        if(actionText != null){
            actionTextView.setText(actionText);
        }
        if(actionDrawable != null){
            actionImageButton.setImageDrawable(actionDrawable);
        }
        if (actionSecondDrawable != null){
            actionSecondImageButton.setImageDrawable(actionSecondDrawable);
            actionSecondLayout.setVisibility(VISIBLE);
            actionSecondImageButton.setVisibility(VISIBLE);
            actionSecondImageButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (actionBarListener != null){
                        actionBarListener.onSecondActionClicked();
                    }
                }
            });
        }

        int type = a.getInt(R.styleable.MyActionBar_type, 0);
        if(type == 1) {
            actionTextView.setVisibility(VISIBLE);
            actionLayout.setVisibility(View.VISIBLE);
            setFirstOnActionClick(actionLayout);
        }else if (type == 2){
            actionLayout.setVisibility(View.VISIBLE);
            actionImageButton.setVisibility(VISIBLE);
            setFirstOnActionClick(actionImageButton);
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

    private void setFirstOnActionClick(View actionLayout) {
        actionLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actionBarListener != null)
                {
                    actionBarListener.onFirstActionClicked();
                }

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

    public void setActionText(String text){

    }

    public void setActionOnClickListener(MyActionBarListener listener)
    {
        actionBarListener = listener;
    }


    public interface MyActionBarListener{
        void onFirstActionClicked();
        void onSecondActionClicked();
    }
}
