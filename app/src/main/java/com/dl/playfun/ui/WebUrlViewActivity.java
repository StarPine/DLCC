package com.dl.playfun.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.dl.playfun.R;
import com.dl.playfun.widget.BasicToolbar;
import com.gyf.immersionbar.ImmersionBar;

/**
 * Author: 彭石林
 * Time: 2022/1/27 15:02
 * Description: 协议url
 */
public class WebUrlViewActivity extends Activity implements BasicToolbar.ToolbarListener{
    private WebView webView;
    private String webUrls = "";
    private void initParam(){
        Intent intent = getIntent();
        webUrls = intent.getStringExtra("arg_web_url");
    }

    BasicToolbar basicToolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_detail);
        initParam();
        View statusView = findViewById(R.id.status_bar_view);
        if (statusView != null) {
            ImmersionBar.setStatusBarView(this, statusView);
        }
        basicToolbar= findViewById(R.id.basic_toolbar);
        if(basicToolbar!=null){
            basicToolbar.setToolbarListener(this);
        }
        webView = findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //如果不设置WebViewClient，请求会跳转系统浏览器
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
//
//                if (url.toString().contains("sina.cn")){
//                    view.loadUrl("http://ask.csdn.net/questions/178242");
//                    return true;
//                }

                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    if (request.getUrl().toString().contains("sina.cn")){
//                        view.loadUrl("http://ask.csdn.net/questions/178242");
//                        return true;
//                    }
                }

                return false;
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }
        });
        webView.loadUrl(webUrls);
    }

    @Override
    public void onBackClick(BasicToolbar toolbar) {
        this.finish();
    }
}
