package com.jixianxueyuan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jixianxueyuan.R;
import com.jixianxueyuan.util.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pengchao on 9/5/15.
 */
public class WebActivity extends Activity {

    @InjectView(R.id.web_view)WebView webView;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);

        ButterKnife.inject(this);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        if(!StringUtils.isBlank(url)){
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(url);
                    return true;
                }
            });
        }
    }
}
