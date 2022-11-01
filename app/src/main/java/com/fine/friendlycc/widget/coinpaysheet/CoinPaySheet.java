package com.fine.friendlycc.widget.coinpaysheet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.fine.friendlycc.entity.ChatRedPackageEntity;
import com.fine.friendlycc.entity.CoinWalletEntity;
import com.fine.friendlycc.entity.CreateOrderEntity;
import com.fine.friendlycc.ui.base.BasePopupWindow;
import com.fine.friendlycc.widget.coinrechargesheet.CoinRechargeSheetView;
import com.fine.friendlycc.widget.dialog.MVDialog;

import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public class CoinPaySheet {
    public static final int PAY_TYPE_COIN_RED_PACKAGE = 1111;

    private CoinPaySheetView coinPaySheetView;

    private Builder builder;

    private CoinPaySheet() {
    }

    private CoinPaySheet(Builder builder) {
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

    public interface CancelClickListener {
        void onCancelClick(CoinPaySheet bottomSheet);
    }

    public interface ItemSelectedListener {
        void onItemSelected(CoinPaySheet bottomSheet, int position);
    }

    public interface UserdetailUnlockListener {
        void onPaySuccess(CoinPaySheet sheet, String orderNo, Integer payPrice);

        void onPauError();
    }

    public interface CoinPayDialogListener {
        void onPaySuccess(CoinPaySheet sheet, String orderNo, Integer payPrice);

        default void toGooglePlayView() {

        }
    }

    public interface CoinRedPackagePayDialogListener {
        void onPaySuccess(CoinPaySheet sheet, int redPackageId);
    }

    public static class Builder {
        private final AppCompatActivity activity;

        private int payType;
        private int payGoodsId;
        private String payGoodsName;

        private int userId;
        private int coinNumber;
        private String desc;

        private CoinPayDialogListener coinPayDialogListener;

        private CoinRedPackagePayDialogListener coinRedPackagePayDialogListener;
        private boolean autoPay = false;

        private UserdetailUnlockListener userdetailUnlockListener;

        public Builder(AppCompatActivity activity) {
            this.activity = activity;
        }

        public Builder setPayParams(int payType, int payGoodsId, String payGoodsName, boolean autoPay, CoinPayDialogListener coinPayDialogListener) {
            this.autoPay = autoPay;
            this.payType = payType;
            this.payGoodsId = payGoodsId;
            this.payGoodsName = payGoodsName;
            this.coinPayDialogListener = coinPayDialogListener;
            return this;
        }

        public Builder setPayParams(int payType, int payGoodsId, String payGoodsName, boolean autoPay, UserdetailUnlockListener userdetailUnlockListener) {
            this.autoPay = autoPay;
            this.payType = payType;
            this.payGoodsId = payGoodsId;
            this.payGoodsName = payGoodsName;
            this.userdetailUnlockListener = userdetailUnlockListener;
            return this;
        }

        public Builder setPayRedPackageParams(int userId, int coinNumber, String desc, String payGoodsName, CoinRedPackagePayDialogListener coinRedPackagePayDialogListener) {
            this.payType = PAY_TYPE_COIN_RED_PACKAGE;
            this.userId = userId;
            this.coinNumber = coinNumber;
            this.desc = desc;
            this.payGoodsName = payGoodsName;
            this.coinRedPackagePayDialogListener = coinRedPackagePayDialogListener;
            return this;
        }

        public CoinPaySheet build() {
            return new CoinPaySheet(this);
        }
    }

    private static class CoinPaySheetView extends BasePopupWindow implements View.OnClickListener {

        private final AppCompatActivity mActivity;
        private final CoinPaySheet bottomSheet;
        private View mPopView;
        private TextView tvBalance;
        private ImageView ivRefresh;
        private TextView tvPayName;
        private TextView tvPayPrice;
        private Button btnPay;
        private Button btnRecharge;
        private ViewGroup loadingView;
        private int mBalance = -1;

        private CreateOrderEntity mOrderEntity = null;

        public CoinPaySheetView(AppCompatActivity activity, CoinPaySheet bottomSheet) {
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
            mPopView = inflater.inflate(R.layout.view_coin_pay_sheet, null);

            tvBalance = mPopView.findViewById(R.id.tv_balance);
            ivRefresh = mPopView.findViewById(R.id.iv_refresh);
            tvPayName = mPopView.findViewById(R.id.tv_pay_name);
            tvPayPrice = mPopView.findViewById(R.id.tv_pay_price);
            btnPay = mPopView.findViewById(R.id.btn_pay);
            btnRecharge = mPopView.findViewById(R.id.btn_recharge);
            loadingView = mPopView.findViewById(R.id.rl_loading);

            ivRefresh.setOnClickListener(this);
            btnPay.setOnClickListener(this);
            btnRecharge.setOnClickListener(this);
            if (!StringUtils.isEmpty(bottomSheet.builder.payGoodsName)) {
                tvPayName.setText(bottomSheet.builder.payGoodsName);
            }
            loadBalance();
            if (bottomSheet.builder.payType == PAY_TYPE_COIN_RED_PACKAGE) {
                tvPayPrice.setText(String.valueOf(bottomSheet.builder.coinNumber));
            } else {
                createOrder();
            }
        }

        /**
         * 设置窗口的相关属性
         */
        @SuppressLint("InlinedApi")
        private void setPopupWindow() {
            this.setContentView(mPopView);// 设置View
            this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
            this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
            this.setFocusable(true);// 设置弹出窗口可
            this.setAnimationStyle(R.style.popup_window_anim);// 设置动画
            this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
            mPopView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    try {
                        int height = mPopView.findViewById(R.id.pop_container).getTop();
                        int y = (int) event.getY();
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (y < height) {
                                dismiss();
                                if (bottomSheet.builder.userdetailUnlockListener != null) {
                                    bottomSheet.builder.userdetailUnlockListener.onPauError();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
        }

        @Override
        public void dismiss() {
//            if (mActivity != null && !mActivity.isFinishing()) {
//                Window dialogWindow = mActivity.getWindow();
//                WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//                dialogWindow.setAttributes(lp);
//                if(bottomSheet.builder.userdetailUnlockListener!=null){
//                    bottomSheet.builder.userdetailUnlockListener.onPauError();
//                }
//            }
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
            if (view.getId() == R.id.iv_refresh) {
                loadBalance();
            } else if (view.getId() == R.id.btn_pay) {
                pay();
            } else if (view.getId() == R.id.btn_recharge) {
                showRecharge();
            }
        }

        private void pay() {
            if (bottomSheet.builder.payType == PAY_TYPE_COIN_RED_PACKAGE) {
                payCoinRedPackage();
            } else {
                payOrder();
            }
        }
        //调集充值按钮
        private void showRecharge() {
            this.dismiss();
            CoinRechargeSheetView coinRechargeSheetView = new CoinRechargeSheetView(mActivity);
            coinRechargeSheetView.show();
            //
            //loadBalance();//刷新钻石
            // payorder
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
            Injection.provideDemoRepository().createOrder(bottomSheet.builder.payGoodsId, bottomSheet.builder.payType, 1, null)
                    .doOnSubscribe(this)
                    .compose(RxUtils.schedulersTransformer())
                    .compose(RxUtils.exceptionTransformer())
                    .subscribe(new BaseObserver<BaseDataResponse<CreateOrderEntity>>() {
                        @Override
                        public void onSuccess(BaseDataResponse<CreateOrderEntity> response) {
                            mOrderEntity = response.getData();
                            tvPayPrice.setText(String.valueOf(mOrderEntity.getMoney()));
                            autoPay();
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
                Log.d(CoinPaySheet.class.getSimpleName(), "mBalance:" + mBalance + ", mOrderEntity money:" + mOrderEntity.getMoney());
                loadingView.postDelayed(this::pay, 100);
            }
        }

        private void payOrder() {
            if (mOrderEntity == null) {
                ToastUtils.showShort(R.string.playfun_order_no_exist);
                return;
            }
            if (mBalance < mOrderEntity.getMoney()) {
                MVDialog.getInstance(mActivity)
                        .setTitele(StringUtils.getString(R.string.playfun_insufficient_diamonds))
                        .setContent(StringUtils.getString(R.string.playfun_top_up_to_pay))
                        .setConfirmText(StringUtils.getString(R.string.playfun_dialong_coin_rechaege_top_up))
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
                            if (bottomSheet.builder.coinPayDialogListener != null) {
                                bottomSheet.builder.coinPayDialogListener.onPaySuccess(bottomSheet, mOrderEntity.getOrderNumber(), mOrderEntity.getMoney());
                            }
                            if (bottomSheet.builder.userdetailUnlockListener != null) {
                                bottomSheet.builder.userdetailUnlockListener.onPaySuccess(bottomSheet, mOrderEntity.getOrderNumber(), mOrderEntity.getMoney());
                            }

                        }

                        @Override
                        public void onError(RequestException e) {
                            ToastUtils.showShort(e.getMessage());
                            if (bottomSheet.builder.userdetailUnlockListener != null) {
                                bottomSheet.builder.userdetailUnlockListener.onPauError();
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

        private void payCoinRedPackage() {
            if (mBalance < bottomSheet.builder.coinNumber) {
                MVDialog.getInstance(mActivity)
                        .setTitele(StringUtils.getString(R.string.playfun_insufficient_diamonds))
                        .setContent(StringUtils.getString(R.string.playfun_top_up_to_pay))
                        .setConfirmText(StringUtils.getString(R.string.playfun_dialong_coin_rechaege_top_up))
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
            Injection.provideDemoRepository().sendCoinRedPackage(bottomSheet.builder.userId, bottomSheet.builder.coinNumber, bottomSheet.builder.desc)
                    .doOnSubscribe(this)
                    .compose(RxUtils.schedulersTransformer())
                    .compose(RxUtils.exceptionTransformer())
                    .subscribe(new BaseObserver<BaseDataResponse<ChatRedPackageEntity>>() {
                        @Override
                        public void onSuccess(BaseDataResponse<ChatRedPackageEntity> response) {
                            if (bottomSheet.builder.coinRedPackagePayDialogListener != null) {
                                bottomSheet.builder.coinRedPackagePayDialogListener.onPaySuccess(bottomSheet, response.getData().getId());
                            }
                        }

                        @Override
                        public void onError(RequestException e) {
                            ToastUtils.showShort(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            super.onComplete();
                            loadingView.setVisibility(View.GONE);
                        }

                    });
        }

    }

}
