package com.dl.playfun.widget.coinrechargesheet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetailsParams;
import com.dl.playfun.R;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.app.BillingClientLifecycle;
import com.dl.playfun.app.Injection;
import com.dl.playfun.data.source.http.exception.RequestException;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseDataResponse;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.entity.CoinWalletEntity;
import com.dl.playfun.entity.CreateOrderEntity;
import com.dl.playfun.entity.GoodsEntity;
import com.dl.playfun.ui.base.BaseDialog;
import com.dl.playfun.ui.dialog.adapter.CoinExchargeIntegralAdapter;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Author: 彭石林
 * Time: 2021/9/29 14:31
 * Description: 积分购买兑换弹窗
 */
public class CoinExchargeItegralDialog extends BaseDialog implements View.OnClickListener {

    public static final String TAG = "CoinExchargeItegralDialog";
    private static CoinExchargeIntegralAdapterListener coinExchargeIntegralAdapterListener;
    private static GoodsEntity sel_goodsEntity;
    private final AppCompatActivity mActivity;
    private View mPopView;
    private RecyclerView recyclerView;
    private TextView tvBalance;
    private ImageView ivRefresh;
    private ViewGroup loadingView;
    private CoinExchargeIntegralAdapter adapter;
    private BillingClientLifecycle billingClientLifecycle;
    private List<GoodsEntity> mGoodsList;
    private String orderNumber;

    public CoinExchargeItegralDialog(@NonNull Context context, AppCompatActivity activity) {
        super(context);
        this.mActivity = activity;
        this.billingClientLifecycle = ((AppContext)activity.getApplication()).getBillingClientLifecycle();
        init(activity);
    }

    public CoinExchargeIntegralAdapterListener getCoinRechargeSheetViewListener() {
        return coinExchargeIntegralAdapterListener;
    }

    public void setCoinRechargeSheetViewListener(CoinExchargeIntegralAdapterListener coinExchargeIntegralAdapterListener) {
        CoinExchargeItegralDialog.coinExchargeIntegralAdapterListener = coinExchargeIntegralAdapterListener;
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        mPopView = inflater.inflate(R.layout.view_coin_exrcharge_integral_sheet, null);

        recyclerView = mPopView.findViewById(R.id.recycler_view);
        tvBalance = mPopView.findViewById(R.id.tv_balance);
        ivRefresh = mPopView.findViewById(R.id.iv_refresh);
        loadingView = mPopView.findViewById(R.id.rl_loading);
        ivRefresh.setOnClickListener(this);

        adapter = new CoinExchargeIntegralAdapter(recyclerView);
        adapter.setCoinExchargeIntegralAdapterListener(new CoinExchargeIntegralAdapter.CoinExchargeIntegralAdapterListener() {
            @Override
            public void onBuyClick(View view, int position) {
                GoodsEntity goodsEntity = mGoodsList.get(position);
                sel_goodsEntity = goodsEntity;
                createOrder(goodsEntity);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        if(billingClientLifecycle!=null){
            //查询并消耗本地历史订单类型： INAPP 支付购买  SUBS订阅
            billingClientLifecycle.queryAndConsumePurchase(BillingClient.SkuType.INAPP);
        }
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
                        paySuccessNotify(packageName,orderNumber,purchase.getSkus(),purchase.getPurchaseToken(),1);
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
        loadBalance();
        //查询商品价格
        loadGoods();
    }

    @Override
    public void dismiss() {
        if (mActivity != null && !mActivity.isFinishing()) {
            Window dialogWindow = mActivity.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.alpha = 1.0f;
            dialogWindow.setAttributes(lp);
        }

        // coinExchargeIntegralAdapterListener = null;
        super.dismiss();
    }

    public void show() {
        //设置背景透明,去四个角
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(mPopView);
        //设置宽度充满屏幕
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        super.show();
    }

    @SuppressLint("StringFormatMatches")
    private void loadBalance() {
        Injection.provideDemoRepository()
                .coinWallet()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<CoinWalletEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CoinWalletEntity> response) {
                        tvBalance.setText(String.format(mActivity.getResources().getString(R.string.playfun_x_coin), response.getData().getTotalCoin()));
                    }

                    @Override
                    public void onError(RequestException e) {
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });
    }

    private void loadGoods() {
        Injection.provideDemoRepository()
                .pointsGoodList()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<List<GoodsEntity>>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<List<GoodsEntity>> response) {
                        loadingView.setVisibility(View.GONE);
                        mGoodsList = response.getData();
                        adapter.setData(mGoodsList);
//                        querySkuList(response.getData());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void createOrder(GoodsEntity goodsEntity) {
        loadingView.setVisibility(View.VISIBLE);
        Injection.provideDemoRepository()
                .createOrder(goodsEntity.getId(), 14, 2, null)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<CreateOrderEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CreateOrderEntity> response) {
                        loadingView.setVisibility(View.GONE);
                        orderNumber = response.getData().getOrderNumber();
                        // dialog.show(mActivity.getSupportFragmentManager(), PayMethodDialog.class.getCanonicalName());
                        List<GoodsEntity> goodsEntityList = new ArrayList<>();
                        goodsEntityList.add(goodsEntity);
                        querySkuList(goodsEntityList);
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        loadingView.setVisibility(View.GONE);
                    }
                });
    }

    public void paySuccessNotify(String packageName, String orderNumber, List<String> productId, String token, Integer event) {
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
                        if (coinExchargeIntegralAdapterListener != null) {
                            coinExchargeIntegralAdapterListener.onPaySuccess(CoinExchargeItegralDialog.this, sel_goodsEntity);
                        } else {
                            CoinExchargeItegralDialog.this.dismiss();
                            ToastUtils.showShort(R.string.playfun_pay_success);
                            loadBalance();
                            dismiss();
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        loadingView.setVisibility(View.GONE);
                        if (coinExchargeIntegralAdapterListener != null) {
                            coinExchargeIntegralAdapterListener.onPayFailed(CoinExchargeItegralDialog.this, e.getMessage());
                        } else {
                            CoinExchargeItegralDialog.this.dismiss();
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_refresh) {
            loadBalance();
        }
    }

    private void querySkuList(List<GoodsEntity> goodsList) {

        if (goodsList != null && !goodsList.isEmpty()) {
            List<String> skus = new ArrayList();

            for (GoodsEntity datum : goodsList) {
                skus.add(datum.getGoogleGoodsId());
            }

            this.loadingView.setVisibility(View.VISIBLE);
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(skus).setType(BillingClient.SkuType.INAPP);
            billingClientLifecycle.querySkuDetailsLaunchBillingFlow(params,mActivity,orderNumber);
        }

    }

    public interface CoinExchargeIntegralAdapterListener {
        void onPaySuccess(CoinExchargeItegralDialog sheetView, GoodsEntity goodsEntity);

        void onPayFailed(CoinExchargeItegralDialog sheetView, String msg);
    }
}
