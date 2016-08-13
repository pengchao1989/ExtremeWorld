package com.jixianxueyuan.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;

import com.jixianxueyuan.R;
import com.jixianxueyuan.util.StringUtils;
import com.jixianxueyuan.widget.MyActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by pengchao on 9/5/15.
 */
public class WebActivity extends BaseActivity {

    public static final String INTENT_TITLE = "INTENT_TITLE";
    public static final String INTENT_URL = "INTENT_URL";

    @BindView(R.id.web_actionbar)
    MyActionBar actionBar;
    @BindView(R.id.web_view)WebView webView;

    private AlertDialog progressDialog;

    private String title;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);

        ButterKnife.bind(this);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        initWebViewSettng();

        initParams();
        actionBar.setTitle(title);

        if(!StringUtils.isBlank(url)){
            webView.loadUrl(url);
        }

        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    private void initParams(){
        title = getIntent().getStringExtra(INTENT_TITLE);
        url = getIntent().getStringExtra(INTENT_URL);
    }

    private void initWebViewSettng(){
        WebSettings webSetting = webView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
    }

    public static void startActivity(Context contex, String title, String url){
        Intent intent = new Intent(contex, WebActivity.class);
        intent.putExtra(INTENT_TITLE, title);
        intent.putExtra(INTENT_URL, url);
        contex.startActivity(intent);
    }

    private void showLocationProgress(){
        progressDialog = new SpotsDialog(this,R.style.ProgressDialogWait);
        progressDialog.setTitle(getString(R.string.wait_location));
        progressDialog.show();
    }

    private void hideLocationProgress(){
        progressDialog.dismiss();
    }
}
