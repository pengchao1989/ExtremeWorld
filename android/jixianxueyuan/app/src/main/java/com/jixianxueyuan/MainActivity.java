package com.jixianxueyuan;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.jixianxueyuan.activity.CreateShortVideoActivity;
import com.jixianxueyuan.activity.HomeActivity;
import com.jixianxueyuan.record.ui.record.MediaRecorderActivity;
import com.jixianxueyuan.util.MyLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends Activity {

    @InjectView(R.id.activity_qq_login)
    Button qqLoginButton;

    @InjectView(R.id.activity_main_danmu)
    Button danmuButton;

    @InjectView(R.id.activity_main_appname)
    ShimmerTextView appNameTextView;

    Tencent mTencent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        final Shimmer shimmer = new Shimmer();
        shimmer.setRepeatCount(0);
        shimmer.start(appNameTextView);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

    }

    @OnClick(R.id.activity_qq_login)void qqLogin()
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

/*        mTencent = Tencent.createInstance("101220015", this.getApplicationContext());
        mTencent.setOpenId("");
        if (!mTencent.isSessionValid())
        {
            mTencent.login(this, "get_user_info,add_t", new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    MyLog.d("MainActivity", "login info =" + o.toString());
                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            });
        }*/
    }


    @OnClick(R.id.activity_main_danmu) void danmu()
    {
        /*Intent intent = new Intent(this, VideoDetailActivity.class);*/
        Intent intent = new Intent(this, CreateShortVideoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.activity_main_record) void record()
    {
        Intent intent = new Intent(this, MediaRecorderActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResult(requestCode, resultCode, data);
    }


}
