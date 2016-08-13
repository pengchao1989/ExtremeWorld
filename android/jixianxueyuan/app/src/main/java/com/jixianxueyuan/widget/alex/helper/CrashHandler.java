package com.jixianxueyuan.widget.alex.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作者：Alex
 * 时间：2016年08月06日    08:06
 * 博客：http://www.jianshu.com/users/c3c4ea133871/subscriptions
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    /**
     * 存储 错误日志的 路径
     */
    private String logPath;
    private Context context;
    private OnCrashListener onCrashListener;

    /**
     * 不需要包含 SD 根目录
     */
    public CrashHandler(Context context) {
        this(context, "本地信息/cash.log");
    }
    /**
     * 不需要包含 SD 根目录
     */
    public CrashHandler(Context context, OnCrashListener onCrashListener) {
        this(context, "本地信息/cash.log", onCrashListener);
    }
    /**
     * 不需要包含 SD 根目录
     */
    public CrashHandler(Context context, String logPath) {
        this(context, logPath,null);
    }

    /**
     * 不需要包含 SD 根目录
     */
    public CrashHandler(Context context, String logPath, OnCrashListener onCrashListener) {
        this.logPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + logPath;
        this.context = context;
        this.onCrashListener = onCrashListener;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void setOnCrashListener(OnCrashListener onCrashListener) {
        this.onCrashListener = onCrashListener;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        StringBuilder errorBuilder = new StringBuilder();
        errorBuilder.append("\n" + getStackTraceString(ex));

        long currentTime = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTime));
        String phoneInfo = getPhoneInfo();
        String cashMessage = time + "\n" + phoneInfo + errorBuilder.toString();
        if (onCrashListener != null) {
            onCrashListener.onCrash(cashMessage);
        }
        saveFile(cashMessage, false);

        Process.killProcess(Process.myPid());

    }

    private String getPhoneInfo() {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" versionName : " + packageInfo.versionName);
            stringBuilder.append("\n versionCode : " + packageInfo.versionCode);
            stringBuilder.append("\n OS  version : " + Build.VERSION.RELEASE);
            stringBuilder.append("\n 制造商 : " + Build.MANUFACTURER);
            stringBuilder.append("\n手机型号 : " + Build.MODEL);
            stringBuilder.append("\n cpu架构 : " + Build.CPU_ABI + "  " + Build.CPU_ABI2);
            return stringBuilder.toString();
        } catch (Exception e) {
            Log.e("", "有异常：" + e);
        }
        return "";
    }

    /**
     * 保存文件
     *
     * @param append true-追加|false-清空
     */
    private boolean saveFile(String result, boolean append) {
        FileOutputStream output = null;
        try {
            File file = new File(logPath);
            String parentStr = file.getParent();
            boolean createSDCardDir = createSDCardDir(parentStr);
            if (!createSDCardDir) {
                return false;
            }
            output = new FileOutputStream(new File(logPath), append);
            output.write(result.getBytes());
            output.flush();
            return true;
        } catch (Exception e) {
            Log.e("", "有异常：" + e);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                Log.e("", "有异常：" + e);
            }
        }
        return false;
    }

    private boolean createSDCardDir(String newPath) {
        if (Environment.getExternalStorageDirectory().getAbsolutePath() == null) {
            return false;
        }
        //得到一个路径，内容是sdcard的文件夹路径和名字
        //LogUtils.e("newPath = "+newPath);
        File path1 = new File(newPath);
        if (!path1.exists()) {
            //若不存在，创建目录，可以在应用启动的时候创建
            boolean mkdirs = path1.mkdir();
            //LogUtils.e("创建"+path1.getAbsolutePath()+" 成功 = "+mkdirs);
            return mkdirs;
        }
        return true;
    }

    private String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public interface OnCrashListener {
        public void onCrash(String error);
    }
}

