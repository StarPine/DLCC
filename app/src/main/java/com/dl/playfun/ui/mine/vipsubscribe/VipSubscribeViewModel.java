package com.dl.playfun.ui.mine.vipsubscribe;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.app.AppsFlyerEvent;
import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.exception.RequestException;
import com.dl.playfun.data.source.http.observer.BaseEmptyObserver;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseDataResponse;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.entity.CreateOrderEntity;
import com.dl.playfun.entity.LocalGooglePayCache;
import com.dl.playfun.entity.SystemConfigEntity;
import com.dl.playfun.entity.SystemRoleMoneyConfigEntity;
import com.dl.playfun.entity.TaskAdEntity;
import com.dl.playfun.entity.UserDataEntity;
import com.dl.playfun.entity.VipInfoEntity;
import com.dl.playfun.entity.VipPackageItemEntity;
import com.dl.playfun.event.UserUpdateVipEvent;
import com.dl.playfun.utils.Utils;
import com.dl.playfun.viewmodel.BaseViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author wulei
 */
public class VipSubscribeViewModel extends BaseViewModel<AppRepository> {
    public static final String TAG = "VipSubscribeViewModel";

    //福袋
    public ObservableField<List<TaskAdEntity>> adItemVipEntityList = new ObservableField<>(new ArrayList<>());

    private final int consumeImmediately = 0;
    private final int consumeDelay = 1;

    public List<VipPackageItemEntity> vipPackageItemEntityList = new ArrayList<>();

    //订阅相关配置
    public BindingRecyclerViewAdapter<VipSubscribeItemViewModel> vipSubscribeAdapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<VipSubscribeItemViewModel> vipSubscribeList = new ObservableArrayList<>();
    public ItemBinding<VipSubscribeItemViewModel> itemVipSubscribe = ItemBinding.of(BR.viewModel, R.layout.item_vip_subscribe);

    //特权相关配置
    public BindingRecyclerViewAdapter<VipPrivilegeItemViewModel> vipPrivilegeAdapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<VipPrivilegeItemViewModel> vipPrivilegeList = new ObservableArrayList<>();
    public ItemBinding<VipPrivilegeItemViewModel> itemVipPrivilege = ItemBinding.of(BR.viewModel, R.layout.item_vip_privilege);

    public ObservableField<Integer> selectedPosition = new ObservableField<>(-1);

    public ObservableField<VipPackageItemEntity> checkedVipPackageItemEntity = new ObservableField<>();
    public ObservableField<VipInfoEntity> vipInfoEntity = new ObservableField<>();

    public String orderNumber;
    public Integer pay_good_day = 0;
    UIChangeObservable uc = new UIChangeObservable();
    private Integer ActualValue;
    public BindingCommand confirmOnClickCommand = new BindingCommand(() -> rechargeCreateOrder());

    public VipPackageItemEntity $vipPackageItemEntity;

    public VipSubscribeViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
        UserDataEntity userDataEntity = model.readUserData();
    }

    public void itemClick(int position, VipPackageItemEntity vipPackageItemEntity) {
        for (VipSubscribeItemViewModel vipSubscribeItemViewModel : vipSubscribeList) {
            vipSubscribeItemViewModel.itemEntity.get().setSelected(false);
        }
        $vipPackageItemEntity = vipPackageItemEntity;
        checkedVipPackageItemEntity.set(vipPackageItemEntity);
        AppContext.instance().logEvent("VIP_" + (position + 1));
        pay_good_day = vipSubscribeList.get(position).itemEntity.get().getActualValue();
        vipSubscribeList.get(position).itemEntity.get().setSelected(true);
        selectedPosition.set(position);
        forePrivilegesInfo(vipSubscribeList.get(position).itemEntity.get());
    }

    public void loadPackage() {
        model.vipPackages()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseEmptyObserver<BaseDataResponse<VipInfoEntity>>(this) {

                    @Override
                    public void onSuccess(BaseDataResponse<VipInfoEntity> response) {
                        super.onSuccess(response);
                        vipSubscribeList.clear();
                        VipInfoEntity data = response.getData();
                        vipInfoEntity.set(data);
                        List<VipPackageItemEntity> list = data.getList();
                        vipPackageItemEntityList = list;
                        if (vipPackageItemEntityList != null && vipPackageItemEntityList.size() > 0) {
                            int size = vipPackageItemEntityList.size();
                            for (int i = 0; i < size; i++) {
                                VipPackageItemEntity vipPackage = vipPackageItemEntityList.get(i);
                                VipSubscribeItemViewModel item = new VipSubscribeItemViewModel(VipSubscribeViewModel.this, vipPackage, null);
                                vipSubscribeList.add(item);

                                if (vipPackage.getIsRecommend() == 1) {
                                    vipPackage.setSelected(true);
                                    $vipPackageItemEntity = vipPackage;
                                    pay_good_day = vipPackage.getActualValue();
                                    checkedVipPackageItemEntity.set(vipPackage);
                                    selectedPosition.set(i);
                                    forePrivilegesInfo(vipPackage);
                                }
                            }
                        }

                    }
                });
    }

    private void forePrivilegesInfo(VipPackageItemEntity vipPackage) {
        vipPrivilegeList.clear();
        if (vipPackage.getPrivileges() != null && vipPackage.getPrivileges().size() > 0) {
            for (VipPackageItemEntity.PrivilegesBean privilege : vipPackage.getPrivileges()) {
                VipPrivilegeItemViewModel privilegeItemViewModel = new VipPrivilegeItemViewModel(VipSubscribeViewModel.this, privilege);
                vipPrivilegeList.add(privilegeItemViewModel);
            }
        }
    }

    private void createOrder(int goodsId, String payCode) {
        model.createOrder(goodsId, 2, 2, null)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> {
                    showHUD();
                })
                .subscribe(new BaseObserver<BaseDataResponse<CreateOrderEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CreateOrderEntity> response) {
                        dismissHUD();
                        pay_good_day = response.getData().getActual_value();
                        ActualValue = response.getData().getActual_value();
                        orderNumber = response.getData().getOrderNumber();
                        uc.clickPay.postValue(payCode);
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

    private void rechargeCreateOrder() {
        if (selectedPosition.get() < 0) {
            ToastUtils.showShort(R.string.playfun_please_choose_top_up_package);
            return;
        }
        try {
            AppContext.instance().logEvent(AppsFlyerEvent.Get_vip);
            //调整取google的商品id为后天返回类
            String payCode = vipSubscribeList.get(selectedPosition.get()).itemEntity.get().getGoogleGoodsId();
            // Log.e("当前vip订阅代码",payCode+"====================");
            createOrder(vipSubscribeList.get(selectedPosition.get()).itemEntity.get().getId(), payCode);
            //uc.clickPay.postValue(payCode);
        } catch (Exception e) {
            AppContext.instance().logEvent(AppsFlyerEvent.vip_google_arouse_error);
            ToastUtils.showShort(e.getMessage());
        }

    }

    public void paySuccessNotify(String packageName, List<String> productId, String token, Integer event) {
        if (event == 0) {
            UserDataEntity userDataEntity = model.readUserData();
            userDataEntity.setIsVip(1);
            userDataEntity.setEndTime(Utils.formatday.format(Utils.addDate(new Date(), pay_good_day == null ? 0 : pay_good_day)));
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
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> {
                    showHUD();
                })
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        if (event.intValue() == 0) {
                            ToastUtils.showShort(StringUtils.getString(R.string.playfun_pay_success));
                            model.clearGooglePayCache();
                            Integer sucb = uc.successBack.getValue();
                            if (sucb != null) {
                                sucb += 1;
                            } else {
                                sucb = 1;
                            }
                            uc.successBack.postValue(sucb);
                            RxBus.getDefault().post(new UserUpdateVipEvent(Utils.formatday.format(Utils.addDate(new Date(), pay_good_day)), 1));
                        }

                        //pop();
                        //loadProfile();
                    }

                    @Override
                    public void onError(RequestException e) {
                        if (event.intValue() != 0) {
                            return;
                        }
                        LocalGooglePayCache localGooglePayCache = new LocalGooglePayCache();
                        localGooglePayCache.setOrderNumber(orderNumber);
                        localGooglePayCache.setPackageName(packageName);
                        localGooglePayCache.setProductId(productId);
                        localGooglePayCache.setToken(token);
                        localGooglePayCache.setType(2);
                        localGooglePayCache.setUserId(model.readUserData().getId());
                        model.saveGooglePlay(localGooglePayCache);
                        model.paySuccessNotify(localGooglePayCache.getPackageName(), localGooglePayCache.getOrderNumber(), localGooglePayCache.getProductId(), localGooglePayCache.getToken(), 2, event)
                                .doOnSubscribe(VipSubscribeViewModel.this)
                                .compose(RxUtils.schedulersTransformer())
                                .compose(RxUtils.exceptionTransformer())
                                .subscribe(new BaseObserver<BaseResponse>() {
                                    @Override
                                    public void onSuccess(BaseResponse baseResponse) {
                                        model.clearGooglePayCache();
                                    }

                                    @Override
                                    public void onComplete() {
                                        model.clearGooglePayCache();
                                        Integer sucb = uc.successBack.getValue();
                                        if (sucb != null) {
                                            sucb += 1;
                                        } else {
                                            sucb = 1;
                                        }
                                        uc.successBack.postValue(sucb);
                                        RxBus.getDefault().post(new UserUpdateVipEvent(Utils.formatday.format(Utils.addDate(new Date(), pay_good_day)), 1));
                                        //pop();

                                    }
                                });
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public class UIChangeObservable {
        public SingleLiveEvent<String> clickPay = new SingleLiveEvent<>();
        public SingleLiveEvent<Integer> successBack = new SingleLiveEvent<>();
    }


}