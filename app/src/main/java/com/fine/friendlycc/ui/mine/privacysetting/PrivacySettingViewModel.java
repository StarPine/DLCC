package com.fine.friendlycc.ui.mine.privacysetting;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.bean.PrivacyBean;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * @author wulei
 */
public class PrivacySettingViewModel extends BaseViewModel<AppRepository> {

    public ObservableField<PrivacyBean> privacyEntity = new ObservableField<>(new PrivacyBean());
    public BindingCommand switchOnClickCommand = new BindingCommand(() -> {
        setPrivacy();
    });
    public BindingCommand switchHidden3kmOnClickCommand = new BindingCommand(() -> {
        boolean b = privacyEntity.get().getNearby();
    });

    public PrivacySettingViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getPrivacy();
    }

    /**
     * 获取我的隐私
     */
    public void getPrivacy() {
        model.getPrivacy()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseObserver<BaseDataResponse<PrivacyBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<PrivacyBean> response) {
                        privacyEntity.set(response.getData());
                    }
                });
    }

    /**
     * 设置我的隐私
     */
    public void setPrivacy() {
        model.setPrivacy(privacyEntity.get())
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }
}