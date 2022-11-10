package com.fine.friendlycc.ui.mine.wallet.diamond.recharge;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.CreateOrderEntity;
import com.fine.friendlycc.entity.DiamondInfoEntity;
import com.fine.friendlycc.entity.GoodsEntity;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.mine.vipsubscribe.VipSubscribeFragment;
import com.fine.friendlycc.ui.mine.wallet.coin.CoinFragment;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @Name： PlayCC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/6/15 18:23
 * 修改备注：
 */
public class DiamondRechargeViewModel extends BaseViewModel<AppRepository> {

    public BindingRecyclerViewAdapter<DiamondRechargeItemViewModel> diamondRechargeAdapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<DiamondRechargeItemViewModel> diamondRechargeList = new ObservableArrayList<>();
    public ItemBinding<DiamondRechargeItemViewModel> diamondRechargeItem = ItemBinding.of(BR.viewModel, R.layout.item_diamond_recharge);

    public ObservableField<GoodsEntity> selectedGoodsEntity = new ObservableField<>();
    public ObservableField<DiamondInfoEntity> diamondInfo = new ObservableField<>();
    public String orderNumber = null;
    public int selectedPosition = -1;
    public SingleLiveEvent<String> payOnClick = new SingleLiveEvent();
    public SingleLiveEvent<GoodsEntity> paySuccess = new SingleLiveEvent();

    /**
     * 确认支付
     */
    public BindingCommand confirmPayOnClick = new BindingCommand(() -> {
        createOrder();
    });

    /**
     * 跳转会员中心
     */
    public BindingCommand toVipCenter = new BindingCommand(() -> {
        if (diamondInfo.get()== null) {
            return;
        }
        if (diamondInfo.get().getIsVip() == 0 && isMale()){
            AppContext.instance().logEvent(AppsFlyerEvent.VIP_Center);
            start(VipSubscribeFragment.class.getCanonicalName());
        }
    });

    /**
     * 跳转到钻石明细界面
     */
    public BindingCommand clickCoinMoneyView = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            start(CoinFragment.class.getCanonicalName());
        }
    });

    public DiamondRechargeViewModel(@NonNull @NotNull Application application, AppRepository model) {
        super(application, model);
    }

    public void itemClick(int position, GoodsEntity goodsEntity) {
        for (DiamondRechargeItemViewModel itemViewModel : diamondRechargeList) {
            itemViewModel.itemEntity.get().setSelected(false);
        }
        selectedGoodsEntity.set(goodsEntity);
        AppContext.instance().logEvent(AppsFlyerEvent.Top_up + (position + 1));
        diamondRechargeList.get(position).itemEntity.get().setSelected(true);
        selectedPosition = position;
    }


    public void createOrder() {
        if (selectedPosition < 0) {
            ToastUtils.showShort(R.string.playcc_please_choose_top_up_package);
            return;
        }
        model.createOrder(selectedGoodsEntity.get().getId(), 1, 2, null)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<CreateOrderEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CreateOrderEntity> response) {
                        orderNumber = response.getData().getOrderNumber();
                        payOnClick.postValue(selectedGoodsEntity.get().getGoogleGoodsId());
                    }

                    @Override
                    public void onError(RequestException e) {
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
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
        model.paySuccessNotify(packageName, orderNumber, productId, token, 2, event)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> {
                    showHUD();
                })
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        ToastUtils.showShort(StringUtils.getString(R.string.playcc_pay_success));
                        paySuccess.postValue(selectedGoodsEntity.get());
                    }

                    @Override
                    public void onError(RequestException e) {

                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public boolean isMale() {
        return ConfigManager.getInstance().isMale();
    }

    public String getTotalCoin(DiamondInfoEntity diamondInfoEntity) {
        String total = "";
        if (diamondInfoEntity == null) {
            return total;
        }
        int totalCoin = diamondInfoEntity.getTotalCoin();
        if (totalCoin > 9999999) {
            total = "9999999+";
        } else {
            total = totalCoin + "";
        }
        return total;
    }

    /**
     * 获取钻石充值套餐
     */
    public void getRechargeList() {
        model.goods()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<DiamondInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<DiamondInfoEntity> response) {
                        diamondRechargeList.clear();
                        DiamondInfoEntity infoEntity = response.getData();
                        diamondInfo.set(infoEntity);
                        List<GoodsEntity> data = infoEntity.getList();
                        if (data == null || data.size() <= 0){
                            return;
                        }
                        for (GoodsEntity goodsEntity : data) {
                            DiamondRechargeItemViewModel itemViewModel = new DiamondRechargeItemViewModel(DiamondRechargeViewModel.this, goodsEntity);
                            diamondRechargeList.add(itemViewModel);
                        }
                        //默认选中第一个钻石套餐
                        int type = data.get(0).getType();
                        if (type == 2 && data.size() > 1){
                            data.get(1).setSelected(true);
                            selectedGoodsEntity.set(data.get(1));
                            selectedPosition = 1;
                        }else {
                            data.get(0).setSelected(true);
                            selectedGoodsEntity.set(data.get(0));
                            selectedPosition = 0;
                        }

                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

}
