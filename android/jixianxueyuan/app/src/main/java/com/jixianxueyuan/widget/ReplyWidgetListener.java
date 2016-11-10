package com.jixianxueyuan.widget;

import java.util.List;

/**
 * Created by pengchao on 5/24/15.
 */
public interface ReplyWidgetListener {

    void onCommit(String text);
    void onImageChange(List<String> imagePath);
}
