package com.dl.playfun.ui.coinpusher.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.R;
import com.dl.playfun.data.source.http.exception.RequestException;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseDataResponse;
import com.dl.playfun.databinding.DialogCoinpusherListBinding;
import com.dl.playfun.entity.CoinPusherBalanceDataEntity;
import com.dl.playfun.entity.CoinPusherDataInfoEntity;
import com.dl.playfun.entity.CoinPusherRoomDeviceInfo;
import com.dl.playfun.entity.CoinPusherRoomInfoEntity;
import com.dl.playfun.entity.CoinPusherRoomTagInfoEntity;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.ui.base.BaseDialog;
import com.dl.playfun.ui.coinpusher.dialog.adapter.CoinPusherRoomListAdapter;
import com.dl.playfun.ui.coinpusher.dialog.adapter.CoinPusherRoomTagAdapter;
import com.dl.playfun.viewadapter.CustomRefreshHeader;
import com.tencent.qcloud.tuicore.Status;

import java.lang.reflect.Field;
import java.util.List;

import me.goldze.mvvmhabit.binding.viewadapter.recyclerview.LayoutManagers;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Author: 彭石林
 * Time: 2022/8/19 11:16
 * Description: 推币机弹窗选择页面
 */
public class CoinPusherRoomListDialog extends BaseDialog {
    private DialogCoinpusherListBinding binding;
    private final Context mContext;
    private CoinPusherRoomTagAdapter coinPusherRoomTagAdapter;
    private CoinPusherRoomListAdapter coinPusherRoomListAdapter;

    private DialogEventListener dialogEventListener;
    //当前用户金币余额
    private int totalMoney = 0;

    private int SEL_COIN_PUSHER_TAG_IDX = -1;

    private Integer checkedTagId = null;

    private boolean buyErrorDissmiss = false;

    public DialogEventListener getDialogEventListener() {
        return dialogEventListener;
    }

    public void setDialogEventListener(DialogEventListener dialogEventListener) {
        this.dialogEventListener = dialogEventListener;
    }

    public CoinPusherRoomListDialog(Context context) {
        super(context);
        this.mContext = context;
        initView();
        loadData();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_coinpusher_list, null, false);
        //支持LiveData绑定xml，数据改变，UI自动会更新
        binding.setLifecycleOwner(this);
        // 设备等级分类
        LayoutManagers.LayoutManagerFactory rcvTitleLayoutManager= LayoutManagers.linear(LinearLayoutManager.HORIZONTAL,false);
        binding.rcvTitle.setLayoutManager(rcvTitleLayoutManager.create(binding.rcvTitle));
        coinPusherRoomTagAdapter = new CoinPusherRoomTagAdapter();
        coinPusherRoomTagAdapter.setOnItemClickListener(position -> {
            if(SEL_COIN_PUSHER_TAG_IDX !=-1){
                if(SEL_COIN_PUSHER_TAG_IDX !=position){
                    coinPusherRoomTagAdapter.setDefaultSelect(position);
                    SEL_COIN_PUSHER_TAG_IDX = position;
                    checkedTagId = coinPusherRoomTagAdapter.getItemData(SEL_COIN_PUSHER_TAG_IDX).getId();
                }
            }else{
                SEL_COIN_PUSHER_TAG_IDX = position;
                coinPusherRoomTagAdapter.setDefaultSelect(SEL_COIN_PUSHER_TAG_IDX);
                checkedTagId = coinPusherRoomTagAdapter.getItemData(SEL_COIN_PUSHER_TAG_IDX).getId();
            }
            startRefreshDataInfo();
        });
        binding.rcvTitle.setAdapter(coinPusherRoomTagAdapter);
        //表格布局
        LayoutManagers.LayoutManagerFactory layoutManagerFactory = LayoutManagers.grid(2);
        binding.rcvContent.setLayoutManager(layoutManagerFactory.create(binding.rcvContent));
        coinPusherRoomListAdapter = new CoinPusherRoomListAdapter();
        binding.rcvContent.setAdapter(coinPusherRoomListAdapter);
        //点击设备
        coinPusherRoomListAdapter.setOnItemClickListener(position -> {
            CoinPusherRoomDeviceInfo deviceInfo = coinPusherRoomListAdapter.getItemEntity(position);
            if(ObjectUtils.isNotEmpty(deviceInfo)){
                //拨打语音小窗口不允许打电话
                if (Status.mIsShowFloatWindow){
                    ToastUtils.showShort(R.string.audio_in_call);
                    return;
                }
                playingCoinPusherStart(deviceInfo.getRoomId());
            }
        });
        //点击空白页面
        binding.flLayoutEmpty.setOnClickListener(v -> {
            //级别列表为空
            if(binding.rcvTitle.getVisibility() == View.GONE){
                loadData();
            }else {
                startRefreshDataInfo();
            }
        });
        binding.refreshLayout.setRefreshHeader(new CustomRefreshHeader(getContext()));
        binding.refreshLayout.setEnableLoadMore(false);
        binding.refreshLayout.setOnRefreshListener(v->startRefreshDataInfo());

        binding.rlCoin.setOnClickListener(v ->{
            CoinPusherConvertDialog coinPusherConvertDialog = new CoinPusherConvertDialog(getContext());
            coinPusherConvertDialog.setItemConvertListener(new CoinPusherConvertDialog.ItemConvertListener() {
                @Override
                public void convertSuccess(CoinPusherBalanceDataEntity coinPusherDataEntity) {
                        totalMoney = coinPusherDataEntity.getTotalGold();
                        tvTotalMoneyRefresh();
                }

                @Override
                public void buyError() {
                    buyErrorDissmiss = true;
                    coinPusherConvertDialog.dismissHud();
                    coinPusherConvertDialog.dismiss();
                    getDialogEventListener().buyErrorPayView();
                    CoinPusherRoomListDialog.this.dismiss();
                }
            });
            coinPusherConvertDialog.setOnDismissListener(dialog -> {
                if(!buyErrorDissmiss){
                    coinPusherConvertDialog.dismissHud();
                    CoinPusherRoomListDialog.this.show();
                }
            });
            coinPusherConvertDialog.show();
            CoinPusherRoomListDialog.this.dismiss();
        });
    }

    public void show() {
        //设置背景透明,去四个角
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(binding.getRoot());
        //设置宽度充满屏幕
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
        window.setWindowAnimations(R.style.BottomDialog_Animation);
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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


    @Override
    public void showHud() {
        super.showHud();
    }

    @Override
    public void dismissHud() {
        super.dismissHud();
    }

    public void loadData() {
        ConfigManager.getInstance().getAppRepository().qryCoinPusherRoomTagList()
                 .doOnSubscribe(this)
                 .compose(RxUtils.schedulersTransformer())
                 .compose(RxUtils.exceptionTransformer())
                 .subscribe(new BaseObserver<BaseDataResponse<CoinPusherRoomTagInfoEntity>>() {
                     @Override
                     public void onSuccess(BaseDataResponse<CoinPusherRoomTagInfoEntity> coinPusherRoomTagInfoEntityResponse) {
                         CoinPusherRoomTagInfoEntity coinPusherRoomTagInfoEntity = coinPusherRoomTagInfoEntityResponse.getData();
                         if(ObjectUtils.isNotEmpty(coinPusherRoomTagInfoEntity)){
                             List<CoinPusherRoomTagInfoEntity.DeviceTag> deviceTagList = coinPusherRoomTagInfoEntity.getTypeArr();
                             totalMoney = coinPusherRoomTagInfoEntity.getTotalGold();
                             tvTotalMoneyRefresh();
                             if(ObjectUtils.isNotEmpty(deviceTagList)){
                                 coinPusherRoomTagAdapter.setItemData(deviceTagList);
                                 int idx = 0;
                                 for (CoinPusherRoomTagInfoEntity.DeviceTag tagData : deviceTagList) {
                                     if(tagData.getIsRecommend() ==1 ){
                                         checkedTagId = tagData.getId();
                                         break;
                                     }
                                     idx++;
                                 }
                                 coinPusherRoomTagAdapter.setDefaultSelect(idx);
                                 startRefreshDataInfo();
                                 emptyListState(0);
                             }else{
                                 //级别没有数据
                                 emptyListState(1);
                             }
                         }
                     }

                     @Override
                     public void onError(RequestException e) {
                         super.onError(e);
                         //级别没有数据
                         emptyListState(1);
                     }

                     @Override
                     public void onComplete() {
                         super.onComplete();
                     }
                 });
    }
    /**
    * @Desc TODO(根据级别查询设备列表)
    * @author 彭石林
    * @parame [tagId]
    * @return void
    * @Date 2022/9/6
    */
    private void qryCoinPusherRoomList(Integer tagId){
        binding.refreshLayout.autoRefresh();
        ConfigManager.getInstance().getAppRepository().qryCoinPusherRoomList(tagId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<CoinPusherRoomInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CoinPusherRoomInfoEntity> coinPusherRoomInfoEntityResponse) {
                        CoinPusherRoomInfoEntity coinPusherRoomInfoEntity = coinPusherRoomInfoEntityResponse.getData();
                        List<CoinPusherRoomDeviceInfo> deviceInfoList = coinPusherRoomInfoEntity.getList();
                        if(ObjectUtils.isNotEmpty(deviceInfoList)){
                            coinPusherRoomListAdapter.setItemData(deviceInfoList);
                            emptyListState(0);
                        }else{
                            coinPusherRoomListAdapter.setItemData(null);
                            //房间列表没有数据
                            emptyListState(1);
                        }
                    }
                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        //房间列表没有数据
                        emptyListState(1);
                    }
                    @Override
                    public void onComplete() {
                        stopRefreshOrLoadMore();
                    }
                });
    }
    /**
    * @Desc TODO(选择房间)
    * @author 彭石林
    * @parame [roomId]
    * @return void
    * @Date 2022/9/6
    */
    private void playingCoinPusherStart(Integer roomId){
        ConfigManager.getInstance().getAppRepository().playingCoinPusherStart(roomId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<CoinPusherDataInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CoinPusherDataInfoEntity> coinPusherDataInfoEntityResponse) {
                        CoinPusherDataInfoEntity coinPusherDataInfoEntity = coinPusherDataInfoEntityResponse.getData();
                        if(ObjectUtils.isNotEmpty(coinPusherDataInfoEntity) && getDialogEventListener()!=null){
                            getDialogEventListener().startViewing(coinPusherDataInfoEntity);
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });
    }

    private void tvTotalMoneyRefresh(){
        binding.tvTotalMoney.post(() -> binding.tvTotalMoney.setText(totalMoney > 99999 ? "99999+" : totalMoney+""));

    }

    private void startRefreshDataInfo(){
        qryCoinPusherRoomList(checkedTagId);
    }
    private void stopRefreshOrLoadMore(){
        //结束刷新
        binding.refreshLayout.finishRefresh(true);
    }

    private void emptyListState(int state) {
        if(state == 0){//正常
            binding.flLayoutEmpty.setVisibility(View.GONE);
            if(coinPusherRoomTagAdapter.getItemCount() > 0){
                binding.rcvTitle.setVisibility(View.VISIBLE);
            }
            if(coinPusherRoomListAdapter.getItemCount() > 0){
                binding.refreshLayout.setVisibility(View.VISIBLE);
            }

        }else if(state == 1){//级别为空
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.flLayoutEmpty.getLayoutParams();
            if(coinPusherRoomListAdapter.getItemCount() == 0){
                layoutParams.topMargin = binding.rcvTitle.getBottom();
                binding.flLayoutEmpty.setLayoutParams(layoutParams);
                binding.flLayoutEmpty.setVisibility(View.VISIBLE);
                binding.refreshLayout.setVisibility(View.GONE);
                String emptyText = StringUtils.getString(R.string.playfun_coinpusher_room_empty_text);
                SpannableString stringBuilder = new SpannableString(emptyText);
                ForegroundColorSpan whiteSpan = new ForegroundColorSpan(ColorUtils.getColor(R.color.gray_light));
                stringBuilder.setSpan(whiteSpan, 0, emptyText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.tvEmpty.setText(stringBuilder);
            }
            if(coinPusherRoomTagAdapter.getItemCount() == 0){
                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                binding.flLayoutEmpty.setLayoutParams(layoutParams);
                binding.flLayoutEmpty.setVisibility(View.VISIBLE);
                binding.rcvTitle.setVisibility(View.GONE);
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
    }

    public interface DialogEventListener{
        void startViewing(CoinPusherDataInfoEntity coinPusherDataInfoEntity);
        void buyErrorPayView();
    }
}
