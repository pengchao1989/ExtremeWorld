package com.jixianxueyuan.util.qiniu;

/**
 * Created by pengchao on 9/4/15.
 */
public interface QiniuSingleImageUploadListener {
    void onUploading(double percent);
    void onUploadComplete(String url);
    void onError(String error);
}
