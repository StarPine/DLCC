package com.fine.friendlycc.ui.coinpusher.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.databinding.DialogCoinpusherConverDetailBinding;
import com.fine.friendlycc.entity.CoinPusherConverInfoEntity;
import com.fine.friendlycc.entity.CoinPusherBalanceDataEntity;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.base.BaseDialog;
import com.fine.friendlycc.ui.coinpusher.dialog.adapter.CoinPusherCapsuleADetailAdapter;
import com.misterp.toast.SnackUtils;

import java.lang.reflect.Field;
import java.util.List;

import me.goldze.mvvmhabit.binding.viewadapter.recyclerview.LayoutManagers;
import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * Author: 彭石林
 * Time: 2022/8/25 14:35
 * Description: 兑换选择宝盒明细
 */
public class CoinPusherConvertCapsuleDialog extends BaseDialog {

    private final Context mContext;

    private DialogCoinpusherConverDetailBinding binding;
    //宝盒适配器
    private CoinPusherCapsuleADetailAdapter coinPusherCapsuleADetailAdapter;

    private int SEL_COIN_PUSHER_CAPSULE  = 0;

    private ItemConvertListener itemConvertListener;

    //兑换弹窗选择明细
    private String convertItemTitle;
    private String convertItemContent;
    private Integer convertConfigId;

    public ItemConvertListener getItemConvertListener() {
        return itemConvertListener;
    }

    public void setItemConvertListener(ItemConvertListener itemConvertListener) {
        this.itemConvertListener = itemConvertListener;
    }

    public CoinPusherConvertCapsuleDialog(Context context,Integer convertConfigId,String convertItemTitle,String convertItemContent, List<CoinPusherConverInfoEntity.GoldCoinInfo.GoldCoinItem> itemData) {
        super(context);
        this.mContext = context;
        this.convertItemTitle = convertItemTitle;
        this.convertItemContent = convertItemContent;
        this.convertConfigId = convertConfigId;
        initView();
        coinPusherCapsuleADetailAdapter.setItemData(itemData);
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_coinpusher_conver_detail, null, false);
        binding.imgClose.setOnClickListener(v -> dismiss());
        //支持LiveData绑定xml，数据改变，UI自动会更新
        binding.setLifecycleOwner(this);
        binding.tvTitle.setText(convertItemTitle);
        binding.tvTitle2.setText(convertItemContent);
        //表格布局
        LayoutManagers.LayoutManagerFactory layoutManagerFactory = LayoutManagers.grid(3);
        binding.rcvList.setLayoutManager(layoutManagerFactory.create(binding.rcvList));
        coinPusherCapsuleADetailAdapter = new CoinPusherCapsuleADetailAdapter();
        binding.rcvList.setAdapter(coinPusherCapsuleADetailAdapter);

        coinPusherCapsuleADetailAdapter.setOnItemClickListener(position -> {
            if(SEL_COIN_PUSHER_CAPSULE!=position){
                coinPusherCapsuleADetailAdapter.setDefaultSelect(position);
                SEL_COIN_PUSHER_CAPSULE = position;
            }
        });
        binding.tvSub.setOnClickListener(v -> {
            if(SEL_COIN_PUSHER_CAPSULE!=-1){
                CoinPusherConverInfoEntity.GoldCoinInfo.GoldCoinItem itemEntity = coinPusherCapsuleADetailAdapter.getItemData(SEL_COIN_PUSHER_CAPSULE);
                if(itemEntity!=null){
                    convertCoinPusherGoldsCoin(convertConfigId,itemEntity.getValue(),itemEntity.getType());
                }
            }
        });
    }


    public void show() {
        //设置背景透明,去四个角
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(binding.getRoot());
        //设置宽度充满屏幕
        Window window = getWindow();
        window.setGravity(Gravity.CENTER); //可设置dialog的位置
        window.setWindowAnimations(R.style.NullAnimationDialog);
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // 解决 状态栏变色的bug
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    @SuppressLint("PrivateApi")
                    Class<?> decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                    Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                    field.setAccessible(true);
                    // 去掉高版本蒙层改为透明
                    field.setInt(window.getDecorView(), Color.TRANSPARENT);
                } catch (Exception ignored) {
                }
            }
        }
        super.show();
    }
    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void showHud() {
        super.showHud();
    }

    @Override
    public void dismissHud() {
        super.dismissHud();
    }

    //购买宝盒
    private void convertCoinPusherGoldsCoin(final Integer id,final Integer amount,Integer type){
        ConfigManager.getInstance().getAppRepository()
                .convertCoinPusherGoldsCoin(id,type)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHud())
                .subscribe(new BaseObserver<BaseDataResponse<CoinPusherBalanceDataEntity>>(){
                    @Override
                    public void onSuccess(BaseDataResponse<CoinPusherBalanceDataEntity> coinPusherDataEntityBaseDataResponse) {
                        CoinPusherBalanceDataEntity coinPusherBalanceDataEntity = coinPusherDataEntityBaseDataResponse.getData();
                        if(coinPusherBalanceDataEntity !=null && getItemConvertListener()!=null){
                            getItemConvertListener().success(coinPusherBalanceDataEntity);
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        if(e.getCode()!=null && e.getCode().intValue()==21001 ){//钻石余额不足
                            SnackUtils.showCenterShort(binding.getRoot(),StringUtils.getString(R.string.playcc_dialog_exchange_integral_total_text1));
                            if(getItemConvertListener()!=null){
                                getItemConvertListener().buyError();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHud();
                    }

                });
    }

    public interface ItemConvertListener {
        void success(CoinPusherBalanceDataEntity coinPusherBalanceDataEntity);

        default void buyError() {

        }
    }
}
