package com.fine.friendlycc.widget.coinrechargesheet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetailsParams;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.BillingClientLifecycle;
import com.fine.friendlycc.app.Injection;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.CreateOrderEntity;
import com.fine.friendlycc.entity.DiamondInfoEntity;
import com.fine.friendlycc.entity.GoodsEntity;
import com.fine.friendlycc.ui.base.BasePopupWindow;
import com.fine.friendlycc.ui.dialog.adapter.CoinRechargeAdapter;
import com.fine.friendlycc.widget.dialog.TraceDialog;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author litchi
 * 储值列表 popup
 */
public class CoinRechargeSheetView extends BasePopupWindow implements View.OnClickListener, CoinRechargeAdapter.CoinRechargeAdapterListener{
    public static final String TAG = "CoinRechargeSheetView";

    private final Activity mActivity;
    private BillingClientLifecycle billingClientLifecycle;
    private View mPopView;
    private RecyclerView recyclerView;
    private Button btn_buy;
    private ImageView iv_close;
    private ViewGroup loadingView;
    private CoinRechargeAdapter adapter;
    private List<GoodsEntity> mGoodsList;
    private GoodsEntity currGoodsInfo;
    private ClickListener clickListener;
    private boolean isFinsh = false;
    public String orderNumber = null;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public CoinRechargeSheetView(Activity activity) {
        super(activity);
        this.mActivity = activity;
        init(activity);
        setPopupWindow();
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        mPopView = inflater.inflate(R.layout.view_coin_recharge_sheet, null);
        recyclerView = mPopView.findViewById(R.id.recycler_view);
        btn_buy = mPopView.findViewById(R.id.btn_buy);
        iv_close = mPopView.findViewById(R.id.iv_close);
        loadingView = mPopView.findViewById(R.id.rl_loading);
        btn_buy.setOnClickListener(this);
        iv_close.setOnClickListener(this);

        adapter = new CoinRechargeAdapter(recyclerView);
        adapter.setCoinRechargeAdapterListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        initData();
        initListener();
        //查询商品列表
        loadGoods();
    }

    private void initData() {
        this.billingClientLifecycle = ((AppContext)mActivity.getApplication()).getBillingClientLifecycle();
        if(billingClientLifecycle!=null){
            //查询并消耗本地历史订单类型： INAPP 支付购买  SUBS订阅
            billingClientLifecycle.queryAndConsumePurchase(BillingClient.SkuType.INAPP);
            billingClientLifecycle.queryAndConsumePurchase(BillingClient.SkuType.SUBS);
            billingClientLifecycle.queryPurchasesAsync(BillingClient.SkuType.INAPP);
            billingClientLifecycle.queryPurchasesAsync(BillingClient.SkuType.SUBS);
        }
    }

    private void initListener() {

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
                        paySuccessNotify(packageName, purchase.getSkus(), purchase.getPurchaseToken(), 1);
                        Log.e("BillingClientLifecycle","dialog支付购买成功："+purchase.toString());
                    }
                    break;
            }
        });

        this.billingClientLifecycle.PAYMENT_FAIL.observe(this, billingPurchasesState -> {
            Log.e("BillingClientLifecycle","支付购买失败回调");
            ToastUtils.showShort(StringUtils.getString(R.string.playfun_pay_fail));
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
        //查询消耗本地历史订单
        this.billingClientLifecycle.PurchaseHistory.observe(this,billingPurchasesState -> {
            Log.e("BillingClientLifecycle","查询本地历史订单。没有消耗确认的商品");
            switch (billingPurchasesState.getBillingFlowNode()){
                //有历史订单支付。开始消耗
                case queryPurchaseHistory:
                    break;
                //确认收货
                case acknowledgePurchase:
                    break;
            }
        });
    }

    /**
     * 设置窗口的相关属性
     */
    @SuppressLint("InlinedApi")
    private void setPopupWindow() {
        this.setContentView(mPopView);// 设置View
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
        this.setOutsideTouchable(false);//设置popupwindow外部可点击
        this.setFocusable(true);// 设置弹出窗口可
        this.setTouchable(true);
        this.setAnimationStyle(R.style.popup_window_anim);// 设置动画
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
    }

    @Override
    public void dismiss() {
        if (!isFinsh){
            showRetainDialog();
            return;
        }
        if (mActivity != null && !mActivity.isFinishing()) {
            Window dialogWindow = mActivity.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.alpha = 1.0f;
            dialogWindow.setAttributes(lp);
        }
        super.dismiss();
    }

    public void show() {
        super.showLifecycle();
        if (mActivity != null && !mActivity.isFinishing()) {
            Window dialogWindow = mActivity.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.alpha = 0.5f;
            dialogWindow.setAttributes(lp);
        }
        this.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void loadGoods() {
        Injection.provideDemoRepository()
                .goods()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> {
                    showLoading();
                })
                .subscribe(new BaseObserver<BaseDataResponse<DiamondInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<DiamondInfoEntity> response) {
                        mGoodsList = response.getData().getList();
                        if (mGoodsList == null || mGoodsList.size() <= 0){
                            return;
                        }
                        adapter.setData(mGoodsList);

                        //默认选中第一个钻石套餐
                        int type = mGoodsList.get(0).getType();
                        if (type == 2 && mGoodsList.size() > 1){
                            mGoodsList.get(1).setSelected(true);
                            currGoodsInfo = mGoodsList.get(1);
                        }else {
                            mGoodsList.get(0).setSelected(true);
                            currGoodsInfo = mGoodsList.get(0);
                        }

                    }

                    @Override
                    public void onComplete() {
                        hideLoading();
                    }
                });
    }

    /**
     * 显示奖励dialog
     */
    private void showRewardDialog() {
        if (currGoodsInfo == null){
            isFinsh = true;
            dismiss();
            return;
        }
        int totalReward ;
        if (currGoodsInfo.getType() == 1){
            totalReward = currGoodsInfo.getGiveCoin() + currGoodsInfo.getActualValue() + currGoodsInfo.getGoldPrice();
        }else {
            totalReward = currGoodsInfo.getGiveCoin();
        }

        TraceDialog.getInstance(mActivity)
                .setTitle(mActivity.getString(R.string.playfun_recharge_success))
                .setConfirmOnlick(dialog -> {
                    if (clickListener != null){
                        clickListener.paySuccess(currGoodsInfo);
                    }
                    isFinsh = true;
                    dialog.dismiss();
                    dismiss();
                })
                .dayRewardDialog(true,
                        currGoodsInfo.getDayGiveCoin(),
                        currGoodsInfo.getDayGiveVideoCard(),
                        totalReward,
                        currGoodsInfo.getVideoCard())
                .show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_buy) {
            //购买
            createOrder();
        }else if (v.getId() == R.id.iv_close){
            dismiss();
        }
    }

    @Override
    public void itemViewOnClick(View view, int position) {
        for (GoodsEntity entity : mGoodsList) {
            entity.setSelected(false);
        }
        mGoodsList.get(position).setSelected(true);
        currGoodsInfo = mGoodsList.get(position);
        adapter.notifyDataSetChanged();
    }

    /**
     * 创建订单
     */
    public void createOrder() {
        if (currGoodsInfo == null) {
            ToastUtils.showShort(R.string.playfun_please_choose_top_up_package);
            return;
        }
        Injection.provideDemoRepository()
                .createOrder(currGoodsInfo.getId(), 1, 2, null)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> {showLoading();})
                .subscribe(new BaseObserver<BaseDataResponse<CreateOrderEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CreateOrderEntity> response) {
                        orderNumber = response.getData().getOrderNumber();
                        pay(currGoodsInfo.getGoogleGoodsId());
                    }

                    @Override
                    public void onError(RequestException e) {
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        hideLoading();
                    }
                });
    }

    //进行支付
    private void pay(String payCode) {
        List<String> skuList = new ArrayList<>();
        skuList.add(payCode);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        if (currGoodsInfo.getType() == 2){
            params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        }else {
            params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        }
        billingClientLifecycle.querySkuDetailsLaunchBillingFlow(params,mActivity,orderNumber);
    }

    /**
     * 回调结果给后台
     *
     * @param packageName
     * @param productId
     * @param token
     * @param event
     */
    public void paySuccessNotify(String packageName, List<String> productId, String token, Integer event) {
        Injection.provideDemoRepository()
                .paySuccessNotify(packageName, orderNumber, productId, token, 2, event)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> {
                    showLoading();
                })
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        ToastUtils.showShort(StringUtils.getString(R.string.playfun_pay_success));
                        showRewardDialog();
                    }

                    @Override
                    public void onError(RequestException e) {

                    }

                    @Override
                    public void onComplete() {
                        hideLoading();
                    }
                });
    }

    private void showLoading(){
        loadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoading(){
        loadingView.setVisibility(View.GONE);
    }

    public void showRetainDialog() {
        TraceDialog.getInstance(mActivity)
                .setConfirmOnlick(dialog -> {
                    isFinsh = true;
                    dialog.dismiss();
                    dismiss();
                })
                .rechargeRetainDialog()
                .show();
    }

    public interface ClickListener {
        //跳转去谷歌支付页面
        default void paySuccess(GoodsEntity goodsEntity) {

        }
    }
}