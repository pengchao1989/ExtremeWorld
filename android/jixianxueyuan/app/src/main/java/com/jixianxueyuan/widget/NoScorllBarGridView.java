package com.jixianxueyuan.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by pengchao on 6/4/15.
 */
public class NoScorllBarGridView extends GridView {
    public NoScorllBarGridView(Context context, AttributeSet attrs) {

        super(context, attrs);

    }

    public NoScorllBarGridView(Context context) {

        super(context);

    }

    public NoScorllBarGridView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //重写onMeasure使GridView失去滑动条，以便嵌套至scrollview中能全部显示
        int expandSpec = MeasureSpec.makeMeasureSpec(

                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
