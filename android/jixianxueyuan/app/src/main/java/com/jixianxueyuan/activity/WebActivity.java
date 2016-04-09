package com.jixianxueyuan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jixianxueyuan.R;
import com.jixianxueyuan.util.StringUtils;
import com.jixianxueyuan.widget.AdvancedWebView;
import com.jixianxueyuan.widget.MyActionBar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dmax.dialog.SpotsDialog;

/**
 * Created by pengchao on 9/5/15.
 */
public class WebActivity extends BaseActivity implements AdvancedWebView.Listener {

    public static final String INTENT_TITLE = "INTENT_TITLE";
    public static final String INTENT_URL = "INTENT_URL";

    @InjectView(R.id.web_actionbar)
    MyActionBar actionBar;
    @InjectView(R.id.web_view)AdvancedWebView webView;

    private AlertDialog progressDialog;

    private String title;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);

        ButterKnife.inject(this);

        initParams();
        actionBar.setTitle(title);

        if(!StringUtils.isBlank(url)){
            webView.loadUrl(url);
            webView.setListener(this, this);
        }
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

    public static void startActivity(Context contex, String title, String url){
        Intent intent = new Intent(contex, WebActivity.class);
        intent.putExtra(INTENT_TITLE, title);
        intent.putExtra(INTENT_URL, url);
        contex.startActivity(intent);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        showLocationProgress();
    }

    @Override
    public void onPageFinished(String url) {
        hideLocationProgress();
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

    }

    @Override
    public void onExternalPageRequest(String url) {

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
