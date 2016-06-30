package com.jixianxueyuan.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jixianxueyuan.R;
import com.jixianxueyuan.util.MyLog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pengchao on 5/24/15.
 */
public class RichEditWidget {

    Context context;
    ReplyWidgetListener replyWidgetListener;
    NewEditWidgetListener newEditWidgetListener;

    LinearLayout contentContainer;
    ImageView imgButton;
    ImageView videoButton;
    EditText emojiconEditText;


    List<String> localImagePathList;


    private static final int GET_IMAGE_SUCCESS = 0x1;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what)
            {
                case GET_IMAGE_SUCCESS:
                    int editCursor = emojiconEditText.getSelectionStart();
                    Bundle bundle = msg.getData();
                    CharSequence cs = bundle.getCharSequence("cs");
                    emojiconEditText.getText().insert(editCursor, cs);
                    break;
            }
        }
    };
    public RichEditWidget(Context context, LinearLayout contentContainer)
    {
        this.context = context;
        this.contentContainer = contentContainer;

        initView();
    }

    private void initView()
    {
        View view = LayoutInflater.from(context).inflate(R.layout.rich_edit_widget, null);

        imgButton = (ImageView) view.findViewById(R.id.new_edit_widget_img_button);
        videoButton = (ImageView) view.findViewById(R.id.new_edit_widget_video_button);
        emojiconEditText = (EditText) view.findViewById(R.id.reply_widget_edittext);
        final View rootView = view.findViewById(R.id.reply_widget_root_view);

        imgButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(newEditWidgetListener != null)
                {
                    newEditWidgetListener.onImage();
                }
            }
        });

        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newEditWidgetListener != null){
                    newEditWidgetListener.onVideo();
                }
            }
        });


        contentContainer.addView(view);

    }

    public String getText()
    {
        return emojiconEditText.getText().toString();
    }
    public String getHtml()
    {
        return Html.toHtml(emojiconEditText.getText());
    }
    public List<String> getLocalImagePathList()
    {
        bulidLocalImagePath();
        return localImagePathList;
    }

    public boolean insertImg(String imgPath)
    {
        //根据图片绝对地址，查询或生成缩略图，插入EditText中

        Thread thread = new Thread(new InsertImageRunnable(imgPath));
        thread.start();

        return true;
    }

    public void setReplyWidgetListener(ReplyWidgetListener replyWidgetListener) {
        this.replyWidgetListener = replyWidgetListener;
    }

    public void setNewEditWidgetListener(NewEditWidgetListener listener)
    {
        this.newEditWidgetListener = listener;
    }

    //构建本地图片路径集，并把本地路径作为key
    private void bulidLocalImagePath()
    {
        localImagePathList = new LinkedList<String>();

        String contentStrWithHtml = Html.toHtml(emojiconEditText.getText());
        MyLog.d("RichEditWidget", "initLocalFilePathKey contentStrWithHtml="+contentStrWithHtml);
        String regex = "<(?=img)[^>]+>";
        String regexPath = "\".*\"";

        Pattern ptag = Pattern.compile(regex);
        Matcher mtag=ptag.matcher(contentStrWithHtml);

        Pattern pPath = Pattern.compile(regexPath);
        while(mtag.find())
        {
            String tag = mtag.group();

            Matcher mPath= pPath.matcher(tag);

            if(mPath.find())
            {
                localImagePathList.add(mPath.group().replaceAll("\"", ""));
            }

        }

    }


    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId){
        iconToBeChanged.setImageResource(drawableResourceId);
    }


    private class InsertImageRunnable implements Runnable
    {
        String imgPath;

        public InsertImageRunnable(String path)
        {
            imgPath = path;
        }


        @Override
        public void run() {
            Html.ImageGetter imageGetter = new Html.ImageGetter()
            {
                public Drawable getDrawable(String source)
                {
                    Drawable drawable = null;
                    try {
                        File imageFile = new File(source);
                        Bitmap bitmap =  Picasso.with(context).load(imageFile).resize(160,160).centerCrop().get();

                        drawable= new BitmapDrawable(bitmap);

                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return drawable;

                }
            };

            CharSequence cs = Html.fromHtml("<img src='" + imgPath + "'/><br/>",imageGetter, null);

            Message msg = new Message();
            msg.what = GET_IMAGE_SUCCESS;
            Bundle bundle = new Bundle();
            bundle.putCharSequence("cs", cs);
            msg.setData(bundle);
            handler.sendMessage(msg);


        }
    }
}
