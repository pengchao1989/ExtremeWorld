package com.jixianxueyuan.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.adapter.OfflineVideoListAdapter;
import com.jixianxueyuan.commons.downloader.TopicDownloaderManager;
import com.jixianxueyuan.fragment.MineFragment;
import com.jixianxueyuan.util.MyLog;
import com.jixianxueyuan.widget.MyActionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by pengchao on 17-3-25.
 */

public class OfflineVideoListActivity extends BaseActivity {

    private static final String tag = OfflineVideoListActivity.class.getSimpleName();


    @BindView(R.id.my_actionbar)MyActionBar myActionBar;
    @BindView(R.id.offline_video_list_view)ListView listView;

    private OfflineVideoListAdapter offlineVideoListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offlie_video_list_activity);
        ButterKnife.bind(this);

        List<TopicDownloaderManager.VideoCacheFile> videoCacheFileList = TopicDownloaderManager.getInstance().getCacheVideoFileList(this);
        for (TopicDownloaderManager.VideoCacheFile  videoCacheFile : videoCacheFileList){
            MyLog.d(tag, "fileName=" + videoCacheFile.getFileName());
            MyLog.d(tag, "filePath=" + videoCacheFile.getFilePath());
            MyLog.d(tag, "absFilePath=" + videoCacheFile.getAbsFilePath());
        }

        offlineVideoListAdapter = new OfflineVideoListAdapter(this, videoCacheFileList);
        listView.setAdapter(offlineVideoListAdapter);

        myActionBar.setActionOnClickListener(new MyActionBar.MyActionBarListener() {
            @Override
            public void onFirstActionClicked() {

                final MaterialDialog mMaterialDialog = new MaterialDialog(OfflineVideoListActivity.this);
                mMaterialDialog.setTitle("提示");
                mMaterialDialog.setMessage("文件保存在根目录的Download/skateboardGroup中,用自取");
                mMaterialDialog.setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
                mMaterialDialog.show();
            }

            @Override
            public void onSecondActionClicked() {

            }
        });
    }

    @OnItemClick(R.id.offline_video_list_view)void onItemClicked(int position){
        TopicDownloaderManager.VideoCacheFile videoCacheFile = offlineVideoListAdapter.getItem(position);
        if (videoCacheFile != null){

            Intent intent = new Intent(Intent.ACTION_VIEW);

            String type = "video/mp4";
            Uri uri = Uri.parse("file://" + videoCacheFile.getAbsFilePath());
            intent.setDataAndType(uri, type);
            startActivity(intent);

        }
    }
}
