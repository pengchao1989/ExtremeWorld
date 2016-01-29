package com.jixianxueyuan.commons;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

    /**
     * 保存路径的文件夹名称
     */
    public static  String DIR_NAME = "qupai_video_test";

    /**
     * 给指定的文件名按照时间命名
     */
    private static  SimpleDateFormat OUTGOING_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");


    /**
     * 得到指定的Video保存路径
     * @return
     */
    public static File getDoneVideoPath(Context context) {
        String str = OUTGOING_DATE_FORMAT.format(new Date());
        File dir = new File(context.getFilesDir()
                + File.separator + DIR_NAME + "/" + str);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    private static File getStorageDir(Context context) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.MEDIA_MOUNTED), "qupaiVideo");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TAG", "Directory not created");
            }
        }

        return file;
    }

    /**
     * 得到输出的Video保存路径
     * @return
     */
    public static String newOutgoingFilePath(Context context) {
        String str = OUTGOING_DATE_FORMAT.format(new Date());
        String fileName = getStorageDir(context).getPath()+ "/" + File.separator + str + ".mp4";
        return fileName;
    }

}
