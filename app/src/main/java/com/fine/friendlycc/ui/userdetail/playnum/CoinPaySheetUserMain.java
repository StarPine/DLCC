package com.fine.friendlycc.ui.userdetail.playnum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.Injection;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.CoinWalletEntity;
import com.fine.friendlycc.entity.CreateOrderEntity;
import com.fine.friendlycc.ui.base.BasePopupWindow;
import com.fine.friendlycc.widget.coinrechargesheet.CoinRechargeSheetView;
import com.fine.friendlycc.widget.dialog.MVDialog;

import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @ClassName CoinPaySheetUserMain
 * @Description 充值次数封装弹窗-用户主页
 * @Author 彭石林
 * @Date 2021/7/6 17:12
 * @Phone 16620350375
 * @email 15616314565@163.com
 * @Version 1.0
 **/
public class CoinPaySheetUserMain {
    public static final int PAY_TYPE_COIN_RED_PACKAGE = 1111;
    private static final boolean canDismiss = false;// 设为false可以控制dismiss()无参方法不调用 主要是为了点击PopupWindow外部不消失
    private static int payMoney = 0;
    private static int num_val = 1;
    private final CoinPaySheetView coinPaySheetView;
    private final Builder builder;

    private CoinPaySheetUserMain(Builder builder) {
        this.builder = builder;
        coinPaySheetView = new CoinPaySheetView(builder.activity, this);

    }

    public void show() {
        if (coinPaySheetView != null) {
            coinPaySheetView.show();
        }
    }

    public void dismiss() {
        if (coinPaySheetView != null) {
            coinPaySheetView.dismiss();
        }
    }

    public void dismiss2(int type) {
        if (coinPaySheetView != null) {
            coinPaySheetView.dismiss2(type);
        }
    }

    public interface CancelClickListener {
        void onCancelClick(CoinPaySheetUserMain bottomSheet);
    }

    public interface ItemSelectedListener {
        void onItemSelected(CoinPaySheetUserMain bottomSheet, int position);
    }

    public interface UserdetailUnlockListener {
        void onPaySuccess(CoinPaySheetUserMain sheet, String orderNo, Integer payPrice);

        void onPauError(int type);
    }

    public static class Builder {
        private final AppCompatActivity activity;

        private int payType;
        private int payGoodsId;
        private int goodMoney;
        private boolean autoPay = false;

        private UserdetailUnlockListener userdetailUnlockListener;

        public Builder(AppCompatActivity activity) {
            this.activity = activity;
        }

        public Builder setPayParams(int payType, int payGoodsId, int payGoodsMoney, boolean autoPay, UserdetailUnlockListener userdetailUnlockListener) {
            this.autoPay = autoPay;
            this.payType = payType;
            this.payGoodsId = payGoodsId;
            this.goodMoney = payGoodsMoney;
            payMoney = payGoodsMoney;
            this.userdetailUnlockListener = userdetailUnlockListener;
            return this;
        }

        public CoinPaySheetUserMain build() {
            return new CoinPaySheetUserMain(this);
        }
    }

    private static class CoinPaySheetView extends BasePopupWindow implements View.OnClickListener {

        private final AppCompatActivity mActivity;
        private final CoinPaySheetUserMain bottomSheet;
        private View mPopView;
        private TextView tvBalance;
        private ImageView ivRefresh;
        private TextView tvPayPrice;
        private Button btnPay;
        private Button btnRecharge;
        private ViewGroup loadingView;
        //添加次数-减少次数按钮
        private ImageView icMinNo;
        private ImageView icMaxNo;
        private TextView user_detail_num;
        private ImageView icClose;
        private int mBalance = -1;

        private CreateOrderEntity mOrderEntity = null;

        public CoinPaySheetView(AppCompatActivity activity, CoinPaySheetUserMain bottomSheet) {
            super(activity);
            this.mActivity = activity;
            this.bottomSheet = bottomSheet;
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
            mPopView = inflater.inflate(R.layout.view_coin_pay_user_detail, null);

            tvBalance = mPopView.findViewById(R.id.tv_balance);
            ivRefresh = mPopView.findViewById(R.id.iv_refresh);
            tvPayPrice = mPopView.findViewById(R.id.tv_pay_price);
            btnPay = mPopView.findViewById(R.id.btn_pay);
            btnRecharge = mPopView.findViewById(R.id.btn_recharge);
            loadingView = mPopView.findViewById(R.id.rl_loading);

            icMinNo = mPopView.findViewById(R.id.user_detail_min);
            icMaxNo = mPopView.findViewById(R.id.user_detail_max);
            user_detail_num = mPopView.findViewById(R.id.user_detail_num);
            icClose = mPopView.findViewById(R.id.close);
            icMinNo.setOnClickListener(this);
            icMaxNo.setOnClickListener(this);
            icClose.setOnClickListener(this);

            ivRefresh.setOnClickListener(this);
            btnPay.setOnClickListener(this);
            btnRecharge.setOnClickListener(this);
            //tvPayPrice.setText(String.valueOf(bottomSheet.builder.goodMoney));
            tvPayPrice.setText(String.valueOf(payMoney));
            num_val = 1;
            loadBalance();

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
//            if (mActivity != null && !mActivity.isFinishing()) {
//                Window dialogWindow = mActivity.getWindow();
//                WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//                dialogWindow.setAttributes(lp);
//            }
            if (canDismiss)
                dismiss2(0);
            else {
                StackTraceElement[] stackTrace = new Exception().getStackTrace();
                if (stackTrace.length >= 2 && "dispatchKeyEvent".equals(stackTrace[1].getMethodName())) {
                    dismiss2(0);
                }
            }
        }

        public void dismiss2(int type) {
            if (bottomSheet.builder.userdetailUnlockListener != null) {
                bottomSheet.builder.userdetailUnlockListener.onPauError(type);
            }
            super.dismiss();
        }

        public void show() {

//            if (mActivity != null && !mActivity.isFinishing()) {
//                Window dialogWindow = mActivity.getWindow();
//                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//                lp.alpha = 0.5f;
//                dialogWindow.setAttributes(lp);
//            }
            this.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }

        @Override
        public void onClick(View view) {
            if (payMoney == 0) {
                payMoney = Integer.parseInt(tvPayPrice.getText().toString());
            }
            int id = view.getId();
            if (id == R.id.iv_refresh) {
                loadBalance();
            } else if (id == R.id.btn_pay) {
                createOrder();
            } else if (id == R.id.btn_recharge) {
                showRecharge();
            } else if (id == R.id.user_detail_min) {
                num_val = Integer.parseInt(user_detail_num.getText().toString());
                if (num_val - 1 == 0) {
                    return;
                } else {
                    num_val--;
                    if (num_val <= 1) {
                        icMinNo.setBackground(mActivity.getResources().getDrawable(R.drawable.ic_user_detail_min_no));
                    }
                    tvPayPrice.setText(String.valueOf(num_val * payMoney));
                    user_detail_num.setText(String.valueOf(num_val));
                }
            } else if (id == R.id.user_detail_max) {
                num_val = Integer.parseInt(user_detail_num.getText().toString());
                if (num_val < 15) {
                    num_val++;
                    tvPayPrice.setText(String.valueOf(num_val * payMoney));
                    user_detail_num.setText(String.valueOf(num_val));
                    icMinNo.setBackground(mActivity.getResources().getDrawable(R.drawable.ic_user_detail_min_ed));
                }
            } else if (id == R.id.close) {
                dismiss2(-1);
            }
        }

        private void pay() {
            payOrder();
        }

        private void showRecharge() {
            this.dismiss();
            CoinRechargeSheetView coinRechargeSheetView = new CoinRechargeSheetView(mActivity);
            coinRechargeSheetView.show();
        }

        private void loadBalance() {
            Injection.provideDemoRepository().coinWallet()
                    .doOnSubscribe(this)
                    .compose(RxUtils.schedulersTransformer())
                    .compose(RxUtils.exceptionTransformer())
                    .subscribe(new BaseObserver<BaseDataResponse<CoinWalletEntity>>() {
                        @Override
                        public void onSuccess(BaseDataResponse<CoinWalletEntity> response) {
                            mBalance = response.getData().getTotalCoin();
                            tvBalance.setText(String.valueOf(response.getData().getTotalCoin()));
                            autoPay();
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

        private void createOrder() {
            loadingView.setVisibility(View.VISIBLE);
            Injection.provideDemoRepository().createOrderUserDetail(bottomSheet.builder.payGoodsId, bottomSheet.builder.payType, 1, num_val)
                    .doOnSubscribe(this)
                    .compose(RxUtils.schedulersTransformer())
                    .compose(RxUtils.exceptionTransformer())
                    .subscribe(new BaseObserver<BaseDataResponse<CreateOrderEntity>>() {
                        @Override
                        public void onSuccess(BaseDataResponse<CreateOrderEntity> response) {
                            mOrderEntity = response.getData();
                            tvPayPrice.setText(String.valueOf(mOrderEntity.getMoney()));
                            autoPay();
                            payOrder();
                        }

                        @Override
                        public void onError(RequestException e) {
                            ToastUtils.showShort(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            loadingView.setVisibility(View.GONE);
                            super.onComplete();
                        }
                    });
        }

        private void autoPay() {
            if (bottomSheet.builder.autoPay && mBalance != -1 && mOrderEntity != null) {
                bottomSheet.builder.autoPay = false;
                loadingView.postDelayed(this::pay, 100);
            }
        }

        private void payOrder() {
            if (mOrderEntity == null) {
                ToastUtils.showShort(R.string.playcc_order_no_exist);
                return;
            }
            if (mBalance < mOrderEntity.getMoney()) {
                MVDialog.getInstance(mActivity)
                        .setTitele(StringUtils.getString(R.string.playcc_insufficient_diamonds))
                        .setContent(StringUtils.getString(R.string.playcc_top_up_to_pay))
                        .setConfirmText(StringUtils.getString(R.string.playcc_dialong_coin_rechaege_top_up))
                        .chooseType(MVDialog.TypeEnum.CENTER)
                        .setConfirmOnlick(dialog -> {
                            dialog.dismiss();
                            showRecharge();
                        })
                        .chooseType(MVDialog.TypeEnum.CENTER)
                        .show();
                return;
            }
            loadingView.setVisibility(View.VISIBLE);
            Injection.provideDemoRepository().coinPayOrder(mOrderEntity.getOrderNumber())
                    .doOnSubscribe(this)
                    .compose(RxUtils.schedulersTransformer())
                    .compose(RxUtils.exceptionTransformer())
                    .subscribe(new BaseObserver<BaseResponse>() {
                        @Override
                        public void onSuccess(BaseResponse response) {
                            if (bottomSheet.builder.userdetailUnlockListener != null) {
                                bottomSheet.builder.userdetailUnlockListener.onPaySuccess(bottomSheet, mOrderEntity.getOrderNumber(), mOrderEntity.getMoney());
                            }

                        }

                        @Override
                        public void onError(RequestException e) {
                            ToastUtils.showShort(e.getMessage());
                            if (bottomSheet.builder.userdetailUnlockListener != null) {
                                bottomSheet.builder.userdetailUnlockListener.onPauError(-1);
                            }
                        }

                        @Override
                        public void onComplete() {
                            super.onComplete();
                            if (bottomSheet.builder.userdetailUnlockListener == null) {
                                loadingView.setVisibility(View.GONE);
                            }
                        }

                    });
        }

    }

}