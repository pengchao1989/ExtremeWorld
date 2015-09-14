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
        if(errorInfo == null){
            return result;
        }
        if (errorInfo.getErrorCode() == MyErrorCode.NAME_REPEAT.getErrorCode()){
            result = context.getString(R.string.err_name_repeat);
        }else if(errorInfo.getErrorCode() == MyErrorCode.VERIFICATION_CODE_ERROR.getErrorCode()){
            result = context.getString(R.string.err_verification_code);
        }else if(errorInfo.getErrorCode() == MyErrorCode.PHONE_REGISTERED.getErrorCode()){
            result = context.getString(R.string.err_phone_registered);
        }
        else {
            result = errorInfo.getErrorInfo();
        }

        return result;
    }

    public static void showErrorToast(Context context, ErrorInfo errorInfo){
        Toast.makeText(context, getErrorStr(context,errorInfo), Toast.LENGTH_SHORT).show();
    }
}
