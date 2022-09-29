package com.github.lzyzsd.jsbridge;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.CookieManager;

public class DeLiBridgeWebView extends BridgeWebView {

    public DeLiBridgeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DeLiBridgeWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DeLiBridgeWebView(Context context) {
        super(context);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    @Override
    protected void init() {
        super.init();
//        EventManager.initJsBridgeEvent(this);

        // 用系统默认浏览器下载
//        setDownloadListener(new DownloadListener() {
//            @Override
//            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                Uri uri = Uri.parse(url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                getContext().startActivity(intent);
//            }
//        });
        CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        CookieManager.getInstance().setAcceptCookie(true);
        // 根据使用设置
//        WebSettings settings = getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setAllowUniversalAccessFromFileURLs(true);
//        settings.setAllowFileAccessFromFileURLs(true);
//        settings.setAppCacheEnabled(true);
//        settings.setDomStorageEnabled(true);
//        settings.setAllowFileAccess(true);
//        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        settings.setAllowContentAccess(true);
//        settings.setBlockNetworkLoads(false);
//        settings.setDatabaseEnabled(true);
//        settings.setGeolocationEnabled(true);
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        settings.setBlockNetworkImage(false);
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
//        settings.setSupportZoom(true);
////        settings.setBuiltInZoomControls(true);//fuck
//        settings.setDisplayZoomControls(true);
//        requestFocus();
//        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        // 设置UA

        // 要看是否有必要打通 app http请求 和 通过webview请求的cookie（代码在MaWebView中未集成）

    }
}
