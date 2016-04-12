package com.jixianxueyuan.commons.im;

import android.content.Context;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.util.ACache;
import com.jixianxueyuan.util.PreferencesUtils;
import com.renn.rennsdk.param.PutBlogParam;

/**
 * Created by pengchao on 4/12/16.
 */
public class IMManager  {

    private static final String LAST_MESSAGE_TIME = "LAST_MESSAGE_TIME";

    private volatile static IMManager instance;

    public interface LoginResultListener{
        public void onSuccess();
        public void onError(String err);
    }


    private static final String APP_KEY = "23213193";

    private YWIMKit ywimKit;

    private IMManager(){
        Mine mine = MyApplication.getContext().getMine();
        String userid = mine.getUserInfo().getLoginName();
        ywimKit = YWAPI.getIMKitInstance(userid, APP_KEY);
    }

    public static IMManager getInstance(){
        if (instance == null){
            synchronized (IMManager.class) {
                if (instance == null){
                    instance = new IMManager();
                }
            }
        }
        return instance;
    }

    public void Login(final LoginResultListener listener){

        Mine mine = MyApplication.getContext().getMine();
        String userid = mine.getUserInfo().getLoginName();
        String password = mine.getUserInfo().getLoginName();
        IYWLoginService loginService = ywimKit.getLoginService();
        YWLoginParam loginParam = YWLoginParam.createLoginParam(userid, password);
        loginService.login(loginParam, new IWxCallback() {

            @Override
            public void onSuccess(Object... arg0) {
                if(listener != null){
                    listener.onSuccess();
                }
            }

            @Override
            public void onProgress(int arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(int errCode, String description) {
                //如果登录失败，errCode为错误码,description是错误的具体描述信息
                if(listener != null){
                    listener.onError(description);
                }
            }
        });
    }

    public YWIMKit getYwimKit(){
        return ywimKit;
    }

    public boolean isReceiverNewMessage(Context context){
        if (ywimKit != null){

            long cacheLastTime = PreferencesUtils.getLong(context, LAST_MESSAGE_TIME);
            if (ywimKit.getIMCore().getConversationService().getConversationList().size() > 0){
                long lastTime = ywimKit.getIMCore().getConversationService().getConversationList().get(0).getLatestTimeInMillisecond();
                PreferencesUtils.putLong(context, LAST_MESSAGE_TIME, lastTime);

                if (cacheLastTime > 0 && (lastTime > cacheLastTime)){
                    return true;
                }
            }
        }
        return false;
    }
}
