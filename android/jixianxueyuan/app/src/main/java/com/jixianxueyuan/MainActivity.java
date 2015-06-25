package com.jixianxueyuan;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.jixianxueyuan.activity.CreateShortVideoActivity;
import com.jixianxueyuan.activity.HomeActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

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
    }


    @OnClick(R.id.activity_main_danmu) void danmu()
    {
        /*Intent intent = new Intent(this, VideoDetailActivity.class);*/
        Intent intent = new Intent(this, CreateShortVideoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.activity_main_record) void record()
    {
    }
}
