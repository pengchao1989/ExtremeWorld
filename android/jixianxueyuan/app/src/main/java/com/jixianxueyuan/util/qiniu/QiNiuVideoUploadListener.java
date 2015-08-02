package com.jixianxueyuan.util.qiniu;

import java.util.LinkedHashMap;

/**
 * Created by pengchao on 8/2/15.
 */
public interface QiNiuVideoUploadListener {
    void onUploading();
    void onUploadFailed();
    void onUploadComplete(LinkedHashMap<String,VideoUploadResult> result);
    void onUploadCancelled();
}
