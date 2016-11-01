package com.jixianxueyuan.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by pengchao on 01/11/16.
 */

public class DeviceUtils {

    // 设备型号
    public static String getDeviceModel(){
        return new Build().MODEL;
    }

    public static String getVersionSdk(){
        return new String(Build.VERSION.SDK);
    }

    public static String getDeviceSystemVersion(){
        return new String(Build.VERSION.RELEASE);
    }

    //获取版本号
    public static String getVersionName(Context context)
    {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    //获取版本号(内部识别号)
    public static int getVersionCode(Context context)
    {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }
}
