package com.jixianxueyuan.receiver;

import android.content.Context;
import com.alibaba.cchannel.push.receiver.CPushMessage;
import com.alibaba.cchannel.push.receiver.CPushMessageReceiver;
import com.alibaba.cchannel.push.receiver.ChannelStatus;
import com.jixianxueyuan.util.MyLog;

import java.util.Map;

/**
 * Created by pengchao on 8/6/15.
 */
public class CustomReceiver extends CPushMessageReceiver {

    public static final  String tag = CustomReceiver.class.getSimpleName();

    /**
     * 接收到消息的处理
     * @param context
     * @param message
     */
    @Override
    protected void onMessage(Context context, CPushMessage message) {
        MyLog.d(tag, "onMessage msg=" + message.getTitle());
    }

    /**
     * 接收到通知的扩展处理
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    protected void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        MyLog.d(tag, "onNotification msg=" + title);
    }

    /**
     * 从通知栏打开通知的扩展处理
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    protected void onNotificationOpened(Context context, String title, String summary, String extraMap) {

    }

    /**
     * 从通知栏删除通知的扩展处理
     * @param context
     * @param messageId
     */
    @Override
    protected void onNotificationRemoved(Context context, long messageId) {

    }

    /**
     * 通道状态改变
     * @param context
     * @param channelStatus
     */
    @Override
    protected void onChannelStatusChanged(Context context, ChannelStatus channelStatus) {

    }

}