package com.fine.friendlycc.ui.mine.setting;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.AppUtils;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.bean.VersionBean;
import com.fine.friendlycc.ui.mine.blacklist.BlacklistFragment;
import com.fine.friendlycc.ui.mine.privacysetting.PrivacySettingFragment;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import org.jetbrains.annotations.NotNull;

import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * Author: 彭石林
 * Time: 2021/10/25 14:38
 * Description: This is MeSettingViewModel
 */
public class MeSettingViewModel extends BaseViewModel<AppRepository> {

   public UIChangeObservable uc = new UIChangeObservable();

    public ObservableField<String> currentVersion = new ObservableField<>();
    //美顏跳转
    public BindingCommand facebeauty = new BindingCommand(() -> {
        uc.starFacebeautyActivity.call();
    });
    //黑名单按钮的点击事件
    public BindingCommand blacklistOnClickCommand = new BindingCommand(() -> {
        CCApplication.instance().logEvent(AppsFlyerEvent.Blocked_List);
        start(BlacklistFragment.class.getCanonicalName());
    });
    //隐私设置按钮的点击事件
    public BindingCommand privacySettingOnClickCommand = new BindingCommand(() -> {
        CCApplication.instance().logEvent(AppsFlyerEvent.Privacy_Settings);
        start(PrivacySettingFragment.class.getCanonicalName());
    }
    );
    //设置按钮的点击事件
    public BindingCommand settingOnClickCommand = new BindingCommand(() -> {
        CCApplication.instance().logEvent(AppsFlyerEvent.System_Settings);
        start(SettingFragment.class.getCanonicalName());
    });
    //当前版本按钮的点击事件
    public BindingCommand versionOnClickCommand = new BindingCommand(() -> {
        model.detectionVersion("Android").compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<VersionBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<VersionBean> versionEntityBaseDataResponse) {
                        dismissHUD();
                        VersionBean versionEntity = versionEntityBaseDataResponse.getData();
                        if (versionEntity != null) {
                            uc.versionEntitySingl.postValue(versionEntity);
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }
    );
    public MeSettingViewModel(@NonNull @NotNull Application application, AppRepository model) {
        super(application, model);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        String appVersionName = AppUtils.getAppVersionName();
        currentVersion.set(appVersionName);
    }

    public class UIChangeObservable {
        public SingleLiveEvent<VersionBean> versionEntitySingl = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> starFacebeautyActivity = new SingleLiveEvent<>();
    }
}