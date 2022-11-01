package com.fine.friendlycc.ui.mine.invitewebdetail;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.appsflyer.CreateOneLinkHttpTask;
import com.appsflyer.share.LinkGenerator;
import com.appsflyer.share.ShareInviteHelper;
import com.blankj.utilcode.util.IntentUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.BillingClientLifecycle;
import com.fine.friendlycc.databinding.FragmentInviteWebDetailBinding;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.utils.SoftKeyBoardListener;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.widget.dialog.MMAlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * 邀请网页详细
 *
 * @author wulei
 */
public class InviteWebDetailFragment extends BaseToolbarFragment<FragmentInviteWebDetailBinding, InviteWebDetailViewModel> {

    public static final String ARG_WEB_URL = "arg_web_url";
    public static final String ARG_USER_ID = "arg_user_id";
    private final String TAG = "邀请网页详细";
    protected InputMethodManager inputMethodManager;
    private String url;
    private String userId;
    private boolean SoftKeyboardShow = false;

    private BillingClientLifecycle billingClientLifecycle;

    public static Bundle getStartBundle(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_WEB_URL, url);
        return bundle;
    }

    public static Bundle getStartBundle(String url, String userId) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_WEB_URL, url);
        bundle.putString(ARG_USER_ID, userId);
        return bundle;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_invite_web_detail;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        super.initParam();
        url = getArguments().getString(ARG_WEB_URL);
        userId = getArguments().getString(ARG_USER_ID);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        inputMethodManager = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        SoftKeyBoardListener.setListener(mActivity, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                SoftKeyboardShow = true;
            }

            @Override
            public void keyBoardHide(int height) {
                SoftKeyboardShow = false;
            }
        });
        viewModel.clickPay.observe(this, payCode -> pay(payCode));

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
                        String packageName = purchase.getPackageName();
                        viewModel.paySuccessNotify(packageName, purchase.getSkus(), purchase.getPurchaseToken(), 1);
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

    /**
     * 如果软键盘显示就隐藏软键盘
     */
    protected void hideSoftKeyboard() {
        if (SoftKeyboardShow) {
            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    @Override
    public InviteWebDetailViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(InviteWebDetailViewModel.class);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initData() {
        super.initData();
        this.billingClientLifecycle = ((AppContext)mActivity.getApplication()).getBillingClientLifecycle();
        WebSettings settings = binding.webView.getSettings();
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        //如果不设置WebViewClient，请求会跳转系统浏览器
        binding.webView.setWebViewClient(new MyWebViewClient());
        binding.webView.setWebChromeClient(new WebChromeClient());
        binding.webView.addJavascriptInterface(new ShareJavaScriptInterface(mActivity), "Native");
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        if (url != null) {
            binding.webView.loadUrl(url);

        }
    }


    //进行支付
    private void pay(String payCode) {
        List<String> skuList = new ArrayList<>();
        skuList.add(payCode);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        billingClientLifecycle.querySkuDetailsLaunchBillingFlow(params,mActivity,viewModel.orderNumber);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView webView, String url) {
            webView.loadUrl("javascript:" + url);
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
        public void AlertShareVip(String goodId) {
            MMAlertDialog.AlertShareVip(mActivity, true, new MMAlertDialog.RegUserAlertInterface() {
                @Override
                public void confirm(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (StringUtil.isEmpty(goodId)) {
                        ToastUtils.showShort(R.string.playfun_invite_web_detail_error3);
                        return;
                    }
                    querySkuOrder(goodId);
                }
            }).show();
        }

        @JavascriptInterface
        public void AlertShareVipGet() {
            AlertDialog alertDialog = MMAlertDialog.AlertShareVipGet(mActivity, true, new MMAlertDialog.DilodAlertMessageInterface() {
                @Override
                public void confirm(DialogInterface dialog, int which, int sel_Index, String code) {
                    dialog.dismiss();
                    if (!StringUtil.isEmpty(code)) {
                        viewModel.saveInviteCode(code);
                    } else {
                        ToastUtils.showShort(R.string.playfun_invite_web_detail_error4);
                    }
                }

                @Override
                public void cancel(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    hideSoftKeyboard();
                }
            });
        }

        @JavascriptInterface
        public void AlertShareWriteCode(String code) {
            viewModel.saveInviteCode(code);
        }


        @JavascriptInterface
        public void shareText(String title, String url) {
            viewModel.showHUD();
            LinkGenerator linkGenerator = ShareInviteHelper.generateInviteUrl(mActivity);
            //渠道
            linkGenerator.setChannel("GoogleAppstore");//
            //自定义用户ID
            linkGenerator.setReferrerCustomerId(userId);
            //重定向：
            linkGenerator.addParameter("deep_link_value", "AppStore-PlayChat-Google-TW");
            linkGenerator.addParameter("is_retargeting", "true");
            linkGenerator.addParameter("af_sub1", userId);
            linkGenerator.addParameter("af_sub2", "invite");
            // optional - set a brand domain to the user invite link
            //LinHuang:
            CreateOneLinkHttpTask.ResponseListener listener = new CreateOneLinkHttpTask.ResponseListener() {
                @Override
                public void onResponse(String oneLink) {
                    oneLink = oneLink.substring(oneLink.lastIndexOf('/') + 1);
                    String urls_one = url + "?code=" + userId + "&onelink=" + oneLink;
                    // write logic to let user share the invite link
                    Map<String, String> mapData = new HashMap<>();
                    mapData.put("af_sub1", userId);
                    mapData.put("customer_user_id", userId);
                    mapData.put("af_sub2", "invite");
                    ShareInviteHelper.logInvite(mActivity, "com.ld.play.chat-Taiwan", mapData);

                    binding.webView.post(() -> {
                        viewModel.dismissHUD();
                        ClipboardManager clipboard = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("playchat", title + "\n" + urls_one);
                        clipboard.setPrimaryClip(clip);
                    });
                    Intent intent = IntentUtils.getShareTextIntent(title + "\n" + urls_one);
                    startActivity(intent);
                }

                @Override
                public void onResponseError(String s) {
                    // handle response error
                }
            };
            linkGenerator.generateLink(mActivity, listener);
        }

        @JavascriptInterface
        public void shareImage(String content, String imgUrl) {
            if (imgUrl == null) {
                return;
            }
            Intent intent = IntentUtils.getShareImageIntent(content, Uri.parse(imgUrl));
            startActivity(intent);
        }
    }

    //根据iD查询谷歌商品。并且订购它 7days_free
    public void querySkuOrder(String goodId) {
        if (billingClientLifecycle == null || !billingClientLifecycle.isConnectionSuccessful()) {
            ToastUtils.showShort(R.string.playfun_invite_web_detail_error);
            return;
        }
        ArrayList<String> goodList = new ArrayList<>();
        goodList.add(goodId);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(goodList).setType(BillingClient.SkuType.SUBS);
        billingClientLifecycle.querySkuDetailsAsync(params, (billingResult, skuDetailsList) -> {
            if(billingResult!=null){
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                    if (skuDetailsList == null) {
                        //订阅找不到
                        Log.w(TAG, "onSkuDetailsResponse: null SkuDetails list");
                        ToastUtils.showShort(R.string.playfun_invite_web_detail_error2);
                    } else {
                        for (SkuDetails skuDetails : skuDetailsList) {
                            if (skuDetails.getSku().equals(goodId)) {
                                viewModel.createOrder(skuDetails.getSku());
                                break;
                            }
                        }
                    }
                }
            }
        });
    }
}
