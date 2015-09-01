package com.jixianxueyuan.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.alibaba.cchannel.push.receiver.CPushMessage;
import com.alibaba.cchannel.push.receiver.CPushMessageReceiver;
import com.alibaba.cchannel.push.receiver.ChannelStatus;
import com.google.gson.Gson;
import com.jixianxueyuan.MainActivity;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.RemindListActivity;
import com.jixianxueyuan.dto.RemindDTO;
import com.jixianxueyuan.push.PushMessage;
import com.jixianxueyuan.push.PushMessageType;
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
        MyLog.d(tag, "onMessage content=" + message.getContent());

        Gson gson = new Gson();
        PushMessage pushMessage = gson.fromJson(new String(message.getContent()),PushMessage.class);
        switch (pushMessage.getType()){
            case PushMessageType.REMIND:
                notifyRemind(context,pushMessage.getContent());
                break;
        }



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

    private void notifyRemind(Context context,String remindJson){

        Gson gson = new Gson();
        RemindDTO remindDTO = gson.fromJson(remindJson, RemindDTO.class);

        String titleText = remindDTO.getSpeaker().getName() + "回应了你!";
        String contentText = remindDTO.getContent().length() > 50 ? remindDTO.getContent().substring(0,49) :
                remindDTO.getContent();

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
        Intent notificationIntent = new Intent(context,RemindListActivity.class); //点击该通知后要跳转的Activity
        //notificationIntent.putExtra(name, value);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0,notificationIntent,0);


        Notification notification = new Notification.Builder(context)
                .setContentTitle(titleText)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();

        //定义下拉通知栏时要展现的内容信息
        //设置通知的事件消息


        //用mNotificationManager的notify方法通知用户生成标题栏消息通知
        mNotificationManager.notify(1, notification);
    }

}