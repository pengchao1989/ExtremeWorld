package com.jixianxueyuan.commons;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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

    private static String getVideoRealPathFromURI(Context context, Uri uri) {
/*        String res = null;
        String[] proj = { MediaStore.Video.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null){
            if(cursor.moveToFirst()){;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
            return res;
        }
        return "";*/

        String string =uri.toString();
        File file;
        String a[]=new String[2];
        //判断文件是否在sd卡中
        if (string.indexOf(String.valueOf(Environment.getExternalStorageDirectory()))!=-1){
            //对Uri进行切割
            a = string.split(String.valueOf(Environment.getExternalStorageDirectory()));
            //获取到file
            file = new File(Environment.getExternalStorageDirectory(),a[1]);

        }else if(string.indexOf(String.valueOf(Environment.getDataDirectory()))!=-1){ //判断文件是否在手机内存中
            //对Uri进行切割
            a =string.split(String.valueOf(Environment.getDataDirectory()));
            //获取到file
            file = new File(Environment.getDataDirectory(),a[1]);
        }else{
            //出现其他没有考虑到的情况
            Toast.makeText(context,"文件路径解析失败！",Toast.LENGTH_SHORT).show();
            return "";
        }
        return file.getAbsolutePath();

    }

    private static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null){
            if(cursor.moveToFirst()){;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
            return res;
        }else {
            return "";
        }

    }

    public static String getSelfRealPathFromURI(Context context, Uri contentUri){
        String filePath = getRealPathFromURI(context, contentUri);
        if (!TextUtils.isEmpty(filePath)){
            return filePath;
        }else {
            filePath = getVideoRealPathFromURI(context, contentUri);
        }
        return filePath;
    }
}
