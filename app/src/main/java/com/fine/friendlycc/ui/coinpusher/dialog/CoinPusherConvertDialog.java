package com.fine.friendlycc.ui.coinpusher.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.databinding.DialogCoinpusherConverBinding;
import com.fine.friendlycc.entity.CoinPusherBalanceDataEntity;
import com.fine.friendlycc.entity.CoinPusherConverInfoEntity;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.base.BaseDialog;
import com.fine.friendlycc.ui.coinpusher.dialog.adapter.CoinPusherCapsuleAdapter;
import com.fine.friendlycc.ui.coinpusher.dialog.adapter.CoinPusherConvertAdapter;

import java.lang.reflect.Field;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.binding.viewadapter.recyclerview.LayoutManagers;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Author: 彭石林
 * Time: 2022/8/23 12:22
 * Description: 兑换购买弹窗
 */
public class CoinPusherConvertDialog  extends BaseDialog {

    private final Context mContext;
    private DialogCoinpusherConverBinding binding;
    //宝盒适配器
    private CoinPusherCapsuleAdapter coinPusherCapsuleAdapter;
    //金币兑换砖石
    private CoinPusherConvertAdapter coinPusherConvertAdapter;

    private ItemConvertListener itemConvertListener;

    //兑换弹窗选择明细
    private String convertItemTitle;
    private String convertItemContent;

    public ItemConvertListener getItemConvertListener() {
        return itemConvertListener;
    }

    public void setItemConvertListener(ItemConvertListener itemConvertListener) {
        this.itemConvertListener = itemConvertListener;
    }

    //当前用户金币余额
    private int totalMoney = 0;

    private int SEL_COIN_PUSHER_CAPSULE  = -1;

    public CoinPusherConvertDialog(Context context) {
        super(context);
        this.mContext = context;
        initView();
        onClickListener();
        loadData();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_coinpusher_conver, null, false);
        //支持LiveData绑定xml，数据改变，UI自动会更新
        binding.setLifecycleOwner(this);
        //表格布局
        LayoutManagers.LayoutManagerFactory layoutManagerFactory = LayoutManagers.grid(2);
        binding.rcvCapsule.setLayoutManager(layoutManagerFactory.create(binding.rcvCapsule));
        coinPusherCapsuleAdapter = new CoinPusherCapsuleAdapter();
        binding.rcvCapsule.setAdapter(coinPusherCapsuleAdapter);

        binding.rcvConvert.setLayoutManager(layoutManagerFactory.create(binding.rcvConvert));
        coinPusherConvertAdapter = new CoinPusherConvertAdapter();
        binding.rcvConvert.setAdapter(coinPusherConvertAdapter);

    }
    //点击事件初始化
    private void onClickListener(){
        //关闭当前弹窗
        binding.imgClose.setOnClickListener(v-> dismiss());
        binding.tvTabCapsule.setOnClickListener(v->{
            if(binding.llCapsuleLayout.getVisibility() == View.GONE){
                binding.tvTabConver.setTextColor(ColorUtils.getColor(R.color.play_chat_gray_3));
                binding.tvTabCapsule.setTextColor(ColorUtils.getColor(R.color.black_434));
                binding.llCapsuleLayout.setVisibility(View.VISIBLE);
                binding.llConverLayout.setVisibility(View.GONE);
            }
        });
        binding.tvTabConver.setOnClickListener(v->{
            if(binding.llConverLayout.getVisibility() == View.GONE){
                binding.tvTabCapsule.setTextColor(ColorUtils.getColor(R.color.play_chat_gray_3));
                binding.tvTabConver.setTextColor(ColorUtils.getColor(R.color.black_434));
                binding.llConverLayout.setVisibility(View.VISIBLE);
                binding.llCapsuleLayout.setVisibility(View.GONE);
            }
        });

        coinPusherCapsuleAdapter.setOnItemClickListener(position -> {
            if(SEL_COIN_PUSHER_CAPSULE!=position){
                coinPusherCapsuleAdapter.setDefaultSelect(position);
                SEL_COIN_PUSHER_CAPSULE = position;
            }
            //选择购买宝盒明细弹窗
            CoinPusherConvertCapsuleDialog pusherConvertCapsuleDialog = new CoinPusherConvertCapsuleDialog(getContext(),coinPusherCapsuleAdapter.getItemData(SEL_COIN_PUSHER_CAPSULE).getId(),convertItemTitle,convertItemContent,coinPusherCapsuleAdapter.getItemData(SEL_COIN_PUSHER_CAPSULE).getItem());
            pusherConvertCapsuleDialog.setItemConvertListener(new CoinPusherConvertCapsuleDialog.ItemConvertListener() {
                @Override
                public void success(CoinPusherBalanceDataEntity coinPusherDataEntity) {
                        ToastUtils.showShort(R.string.playfun_coinpusher_text_5);
                        //购买成功数据相加
                        totalMoney = coinPusherDataEntity.getTotalGold();
                        tvTotalMoneyRefresh();
                        coinPusherConvertAdapter.setMaxValuerSelect(totalMoney);
                        if(getItemConvertListener()!=null){
                            getItemConvertListener().convertSuccess(coinPusherDataEntity);
                        }
                        pusherConvertCapsuleDialog.dismiss();
                }

                @Override
                public void buyError() {
                    pusherConvertCapsuleDialog.dismiss();
                    if(getItemConvertListener()!=null){
                        getItemConvertListener().buyError();
                    }
                }
            });
            pusherConvertCapsuleDialog.show();
        });
        coinPusherConvertAdapter.setOnItemClickListener(position -> {
                if(SEL_COIN_PUSHER_CAPSULE!=position){
                    coinPusherConvertAdapter.setDefaultSelect(position);
                    SEL_COIN_PUSHER_CAPSULE = position;
                    if(totalMoney >= coinPusherConvertAdapter.getItemData(position).getGoldValue()){
                        binding.tvSubConvert.setTextColor(ColorUtils.getColor(R.color.black));
                        binding.tvSubConvert.setEnabled(true);
                    }else{
                        binding.tvSubConvert.setEnabled(false);
                        binding.tvSubConvert.setTextColor(ColorUtils.getColor(R.color.gray_light));
                    }
                }
        });
        //购买按钮
        binding.tvSubConvert.setOnClickListener(v -> convertCoinPusherDiamonds(coinPusherConvertAdapter.getItemData(SEL_COIN_PUSHER_CAPSULE).getId()));
        //空白页面刷新
        binding.flLayoutEmpty.setOnClickListener(v -> loadData());
    }
    //监听dialog隐藏
    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
    }

    public void show() {
        //设置背景透明,去四个角
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(binding.getRoot());
        //设置宽度充满屏幕
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
        window.setWindowAnimations(R.style.NullAnimationDialog);
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setStatusBarColor(Color.TRANSPARENT);
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

    public void loadData(){
        ConfigManager.getInstance().getAppRepository().qryCoinPusherConverList()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHud())
                .subscribe(new BaseObserver<BaseDataResponse<CoinPusherConverInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CoinPusherConverInfoEntity> converInfoEntityResponse) {
                        CoinPusherConverInfoEntity coinPusherConvertInfo = converInfoEntityResponse.getData();
                        if(ObjectUtils.isNotEmpty(coinPusherConvertInfo)){
                            //给宝盒列表数据-展示
                            if(ObjectUtils.isNotEmpty(coinPusherConvertInfo.getGoldCoinList())){
                                coinPusherCapsuleAdapter.setItemData(coinPusherConvertInfo.getGoldCoinList());
                                totalMoney = coinPusherConvertInfo.getTotalGold();
                                binding.tvCapsuleHint.setText(coinPusherConvertInfo.getGoldTips());
                                tvTotalMoneyRefresh();
                            }
                            if(!StringUtils.isEmpty(coinPusherConvertInfo.getDiamondsTips())){
                                binding.tvConverTitle.setText(coinPusherConvertInfo.getDiamondsTips());
                            }
                            convertItemTitle = coinPusherConvertInfo.getExchangeTips();
                            convertItemContent = coinPusherConvertInfo.getExchangeSubtitle();

                            //金币兑换砖石
                            if(ObjectUtils.isNotEmpty(coinPusherConvertInfo.getDiamondsList())){
                                coinPusherConvertAdapter.setItemData(coinPusherConvertInfo.getDiamondsList(),totalMoney);
                            }
                            emptyListState(0);
                        }else{
                            emptyListState(1);
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        emptyListState(1);
                    }

                    @Override
                    public void onComplete() {
                        dismissHud();
                    }
                });

    }

    @Override
    public void showHud() {
        if(isShowing()){
            super.showHud();
        }
    }

    @Override
    public void dismissHud() {
        super.dismissHud();
    }

    private void tvTotalMoneyRefresh(){
        String val = totalMoney > 99999 ? "99999+" : totalMoney+"";
        String format = String.format(StringUtils.getString(R.string.playfun_coinpusher_text_4),val);
        binding.tvConverDetail.setText(Html.fromHtml(format));
    }
    //金币兑换砖石礼包
    private void convertCoinPusherDiamonds(final Integer id){
        Observable<BaseDataResponse<CoinPusherBalanceDataEntity>> convertCoinPusherDiamonds= ConfigManager.getInstance().getAppRepository().convertCoinPusherDiamonds(id);
        ConfigManager.getInstance().getAppRepository().convertCoinPusherDiamonds(id)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHud())
                .subscribe(new BaseObserver<BaseDataResponse<CoinPusherBalanceDataEntity>>(){

                    @Override
                    public void onSuccess(BaseDataResponse<CoinPusherBalanceDataEntity> coinPusherDataEntityResponse) {
                        CoinPusherBalanceDataEntity coinPusherDataEntity = coinPusherDataEntityResponse.getData();
                        if(ObjectUtils.isNotEmpty(coinPusherDataEntity)){
                            totalMoney = coinPusherDataEntity.getTotalGold();
                            coinPusherConvertAdapter.setMaxValuerSelect(totalMoney);
                            tvTotalMoneyRefresh();
                        }
                        //弹出兑换成功
                        ToastUtils.showShort(R.string.playfun_coinpusher_text_6);
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                    }
                    @Override
                    public void onComplete() {
                        dismissHud();
                    }
                });
    }

    private void emptyListState(int state) {
        if(state == 0){//正常
            binding.flLayoutEmpty.setVisibility(View.GONE);
        }else if(state == 1){//级别为空
                binding.flLayoutEmpty.setVisibility(View.VISIBLE);
                String emptyText = StringUtils.getString(R.string.playfun_coinpusher_error_text1);
                String refreshText = StringUtils.getString(R.string.playfun_refresh);
                SpannableString stringBuilder = new SpannableString(emptyText + refreshText);
                ForegroundColorSpan whiteSpan = new ForegroundColorSpan(ColorUtils.getColor(R.color.gray_light));
                ForegroundColorSpan redSpan = new ForegroundColorSpan(ColorUtils.getColor(R.color.purple_text));
                stringBuilder.setSpan(whiteSpan, 0, emptyText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.setSpan(redSpan, stringBuilder.length()-refreshText.length(), stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                //添加下划线
                stringBuilder.setSpan(new UnderlineSpan(), stringBuilder.length()-refreshText.length(), stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.tvEmpty.setText(stringBuilder);
        }
    }

    public interface ItemConvertListener{
        void convertSuccess(CoinPusherBalanceDataEntity coinPusherDataEntity);
        default void buyError() {

        }
    }
}
