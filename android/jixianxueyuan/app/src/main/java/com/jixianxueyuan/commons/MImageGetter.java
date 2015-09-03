package com.jixianxueyuan.commons;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by pengchao on 7/6/15.
 */
public class MImageGetter implements Html.ImageGetter {
    Context c;

    public MImageGetter(TextView text,Context c) {
        this.c = c;
    }

    public Drawable getDrawable(String source) {
        Drawable drawable = null;

        URL url = null;

        String regexPIC_URL="http://img\\.jixianxueyuan\\.com.*";

        try {
            if(source.matches(regexPIC_URL))
            {
                url = new URL(source + "!androidContentImg");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = null;

        try {
            TypedValue typedValue = new TypedValue();
            typedValue.density = TypedValue.DENSITY_DEFAULT;
            drawable = Drawable.createFromStream( url.openStream(), "src");
            DisplayMetrics dm = c.getResources().getDisplayMetrics();
            int dwidth = dm.widthPixels-10;//padding left + padding right
            float dheight = (float)drawable.getIntrinsicHeight()*(float)dwidth/(float)drawable.getIntrinsicWidth();
            int dh = (int)(dheight+0.5);
            int wid = dwidth;
            int hei = dh;
                /*int wid = drawable.getIntrinsicWidth() > dwidth? dwidth: drawable.getIntrinsicWidth();
                int hei = drawable.getIntrinsicHeight() > dh ? dh: drawable.getIntrinsicHeight();*/
            drawable.setBounds(0, 0, wid, hei);
            return drawable;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

}
