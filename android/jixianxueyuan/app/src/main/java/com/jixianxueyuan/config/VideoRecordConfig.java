package com.jixianxueyuan.config;

import android.content.Context;

import com.jixianxueyuan.util.DiskCachePath;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pengchao on 8/2/15.
 */
public class VideoRecordConfig {
    /**
     * 默认时长
     */
    public static  int DEFAULT_DURATION_LIMIT = 8;
    /**
     * 默认码率
     */
    public static  int DEFAULT_BITRATE =2000 * 1000;
    /**
     * 默认Video保存路径，请开发者自行指定
     */
    public static  String VIDEOPATH(Context context){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
        String str = simpleDateFormat.format(new Date());
        return DiskCachePath.getDiskCacheDir(context,"short_video_record").getPath() +"/" + str + ".mp4";
    };

    public static String THUMBPATH(Context context){
        return VIDEOPATH(context) + ".jpg";
    }
}
