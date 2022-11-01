package com.fine.friendlycc.ui.mine.invitewebdetail;

import android.app.Application;

import androidx.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.SystemConfigEntity;
import com.fine.friendlycc.entity.SystemRoleMoneyConfigEntity;
import com.fine.friendlycc.entity.UserDataEntity;
import com.fine.friendlycc.event.UserUpdateVipEvent;
import com.fine.friendlycc.utils.Utils;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public class InviteWebDetailViewModel extends BaseViewModel<AppRepository> {
    private final String TAG = "邀请网页详细处理";
    private final Integer pay_good_day = 7;
    public SingleLiveEvent<String> clickPay = new SingleLiveEvent();

    public String orderNumber = null;

    public InviteWebDetailViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    public void saveInviteCode(String code) {
        model.userInvite(code, 3, null)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        ToastUtils.showShort(R.string.playfun_invite_web_detail_ok);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void createOrder(String playCode) {
        model.freeSevenDay(2, 1)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> {
                    showHUD();
                })
                .subscribe(new BaseObserver<BaseDataResponse<Map<String, String>>>() {

                    @Override
                    public void onSuccess(BaseDataResponse<Map<String, String>> mapBaseDataResponse) {
                        orderNumber = mapBaseDataResponse.getData().get("order_number");
                        clickPay.postValue(playCode);
                    }

                    @Override
                    public void onError(RequestException e) {
                        dismissHUD();
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                        super.onComplete();
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
        if (event.intValue() == BillingClient.BillingResponseCode.OK) {
            UserDataEntity userDataEntity = model.readUserData();
            userDataEntity.setIsVip(1);
            model.saveUserData(userDataEntity);
            SystemConfigEntity systemConfigEntity = model.readSystemConfig();
            SystemRoleMoneyConfigEntity sysManUserConfigEntity = systemConfigEntity.getManUser();
            sysManUserConfigEntity.setSendMessagesNumber(-1);
            systemConfigEntity.setManUser(sysManUserConfigEntity);
            SystemRoleMoneyConfigEntity sysManRealConfigEntity = systemConfigEntity.getManReal();
            sysManRealConfigEntity.setSendMessagesNumber(-1);
            systemConfigEntity.setManReal(sysManRealConfigEntity);
            SystemRoleMoneyConfigEntity sysManVipConfigEntity = systemConfigEntity.getManVip();
            sysManVipConfigEntity.setSendMessagesNumber(-1);
            systemConfigEntity.setManVip(sysManVipConfigEntity);
            model.saveSystemConfig(systemConfigEntity);
        }
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
                        try {
                            RxBus.getDefault().post(new UserUpdateVipEvent(Utils.formatday.format(Utils.addDate(new Date(), pay_good_day)), 1));
                        } catch (Exception e) {

                        }
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