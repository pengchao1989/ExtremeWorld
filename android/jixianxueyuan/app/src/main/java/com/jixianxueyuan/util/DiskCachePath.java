package com.jixianxueyuan.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.jixianxueyuan.commons.FileUtils;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Created by pengchao on 5/26/15.
 * 目录为SDCard/Android/data/包名/cache/uniqueName
 */
public class DiskCachePath {

    static public File getDiskCacheDir(Context context, String uniqueName) {

        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        File dir = new File(cachePath + File.separator + uniqueName);
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir;
    }

    //dirType Environment.DIRECTORY_DOWNLOADS
    static public File getExtDiskCacheDir(Context context, String dirType, String uniqueName){
        String cachePath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = Environment.getExternalStoragePublicDirectory(dirType).getPath();


            cachePath = cachePath + uniqueName;
        }
        if (!TextUtils.isEmpty(cachePath)){
            return  new File(cachePath);
        }

        return null;
    }
}
