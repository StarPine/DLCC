package com.fine.friendlycc.ui.splash;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;

import com.appsflyer.AppsFlyerLib;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.BuildConfig;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.app.EaringlSwitchUtil;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseDisposableObserver;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.bean.AllConfigBean;
import com.fine.friendlycc.bean.ApiConfigManagerBean;
import com.fine.friendlycc.bean.CityAllBean;
import com.fine.friendlycc.bean.TokenBean;
import com.fine.friendlycc.bean.UserDataBean;
import com.fine.friendlycc.event.LoginExpiredEvent;
import com.fine.friendlycc.ui.login.LoginFragment;
import com.fine.friendlycc.ui.main.MainFragment;
import com.fine.friendlycc.utils.ExceptionReportUtils;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * @author wulei
 */
public class SplashViewModel extends BaseViewModel<AppRepository> {

    public ObservableBoolean hintRetryShow = new ObservableBoolean(false);
    public ObservableBoolean isDebug = new ObservableBoolean(false);

    public SplashViewModel(@NonNull Application application, AppRepository appRepository) {
        super(application, appRepository);
        isDebug.set(BuildConfig.DEBUG);
    }

    public BindingCommand RetryCLick = new BindingCommand(this::initApiConfig);

    public BindingCommand loadRelease = new BindingCommand(() -> {
        AppConfig.isTest = false;
        initApiConfig();
    });

    public BindingCommand loadDebug = new BindingCommand(() -> {
        AppConfig.isTest = true;
        initApiConfig();
    });

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        if (!BuildConfig.DEBUG){
            initApiConfig();
        }
    }
    private void initData() {
        if (model.readLoginInfo() != null && !StringUtils.isEmpty(model.readLoginInfo().getToken()) && model.readUserData() != null && model.readUserData().getSex() != null && model.readUserData().getSex() != -1 && !StringUtil.isEmpty(model.readUserData().getNickname())) {
            loadProfile();
        } else {
            Observable.just("")
                    .delay(300, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    // Be notified on the main thread
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        initIM();
                        startWithPopTo(LoginFragment.class.getCanonicalName(), SplashFragment.class.getCanonicalName(), true);
                    });
        }
    }
    /**
     * @Desc TODO(初始化获取api配置)
     * @author 彭石林
     * @parame []
     * @return void
     * @Date 2022/7/2
     */
    private void initApiConfig () {
        model.initApiConfig()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<ApiConfigManagerBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<ApiConfigManagerBean> response) {
                        if (!response.isDataEmpty()) {
                            ApiConfigManagerBean apiConfigManager = response.getData();
                            if (apiConfigManager != null) {
                                AppConfig.CHAT_SERVICE_USER_ID = apiConfigManager.getCustomerId();
                                model.saveApiConfigManager(apiConfigManager);
                                initSettingConfig();
                            } else {
                                hintRetryShow.set(true);
                            }
                        } else {
                            hintRetryShow.set(true);
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        hintRetryShow.set(true);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    private void initIM(){
        CCApplication CCApplication = ((CCApplication)getApplication());
        CCApplication.initIM();
        CCApplication.initActivityLifecycleCallbacks();
    }

    /**
     * 加载用户资料
     */
    private void loadProfile () {
        //RaJava模拟登录
        model.getUserData()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseObserver<BaseDataResponse<UserDataBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<UserDataBean> response) {
                        UserDataBean userDataEntity = response.getData();
                        model.saveUserData(userDataEntity);
                        //更新IM userSig
                        if (!TextUtils.isEmpty(userDataEntity.getUserSig())) {
                            TokenBean tokenEntity = model.readLoginInfo();
                            if (tokenEntity == null){
                                RxBus.getDefault().post(new LoginExpiredEvent());
                                return;
                            }
                            tokenEntity.setUserSig(userDataEntity.getUserSig());
                            model.saveLoginInfo(tokenEntity);
                        }
                        AppsFlyerLib.getInstance().setCustomerUserId(String.valueOf(userDataEntity.getId()));
                        CCApplication.instance().mFirebaseAnalytics.setUserId(String.valueOf(userDataEntity.getId()));
                        try {
                            //添加崩溃人员id
                            FirebaseCrashlytics.getInstance().setUserId(String.valueOf(userDataEntity.getId()));
                        } catch (Exception crashErr){
                            Log.e("Crashlytics setUserid ",crashErr.getMessage());
                        }
                        initIM();
                        CCApplication.instance().logEvent(AppsFlyerEvent.Silent_login);
                        startWithPop(MainFragment.class.getCanonicalName());
                    }

                    @Override
                    public void onError(RequestException e) {
                        initIM();
                        if (model.readUserData() != null) {
                            startWithPop(MainFragment.class.getCanonicalName());
                        } else {
                            startWithPop(LoginFragment.class.getCanonicalName());
                        }
                    }
                });
    }

    /**
     * @Desc TODO(初始化执行获取系统配置)
     * @author 彭石林
     * @parame [initSettingConfigListener]
     * @return boolean
     * @Date 2022/5/16
     */
    public void initSettingConfig () {
        model.getAllConfig()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseDisposableObserver<BaseDataResponse<AllConfigBean>>() {

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }

                    @Override
                    public void onSuccess(BaseDataResponse<AllConfigBean> response) {
                        try {
                            model.saveHeightConfig(response.getData().getHeight());
                            model.saveWeightConfig(response.getData().getWeight());
                            model.saveReportReasonConfig(response.getData().getReportReason());
                            model.saveFemaleEvaluateConfig(response.getData().getEvaluate().getEvaluateFemale());
                            model.saveMaleEvaluateConfig(response.getData().getEvaluate().getEvaluateMale());
                            model.saveHopeObjectConfig(response.getData().getHopeObject());
                            model.saveOccupationConfig(response.getData().getOccupation());
                            model.saveCityConfig(response.getData().getCity());
                            model.saveSystemConfig(response.getData().getConfig());
                            model.saveSystemConfigTask(response.getData().getTask());
                            model.saveDefaultHomePageConfig(response.getData().getDefaultHomePage());
                            model.saveGameConfig(response.getData().getGame());
                            model.saveCrystalDetailsConfig(response.getData().getCrystalDetailsConfig());
                            model.putSwitches(EaringlSwitchUtil.KEY_TIPS, response.getData().getIsTips());
                            model.putSwitches(EaringlSwitchUtil.KEY_DELETE_ACCOUNT, response.getData().getConfig().getDeleteAccount());
                            model.putSwitches(EaringlSwitchUtil.INVITE_FRIEND, response.getData().getUserInvite());
                        } catch (Exception e) {
                            ExceptionReportUtils.report(e);
                        }
                        initCityConfigAll();
                    }

                    @Override
                    public void onError(RequestException e) {
                        e.printStackTrace();
                        hintRetryShow.set(true);
                    }

                });
    }

    public void initCityConfigAll() {
        model.getCityConfigAll()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<CityAllBean>>() {
                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }

                    @Override
                    public void onSuccess(BaseDataResponse<CityAllBean> response) {
                        CityAllBean cityData = response.getData();
                        if (ObjectUtils.isNotEmpty(cityData.getCityAll())) {
                            model.saveCityConfigAll(cityData.getCityAll());
                            initData();
                        } else {
                            hintRetryShow.set(true);
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        e.printStackTrace();
                        hintRetryShow.set(true);
                    }

                });
    }
}