package com.jixianxueyuan.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jixianxueyuan.R;
import com.jixianxueyuan.dto.ExhibitionDTO;
import com.jixianxueyuan.util.ImageUriParseUtil;

/**
 * Created by pengchao on 4/1/16.
 */
public class ExhibitionItemHolderView implements Holder<ExhibitionDTO> {

    private SimpleDraweeView imageView;
    private TextView textView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        View itemView = LayoutInflater.from(context).inflate(R.layout.exhibition_item, null);
        imageView = (SimpleDraweeView) itemView.findViewById(R.id.exhibition_item_image);
        textView = (TextView) itemView.findViewById(R.id.exhibition_item_title);
        return itemView;
    }

    @Override
    public void UpdateUI(Context context,int position, ExhibitionDTO data) {
        String imgUrl = data.getImg();
        imageView.setImageURI(ImageUriParseUtil.parse(imgUrl));
        textView.setText(data.getTitle());
    }
}
