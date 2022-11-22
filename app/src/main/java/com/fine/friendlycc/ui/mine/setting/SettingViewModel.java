package com.fine.friendlycc.ui.mine.setting;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.bean.PrivacyBean;
import com.fine.friendlycc.bean.UserInfoBean;
import com.fine.friendlycc.event.IsAuthBindingEvent;
import com.fine.friendlycc.manager.GlideCacheManager;
import com.fine.friendlycc.ui.login.LoginFragment;
import com.fine.friendlycc.ui.main.MainFragment;
import com.fine.friendlycc.ui.message.pushsetting.PushSettingFragment;
import com.fine.friendlycc.ui.mine.creenlock.ScreenLockFragment;
import com.fine.friendlycc.ui.mine.setting.account.CommunityAccountFragment;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public class SettingViewModel extends BaseViewModel<AppRepository> {

    public ObservableField<PrivacyBean> privacyEntity = new ObservableField<>(new PrivacyBean());
    public ObservableField<String> cacheSize = new ObservableField<>();
    public ObservableField<Integer> isAuth = new ObservableField<>(0);
    //服务条款
    public ObservableBoolean showUrl = new ObservableBoolean(false);
    //隐私政策
    public ObservableBoolean showUrl2 = new ObservableBoolean(false);
    //绑定社群账号
    public BindingCommand bindingCommunityAccount = new BindingCommand(() ->{
        start(CommunityAccountFragment.class.getCanonicalName());
    });
    //推送设置按钮的点击事件
    public BindingCommand pushSettingOnClickCommand = new BindingCommand(() -> start(PushSettingFragment.class.getCanonicalName()));
    //隐私设置按钮的点击事件
    public BindingCommand clearCacheOnClickCommand = new BindingCommand(() -> {
        GlideCacheManager.getInstance().clearImageDiskCache(getApplication());
        cacheSize.set("0.00KB");
        ToastUtils.showShort(R.string.playcc_cleared_image_cache);
    });
    public BindingCommand settintAppLockOnClickCommand = new BindingCommand(() -> {
        start(ScreenLockFragment.class.getCanonicalName());
    });
    UIChangeObservable uc = new UIChangeObservable();
    //绑定手机号按钮的点击事件
    public BindingCommand bindMobileOnClickCommand = new BindingCommand(() -> uc.bindMobile.call());
    //退出登录
    public BindingCommand logoutOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.clickLogout.call();
        }
    });
    private Disposable isAuthChangeSubscription;

    public SettingViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String strCacheSize = GlideCacheManager.getInstance().getCacheSize(getApplication());
        cacheSize.set(strCacheSize);
    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        getPrivacy();

    }

    /**
     * 获取我的隐私
     */
    private void getPrivacy() {
        model.getPrivacy()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<PrivacyBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<PrivacyBean> response) {
                        privacyEntity.set(response.getData());
                    }
                });
    }

    public void logout() {
        //友盟用户统计
        // MobclickAgent.onProfileSignOff();
        AppConfig.userClickOut = true;
        showHUD();
        model.logout();
        startWithPopTo(LoginFragment.class.getCanonicalName(), MainFragment.class.getCanonicalName(), true);
        dismissHUD();
    }

    public void loadUserInfo() {
        model.getUserInfo()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> {
                    showHUD();
                })
                .subscribe(new BaseObserver<BaseDataResponse<UserInfoBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<UserInfoBean> response) {
                        isAuth.set(response.getData().getIsAuth());
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                        super.onComplete();
                    }
                });
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        //第三方登录后接受绑定通知
        isAuthChangeSubscription = RxBus.getDefault().toObservable(IsAuthBindingEvent.class)
                .subscribe(event -> {
                    isAuth.set(1);
                });
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(isAuthChangeSubscription);
    }

    public class UIChangeObservable {
        public SingleLiveEvent<Void> bindMobile = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> clickLogout = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> clickClearCache = new SingleLiveEvent<>();
    }

}