package com.jixianxueyuan.widget.fulltoast;


import android.content.Context;

import com.jixianxueyuan.app.MyApplication;

/**
 * Created by pengchao on 2016-09-07.
 */
public class FullToastController {

    private volatile static FullToastController sInstance;
    private FullToast mFullToast;

    public static FullToastController getInstance(){
        if (sInstance == null){
            synchronized (FullToast.class){
                if (sInstance == null){
                    sInstance = new FullToastController(MyApplication.getContext());
                }
            }
        }
        return sInstance;
    }

    private FullToastController(Context context){
        mFullToast = new FullToast(context);
    };

    public static void toast(String title,String content, String tips){
        getInstance().mFullToast.toast(title, content, tips);
    }
}
