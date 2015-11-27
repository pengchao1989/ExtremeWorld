package com.jixianxueyuan.config;

import android.content.Context;
import android.graphics.Bitmap;

import com.jixianxueyuan.R;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by pengchao on 9/3/15.
 */
public class ImageLoaderConfig {

    public static DisplayImageOptions getAvatarOption(Context context){
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.avatar_def)
                .showImageForEmptyUri(R.mipmap.avatar_def) // resource or drawable
                .showImageOnFail(R.mipmap.icon_error_network) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1000)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .build();


        return options;

    }


    public static DisplayImageOptions getImageOption(Context context){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.image_loading)
                .showImageForEmptyUri(R.mipmap.image_default) // resource or drawable
                .showImageOnFail(R.mipmap.image_group_load_f) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1000)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default

                .build();

        return options;

    }


    public static DisplayImageOptions getHeadOption(Context context){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.image_loading)
                .showImageForEmptyUri(R.mipmap.default_head) // resource or drawable
                .showImageOnFail(R.mipmap.default_head) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1000)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default

                .build();

        return options;

    }


}
