package com.jixianxueyuan.commons;

import android.content.Context;
import android.widget.Toast;

import com.jixianxueyuan.R;
import com.jixianxueyuan.config.MyErrorCode;
import com.jixianxueyuan.dto.Error;

/**
 * Created by pengchao on 9/3/15.
 */
public class MyErrorHelper {

    public static String getErrorStr(Context context, Error error){
        String result = "";
        if(error == null){
            return result;
        }
        if (error.getErrorCode() == MyErrorCode.NAME_REPEAT.getErrorCode()){
            result = context.getString(R.string.err_name_repeat);
        }else if(error.getErrorCode() == MyErrorCode.VERIFICATION_CODE_ERROR.getErrorCode()){
            result = context.getString(R.string.err_verification_code);
        }else if(error.getErrorCode() == MyErrorCode.PHONE_REGISTERED.getErrorCode()){
            result = context.getString(R.string.err_phone_registered);
        }
        else {
            result = error.getErrorInfo();
        }

        return result;
    }

    public static void showErrorToast(Context context, Error error){
        Toast.makeText(context, getErrorStr(context, error), Toast.LENGTH_SHORT).show();
    }
}
