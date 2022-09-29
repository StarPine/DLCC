package com.github.lzyzsd.jsbridge.event;

import android.util.Log;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.OnBridgeCallback;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * app中只有一个JsEventManager
 * 用来触发JsEvent, 目的是打通web与native的基于事件的编程
 * webview中可以通过 deliApp.trigger(event) 的方法来触发事件，事件在java用用EventBus进行广播
 * webview中可以通过registerEventListener 订阅原生的事件， 通过unRegisterEventListener取消订阅
 * 而综合以上两点，两个webview可以通信！ 譬如webviewA 订阅事件 send2A， webviewB触发事件 deliApp.trigger('{"name":"send2A", "data":"hi"}'), webviewA就收到B的信息了
 * <p>
 * Created by linqiye on 9/29/16.
 */
public class EventManager {

    public static final String TAG = "EventManager";

    public static EventManager INSTANCE = new EventManager();

    private final BiMultimap<String, WeakReference<BridgeWebView>> mListenerCollection = new BiMultimap<>();
    // 不保存引用,HashMap 会保存key的引用...
    private final HashMap<Integer, WeakReference<BridgeWebView>> wv2RefMap = new HashMap<>();

    public static void trigger(EventBean bean) {
        if (bean.name != null && !bean.name.isEmpty()) {
            postEvent(bean);
            INSTANCE.triger4WebView(bean.name, bean.params);
        }
    }

    public static void trigger(String data) {
//        Log.i(TAG, "trigger: " + data);
        EventBean bean = null;
        if (data.contains("name")) {
            bean = new Gson().fromJson(data, EventBean.class);
        } else {
            bean = new EventBean();
            bean.name = data;
        }
//        EventBus.getDefault().post(bean);
        trigger(bean);
    }

    // TODO: 暂时只支持一个webview
    public static void postEvent(EventBean bean) {
//        Log.i("webview","postEvent " + bean.toString());
        switch (bean.name) {
            case "":
                break;
            case "onJsBridgeLoaded":
                EventBus.getDefault().post(new Event.OnJsBridgeLoadedEvent(bean.params));
                break;
            case "jsHandlerReady":
                EventBus.getDefault().post(new Event.OnJsHandlerReady(bean.params));
                break;
            case Event.OnBackPress.NAME:
                EventBus.getDefault().post(new Event.OnBackPress());
                break;
        }

    }

    public void addListener(String name, BridgeWebView webView) {
        WeakReference<BridgeWebView> ref = new WeakReference<>(webView);
        mListenerCollection.put(name, ref);
        wv2RefMap.put(webView.hashCode(), ref);
        // TODO: 1. 考虑到持有webview引用陈本极高， 2. 不适用weakReference，因为一旦destroy，业务上要立刻unListen。
        webView.listernOnDestory(this::removeWebviewAllListener);
    }

    public void removeWebviewAllListener(BridgeWebView webView) {
        WeakReference<BridgeWebView> ref = wv2RefMap.get(webView.hashCode());
        mListenerCollection.removeAllValue(ref);
    }

    public void removeListener(String name, BridgeWebView webView) {
        WeakReference<BridgeWebView> ref = wv2RefMap.get(webView.hashCode());
        mListenerCollection.remove(name, ref);
    }

    public void triger4WebView(String name, String param) {
        Set<WeakReference<BridgeWebView>> set = mListenerCollection.getValue(name);
        Iterator<WeakReference<BridgeWebView>> iterator = set.iterator();// 要边遍历边删除
        while (iterator.hasNext()) {
            WeakReference<BridgeWebView> ref = iterator.next();
            BridgeWebView webView = ref.get();
            if (null == webView) {
                iterator.remove();
            } else if (webView.getStatus() == BridgeWebView.STATUS.DESTROIED) {
                iterator.remove();
            } else {
                webView.callHandler(name, param, new OnBridgeCallback() {
                    @Override
                    public void onCallBack(String data) {
                        Log.i(TAG, "onCallBack: " + data);
                    }
                });
            }
        }
//        for (BridgeWebView webView : set){
////            Log.i("webview", "triger4WebView " + name);
//            webView.callHandler(name, param, new OnBridgeCallback() {
//                @Override
//                public void onCallBack(String data) {
//                    Log.i(TAG, "onCallBack: " + data);
//                }
//            });
//        }
    }

    public boolean isListeningEvent(String name, BridgeWebView webView) {
        Set<WeakReference<BridgeWebView>> set = mListenerCollection.getValue(name);
        if (set == null) {
            return false;
        }
        for (WeakReference<BridgeWebView> ref : set) {
            BridgeWebView bw = ref.get();
            if (bw != null && bw == webView) {
                return true;
            }
        }
        return false;
    }

}
