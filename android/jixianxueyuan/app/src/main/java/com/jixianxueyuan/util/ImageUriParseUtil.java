package com.jixianxueyuan.util;

import android.net.Uri;

/**
 * Created by pengchao on 9/2/16.
 */
public class ImageUriParseUtil {
    public static Uri parse(String uriString){
        Uri uri = Uri.parse(uriString);
        if (uri == null){
            uri = Uri.EMPTY;
        }
        return uri;
    }
}
