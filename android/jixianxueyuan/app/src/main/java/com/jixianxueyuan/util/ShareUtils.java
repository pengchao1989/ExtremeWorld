package com.jixianxueyuan.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.MenuItem;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.jixianxueyuan.R;

import java.io.File;
import java.util.List;

/**
 * Created by pengchao on 12/18/15.
 */
public class ShareUtils {

    public static void showShareMenuSheet(final Context context, final BottomSheetLayout bottomSheetLayout,
                                          final String title, final String content, final String imagePaht,
                                          final MenuSheetView.MenuType menuType) {
        MenuSheetView menuSheetView =
                new MenuSheetView(context, menuType, "分享给好友...", new MenuSheetView.OnMenuItemClickListener() {
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
                            ShareUtils.share(context,title, content, imagePaht, shareItem);
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

    public static void share(Context context, String msgTitle,String msgText, String imgPath, ShareItem share){
        shareMsg(context, msgTitle, msgText, imgPath, share);
    }


    private static  void shareMsg(Context context, String msgTitle, String msgText,
                          String imgPath, ShareItem share) {
        if (!share.packageName.isEmpty() && !isAvilible(context, share.packageName)) {
            Toast.makeText(context, "请先-安装" + share.title, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent("android.intent.action.SEND");
        if ((imgPath == null) || (imgPath.equals(""))) {
            intent.setType("text/plain");
        } else {
            File f = new File(imgPath);
            if ((f != null) && (f.exists()) && (f.isFile())) {
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
            }
        }

        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(!share.packageName.isEmpty()) {
            intent.setComponent(new ComponentName(share.packageName,share.activityName));
            context.startActivity(intent);
        }
        else {
            context.startActivity(Intent.createChooser(intent, msgTitle));
        }
    }

    public static  boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (((PackageInfo) pinfo.get(i)).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    public static class ShareItem{
        String title;
        int logo;
        String activityName;
        String packageName;

        public ShareItem(String title, int logo, String activityName, String packageName) {
            this.title = title;
            this.logo = logo;
            this.activityName = activityName;
            this.packageName = packageName;
        }

    }
}
