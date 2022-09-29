package com.github.lzyzsd.jsbridge;

// 直接用callHandler就好了，这个方法多此一举啊
@Deprecated
public interface WebViewJavascriptBridge {

    void sendToWeb(Object data);

    void sendToWeb(Object data, OnBridgeCallback responseCallback);

    void sendToWeb(String function, Object... values);

}
