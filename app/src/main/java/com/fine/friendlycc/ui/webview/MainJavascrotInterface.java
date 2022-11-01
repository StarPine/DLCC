package com.fine.friendlycc.ui.webview;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.OnBridgeCallback;

import java.util.Map;

/**
 * Created on 2019/7/10.
 * Author: bigwang
 * Description:
 */
public class MainJavascrotInterface extends BridgeWebView.BaseJavascriptInterface {

    public MainJavascrotInterface(Map<String, OnBridgeCallback> callbacks, BridgeWebView webView) {
        super(callbacks, webView);
    }

    @Override
    public String send(String data) {
        return "it is default response";
    }


    @JavascriptInterface
    public String submitFromWeb(String data, String callbackId) {
        Log.d("chromium data", data + ", callbackId: " + callbackId + " " + Thread.currentThread().getName());
        // 不需要这样，直接返回数据就可以了。注意，看看能否不用callbackId！，直接干掉
//        mWebView.sendResponse("submitFromWeb response", callbackId);
        // kl add the return below
        return "submitFromWeb return data";
    }

    /**
     * 凡是支持异步的，都需要最后一个参数是String callbackId
     *
     * @param data
     * @param callbackId
     */
    @JavascriptInterface
    public void submitFromWebAsync(String data, String callbackId) {
        Log.d("chromium data", data + ", callbackId: " + callbackId + " " + Thread.currentThread().getName());
        // 不需要这样，直接返回数据就可以了。注意，看看能否不用callbackId！，直接干掉
        this.mWebView.postDelayed(() -> {
            mWebView.sendResponse("submitFromWeb response 2s delay", callbackId);
        }, 2000);
    }
}
