package com.fine.friendlycc.widget.coinrechargesheet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.api.AppGameConfig;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.CoinExchangeBoxInfo;
import com.fine.friendlycc.entity.CoinExchangePriceInfo;
import com.fine.friendlycc.entity.GameCoinBuy;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.widget.dialog.MVDialog;
import com.fine.friendlycc.R;
import com.fine.friendlycc.ui.base.BasePopupWindow;
import com.fine.friendlycc.ui.dialog.adapter.GameCoinExchargeAdapter;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author KL
 */
public class GameCoinExchargeSheetView extends BasePopupWindow implements View.OnClickListener, GameCoinExchargeAdapter.CoinRechargeAdapterListener{
    public static final String TAG = "GameCoinExchargeS...";

    private final AppCompatActivity mActivity;
    private View mPopView;
    private RecyclerView recyclerView;
    private TextView tvGameTotal;
    private TextView tvJmTotal;
    private ImageView ivRefresh;
    private ViewGroup loadingView;
    private GameCoinExchargeAdapter adapter;
    private CoinExchangeBoxInfo mBoxInfo = null;
    //游戏货币图标
    private ImageView imgGameCoin;

    private CoinRechargeSheetViewListener coinRechargeSheetViewListener;

    private Disposable mSubscription;
    //剩余钻石
    private int maleBalance = 0;
    //是否是语音视频通话
    private boolean isCallMedia = false;

    public int getMaleBalance() {
        return maleBalance;
    }

    public void setMaleBalance(int maleBalance) {
        this.maleBalance = maleBalance;
    }

    public boolean isCallMedia() {
        return isCallMedia;
    }

    public void setCallMedia(boolean callMedia) {
        isCallMedia = callMedia;
    }

    //    private GoodsEntity sel_goodsEntity;

    public GameCoinExchargeSheetView(AppCompatActivity activity) {
        super(activity);
        this.mActivity = activity;
        init(activity);
        setPopupWindow();
    }

    public CoinRechargeSheetViewListener getCoinRechargeSheetViewListener() {
        return coinRechargeSheetViewListener;
    }

    public void setCoinRechargeSheetViewListener(CoinRechargeSheetViewListener coinRechargeSheetViewListener) {
        this.coinRechargeSheetViewListener = coinRechargeSheetViewListener;
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        mPopView = inflater.inflate(R.layout.view_coin_exchange_sheet, null);
        imgGameCoin = mPopView.findViewById(R.id.img_game_coin);
        //设置游戏货币图标。根据用户传递
        AppGameConfig appGameConfig = ConfigManager.getInstance().getAppRepository().readGameConfigSetting();
        if(!ObjectUtils.isEmpty(appGameConfig)){
            if(appGameConfig.getGamePlayCoinSmallImg()!=0){
                imgGameCoin.setImageResource(appGameConfig.getGamePlayCoinSmallImg());
            }
        }
        recyclerView = mPopView.findViewById(R.id.recycler_view);
        tvGameTotal = mPopView.findViewById(R.id.tv_game_coin_total);
        tvJmTotal = mPopView.findViewById(R.id.tv_jm_coin_total);
        ivRefresh = mPopView.findViewById(R.id.iv_refresh);
        loadingView = mPopView.findViewById(R.id.rl_loading);
        ivRefresh.setOnClickListener(this);

        adapter = new GameCoinExchargeAdapter(recyclerView);
        adapter.setCoinRechargeAdapterListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        maleBalance = 0;
        isCallMedia = false;
        //查询商品价格
        loadCoinExchangeBoxInfo();
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
                int height = mPopView.findViewById(R.id.pop_container).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void dismiss() {
        if (mSubscription != null) {
            mSubscription.dispose();
        }
        if (mActivity != null && !mActivity.isFinishing()) {
            Window dialogWindow = mActivity.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.alpha = 1.0f;
            dialogWindow.setAttributes(lp);
        }

        coinRechargeSheetViewListener = null;
        super.dismiss();
    }

    public void show() {
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

    private void loading(boolean loading){
        loadingView.setVisibility(loading? View.VISIBLE : View.GONE);
    }


    private void loadCoinExchangeBoxInfo() {
        loading(true);
         ConfigManager.getInstance().getAppRepository()
                .getCoinExchangeBoxInfo()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<CoinExchangeBoxInfo>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CoinExchangeBoxInfo> response) {
                        loadingView.setVisibility(View.GONE);
                        mBoxInfo = response.getData();
                        adapter.setData(mBoxInfo.getPriceList());
                        tvGameTotal.setText(String.valueOf(mBoxInfo.getTotalAppCoins()));
                        int totalCoin = mBoxInfo.getTotalCoins().intValue();
                        if (isCallMedia) {
                            totalCoin = maleBalance;
                        }
                        tvJmTotal.setText(String.valueOf(totalCoin >= 0 ? totalCoin : 0));
                        //tvJmTotal.setText(String.valueOf(mBoxInfo.getTotalCoins()));
                    }

                    @Override
                    public void onComplete() {
                        loading(false);
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_refresh) {
            loadCoinExchangeBoxInfo();
        }
    }

    @Override
    public void onBuyClick(View view, int position) {
        if(mBoxInfo == null) return;
        CoinExchangePriceInfo exChangeInfo = mBoxInfo.getPriceList().get(position);
        if(mBoxInfo.getTotalAppCoins() < exChangeInfo.getGameCoins()){
            ToastUtils.showShort(R.string.playfun_exchange_coin_not_enough_toast);
            topupGameCoins();
            return;
        }
        MVDialog.getInstance(GameCoinExchargeSheetView.this.mActivity)
                .setTitele(StringUtils.getString(R.string.playfun_recharge_coin_success2))
                .setConfirmText(StringUtils.getString(R.string.playfun_confirm))
                .setConfirmOnlick(dialog -> {
                    dialog.dismiss();
                    exchangeCoins(exChangeInfo);
                    //viewModel.loadDatas(1);
                }).setCancelable(true)
                .chooseType(MVDialog.TypeEnum.CENTER)
                .show();

    }

    private void exchangeCoins(CoinExchangePriceInfo exChangeInfo){
        loading(true);
         ConfigManager.getInstance().getAppRepository()
                .exchangeCoins(exChangeInfo.getId())
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        loadCoinExchangeBoxInfo();
                        if(coinRechargeSheetViewListener != null){
                            coinRechargeSheetViewListener.onPaySuccess(GameCoinExchargeSheetView.this, exChangeInfo);
                        }else{
                            ToastUtils.showShort(R.string.playfun_exchange_success_toast);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        if(coinRechargeSheetViewListener != null){
                            coinRechargeSheetViewListener.onPayFailed(GameCoinExchargeSheetView.this, e.getMessage());
                        }
                        loading(false);
                    }
                });
    }

    private void topupGameCoins(){
        CoinExchargeItegralPayDialog coinRechargeSheetView = new CoinExchargeItegralPayDialog(mActivity,mActivity);
        coinRechargeSheetView.show();
        coinRechargeSheetView.setCoinRechargeSheetViewListener(new CoinExchargeItegralPayDialog.CoinRechargeSheetViewListener() {
            @Override
            public void onPaySuccess(CoinExchargeItegralPayDialog sheetView, GameCoinBuy sel_goodsEntity) {
                sheetView.endGooglePlayConnect();
                sheetView.dismiss();
                loadCoinExchangeBoxInfo();
                MVDialog.getInstance(GameCoinExchargeSheetView.this.mActivity)
                        .setTitele(StringUtils.getString(R.string.playfun_recharge_coin_success))
                        .setConfirmText(StringUtils.getString(R.string.playfun_confirm))
                        .setConfirmOnlick(dialog -> {
                            dialog.dismiss();
                            //viewModel.loadDatas(1);
                        })
                        .chooseType(MVDialog.TypeEnum.CENTER)
                        .show();
            }

            @Override
            public void onPayFailed(CoinExchargeItegralPayDialog sheetView, String msg) {
                sheetView.dismiss();
                ToastUtils.showShort(msg);
                AppContext.instance().logEvent(AppsFlyerEvent.Failed_to_top_up);
            }
        });
    }


    public interface CoinRechargeSheetViewListener {
        void onPaySuccess(GameCoinExchargeSheetView sheetView, CoinExchangePriceInfo exChangeInfo);

        void onPayFailed(GameCoinExchargeSheetView sheetView, String msg);
    }
}