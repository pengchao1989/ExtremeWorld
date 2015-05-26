package com.jixianxueyuan.util.filedownload;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;


public class DownloadService extends Service {
    Context context;
    public ArrayList<FileDownloader> download_store_list = new ArrayList<FileDownloader>();

    @Override
    public void onCreate() {
        Log.i("开始运行 download service ", "true");

        context = getApplicationContext();
    }

    public class LocalBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.i("服务开始启动了 ", "true");


        FileDownloader download_manager =
                intent.getParcelableExtra("download_manager");
        Log.i("传整个对象给 service, 测试输出 ", download_manager.get_test());

        Log.i("对象 has_code ", Integer.toString(download_manager.get_obj_id()));
        if (download_manager.should_destroy) {
            Log.i("调试 应该要停止 ", "true");
        }

        if (download_manager.should_pause) {
            Log.i("调试 应该要暂停 ", "true");
        }


        if (get_download_store(download_manager.get_obj_id()) == null) {
            FileTaskThread file_task_thread =
                    new FileTaskThread(intent, download_manager, download_manager.notice_id);

            Thread t = new Thread( file_task_thread );
            t.start();
        } else {
            save_download_manager(download_manager);
        }



        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.i("下载服务关闭 ", "true");

    }



    @Override
    public IBinder onBind(Intent intent) {

        return m_binder;
    }




    private final IBinder m_binder = new LocalBinder();



    private void clear_notice_bar(int notice_id) {
        NotificationServiceBar notification_service_bar =
                new NotificationServiceBar(getApplicationContext(),
                        DownloadService.this);

        notification_service_bar.stop_foreground(notice_id);
    }

    private void stop_service() {

        if (download_store_list.size() == 0) {
            Log.i("已经没有可以下载的了 服务停止 ", Integer.toString(download_store_list.size()));
            DownloadService.this.stopSelf();
            return;
        }

        Log.i("还有可以下载的 服务不能停止 ", Integer.toString(download_store_list.size()));

    }


    private void build_download_done_notification(FileDownloader file_downloader) {

        if (file_downloader.downloaded_size != file_downloader.file_size) {
            Log.i("下载大小跟总大小不相等 downloaded_size ", Integer.toString(file_downloader.downloaded_size));
            Log.i("下载大小跟总大小不相等 file_size ", Integer.toString(file_downloader.file_size));
            file_downloader.downloaded_size = file_downloader.file_size;
        }
        file_downloader.send_broadcast();

        Log.i("通知的文件大小 ", Integer.toString(file_downloader.file_size));
        Intent in = new Intent("app.action.download_done_notification");
        if (file_downloader.intent_extras != null) {
            in.putExtras(file_downloader.intent_extras);
        }

        if (file_downloader.activity_class != null) {
            in.putExtra("activity_class", file_downloader.activity_class.getName());
        }
        in.putExtra("download_manager", file_downloader);
        in.putExtra("store_file", file_downloader.save_file.toString());
        Log.i("store_file 值 ", file_downloader.save_file.toString());
        in.putExtra("filename", file_downloader.get_file_name());
        in.putExtra("file_size", show_human_size(file_downloader.file_size));
        getApplicationContext().sendBroadcast(in);
    }


    private void build_download_pause_receiver(FileDownloader file_downloader) {

        Intent in = new Intent("app.action.download_pause_receiver");
        in.putExtra("download_manager", file_downloader);
        getApplicationContext().sendBroadcast(in);
    }

    private void build_download_stop_receiver(FileDownloader file_downloader) {

        Intent in = new Intent("app.action.download_stop_receiver");
        in.putExtra("download_manager", file_downloader);
        getApplicationContext().sendBroadcast(in);
    }

//    private void clear_local_thread_data(FileDownloader file_downloader) {
//
//        if (file_downloader.download_url == null) {
//            Log.i("清理 cache 数据 download_url 为空　", "true");
//        }
//
//        if (file_downloader.file_record == null) {
//            Log.i("清理 cache 数据 file_record 为空　", "true");
//            return;
//        }
//
//
//        try {
//            file_downloader.file_record.delete(file_downloader.download_url);
//            Log.i("清理 cache 数据　", "true");
//        } catch (Exception e) {
//            Log.i("清理 cache 数据错误 ", e.toString());
//            e.printStackTrace();
//        }
//    }

//    private void delete_file(FileDownloader file_downloader) {
//        try {
//            file_downloader.save_file.delete();
//            Log.i("清理 save_file　", "true");
//        } catch (Exception e) {
//            Log.i("清理 save_file ", e.toString());
//            e.printStackTrace();
//        }
//    }




    public String show_human_size(long bytes) {
        Boolean si = true;
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }



    private class FileTaskThread implements Runnable {
        Intent intent;
        FileDownloader download_manager;
        int notice_id;

        public FileTaskThread(Intent intent,
                              FileDownloader download_manager,
                              int notice_id) {
            this.intent = intent;
            this.download_manager = download_manager;
            Log.i("notice_id值 ", Integer.toString(notice_id));
            this.notice_id = notice_id;

            if (download_manager.should_pause || download_manager.should_destroy) {
                // this.setPriority(MAX_PRIORITY);
                Log.i("设置成最大优先级 ", "true");
            }
        }

        @Override
        public void run() {
            final int obj_id = download_manager.get_obj_id();

            final NotificationServiceBar notification_service_bar =
                    new NotificationServiceBar(getApplicationContext(),
                            DownloadService.this);

//            FileDownloader current_fd = get_download_store(download_manager.get_obj_id());
//            if ( current_fd == null ) {
//                notification_service_bar.
//                        wait_notification(download_manager, notice_id);
//            }


            save_download_manager(download_manager);

            notification_service_bar.
                    update_notification(download_store_list, notice_id);


            try {

                download_manager.init_connection(context);

                download_manager.save_thread_data();


                download_manager.is_finished = false;
                while (!download_manager.is_finished) {


                    // 停止下载
                    if (get_download_store(obj_id).should_destroy) {
                        download_manager.should_destroy = true;
                        Log.i("should_destroy true", "true");
                        break;
                    } else {
                        Log.i("should_destroy false", "false");
                    }

                    // 暂停下载
                    if (get_download_store(obj_id).should_pause) {
                        download_manager.should_pause = true;
                        Log.i("should_pause为 true", "true");
                        break;
                    } else {
                        Log.i("should_pause为 false", "false");
                    }



                    download_manager.is_finished = true;
                    download_manager.continue_download_with_thread();

                    notification_service_bar.
                            handle_notification(download_manager, notice_id);


                    download_manager.send_broadcast();


                    Thread.sleep(900);


                }



                if (download_manager.should_destroy) {
                    Log.i("整个停止下载 ", "true");
                    build_download_stop_receiver(download_manager);
                    clear_notice_bar(notice_id);
                    download_manager.clear_local_thread_data();
                    download_manager.delete_file();
                    remove_download_store(download_manager);

                    stop_service();
                    return;
                }

                if (download_manager.should_pause) {
                    Log.i("线程暂停 ", "true");
                    remove_download_store(download_manager);
                    notification_service_bar.pause_notification(download_store_list,
                            download_manager, notice_id);
                    build_download_pause_receiver(download_manager);

                    return;
                }
                build_download_done_notification(download_manager);
                clear_notice_bar(notice_id);
                remove_download_store(download_manager);
                stop_service();

                return;

            } catch (Exception e) {
                Log.i("下载有错误 ", e.toString());
                e.printStackTrace();
            }

        }
    }



    public FileDownloader get_download_store(int obj_id) {
        if (download_store_list == null) {
            Log.i("download_store_list为 null ", "true");
            return null;
        }
        for (FileDownloader item : download_store_list) {
            if (item.get_obj_id() == obj_id) {
                Log.i("当前 obj_id  ", Integer.toString(item.get_obj_id()));
                Log.i("通过 obj_id 获得当前 file_size ", Integer.toString(item.file_size));
                return item;
            }
        }
        return null;
    }

    private void save_download_manager(FileDownloader fd) {
        int obj_id = fd.get_obj_id();

        FileDownloader download_store = get_download_store(obj_id);

        if (download_store == null) {
            Log.i("第一次保存 obj_id ", "true");
            // download_store = new FileDownloader();
            download_store_list.add(fd);
        } else {
            remove_download_store(download_store);
            download_store_list.add(fd);
        }

        if (fd.should_pause) {
            Log.i("存储暂停 ", "true");
        }

        // download_store.should_pause = fd.should_pause;

        if (fd.should_destroy) {
            Log.i("存储停止 ", "true");
        }
        // download_store.should_destroy = fd.should_destroy;
    }

    private void remove_download_store(FileDownloader fd) {
        FileDownloader download_store = get_download_store(fd.get_obj_id());
        if (download_store != null) {
            download_store_list.remove(download_store);
            Log.i("清除 fd ", "true");
        }

    }




}