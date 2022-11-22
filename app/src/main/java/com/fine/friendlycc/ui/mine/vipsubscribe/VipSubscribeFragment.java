package com.fine.friendlycc.ui.mine.vipsubscribe;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetailsParams;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.app.BillingClientLifecycle;
import com.fine.friendlycc.databinding.FragmentVipSubscribeBinding;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.ui.mine.webdetail.WebDetailFragment;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.utils.ImmersionBarUtils;
import com.fine.friendlycc.widget.BasicToolbar;
import com.fine.friendlycc.widget.dialog.TraceDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wulei
 */
public class VipSubscribeFragment extends BaseToolbarFragment<FragmentVipSubscribeBinding, VipSubscribeViewModel> {
    public static final String TAG = "VipSubscribeFragment";
    private String USER_AGREEMENT = "用戶協議";
    private String PRIVACY_POLICY = "隱私政策";
    private boolean isFinsh = false;

    public BillingClientLifecycle billingClientLifecycle;

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBarUtils.setupStatusBar(this, false, true);
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        ImmersionBarUtils.setupStatusBar(this, true, true);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.fragment_vip_subscribe;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public VipSubscribeViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(VipSubscribeViewModel.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initData() {
        super.initData();
        binding.rcvPrivileges.setNestedScrollingEnabled(false);
        billingClientLifecycle = ((CCApplication) mActivity.getApplication()).getBillingClientLifecycle();
        //查询商品价格
        viewModel.loadPackage();
        if (billingClientLifecycle != null) {
            //查询并消耗本地历史订单类型： INAPP 支付购买  SUBS订阅
            billingClientLifecycle.queryAndConsumePurchase(BillingClient.SkuType.SUBS);
        }
        //查询消耗本地历史订单
        this.billingClientLifecycle.PurchaseHistory.observe(this, billingPurchasesState -> {
            Log.e("BillingClientLifecycle", "查询本地历史订单。没有消耗确认的商品");
            switch (billingPurchasesState.getBillingFlowNode()) {
                //有历史订单支付。开始消耗
                case queryPurchaseHistory:
                    break;
                //确认收货
                case acknowledgePurchase:
                    break;
            }
        });
        this.billingClientLifecycle.PAYMENT_SUCCESS.observe(this, billingPurchasesState -> {
            Log.e("BillingClientLifecycle", "支付购买成功回调");
            switch (billingPurchasesState.getBillingFlowNode()) {
                //查询商品阶段
                case querySkuDetails:
                    break;
                case launchBilling: //启动购买
                    break;
                case purchasesUpdated: //用户购买操作 可在此购买成功 or 取消支付
                    break;
                case acknowledgePurchase:  // 用户操作购买成功 --> 商家确认操作 需要手动确定收货（消耗这笔订单并且发货（给与用户购买奖励）） 否则 到达一定时间 自动退款
                    Purchase purchase = billingPurchasesState.getPurchase();
                    if (purchase != null) {
                        try {
                            CCApplication.instance().logEvent(AppsFlyerEvent.Subscribe_Successfully, viewModel.$vipPackageItemEntity.getPrice(), purchase);
                        } catch (Exception e) {

                        }
                        String packageName = purchase.getPackageName();
                        List<String> sku = purchase.getSkus();
                        String pToken = purchase.getPurchaseToken();
                        viewModel.paySuccessNotify(packageName, sku, pToken, 0);
                        Log.e("BillingClientLifecycle", "dialog支付购买成功：" + purchase.toString());
                    }
                    break;
            }
        });
        this.billingClientLifecycle.PAYMENT_FAIL.observe(this, billingPurchasesState -> {
            Log.e("BillingClientLifecycle", "支付购买失败回调");
            switch (billingPurchasesState.getBillingFlowNode()) {
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
     * 显示奖励dialog
     */
    private void showRewardDialog() {
        if (viewModel.$vipPackageItemEntity.getGiveCoin() <= 0 && viewModel.$vipPackageItemEntity.getVideoCard() <= 0){
            onDestroy();
            pop();
            return;
        }
        TraceDialog.getInstance(mActivity)
                .setConfirmOnlick(dialog -> {
                    dialog.dismiss();
                    onDestroy();
                    pop();
                })
                .dayRewardDialog(true,
                        viewModel.$vipPackageItemEntity.getDayGiveCoin(),
                        viewModel.$vipPackageItemEntity.getDayGiveVideoCard(),
                        viewModel.$vipPackageItemEntity.getGiveCoin(),
                        viewModel.$vipPackageItemEntity.getVideoCard())
                .show();
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.pay_good_day = 0;
        viewModel.uc.clickPay.observe(this, payCode -> pay(payCode));
        viewModel.uc.successBack.observe(this, aBoolean -> {
            showRewardDialog();
        });
        initServiceTips();

    }

    /**
     * 初始化服务提示
     */
    private void initServiceTips() {
        String content = (String) binding.tvVipServiceTip.getText();
        SpannableString spannableString = new SpannableString(content);
        USER_AGREEMENT = getString(R.string.playcc_user_agreement_tips);
        PRIVACY_POLICY = getString(R.string.playcc_privacy_policy_tips);
        setServiceTips(spannableString, binding.tvVipServiceTip, content, USER_AGREEMENT);
        setServiceTips(spannableString, binding.tvVipServiceTip, content, PRIVACY_POLICY);
    }

    private SpannableString setServiceTips(SpannableString spannableString, TextView tvTips, String content, String key) {
        UnderlineSpan colorSpan = new UnderlineSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(mActivity.getResources().getColor(R.color.pseekbar_process_off));//设置颜色
//                ds.setUnderlineText(false); //去掉下划线
            }
        };
        ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void onClick(@NonNull View widget) {
                if (key.equals(USER_AGREEMENT)) {
                    Bundle bundle = WebDetailFragment.getStartBundle(AppConfig.TERMS_OF_SERVICE_URL);
                    viewModel.start(WebDetailFragment.class.getCanonicalName(), bundle);
                } else if (key.equals(PRIVACY_POLICY)) {
                    Bundle bundle = WebDetailFragment.getStartBundle(AppConfig.PRIVACY_POLICY_URL);
                    viewModel.start(WebDetailFragment.class.getCanonicalName(), bundle);
                }

            }
        };
        int tips = content.indexOf(key);
        spannableString.setSpan(clickableSpan, tips, tips + key.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString.setSpan(colorSpan, tips, tips + key.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvTips.setMovementMethod(LinkMovementMethod.getInstance());
        tvTips.setText(spannableString);
        return spannableString;
    }

    //进行支付
    private void pay(String payCode) {
        CCApplication.instance().logEvent(AppsFlyerEvent.Subscribe);
        List<String> skuList = new ArrayList<>();
        skuList.add(payCode);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        billingClientLifecycle.querySkuDetailsLaunchBillingFlow(params, mActivity, viewModel.orderNumber);
    }

    @Override
    public boolean onBackPressedSupport() {
        if (!isFinsh) {
            TraceDialog.getInstance(mActivity)
                    .setCannelOnclick(dialog -> {
                        isFinsh = true;
                        mActivity.onBackPressed();
                        dialog.dismiss();
                    })
                    .vipRetainDialog(viewModel.vipPrivilegeList)
                    .show();
            return true;
        }
        return super.onBackPressedSupport();
    }

    @Override
    public void onBackClick(BasicToolbar toolbar) {
        onBackPressedSupport();
    }
}
