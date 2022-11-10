package com.fine.friendlycc.ui.mine.setting.account;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.R;
import com.fine.friendlycc.app.EaringlSwitchUtil;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.PrivacyEntity;
import com.fine.friendlycc.event.IsAuthBindingEvent;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @ClassName CommunityAccountModel
 * @Description TODO
 * @Author 彭石林
 * @Date 2021/4/29 11:06
 * @Phone 16620350375
 * @email 15616314565@163.com
 * @Version 1.0
 **/
public class CommunityAccountModel extends BaseViewModel<AppRepository> {


    public ObservableField<PrivacyEntity> privacyEntity = new ObservableField<>(new PrivacyEntity());

    public ObservableField<Boolean> deleteAccountFlag = new ObservableField<>(false);

    public UIChangeObservable UC = new UIChangeObservable();

    public CommunityAccountModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }


    public BindingCommand toCancellView = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            start(CommunityAccountCancellFragment.class.getCanonicalName());
        }
    });

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        getPrivacy();
        //注销账号开关
        Integer AccountFlag = model.readSwitches(EaringlSwitchUtil.KEY_DELETE_ACCOUNT);
        if (AccountFlag != null && AccountFlag.intValue() == 1) {
            deleteAccountFlag.set(true);
        }
    }

    public void bindAccount(String id, String type) {
        id += type;
        model.bindAccount(id, type).doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> {
                    showHUD();
                }).subscribe(new BaseObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                ToastUtils.showShort(R.string.playcc_binding_auth_success);
                RxBus.getDefault().post(new IsAuthBindingEvent());
                dismissHUD();
                pop();
            }

            @Override
            public void onComplete() {
                dismissHUD();
                super.onComplete();
            }
        });
    }

    /**
     * 获取我的隐私
     */
    private void getPrivacy() {
        model.getPrivacy()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<PrivacyEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<PrivacyEntity> response) {
                        privacyEntity.set(response.getData());
                    }
                });
    }

    public class UIChangeObservable {
        SingleLiveEvent<Boolean> loadUserFlag = new SingleLiveEvent<>();
        SingleLiveEvent<Boolean> loginAuth = new SingleLiveEvent<>();
    }
}