package com.jixianxueyuan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.RemindListActivity;
import com.jixianxueyuan.activity.SettingActivity;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pengchao on 4/20/15.
 */
public class MineFragment extends Fragment {

    public static final String tag = MineFragment.class.getSimpleName();

    @InjectView(R.id.mine_avatar_imageview)
    ImageView avatarImageView;

    MyApplication application;

    Mine mine;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        application = (MyApplication) this.getActivity().getApplicationContext();
        mine = application.getMine();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.mine_fragment, container, false);

        ButterKnife.inject(this,view);

        ImageLoader.getInstance().displayImage(mine.getUserInfo().getAvatar(), avatarImageView);

        return view;

    }

    @Override
    public void onResume()
    {
        super.onResume();
    }


    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @OnClick(R.id.mine_fragment_remind_reply) void remindReplyOnClick(){
        Intent intent = new Intent(this.getActivity(), RemindListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.mine_fragment_setting)void onSettingClick(){
        Intent intent = new Intent(this.getActivity(), SettingActivity.class);
        startActivity(intent);
    }
}
