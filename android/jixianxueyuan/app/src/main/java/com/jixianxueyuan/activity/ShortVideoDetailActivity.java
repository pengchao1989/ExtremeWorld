package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.VideoView;

import com.jakewharton.disklrucache.DiskLruCache;
import com.jixianxueyuan.R;
import com.jixianxueyuan.server.StaticResourceConfig;
import com.jixianxueyuan.util.DiskCachePath;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.Util;
import com.jixianxueyuan.widget.RoundProgressBarWidthNumber;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 5/26/15.
 * 1.下载video文件
 * 2.通过videoview打开
 */
public class ShortVideoDetailActivity extends Activity {

    public static final String tag = ShortVideoDetailActivity.class.getSimpleName();

    @InjectView(R.id.videoview)VideoView videoView;
    @InjectView(R.id.short_video_detail_progress)
    RoundProgressBarWidthNumber roundProgressBarWidthNumber;

    //MediaController mediaco;

    DiskLruCache mDiskLruCache;

    String mediaPath;

    boolean interceptFlag = false;
    int mProgressNum = 0;

    final int HADLER_DOWNLOAD_VIDEO_SUCCESS = 0x1;
    final int HADLER_DOWNLOAD_VIDEO_FAILED = 0x2;
    final int HANDLER_DOWNLOAD_UPDATE = 0x3;
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HADLER_DOWNLOAD_VIDEO_SUCCESS:

                    roundProgressBarWidthNumber.setVisibility(View.GONE);

                    String url = mediaPath/*"http://7u2nc3.com1.z0.glb.clouddn.com/short_videofm11QHWk09-1CaKh6JpN-A__.mp4"*/;

                    String key = Util.stringToMD5(url);

                    playVideo(DiskCachePath.getDiskCacheDir(ShortVideoDetailActivity.this, "short_video").getPath() + "/"+ key + ".0" );
                    break;
                case HANDLER_DOWNLOAD_UPDATE:
                    roundProgressBarWidthNumber.setProgress(mProgressNum);
                    break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.short_video_detail_activity);

        ButterKnife.inject(this);

        Intent intent = this.getIntent();
        if(intent.hasExtra("mediaPath"))
        {
            mediaPath = StaticResourceConfig.IMG_DOMAIN + intent.getStringExtra("mediaPath");
        }

        openDiskLruCache();


        String url = mediaPath;

        String key = Util.stringToMD5(url);


        try {
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);

            if(snapShot != null)
            {
                playVideo(DiskCachePath.getDiskCacheDir(this, "short_video").getPath() +"/" + key + ".0" );
            }
            else
            {
                roundProgressBarWidthNumber.setVisibility(View.VISIBLE);
                downloadVideoFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void playVideo(String path)
    {
        MyLog.d(tag, "videoPath=" + path);

        videoView.setVideoPath(path);
        videoView.requestFocus();
        videoView.start();
    }


    private void downloadVideoFile() {
        String url = mediaPath;

        Thread thread = new Thread(new DownloadRunnable(url));
        thread.start();

    }

    private void openDiskLruCache()
    {
        try {
            File cacheDir = DiskCachePath.getDiskCacheDir(this, "short_video");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, Util.getAppVersion(this), 1, 20 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class DownloadRunnable implements Runnable
    {
        private String url;

        public DownloadRunnable(String url)
        {
            this.url = url;
        }

        @Override
        public void run() {
            try {
                String key = Util.stringToMD5(this.url);
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                if (editor != null)
                {
                    OutputStream outputStream = editor.newOutputStream(0);
                    if (downloadUrlToStream(this.url, outputStream))
                    {
                        editor.commit();


                    } else
                    {
                        editor.abort();
                    }
                }
                mDiskLruCache.flush();

                handler.sendEmptyMessage(HADLER_DOWNLOAD_VIDEO_SUCCESS);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;

        InputStream is = null;

        try
        {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();

            is = urlConnection.getInputStream();

            int length = urlConnection.getContentLength();
            int count = 0, oldProgressNum = 0;
            byte buf[] = new byte[1024];
            do
            {
                int numread = is.read(buf);
                count += numread;
                mProgressNum = (int) (((float) count / length) * 100);
                if(mProgressNum  > oldProgressNum)
                {
                    oldProgressNum = mProgressNum;
                    MyLog.d(ShortVideoDetailActivity.tag, "progress=" + mProgressNum);
                    handler.sendEmptyMessage(HANDLER_DOWNLOAD_UPDATE);
                }

                if (numread <= 0) {
                    break;
                }
                outputStream.write(buf,0, numread);

            }while (!interceptFlag);

            return true;
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {

                if (is != null) {
                    is.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }




}
