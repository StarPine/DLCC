package com.fine.friendlycc.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aliyun.svideo.common.utils.FastClickUtil;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetailsParams;
import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.app.BillingClientLifecycle;
import com.fine.friendlycc.app.Injection;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.bean.CreateOrderBean;
import com.fine.friendlycc.bean.VipPackageItemBean;
import com.fine.friendlycc.event.UserUpdateVipEvent;
import com.fine.friendlycc.ui.base.BaseDialog;
import com.fine.friendlycc.ui.webview.BrowserView;
import com.fine.friendlycc.utils.Utils;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Author: 彭石林
 * Time: 2021/12/22 20:03
 * Description: This is WebViewDialog
 */
public class WebViewDialog extends BaseDialog {
    private final static String TAG = "WebViewDialog支付";
    private final Context context;
    private Dialog dialog;
    private final String webUrl;
    private final AppCompatActivity mActivity;
    private final ConfirmOnclick confirmOnclick;
    private ImageView iv_default;
    //会员天数
    public Integer pay_good_day = 0;
    private volatile VipPackageItemBean vipPackageItemEntity = new VipPackageItemBean();
    private String orderNumber = null;
    //是否是购买商品 or  订阅vip
    private boolean GooglePayInApp = false;
    private ViewGroup loadingView;
    /**
     * 谷歌支付连接
     */
    public BillingClientLifecycle billingClientLifecycle;

    public WebViewDialog(@NonNull Context context, AppCompatActivity activity, String url, ConfirmOnclick confirmOnclick) {
        super(context);
        this.context = context;
        this.mActivity = activity;
        this.webUrl = url;
        this.confirmOnclick = confirmOnclick;
        initGooglePay();
    }


    private void initGooglePay(){
        this.billingClientLifecycle = ((CCApplication)mActivity.getApplication()).getBillingClientLifecycle();
        this.billingClientLifecycle.PAYMENT_SUCCESS.observe(this, billingPurchasesState -> {
            Log.e("BillingClientLifecycle","支付购买成功回调");
            switch (billingPurchasesState.getBillingFlowNode()){
                //查询商品阶段
                case querySkuDetails:
                    break;
                case launchBilling: //启动购买
                    break;
                case purchasesUpdated: //用户购买操作 可在此购买成功 or 取消支付
                    break;
                case acknowledgePurchase:  // 用户操作购买成功 --> 商家确认操作 需要手动确定收货（消耗这笔订单并且发货（给与用户购买奖励）） 否则 到达一定时间 自动退款
                    Purchase purchase = billingPurchasesState.getPurchase();
                    if(purchase!=null){
                        try {
                            CCApplication.instance().logEvent(AppsFlyerEvent.Successful_top_up, vipPackageItemEntity.getPrice(), purchase);
                        } catch (Exception e) {

                        }
                        String packageName = purchase.getPackageName();
                        paySuccessNotify(packageName, orderNumber, purchase.getSkus(), purchase.getPurchaseToken(), 1,vipPackageItemEntity);
                        Log.e("BillingClientLifecycle","dialog支付购买成功："+purchase.toString());
                    }
                    break;
            }
        });
        this.billingClientLifecycle.PAYMENT_FAIL.observe(this, billingPurchasesState -> {
            Log.e("BillingClientLifecycle","支付购买失败回调");
            switch (billingPurchasesState.getBillingFlowNode()){
                //查询商品阶段-->异常
                case querySkuDetails:
                    break;
                case launchBilling: //启动购买-->异常
                    break;
                case purchasesUpdated: //用户购买操作 可在此购买成功 or 取消支付 -->异常
                    break;
                case acknowledgePurchase:  // 用户操作购买成功 --> 商家确认操作 需要手动确定收货（消耗这笔订单并且发货（给与用户购买奖励）） 否则 到达一定时间 自动退款 -->异常
                    break;
            }
        });
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


    public Dialog noticeDialog() {
        dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        View view = View.inflate(context, R.layout.alert_notice_web_view, null);
        ImageView ic_dialog_close = view.findViewById(R.id.ic_dialog_close);
        BrowserView webView = view.findViewById(R.id.web_view);
        iv_default = view.findViewById(R.id.iv_default);
        loadingView = view.findViewById(R.id.rl_loading);
        ic_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                webView.destroy();
                if (confirmOnclick != null)confirmOnclick.cancel();
            }
        });

        WebSettings settings = webView.getSettings();
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
        webView.addJavascriptInterface(new ShareJavaScriptInterface(context), "Native");
        //视频自动播放
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        webView.setGson(new Gson());
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        setCookie(mActivity,webUrl);
        //设置打开的页面地址
        webView.loadUrl(webUrl);

        //设置背景透明,去四个角
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(view);
        //设置宽度充满屏幕
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        return dialog;
    }

    public static void setCookie(Context context, String url) {
        try {
            CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();//移除
            cookieManager.removeAllCookie();
            cookieManager.setCookie(url, "local="+context.getString(R.string.playcc_local_language));
            cookieManager.setCookie(url, "appId="+ AppConfig.APPID);
            CookieSyncManager.getInstance().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyBrowserViewClient extends BrowserView.BrowserViewClient {


        /**
         * @return android.webkit.WebResourceResponse
         * @Desc TODO(拦截网页加载图片 。 安卓本地做缓存处理 。 提高网页加载图片速度 ；)
         * @author 彭石林
         * @parame [view, request]
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

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            iv_default.setVisibility(View.GONE);
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
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            return true;
        }

        /**
         * 收到加载进度变化
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        }
    }

    //进行支付
    private void pay(String payCode) {
        List<String> skuList = new ArrayList<>();
        skuList.add(payCode);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(GooglePayInApp ? BillingClient.SkuType.SUBS : BillingClient.SkuType.INAPP);
        billingClientLifecycle.querySkuDetailsLaunchBillingFlow(params,mActivity,orderNumber);
    }

    //创建订单
    public void createOrder(Integer goodId, String payCode) {
        loadingView.setVisibility(View.VISIBLE);
        //1购买商品  2订阅商品
        int types = GooglePayInApp == false ? 1 : 2;
        Injection.provideDemoRepository()
                .createOrder(goodId, types, 2, null)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<CreateOrderBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CreateOrderBean> response) {
                        loadingView.setVisibility(View.GONE);
                        orderNumber = response.getData().getOrderNumber();
                        //会员天数
                        pay_good_day = response.getData().getActual_value();
                        pay(payCode);
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        loadingView.setVisibility(View.GONE);
                    }
                });
    }

    //支付成功上报
    public void paySuccessNotify(String packageName, String orderNumber, List<String> productId, String token, Integer event,VipPackageItemBean vipItemEntity) {
        loadingView.setVisibility(View.VISIBLE);
        Injection.provideDemoRepository()
                .paySuccessNotify(packageName, orderNumber, productId, token, 1, event)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        loadingView.setVisibility(View.GONE);
                        //dialog.dismiss();
                        try{
                            if(vipItemEntity==null){
                                ToastUtils.showShort(R.string.playcc_coin_custom_text);
                                return;
                            }
                            if (GooglePayInApp) {
                                RxBus.getDefault().post(new UserUpdateVipEvent(Utils.formatday.format(Utils.addDate(new Date(), pay_good_day)), 1));
                            } else {
                                if (confirmOnclick != null) {
                                    int countCoin = 0;
                                    Integer purchased = vipItemEntity.getPurchased();
                                    if(purchased == null){
                                        purchased = -1;
                                    }
                                    //未进行首充
                                    if (!ObjectUtils.isEmpty(purchased) && purchased.intValue() == 0) {
                                        Integer actualValue = vipItemEntity.getActualValue();
                                        Integer giveCoin = vipItemEntity.getGiveCoin();
                                        if (actualValue != null) {
                                            countCoin += actualValue.intValue();
                                        }
                                        if (giveCoin != null) {
                                            countCoin += giveCoin.intValue();
                                        }

                                    } else {
                                        countCoin = vipItemEntity.getActualValue();
                                    }
                                    //5秒最多发一次
                                    if(!FastClickUtil.isFastCallFun("WebViewDialogSuccess")){
                                        if (confirmOnclick != null) {
                                            confirmOnclick.vipRechargeDiamondSuccess(dialog, countCoin);
                                        }
                                    }
                                }
                            }
                        }catch (Exception e){
                            ToastUtils.showShort(R.string.playcc_coin_custom_text);
                        }

                    }

                    @Override
                    public void onError(RequestException e) {
                        loadingView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public interface  ConfirmOnclick {
        //跳转到vip界面
         void webToVipRechargeVC(Dialog dialog);

        //更多钻石储值
         void moreRechargeDiamond(Dialog dialog);

        //充值钻石
         void vipRechargeDiamondSuccess(Dialog dialog, Integer coinMoney);

         void cancel();
    }

    public class ShareJavaScriptInterface {
        Context mContext;

        ShareJavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public String getToken() {
            String token = Injection.provideDemoRepository().readLoginInfo().getToken();
            return token;
        }

        @JavascriptInterface
        public void webToVipRechargeVC() {
            if (confirmOnclick != null) {
                confirmOnclick.webToVipRechargeVC(dialog);
            }
        }

        @JavascriptInterface
        public void moreRechargeDiamond() {
            if (confirmOnclick != null) {
                confirmOnclick.moreRechargeDiamond(dialog);
            }
        }

        @JavascriptInterface
        public void nonVipRecharge(String data) {
            GooglePayInApp = true;
            //购买商品
            //vip充值
            vipPackageItemEntity = new Gson().fromJson(data, VipPackageItemBean.class);
            if(vipPackageItemEntity!=null && vipPackageItemEntity.getGoogleGoodsId()!=null){
                String googleGoodsId = vipPackageItemEntity.getGoogleGoodsId();
                //创建订单
                createOrder(vipPackageItemEntity.getId(), googleGoodsId);
            }
        }

        @JavascriptInterface
        public void vipRechargeDiamond(String data) {
            GooglePayInApp = false;
            //购买商品
            vipPackageItemEntity = new Gson().fromJson(data, VipPackageItemBean.class);
            if(vipPackageItemEntity!=null && vipPackageItemEntity.getGoogleGoodsId()!=null){
                String googleGoodsId = vipPackageItemEntity.getGoogleGoodsId();
                //创建订单
                createOrder(vipPackageItemEntity.getId(), googleGoodsId);
            }
        }

        @JavascriptInterface
        public void back() {
            dialog.dismiss();
        }

    }
}