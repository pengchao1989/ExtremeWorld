package com.jixianxueyuan.commons.downloader;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.util.DiskCachePath;
import com.jixianxueyuan.util.Util;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pengchao on 17-3-25.
 */

public class TopicDownloaderManager {

    private static TopicDownloaderManager instance;

    public static TopicDownloaderManager getInstance() {
        if (instance == null){
            synchronized (TopicDownloaderManager.class){
                if (instance == null){
                    instance = new TopicDownloaderManager();
                }
            }
        }
        return instance;
    }



    private static final String FILE_DOWNLOAD_DIR = File.separator + "skateboardGroup" + File.separator;
    private Map<Long, String> mDownloadingTopicMap;

    private TopicDownloaderManager(){
        mDownloadingTopicMap = new HashMap<Long, String>();
    }


    public void downloadVideo(Context context, String title, String url){


        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(url) || context == null){
            return;
        }

        String fileName = new String(title);
        if (fileName.length() > 50){
            fileName = fileName.substring(0,49);
        }
        fileName = Util.getTodayPreStr() +  fileName;

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        //设置在什么网络情况下进行下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //设置通知栏标题
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle(title);
        request.setDescription("正在下载滑板圈视频");
        request.setAllowedOverRoaming(false);
        //设置文件存放目录
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,FILE_DOWNLOAD_DIR + fileName);

        DownloadManager downManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        long id= downManager.enqueue(request);

        mDownloadingTopicMap.put(id, title);

        TastyToast.makeText(MyApplication.getContext(), "开始下载:" + title, TastyToast.LENGTH_LONG, TastyToast.INFO);

    }

    public String getTitle(long downloadId){
        if (mDownloadingTopicMap != null){
            return mDownloadingTopicMap.get(downloadId);
        }
        return "";
    }


    public List<VideoCacheFile> getCacheVideoFileList(Context context){

        List<VideoCacheFile> videoCacheFileList = new ArrayList<VideoCacheFile>();
        File cacheDir = DiskCachePath.getExtDiskCacheDir(context,Environment.DIRECTORY_DOWNLOADS, FILE_DOWNLOAD_DIR);
        if (cacheDir != null){
            File[] videoFileArray = cacheDir.listFiles();
            if (videoFileArray != null && videoFileArray.length > 0){
                for (File videoFile : videoFileArray){
                    VideoCacheFile videoCacheFile = new VideoCacheFile();
                    videoCacheFile.setFileName(videoFile.getName());
                    videoCacheFile.setFilePath(videoFile.getPath());
                    videoCacheFile.setAbsFilePath(videoFile.getAbsolutePath());

                    videoCacheFileList.add(videoCacheFile);
                }
            }
        }

        return videoCacheFileList;
    }

    public static class VideoCacheFile {
        String fileName;
        String filePath;
        String absFilePath;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getAbsFilePath() {
            return absFilePath;
        }

        public void setAbsFilePath(String absFilePath) {
            this.absFilePath = absFilePath;
        }
    }

}
