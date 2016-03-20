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
