package com.fine.friendlycc.ui.message.pushsetting;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.bean.PushSettingBean;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * @author wulei
 */
public class PushSettingViewModel extends BaseViewModel<AppRepository> {

    public ObservableField<PushSettingBean> pushSettingEntity = new ObservableField<>();
    public ObservableField<Boolean> isSound = new ObservableField<>(true);
    public ObservableField<Boolean> isShake = new ObservableField<>(true);
    public BindingCommand switchOnClickCommand = new BindingCommand(() -> {
        saveSetting();
    });
    public BindingCommand isSoundOnClickCommand = new BindingCommand(() -> {
        model.saveChatMessageIsSound(isSound.get());
    });
    public BindingCommand isShakeOnClickCommand = new BindingCommand(() -> {
        model.saveChatMessageIsShake(isShake.get());
    });

    public PushSettingViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
        isSound.set(model.readChatMessageIsSound());
        isShake.set(model.readChatMessageIsShake());
    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        loadSetting();
    }

    public void loadSetting() {
        model.getPushSetting()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseObserver<BaseDataResponse<PushSettingBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<PushSettingBean> response) {
                        pushSettingEntity.set(response.getData());
                    }
                });
    }

    public void saveSetting() {
        if (pushSettingEntity.get() == null) {
            return;
        }
        model.savePushSetting(pushSettingEntity.get())
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