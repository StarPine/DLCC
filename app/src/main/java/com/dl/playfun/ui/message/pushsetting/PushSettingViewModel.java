package com.dl.playfun.ui.message.pushsetting;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseDataResponse;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.entity.PushSettingEntity;
import com.dl.playfun.viewmodel.BaseViewModel;

import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * @author wulei
 */
public class PushSettingViewModel extends BaseViewModel<AppRepository> {

    public ObservableField<PushSettingEntity> pushSettingEntity = new ObservableField<>();
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
                .subscribe(new BaseObserver<BaseDataResponse<PushSettingEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<PushSettingEntity> response) {
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
