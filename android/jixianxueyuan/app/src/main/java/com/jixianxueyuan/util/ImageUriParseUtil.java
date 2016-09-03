package com.jixianxueyuan.util;

import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by pengchao on 9/2/16.
 */
public class ImageUriParseUtil {
    public static Uri parse(String uriString){
        Uri uri = null;
        if (!TextUtils.isEmpty(uriString)){
            uri = Uri.parse(uriString);
        }
        if (uri == null){
            uri = Uri.EMPTY;
        }
        return uri;
    }
}
