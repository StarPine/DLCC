package com.github.lzyzsd.jsbridge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;

import com.github.lzyzsd.jsbridge.event.Event;
import com.github.lzyzsd.jsbridge.event.EventManager;
import com.github.lzyzsd.library.BuildConfig;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@SuppressLint("SetJavaScriptEnabled")
/**
 * 使用注意点：
 * 1. 需要前端主动调用deliApp.loadJsBridge() 加载jsBridge
 * 2. 必须在拥有此WebView的Activity或Fragment在onDestroy的时候调用destroy
 * 3. 保持WebView引用成本是很高的，WebView暂用内存，所以除了界面直接引用，其他地方建议使用 WeakReference
 */
public class BridgeWebView extends WebView implements WebViewJavascriptBridge, OnLoadJsBridgeListener {

    //	private final int URL_MAX_CHARACTER_NUM=2097152;
//	public static final String toLoadJs = "WebViewJavascriptBridge.js";
//	Map<String, OnBridgeCallback> responseCallbacks = new HashMap<String, OnBridgeCallback>();
//	Map<String, BridgeHandler> messageHandlers = new HashMap<String, BridgeHandler>();
//	BridgeHandler defaultHandler = new DefaultHandler();
    public final static String PACKAGE_NAME = "deliApp";// 只用这个，要和jsbridge中的同步的

    // native 调用js, js给native做返回用的
    private final Map<String, OnBridgeCallback> mCallbacks = new ArrayMap<>();
    private final Set<IRunnableWebview> onDestroyListeners = new TreeSet<>();

    //    private BridgeWebViewClient mClient;
    protected String pageToken = null; // 用来区分页面的（也可以认为是不同webview）
    protected Runnable mNativeBackPressRunnable;
    private List<Object> mMessages = new ArrayList<>();
    private long mUniqueId = 0;
    private boolean mJSLoaded = false;
    private Gson mGson;
    private STATUS status = STATUS.RUNNING;

    public BridgeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BridgeWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BridgeWebView(Context context) {
        super(context);
        init();
    }

    protected void init() {
        clearCache(true);
        getSettings().setUseWideViewPort(true);
        getSettings().setDomStorageEnabled(true);
//		webView.getSettings().setLoadWithOverviewMode(true);
//        getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        getSettings().setJavaScriptEnabled(true);
//        mContent.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        if (BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
//        mClient = new BridgeWebViewClient(this);
//        super.setWebViewClient(mClient);
        EventBus.getDefault().register(this);
    }

    public void setGson(Gson gson) {
        mGson = gson;
    }

    public STATUS getStatus() {
        return status;
    }

    /**
     * 监听是否destory
     *
     * @param runnableWebview
     */
    public void listernOnDestory(IRunnableWebview runnableWebview) {
        this.onDestroyListeners.add(runnableWebview);
    }

    /**
     * Activity的onBackPress处理闭包。参考DemoActivity
     * 如果Activity要交给WebView处理返回键事件，必须调用这个方法
     *
     * @param runnable
     */
    public void setBackpressRunnable(Runnable runnable) {
        this.mNativeBackPressRunnable = () -> {
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                runnable.run();
            } else {
                this.post(runnable);
            }
        };
    }

    public boolean isJSLoaded() {
        return mJSLoaded;
    }

    public Map<String, OnBridgeCallback> getCallbacks() {
        return mCallbacks;
    }

    @Subscribe
    public void onJsBridgeLoaded(Event.OnJsBridgeLoadedEvent event) {
//        Log.i("webview", Thread.getAllStackTraces().toString());
        Log.d("kl", "onJsBridgeLoaded: " + event.token);
        onLoadFinished();
    }

    @Subscribe
    public void jsHandlerReady(Event.OnJsHandlerReady event) {
        onJsHandlerReady();
    }

    public void onJsHandlerReady() {
        Log.d("kl", "onJsHandlerReady ");
    }

    // webview提供的pageStart有坑的，会存在多次调用的问题
    @Override
    public void onLoadStart(String token) {
        mJSLoaded = false;
        // 上一个页面的callback清空一下
        mCallbacks.clear();
        EventManager.INSTANCE.removeWebviewAllListener(this);
        Log.i("webview", "onLoadStart========================");
    }

//    @Override
//    public void setWebViewClient(WebViewClient client) {
//        mClient.setWebViewClient(client);
//    }

    // webView的onPageFinish也是有坑的，如果某些rescourse很久都没有返回，就会一直不调用
    // 但实际上dom已经渲染出来了
    @Override
    public void onLoadFinished() {
        mJSLoaded = true;

        if (mMessages != null) {
            for (Object message : mMessages) {
                dispatchMessage(message);
            }
            mMessages = null;
        }
    }

    @Override
    public void sendToWeb(Object data) {
        sendToWeb(data, null);
    }

    @Override
    public void sendToWeb(Object data, OnBridgeCallback responseCallback) {
        doSend(null, data, responseCallback);
    }

    @Override
    public void sendToWeb(String function, Object... values) {
        // 必须要找主线程才会将数据传递出去 --- 划重点
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            String jsCommand = String.format(function, values);
            jsCommand = String.format(BridgeUtil.JAVASCRIPT_STR, jsCommand);
            loadUrl(jsCommand);
        }
    }

    /**
     * call javascript registered handler
     * 调用javascript处理程序注册
     *
     * @param handlerName handlerName
     * @param data        data
     * @param callBack    OnBridgeCallback
     */
    public void callHandler(String handlerName, String data, OnBridgeCallback callBack) {
        // TODO: KL 直接用原生的evaluateJavascript 就可以了啊
        doSend(handlerName, data, callBack);
    }

    /**
     * 保存message到消息队列
     *
     * @param handlerName      handlerName
     * @param data             data
     * @param responseCallback OnBridgeCallback
     */
    private void doSend(String handlerName, Object data, OnBridgeCallback responseCallback) {
        if (!(data instanceof String) && mGson == null) {
            return;
        }
        JSRequest request = new JSRequest();
        if (data != null) {
            request.data = data instanceof String ? (String) data : mGson.toJson(data);
        }
        if (responseCallback != null) {
            String callbackId = String.format(BridgeUtil.CALLBACK_ID_FORMAT, (++mUniqueId) + (BridgeUtil.UNDERLINE_STR + SystemClock.currentThreadTimeMillis()));
            mCallbacks.put(callbackId, responseCallback);
            request.callbackId = callbackId;
        }
        if (!TextUtils.isEmpty(handlerName)) {
            request.handlerName = handlerName;
        }
        queueMessage(request);
    }

    /**
     * list<message> != null 添加到消息集合否则分发消息
     *
     * @param message Message
     */
    private void queueMessage(Object message) {
        if (mMessages != null) {// js还没初始化好，就先缓存一下
            mMessages.add(message);
        } else {
            dispatchMessage(message);
        }
    }

    /**
     * 分发message 必须在主线程才分发成功， 真正与js交互的
     *
     * @param message Message
     */
    private void dispatchMessage(Object message) {
        if (mGson == null) {
            return;
        }
        String messageJson = mGson.toJson(message);
        //escape special characters for json string  为json字符串转义特殊字符
        messageJson = messageJson.replaceAll("(\\\\)([^utrn])", "\\\\\\\\$1$2");
        messageJson = messageJson.replaceAll("(?<=[^\\\\])(\")", "\\\\\"");
        messageJson = messageJson.replaceAll("(?<=[^\\\\])(')", "\\\\'");
        messageJson = messageJson.replaceAll("%7B", URLEncoder.encode("%7B"));
        messageJson = messageJson.replaceAll("%7D", URLEncoder.encode("%7D"));
        messageJson = messageJson.replaceAll("%22", URLEncoder.encode("%22"));
        String javascriptCommand = String.format(BridgeUtil.JS_HANDLE_MESSAGE_FROM_JAVA, messageJson);
        // 必须要找主线程才会将数据传递出去 --- 划重点
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            this.evaluateJavascript(javascriptCommand, null);
        } else {
            this.post(new Runnable() {
                @Override
                public void run() {
                    evaluateJavascript(javascriptCommand, null);
                }
            });
        }
    }

    // 这个方法作用是js 调用原生handler，原生异步返回的，但现在原生可以同步返回。
    // 所以现在原生直接返回，js要做异步callback形式，交给前端jsbridge去实现。
//    @Deprecated
    public void sendResponse(Object data, String callbackId) {
        if (!(data instanceof String) && mGson == null) {
            return;
        }
        if (!TextUtils.isEmpty(callbackId)) {
            final JSResponse response = new JSResponse();
            response.responseId = callbackId;
            response.responseData = data instanceof String ? (String) data : mGson.toJson(data);
            dispatchMessage(response);

        }
    }

    // 不让子类继承
    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    @Override
    public void destroy() {
        // TODO: 注意一定要主动调用， 如果EventBus.getDefault().unregister(this);没有调用， EventBus有机会触发多次事件。。。。。
        this.status = STATUS.DESTROIED;
        for (IRunnableWebview runnableWebview : onDestroyListeners) {
            runnableWebview.run(this);
        }
        mCallbacks.clear();
        onDestroyListeners.clear();
//        Log.i("webview", "===================== unregister");
        EventBus.getDefault().unregister(this);
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void addJavascriptInterface(@NonNull Object object, @NonNull String name) {
        if (name.equals(PACKAGE_NAME) && !(object instanceof BaseJavascriptInterface)) {
            throw new RuntimeException("name is deliApp, object must be subclass of BridgeWebview.BaseJavascriptInterface");
        } else {
            super.addJavascriptInterface(object, name);
        }
    }

    @Subscribe
    public void onBackPress(Event.OnBackPress event) {
        // 无监听的情况
        if (mNativeBackPressRunnable != null && !EventManager.INSTANCE.isListeningEvent(Event.OnBackPress.NAME, this)) {
            mNativeBackPressRunnable.run();
        }

    }

//    private boolean ticking = false;

    public void resetBackPress(boolean consumed) {
        if (mNativeBackPressRunnable == null) return;
        if (!consumed) {
            mNativeBackPressRunnable.run();
        }
    }

    public enum STATUS {
        RUNNING,
        DESTROIED
    }

    // TODO: KL: 一定要继承这个类！！！！addJavascriptInterface 基于第二个传入参数，会覆盖之前的！！！！
    public static abstract class BaseJavascriptInterface {

        private final Map<String, OnBridgeCallback> mCallbacks;
        protected BridgeWebView mWebView;

        public BaseJavascriptInterface(Map<String, OnBridgeCallback> callbacks, BridgeWebView webView) {
            mCallbacks = callbacks;
            this.mWebView = webView;
        }

        @JavascriptInterface
        public String send(String data, String callbackId) {
            Log.d("chromium", data + ", callbackId: " + callbackId + " " + Thread.currentThread().getName());
            return send(data);
        }

        @JavascriptInterface
        public void response(String data, String responseId) {
            Log.d("chromium", data + ", responseId: " + responseId + " " + Thread.currentThread().getName());
            if (!TextUtils.isEmpty(responseId)) {
                OnBridgeCallback function = mCallbacks.remove(responseId);
                if (function != null) {
                    function.onCallBack(data);
                }
            }
        }

        @JavascriptInterface
        public void loadJsBridge() {
            mWebView.post(() -> {
                BridgeUtil.webViewLoadLocalJs(mWebView, BridgeUtil.JAVA_SCRIPT);
            });
        }

        @JavascriptInterface
        public void registerEventListener(String name) {
            EventManager.INSTANCE.addListener(name, mWebView);
        }

        @JavascriptInterface
        public void unRegisterEventListener(String name) {
            EventManager.INSTANCE.removeListener(name, mWebView);
        }

        @JavascriptInterface
        public void triggerEvent(String data) {
            EventManager.trigger(data);
        }

        @JavascriptInterface
        public void onLoadNewBridge(String token) {
            mWebView.onLoadStart(token);
        }

        @JavascriptInterface
        public void backPressConsume(boolean flag) {
            mWebView.resetBackPress(flag);
        }

        public abstract String send(String data);
    }

}
