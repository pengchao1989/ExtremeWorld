package com.jixianxueyuan.commons;

import android.content.Context;
import android.widget.Toast;

import com.jixianxueyuan.R;
import com.jixianxueyuan.config.MyErrorCode;
import com.jixianxueyuan.dto.ErrorInfo;

/**
 * Created by pengchao on 9/3/15.
 */
public class MyErrorHelper {

    public static String getErrorStr(Context context, ErrorInfo errorInfo){
        String result = "";
        switch (errorInfo.getErrorCode()){
            case MyErrorCode.NAME_REPEAT:
                result = context.getString(R.string.name_repeat_err);
                break;

            default:
                result = errorInfo.getErrorInfo();
                break;
        }

        return result;
    }

    public static void showErrorToast(Context context, ErrorInfo errorInfo){
        Toast.makeText(context, getErrorStr(context,errorInfo), Toast.LENGTH_SHORT).show();
    }
}
