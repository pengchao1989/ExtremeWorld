package com.jixianxueyuan.util.qiniu;

import java.util.LinkedHashMap;

/**
 * Created by pengchao on 8/1/15.
 */
public interface QiniuUploadListener {
    void onUploading();
    void onUploadFailed();
    void onUploadComplete(LinkedHashMap<String,String> result);
    void onUploadCancelled();
}
