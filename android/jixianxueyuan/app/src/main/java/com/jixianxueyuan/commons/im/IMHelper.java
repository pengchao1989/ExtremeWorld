package com.jixianxueyuan.commons.im;

import android.content.Context;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;

/**
 * Created by pengchao on 2/10/16.
 */
public class IMHelper {
    private Context context;

    public interface LoginResultListener{
        public void onSuccess();
        public void onError(String err);
    }

    public IMHelper(Context context){
        this.context = context;
    }

    public void Login(final LoginResultListener listener){
        YWIMKit mIMKit = YWAPI.getIMKitInstance();

        Mine mine = MyApplication.getContext().getMine();
        String userid = mine.getUserInfo().getLoginName();
        String password = mine.getUserInfo().getLoginName();
        IYWLoginService loginService = mIMKit.getLoginService();
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


}
