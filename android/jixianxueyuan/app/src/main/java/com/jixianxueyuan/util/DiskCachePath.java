package com.jixianxueyuan.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by pengchao on 5/26/15.
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
        return new File(cachePath + File.separator + uniqueName);
    }
}
