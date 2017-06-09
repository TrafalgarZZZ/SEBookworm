package com.fxdsse.SEhomework;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {
    public static final String URL = "url_str";
    WebView webView;
    private String url_str;
    private String title;
    private boolean is_loading = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getWindow().setStatusBarColor(ContextCompat.getColor(WebViewActivity.this, R.color.colorPrimaryDark));

        webView = (WebView) findViewById(R.id.wv_webview);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.wv_toolbar);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                is_loading = true;
                if (getSupportActionBar() != null) {
                    String t = url.substring(url_str.indexOf("://") + 3);
                    t = t.substring(0, t.indexOf("/"));
                    getSupportActionBar().setSubtitle(t);
                    getSupportActionBar().setTitle("正在加载......");
                    url_str = url;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                is_loading = false;
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle(title);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title_received) {
                title = title_received;
            }
        });
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        Bundle bundle = getIntent().getExtras();
        url_str = bundle.getString(URL);

        if (url_str != null) {
            String t = url_str.substring(url_str.indexOf("://") + 3);
            t = t.substring(0, t.indexOf("/"));
            toolbar.setSubtitle(t);
            toolbar.setTitle("正在加载......");
            toolbar.setElevation(0);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            webView.loadUrl(url_str);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_wv_openInBrowser:
                Intent intent_a = new Intent(Intent.ACTION_VIEW);
                intent_a.setData(Uri.parse(url_str));
                startActivity(intent_a);
                break;
            case R.id.menu_wv_shareURL:
                if (is_loading) {
                    Snackbar.make(webView, "正在等待网页加载完成", Snackbar.LENGTH_SHORT).show();
                    return true;
                }
                Intent intent_b = new Intent(Intent.ACTION_SEND);
                intent_b.setType("text/plain");
                String title0 = "";
                if (getSupportActionBar() != null && getSupportActionBar().getTitle() != null)
                    title0 = getSupportActionBar().getTitle().toString();
                intent_b.putExtra(Intent.EXTRA_TEXT, title0 + "\r\n" + url_str + "\r\n\r\n分享自\uD83D\uDE00书虫");
                startActivity(Intent.createChooser(intent_b, "分享此文章到..."));
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
