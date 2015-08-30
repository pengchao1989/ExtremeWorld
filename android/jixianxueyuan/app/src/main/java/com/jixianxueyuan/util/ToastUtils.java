package com.jixianxueyuan.util;

import android.widget.Toast;

import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.dto.ErrorInfo;

/**
 * Created by pengchao on 8/30/15.
 */
public class ToastUtils {

    public static void showError(ErrorInfo errorInfo){
        if(errorInfo != null){
            String errorString = "err:" + errorInfo.getErrorCode() + " " + errorInfo.getErrorInfo();
            Toast.makeText(MyApplication.getContext(),errorString,Toast.LENGTH_SHORT).show();
        }

    }
}
