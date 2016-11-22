package com.jixianxueyuan.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.google.gson.Gson;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.RemindListActivity;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.dto.PointDTO;
import com.jixianxueyuan.dto.RemindDTO;
import com.jixianxueyuan.push.PushMessageType;
import com.jixianxueyuan.util.MyLog;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by pengchao on 8/6/15.
 */
public class CustomReceiver  extends MessageReceiver {
    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiver";

    /**
     * 推送通知的回调方法
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        // TODO 处理推送通知
        if ( null != extraMap ) {
            MyLog.d(REC_TAG, "onNotification msg=" + title);

        } else {
            MyLog.d(REC_TAG,"@收到通知 && 自定义消息为空");
        }
        MyLog.d(REC_TAG,"收到一条推送通知 ： " + title );
    }

    /**
     * 推送消息的回调方法
     *
     * @param context
     */
    @Override
    public void onMessage(Context context, CPushMessage message) {
        try {
            MyLog.d(REC_TAG, "onMessage msg=" + message.getTitle());
            MyLog.d(REC_TAG, "onMessage content=" + message.getContent());

            String pushContent = message.getContent();
            if (!TextUtils.isEmpty(pushContent)){
/*                Gson gson = new Gson();
                PushMessage pushMessage = gson.fromJson(pushContent,PushMessage.class);*/

                JSONObject jsonObject = new JSONObject(pushContent);
                int type = jsonObject.getInt("type");
                String content = jsonObject.getString("content");

                switch (type){
                    case PushMessageType.REMIND:
                        notifyRemind(context,content);
                        break;
                    case PushMessageType.POINT:
                        notifyPoint(context,content);
                        break;
                }
            }
        } catch (Exception e) {
            MyLog.d(REC_TAG, e.toString());
        }
    }

    /**
     * 从通知栏打开通知的扩展处理
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {

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

    private void notifyPoint(Context context,String pointJson){
        Gson gson = new Gson();
        PointDTO pointDTO = gson.fromJson(pointJson, PointDTO.class);
        TastyToast.makeText(MyApplication.getContext(), pointDTO.getDes(), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

    }

}