package com.jixianxueyuan.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.util.filedownload.FileDownloader;
import com.jixianxueyuan.util.filedownload.ProgressUpdateListener;


import java.io.File;
import java.net.URI;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 5/26/15.
 * 1.下载video文件
 * 2.通过videoview打开
 */
public class ShortVIdeoDetail extends Activity {

    public static final String tag = ShortVIdeoDetail.class.getSimpleName();

    @InjectView(R.id.videoview)VideoView videoView;

    MediaController mediaco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.short_video_detail_activity);

        ButterKnife.inject(this);
        mediaco=new MediaController(this);


/*        Uri uri = Uri.parse("http://7u2nc3.com1.z0.glb.clouddn.com/short_videofm11QHWk09-1CaKh6JpN-A__.mp4");
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaco);
        videoView.requestFocus();*/

        downloadVideoFile();
    }


    private void downloadVideoFile() {
        String url = "http://7u2nc3.com1.z0.glb.clouddn.com/short_videofm11QHWk09-1CaKh6JpN-A__.mp4";
        File save_dir  = new File("/sd/com.yumfee.skate");
        FileDownloader fd = new FileDownloader(this, url, save_dir, 1);

        Bundle b = new Bundle();
        b.putString("param_name1", "param_value1");
        fd.set_notification(ShortVIdeoDetail.class, b);


        try {
            fd.download(new ProgressUpdateListener(){

                @Override
                public void on_update(int downloaded_size) {

                    MyLog.d(tag, "file size = " + downloaded_size);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
