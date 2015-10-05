package com.jixianxueyuan.commons;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jixianxueyuan.R;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.dto.AppVersion;
import com.jixianxueyuan.dto.MyResponse;
import com.jixianxueyuan.http.MyRequest;
import com.jixianxueyuan.server.ServerMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pengchao on 10/5/15.
 */
public class UpdateManager {


    private Context mContext = null;
    private final int CHECK_LAST_VERSION = 0X1;
    private final int CHECK_NOT_LAST_VERSION = 0x2;
    private final int CHECK_FAILED = 0x3;

    String mCheckUpdateVersion = null;
    String mCheckUpdateName = null;
    String mCheckUpdateURL = null;
    String mCheckModifyText = null;

    private AlertDialog mUpdateDialog;
    private ProgressDialog mProgressDialog;
    private int mProgressNum = 0;
    private boolean interceptFlag = false;

    private Thread downLoadThread = null;


    private boolean mIsShowLastVersionToast = true;

    private static final String savePath = "/sdcard/Download/";


    private final int HANDLER_REQUEST_CHECKUPDATE_SUCCESS = 0x1;
    private final int HANDLER_REQUEST_CHECKUPDATE_FAILED = 0x2;
    private static final int DOWN_UPDATE = 0x3;
    private static final int DOWN_OVER = 0x4;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case HANDLER_REQUEST_CHECKUPDATE_SUCCESS:
                    int checkResult = CompareVersion();
                    if(checkResult == CHECK_LAST_VERSION)
                    {
                        if(mIsShowLastVersionToast)
                        {
                            Toast.makeText(mContext, "当前为最新版本", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else if(checkResult == CHECK_NOT_LAST_VERSION)
                    {
                        showUpdateDialog();
                    }
                    break;

                case DOWN_UPDATE:
                    mProgressDialog.setProgress(mProgressNum);
                    break;
                case DOWN_OVER:
                    mProgressDialog.dismiss();
                    installApk();
                    break;
            }

        }

    };
    public UpdateManager(Context context)
    {
        mContext = context;
    }

    public void checkUpdateInfo(boolean isShowLastVersionToast)
    {
        mIsShowLastVersionToast = isShowLastVersionToast;
        requestUpdate();
    }

    private void requestUpdate(){
        String url = ServerMethod.check_version();
        MyRequest<AppVersion> myRequest = new MyRequest<AppVersion>(Request.Method.GET, url, AppVersion.class,
                new Response.Listener<MyResponse<AppVersion>>() {
                    @Override
                    public void onResponse(MyResponse<AppVersion> response) {
                        if(response.getStatus() == MyResponse.status_ok){
                            AppVersion appVersion = response.getContent();
                            if(appVersion != null){
                                mCheckUpdateVersion = appVersion.getApkVersion();
                                mCheckUpdateURL = appVersion.getApkUrl();
                                mCheckModifyText = appVersion.getUpdateLog();

                                handler.sendEmptyMessage(HANDLER_REQUEST_CHECKUPDATE_SUCCESS);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getContext().getRequestQueue().add(myRequest);
    }

    int CompareVersion()
    {
        if(mCheckUpdateVersion != null)
        {
            int currentVersionCode = 0;

            //获取当前版本号
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packInfo = null;
            try
            {
                packInfo = packageManager.getPackageInfo(mContext.getPackageName(),0);
            }
            catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            currentVersionCode = packInfo.versionCode;

            int serverVersionCode = Integer.parseInt(mCheckUpdateVersion);

            if(serverVersionCode == currentVersionCode)
            {
                return CHECK_LAST_VERSION;
            }
            else if(serverVersionCode > currentVersionCode)
            {
                return CHECK_NOT_LAST_VERSION;
            }
            else
            {
                return CHECK_FAILED;
            }
        }

        return -1;
    }

    void showUpdateDialog()
    {
        //弹出是否进行更新对话框

        //LinearLayout updateDialogLayout = (LinearLayout)(mContext.getLayoutInflater().inflate(R.layout.update_dialog, null));
        LayoutInflater inflater = LayoutInflater.from(mContext);
        LinearLayout updateDialogLayout = (LinearLayout) inflater.inflate(R.layout.update_dialog,null);

        TextView modifyText = (TextView)updateDialogLayout.findViewById(R.id.update_dialog_newtext);
        if(mCheckModifyText != null)
        {
            modifyText.setText(mCheckModifyText);
        }

        Button Updatebutton = (Button)updateDialogLayout.findViewById(R.id.update_dialog_ok);
        Updatebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mUpdateDialog.cancel();
                showDownloadDialog();

            }

        });

        Button cancelButton = (Button)updateDialogLayout.findViewById(R.id.update_dialog_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mUpdateDialog.cancel();
            }

        });

        mUpdateDialog = new AlertDialog.Builder(mContext)
                .setTitle("有新的版本")
                .setView(updateDialogLayout)
                .create();
        mUpdateDialog.show();
    }

    private void showDownloadDialog(){
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setTitle(R.string.wait);
        mProgressDialog.setMessage("更新完毕后请重新打开应用");
        //mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
		/*		mProgressDialog.setButton2("取消", new OnClickListener()
		{

		});*/
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.show();

        downloadApk();
    }



    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(mCheckUpdateURL);

                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = savePath + mCheckUpdateName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    mProgressNum = (int) (((float) count / length) * 100);
                    // 更新进度
                    handler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        handler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }
    };


    private void downloadApk()
    {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();

    }

    private void installApk() {
        File apkfile = new File(savePath + mCheckUpdateName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        mContext.startActivity(i);

    }

}
