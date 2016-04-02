package com.jixianxueyuan.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.jixianxueyuan.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by pengchao on 4/1/16.
 */
public class NetworkImageHolderView implements Holder<String> {

    private ImageView imageView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context,int position, String data) {
        ImageLoader.getInstance().displayImage(data,imageView);
    }
}
