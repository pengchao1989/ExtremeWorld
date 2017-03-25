package com.jixianxueyuan.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.commons.downloader.TopicDownloaderManager;
import com.sdsmdg.tastytoast.TastyToast;

/**
 * Created by pengchao on 17-3-26.
 */

public class DownLoadCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            String title = TopicDownloaderManager.getInstance().getTitle(id);

            TastyToast.makeText(MyApplication.getContext(), "下载完成:" + title, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

        }else if(intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)){
            TastyToast.makeText(MyApplication.getContext(), "别瞎点！！！", TastyToast.LENGTH_LONG, TastyToast.WARNING);

        }
    }
}
