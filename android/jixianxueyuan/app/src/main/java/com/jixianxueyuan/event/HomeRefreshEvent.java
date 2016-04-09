package com.jixianxueyuan.event;

/**
 * Created by pengchao on 4/9/16.
 */
public class HomeRefreshEvent {

    public static final String EVENT_START = "EVENT_START";
    public static final String EVENT_STOP = "EVENT_STOP";

    public final String message;
    public HomeRefreshEvent(String message) {
        this.message = message;
    }
}
