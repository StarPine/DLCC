package com.fine.friendlycc.ui.webview;

import android.Manifest;
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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.databinding.ActivityWebHomePlayccBinding;
import com.fine.friendlycc.bean.CallingInviteInfo;
import com.fine.friendlycc.calling.Utils;
import com.fine.friendlycc.ui.base.BaseFragment;
import com.fine.friendlycc.ui.mine.wallet.diamond.recharge.DialogDiamondRechargeActivity;
import com.fine.friendlycc.ui.userdetail.detail.UserDetailFragment;
import com.fine.friendlycc.utils.ChatUtils;
import com.fine.friendlycc.widget.action.StatusAction;
import com.fine.friendlycc.widget.action.StatusLayout;
import com.fine.friendlycc.widget.dialog.TraceDialog;
import com.google.gson.Gson;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.qcloud.tuicore.Status;

/**
 * Author: 彭石林
 * Time: 2022/7/29 17:25
 * Description: This is WebHomeActivity
 */
public class WebHomeFragment extends BaseFragment<ActivityWebHomePlayccBinding,WebHomeViewModel> implements StatusAction {

    BrowserView webView;
    String webUrl;

    private ProgressBar mProgressBar;
    private StatusLayout mStatusLayout;

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public WebHomeViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(WebHomeViewModel.class);
    }

    @Override
    public void initParam() {
        super.initParam();
        Bundle bundle = getArguments();
        if(bundle!=null){
            webUrl = bundle.getString("link", AppConfig.FukubukuroWebUrl);
        }

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.activity_web_home_playcc;
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
        binding.webView.addJavascriptInterface(new ShareJavaScriptInterface(getContext()), "Native");
        //视频自动播放
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        webView.setGson(new Gson());
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        //正在加载网页动画
        showLoading();
        //设置打开的页面地址
        //webView.loadUrl("file:///android_asset/index.html");
        webView.loadUrl(webUrl);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        //对方忙线
        viewModel.webUC.otherBusy.observe(this, o -> {
            TraceDialog.getInstance(getContext())
                    .chooseType(TraceDialog.TypeEnum.CENTER)
                    .setTitle(StringUtils.getString(R.string.playcc_other_busy_title))
                    .setContent(StringUtils.getString(R.string.playcc_other_busy_text))
                    .setConfirmText(StringUtils.getString(R.string.playcc_mine_trace_delike_confirm))
                    .setConfirmOnlick(new TraceDialog.ConfirmOnclick() {
                        @Override
                        public void confirm(Dialog dialog) {

                            dialog.dismiss();
                        }
                    }).TraceVipDialog().show();
        });
        viewModel.webUC.sendDialogViewEvent.observe(this, event -> {
            CCApplication.instance().logEvent(AppsFlyerEvent.Top_up);
            toRecharge();
        });
    }

    /**
     * 去充值
     */
    private void toRecharge() {
        Intent intent = new Intent(mActivity, DialogDiamondRechargeActivity.class);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.pop_enter_anim, 0);
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
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
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
        public void back() {
            pop();
        }
        @JavascriptInterface
        public void backView() {
            pop();
        }
        //获取用户token
        @JavascriptInterface
        public String getToken() {
            return viewModel.getToken();
        }

        //获取当前版本
        @JavascriptInterface
        public String getVersionTask() {
            return AppConfig.VERSION_NAME;
        }

        //获取本地用户信息
        @JavascriptInterface
        public String getUserData(){
            return viewModel.getUserData();
        }

        //获取当前AppId
        @JavascriptInterface
        public String getAppId(){
            return AppConfig.APPID;
        }

        //跳转用户主页
        @JavascriptInterface
        public void startUserMainView(String userId){
            if(StringUtils.isEmpty(userId)){
                return;
            }
            Bundle bundle = UserDetailFragment.getStartBundle(Integer.parseInt(userId));
            viewModel.start(UserDetailFragment.class.getCanonicalName(), bundle);
        }
        //拨打视讯
        @JavascriptInterface
        public void callingUserVideos(String IMUserId, String toIMUserId, int callingSource) {
            if (Status.mIsShowFloatWindow){
                ToastUtils.showShort(R.string.audio_in_call);
                return;
            }
            viewModel.getCallingInvitedInfo(2, IMUserId, toIMUserId, callingSource);
        }

        //效验当前是否拥有权限
        @JavascriptInterface
        public void checkPermissionsGroup(){
            binding.webView.post(() -> {
                boolean flagCheck = ActivityCompat.shouldShowRequestPermissionRationale(_mActivity, Manifest.permission.RECORD_AUDIO);
                if(ActivityCompat.shouldShowRequestPermissionRationale(_mActivity,Manifest.permission.CAMERA)){
                    flagCheck = true;
                }
                if(flagCheck){
                    webView.loadUrl("javascript:checkPermissionsSuccess()");
                }else {
                    //权限获取失败
                    webView.loadUrl("javascript:checkPermissionsFial()");
                }
            });
        }

        //获取权限
        @SuppressLint("CheckResult")
        @JavascriptInterface
        public void requestPermissions(){
            binding.webView.post(() -> {
                new RxPermissions(mActivity)
                        .request(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
                        .subscribe(granted -> {
                            if (granted) {
                                webView.loadUrl("javascript:checkPermissionsSuccess()");
                            } else {
                                webView.loadUrl("javascript:checkPermissionsFial()");
                            }
                        });
            });
        }
        //前端掉用拨打接口逻辑、客户端直接提供跳转页面方法
        @JavascriptInterface
        public void toCallVideoUserView(String toIMUserId,String dataInfo) {
            if (Status.mIsShowFloatWindow){
                ToastUtils.showShort(R.string.audio_in_call);
                return;
            }
            if(dataInfo!=null){
                CallingInviteInfo callingInviteInfo = GsonUtils.fromJson(dataInfo,CallingInviteInfo.class);
                if (callingInviteInfo != null) {
                    Utils.tryStartCallSomeone(2, toIMUserId, callingInviteInfo.getRoomId(), new Gson().toJson(callingInviteInfo));
                }
            }
        }

        @JavascriptInterface
        public void  toAndroidSettingView(){
            if(getContext()!=null){
                PermissionChecker.launchAppDetailsSettings(getContext());
            }
        }

        //进入聊天页面
        @JavascriptInterface
        public void startIMChatView(String toIMUserId,String userId,String userName){
            ChatUtils.chatUser(toIMUserId,Integer.parseInt(userId), userName, viewModel);
        }
        //返回国际化语言
        @JavascriptInterface
        public String getMultilingualFlag(){
            return mContext.getString(R.string.playcc_local_language);
        }

        //弹出砖石购买弹窗
        @JavascriptInterface
        public void alertCoinStoredEvent(){
            viewModel.webUC.sendDialogViewEvent.postValue(null);
        }

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
        }else{
            return false;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
        }
    }
}
