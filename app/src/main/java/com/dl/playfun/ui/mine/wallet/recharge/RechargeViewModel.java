package com.dl.playfun.ui.mine.wallet.recharge;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.android.billingclient.api.BillingClient;
import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.R;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.exception.RequestException;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseDataResponse;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.entity.CreateOrderEntity;
import com.dl.playfun.entity.GoodsEntity;
import com.dl.playfun.entity.NoteInfoEntity;
import com.dl.playfun.entity.SystemConfigEntity;
import com.dl.playfun.entity.SystemRoleMoneyConfigEntity;
import com.dl.playfun.entity.UserDataEntity;
import com.dl.playfun.event.UserUpdateVipEvent;
import com.dl.playfun.ui.mine.wallet.coin.CoinFragment;
import com.dl.playfun.utils.Utils;
import com.dl.playfun.viewmodel.BaseViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.Map;

import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @Name： PlayFun_Google
 * @Description：
 * @Author： liaosf
 * @Date： 2022/6/15 18:23
 * 修改备注：
 */
public class RechargeViewModel extends BaseViewModel<AppRepository> {

    public ObservableField<Boolean> isGooglepay = new ObservableField<>(false);
    public ObservableField<GoodsEntity> goodsEntity = new ObservableField<>();
    public String orderNumber = null;
    public SingleLiveEvent<String> clickPay = new SingleLiveEvent();
    public SingleLiveEvent<GoodsEntity> finsh = new SingleLiveEvent();

    /**
     * 选择水晶支付
     */
    public BindingCommand crystalPayOnClick = new BindingCommand(() -> {
        isGooglepay.set(false);
    });

    /**
     * 选择Google支付
     */
    public BindingCommand googlePayOnClick = new BindingCommand(() -> {
        isGooglepay.set(true);
    });

    /**
     * 确认支付
     */
    public BindingCommand confirmPayOnClick = new BindingCommand(() -> {
        createOrder();
    });

    public RechargeViewModel(@NonNull @NotNull Application application, AppRepository model) {
        super(application, model);
    }

    public String getPriceText(){
        return goodsEntity.get().getSymbol() + goodsEntity.get().getSalePrice();
    }

    public void createOrder() {
        model.createOrder(goodsEntity.get().getId(), 1, 2, null)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<CreateOrderEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CreateOrderEntity> response) {
                        orderNumber = response.getData().getOrderNumber();
                        clickPay.postValue(goodsEntity.get().getGoogleGoodsId());
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
                        ToastUtils.showShort(StringUtils.getString(R.string.playfun_pay_success));
                        finsh.postValue(goodsEntity.get());
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

}
