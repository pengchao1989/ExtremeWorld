package com.jixianxueyuan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.jixianxueyuan.R;
import com.jixianxueyuan.activity.CreateTopicActivity;
import com.jixianxueyuan.activity.ProfileEditActivity;
import com.jixianxueyuan.activity.RemindListActivity;
import com.jixianxueyuan.activity.SettingActivity;
import com.jixianxueyuan.activity.SponsorshipActivity;
import com.jixianxueyuan.app.Mine;
import com.jixianxueyuan.app.MyApplication;
import com.jixianxueyuan.config.ImageLoaderConfig;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.util.AndroidShare;
import com.jixianxueyuan.util.ShareUtils;
import com.jixianxueyuan.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by pengchao on 4/20/15.
 */
public class MineFragment extends Fragment {

    public static final String tag = MineFragment.class.getSimpleName();

    @InjectView(R.id.bottom_sheet)BottomSheetLayout bottomSheetLayout;
    @InjectView(R.id.mine_fragment_head_image_view)
    ImageView headImageView;
    @InjectView(R.id.mine_avatar_imageview)
    ImageView avatarImageView;
    @InjectView(R.id.mine_fragment_signature)
    TextView signatureTextView;

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

        ImageLoader.getInstance().displayImage(mine.getUserInfo().getAvatar(), avatarImageView, ImageLoaderConfig.getAvatarOption(this.getActivity()));
        ImageLoader.getInstance().displayImage(mine.getUserInfo().getBg(), headImageView, ImageLoaderConfig.getHeadOption(this.getActivity()));

        signatureTextView.setText(mine.getUserInfo().getSignature());

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

    @OnClick(R.id.mine_avatar_imageview)void onAvatarOnClick(){
        Intent intent = new Intent(this.getActivity(), ProfileEditActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.mine_fragment_sponsorship)void onSponsorship(){
        Intent intent = new Intent(this.getActivity(), SponsorshipActivity.class);
        startActivity(intent);
    }

    @OnLongClick(R.id.mine_fragment_head_image_view)boolean onHeadLongClick(){
        new SweetAlertDialog(this.getContext())
                .setTitleText("更新封面")
                .setContentText("YES！")
                .show();
        return true;
    }
    @OnClick(R.id.mine_fragment_share)void onShareClick(){
        String inviteMessage = mine.getUserInfo().getName()
                + "邀请你加入"
                + Util.getApplicationMetaString(MineFragment.this.getContext(),"HOBBY")
                + "http://dev.jixianxueyuan.com/skateboard/invite2"
                + "?inviteid="
                + mine.getUserInfo().getId();

        showMenuSheet(MenuSheetView.MenuType.GRID);

    }

    private void showMenuSheet(final MenuSheetView.MenuType menuType) {
        MenuSheetView menuSheetView =
                new MenuSheetView(MineFragment.this.getContext(), menuType, "邀请3个好友升级为永久会员...", new MenuSheetView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        ShareUtils.ShareItem shareItem = null;

                        switch (item.getItemId()){
                            case R.id.menu_share_wechat:
                                shareItem = new ShareUtils.ShareItem("微信", R.drawable.umeng_socialize_wechat,
                                        "com.tencent.mm.ui.tools.ShareImgUI", "com.tencent.mm");
                                break;
                            case R.id.menu_share_friend_group:
                                shareItem = new ShareUtils.ShareItem("朋友圈", R.drawable.umeng_socialize_wxcircle,
                                        "com.tencent.mm.ui.tools.ShareToTimeLineUI", "com.tencent.mm");
                                break;
                            case R.id.menu_share_qq:
                                shareItem = new ShareUtils.ShareItem("QQ", R.drawable.umeng_socialize_qq_on,
                                        "com.tencent.mobileqq.activity.JumpActivity","com.tencent.mobileqq");
                                break;
                            case R.id.menu_share_kongjian:
                                shareItem = new ShareUtils.ShareItem("空间", R.drawable.umeng_socialize_qzone_on,
                                        "com.qzone.ui.operation.QZonePublishMoodActivity","com.qzone");
                                break;
                            case R.id.menu_share_weibo:
                                shareItem = new ShareUtils.ShareItem("微博", R.drawable.umeng_socialize_sina_on,
                                        "com.sina.weibo.EditActivity", "com.sina.weibo");
                                break;
                        }


                        if(shareItem != null){
                            ShareUtils.share(MineFragment.this.getContext(),"title", "text", "", shareItem);
                        }




                        if (bottomSheetLayout.isSheetShowing()) {
                            bottomSheetLayout.dismissSheet();
                        }
                        return true;
                    }
                });
        menuSheetView.inflateMenu(R.menu.share);
        bottomSheetLayout.showWithSheetView(menuSheetView);
    }

}
