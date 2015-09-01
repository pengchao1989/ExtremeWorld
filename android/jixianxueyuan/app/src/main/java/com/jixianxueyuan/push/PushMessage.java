package com.jixianxueyuan.push;

import java.io.Serializable;

/**
 * Created by pengchao on 9/1/15.
 */
public class PushMessage implements Serializable {
    private int type;
    private String content;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
