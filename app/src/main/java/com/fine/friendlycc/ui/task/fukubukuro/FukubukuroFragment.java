package com.fine.friendlycc.ui.task.fukubukuro;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.databinding.FragmentFukubukuroBinding;
import com.fine.friendlycc.bean.ExchangeIntegraBean;
import com.fine.friendlycc.bean.ExchangeIntegraOuterBean;
import com.fine.friendlycc.bean.GoodsBean;
import com.fine.friendlycc.event.TaskMainTabEvent;
import com.fine.friendlycc.ui.base.BaseFragment;
import com.fine.friendlycc.ui.task.webview.FukuokaViewFragment;
import com.fine.friendlycc.ui.webview.BrowserView;
import com.fine.friendlycc.utils.ApiUitl;
import com.fine.friendlycc.utils.ToastCenterUtils;
import com.fine.friendlycc.widget.action.StatusAction;
import com.fine.friendlycc.widget.action.StatusLayout;
import com.fine.friendlycc.widget.coinrechargesheet.CoinExchargeItegralDialog;
import com.fine.friendlycc.widget.dialog.TaskFukubukuroDialog;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Author: 彭石林
 * Time: 2021/9/4 16:51
 * Description: This is FukubukuroFragment
 */
public class FukubukuroFragment extends BaseFragment<FragmentFukubukuroBinding, FukubukuroViewModel> implements StatusAction {

    BrowserView webView;
    String webUrl;

    int RESULT_CODE = 0;
    ValueCallback<Uri> mUploadMessage;
    ValueCallback<Uri[]> mUploadMessageArray;

    private ProgressBar mProgressBar;
    private StatusLayout mStatusLayout;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return R.layout.fragment_fukubukuro;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        if(getArguments()!=null){
            webUrl = getArguments().getString("link", AppConfig.FukubukuroWebUrl);
        }
    }

    public static byte[] syncLoad(String url, String type) {
        Bitmap.CompressFormat imgtype = Bitmap.CompressFormat.JPEG;
        if (type.endsWith(".png")) {
            imgtype = Bitmap.CompressFormat.PNG;
        } else if (type.endsWith(".jpg") || type.endsWith(".jpeg")) {
            imgtype = Bitmap.CompressFormat.JPEG;
        }
        FutureTarget<Bitmap> target = Glide.with(CCApplication.instance())
                .asBitmap().dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).load(url).submit();
        try {
            Bitmap bitmap = target.get();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(imgtype, 100, baos);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initData() {
        super.initData();

        mStatusLayout = binding.hlBrowserHint;
        mProgressBar = binding.pbBrowserProgress;
        webView = binding.webView;
        // 设置 WebView 生命管控
        webView.setLifecycleOwner(this);
        WebSettings settings = binding.webView.getSettings();
        // 允许文件访问
        settings.setAllowFileAccess(true);
        //开启网页自适应
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //设置WebView属性，能够执行Javascript脚本
        settings.setJavaScriptEnabled(true);
        //这里需要设置为true，才能让Webivew支持<meta>标签的viewport属性
        settings.setUseWideViewPort(true);
        //提高网页渲染的优先级
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //支持屏幕缩放
        settings.setLoadWithOverviewMode(true);
        //使js可以调用安卓方法
        webView.setBrowserViewClient(new MyBrowserViewClient());
        // 加快网页加载完成的速度，等页面完成再加载图片
        settings.setLoadsImagesAutomatically(true);
        // 本地 DOM 存储（解决加载某些网页出现白板现象）
        settings.setDomStorageEnabled(true);
        // 设置 WebView 的缓存模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 支持启用缓存模式
        settings.setAppCacheEnabled(true);
        // 设置 AppCache 最大缓存值(现在官方已经不提倡使用，已废弃)
        settings.setAppCacheMaxSize(8 * 1024 * 1024);
        // Android 私有缓存存储，如果你不调用setAppCachePath方法，WebView将不会产生这个目录
        settings.setAppCachePath(CCApplication.instance().getCacheDir().getAbsolutePath());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 解决 Android 5.0 上 WebView 默认不允许加载 Http 与 Https 混合内容
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 不显示滚动条
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        //设置谷歌引擎
        webView.setBrowserChromeClient(new MyBrowserChromeClient(webView));
        binding.webView.addJavascriptInterface(new ShareJavaScriptInterface(mActivity), "Native");
        //视频自动播放
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        webView.setGson(new Gson());
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        //正在加载网页动画
        showLoading();
        //设置打开的页面地址
        webView.loadUrl(webUrl);

    }

    //重新加载页面
    public void reloadWebRul(String wenUrl) {
        //正在加载网页动画
        showLoading();
        //设置打开的页面地址
        webView.loadUrl(wenUrl);
    }

    @Override
    public FukubukuroViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(FukubukuroViewModel.class);
    }

    /**
     * @return boolean
     * @Desc TODO(是否对用户按下返回按键放行)
     * @author 彭石林
     * @parame []
     * @Date 2021/9/4
     */
    @Override
    public boolean onBackPressedSupport() {
        if (webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return false;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.DialogExchangeIntegral.observe(this, new Observer<ExchangeIntegraOuterBean>() {
            @Override
            public void onChanged(ExchangeIntegraOuterBean listData) {
                TaskFukubukuroDialog.exchangeIntegralDialog(FukubukuroFragment.this.getContext(),
                        true, String.valueOf(listData.getTotalBonus()),String.valueOf(listData.getTotalCoin()),0,
                        listData.getData(),
                        new TaskFukubukuroDialog.ExchangeIntegraleClick() {
                            @Override
                            public void clickSelectItem(Dialog dialog, ExchangeIntegraBean itemEntity) {
                                if(!ObjectUtils.isEmpty(itemEntity)){
                                    if(listData.getTotalCoin().intValue()>=itemEntity.getCoinValue().intValue()){
                                        dialog.dismiss();
                                        viewModel.ExchangeIntegraBuy(itemEntity.getId());
                                    }else{
                                        ToastCenterUtils.showToast(R.string.dialog_exchange_integral_total_text1);
                                        DialogCoinExchangeIntegralShow(dialog);
                                    }
                                }
                            }
                        }).show();
            }
        });
    }

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RESULT_CODE) {
            if (null == mUploadMessage && null == mUploadMessageArray) {
                return;
            }
            if (null != mUploadMessage && null == mUploadMessageArray) {
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }

            if (null == mUploadMessage && null != mUploadMessageArray) {
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                if (result != null) {
                    mUploadMessageArray.onReceiveValue(new Uri[]{result});
                }
                mUploadMessageArray = null;
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
        }
    }

    @Override
    public StatusLayout getStatusLayout() {
        return mStatusLayout;
    }

    /**
     * 重新加载当前页
     */
    private void reload() {
        webView.reload();
    }

    public void DialogCoinExchangeIntegralShow(Dialog dialog){
        CoinExchargeItegralDialog coinExchargeItegralSheetView = new CoinExchargeItegralDialog(FukubukuroFragment.this.getContext(),mActivity);
        coinExchargeItegralSheetView.setCoinRechargeSheetViewListener(new CoinExchargeItegralDialog.CoinExchargeIntegralAdapterListener() {
            @Override
            public void onPaySuccess(CoinExchargeItegralDialog sheetView, GoodsBean sel_goodsEntity) {
                coinExchargeItegralSheetView.dismiss();
                dialog.dismiss();
                ToastUtils.showShort(R.string.dialog_exchange_integral_success);
            }
            @Override
            public void onPayFailed(CoinExchargeItegralDialog sheetView, String msg) {
                coinExchargeItegralSheetView.dismiss();
                ToastUtils.showShort(msg);
                CCApplication.instance().logEvent(AppsFlyerEvent.Failed_to_top_up);
            }
        });
        coinExchargeItegralSheetView.show();
    }

    /**
     * {@link OnRefreshListener}
     */


    private class MyBrowserViewClient extends BrowserView.BrowserViewClient {

        /**
         * 网页加载错误时回调，这个方法会在 onPageFinished 之前调用
         */
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // 这里为什么要用延迟呢？因为加载出错之后会先调用 onReceivedError 再调用 onPageFinished
            post(() -> showError(v -> reload()));
        }

        /**
         * 开始加载网页
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        /**
         * 完成加载网页
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            mProgressBar.setVisibility(View.GONE);
            showComplete();
        }

        /**
        * @Desc TODO(拦截网页加载图片。安卓本地做缓存处理。提高网页加载图片速度；)
        * @author 彭石林
        * @parame [view, request]
        * @return android.webkit.WebResourceResponse
        * @Date 2021/9/22
        */
        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            String usrPath = request.getUrl().getPath();
            if (usrPath.endsWith(".png") || usrPath.endsWith(".jpg") || usrPath.endsWith(".jpeg")) {
                String mimeType = null;
                if (usrPath.endsWith(".png")) {
                    mimeType = "image/png";
                } else if (usrPath.endsWith(".jpg") || usrPath.endsWith(".jpeg")) {
                    mimeType = "image/jpeg";
                }
                String url = request.getUrl().toString();
                byte[] bytes = syncLoad(url, usrPath);
                if (bytes != null) {
                    return new WebResourceResponse(
                            mimeType, "UTF-8",
                            new ByteArrayInputStream(bytes));
                }
            }
            return null;
        }
    }

    private class MyBrowserChromeClient extends BrowserView.BrowserChromeClient {

        private MyBrowserChromeClient(BrowserView view) {
            super(view);
        }

        /**
         * 收到网页标题
         */
        @Override
        public void onReceivedTitle(WebView view, String title) {
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
        }

        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
            this.openFileChooser(uploadMsg);
        }

        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
            this.openFileChooser(uploadMsg);
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            pickFile();
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            mUploadMessageArray = filePathCallback;
            pickFile();
            return true;
        }

        /**
         * 收到加载进度变化
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mProgressBar.setProgress(newProgress);
        }
    }

    public class ShareJavaScriptInterface {
        Context mContext;

        ShareJavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void openWebView(String url){
            Bundle bundle = new Bundle();
            bundle.putString("link", url);
            viewModel.start(FukuokaViewFragment.class.getCanonicalName(), bundle);
        }

        @JavascriptInterface
        public void toTaskTop() {
            ApiUitl.taskTop = true;
            RxBus.getDefault().post(new TaskMainTabEvent(false));
            //pop();
        }

        @JavascriptInterface
        public String getMultilingualFlag() {
            return mActivity.getString(R.string.playcc_local_language);
        }

        @JavascriptInterface
        public void back() {
            ApiUitl.taskTop = true;
            RxBus.getDefault().post(new TaskMainTabEvent(false));
            //pop();
        }

        @JavascriptInterface
        public void toTaskuroAction() {
            //返回到兑换积分区
            ApiUitl.taskBottom = true;
            RxBus.getDefault().post(new TaskMainTabEvent(false));
            //pop();
        }

        @JavascriptInterface
        public void ToExchangeRecord(int idx) {
        }

        @JavascriptInterface
        public String getToken() {
            return viewModel.getToken();
        }

        @JavascriptInterface
        public void ToGoldDetail() {
        }

        @JavascriptInterface
        public void loadCoinExchangeSheet(){
            viewModel.getExchangeIntegraListData();
        }

        @JavascriptInterface
        public String getVersionTask() {
            return AppConfig.VERSION_NAME;
        }

    }
}