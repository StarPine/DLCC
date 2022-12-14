package com.fine.friendlycc.ui.login;

import android.app.Application;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.appsflyer.AppsFlyerLib;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.bean.ChooseAreaItemBean;
import com.fine.friendlycc.bean.SystemConfigBean;
import com.fine.friendlycc.bean.TokenBean;
import com.fine.friendlycc.bean.UserDataBean;
import com.fine.friendlycc.event.ItemChooseAreaEvent;
import com.fine.friendlycc.manager.ThirdPushTokenMgr;
import com.fine.friendlycc.ui.login.choose.ChooseAreaFragment;
import com.fine.friendlycc.ui.login.register.RegisterFragment;
import com.fine.friendlycc.ui.main.MainFragment;
import com.fine.friendlycc.ui.mine.profile.PerfectProfileFragment;
import com.fine.friendlycc.ui.mine.webdetail.WebDetailFragment;
import com.fine.friendlycc.utils.ApiUitl;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;


/**
 * @author wulei
 */
public class LoginViewModel extends BaseViewModel<AppRepository>  {

    public ObservableField<String> mobile = new ObservableField<>();
    public ObservableField<ChooseAreaItemBean> areaCode = new ObservableField<>();
    public ObservableField<String> code = new ObservableField<>();
    public ObservableField<Boolean> agree = new ObservableField<>(true);

    public SingleLiveEvent<String> getCodeSuccess = new SingleLiveEvent<>();
    public SingleLiveEvent<String> setAreaSuccess = new SingleLiveEvent<>();

    private Disposable ItemChooseAreaSubscription;
    //????????????
    public BindingCommand ChooseAreaView = new BindingCommand(()->{
        start(ChooseAreaFragment.class.getCanonicalName());
    });
    /**
     * ???????????????????????????
     */
    public BindingCommand registerOnClickCommand = new BindingCommand(() -> register());
    public BindingCommand mobileLoginBackOnClickCommand = new BindingCommand(() -> onBackPressed());
    /**
     * ???????????????????????????
     */
    public BindingCommand registerUserOnClickCommand = new BindingCommand(() -> v2Login());
    /**
     * ????????????
     */
    public BindingCommand termsOfServiceOnClickCommand = new BindingCommand(() -> {
        Bundle bundle = WebDetailFragment.getStartBundle(AppConfig.TERMS_OF_SERVICE_URL);
        start(WebDetailFragment.class.getCanonicalName(), bundle);
    });
    /**
     * ????????????
     */
    public BindingCommand usageSpecificationOnClickCommand = new BindingCommand(() -> {
        Bundle bundle = WebDetailFragment.getStartBundle(AppConfig.PRIVACY_POLICY_URL);
        start(WebDetailFragment.class.getCanonicalName(), bundle);
    });
    public ObservableField<String> downTimeStr = new ObservableField<>(StringUtils.getString(R.string.playcc_send_code));
    private boolean isDownTime = false;
    /**
     * ??????????????????????????????
     */
    public BindingCommand sendRegisterSmsOnClickCommand = new BindingCommand(() -> reqVerifyCode());

    public LoginViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    /**
     * ????????????????????????
     */
    private void v2Login() {
        if (ObjectUtils.isEmpty(areaCode.get())) {
            ToastUtils.showShort(R.string.register_error_hint);
            return;
        }
        if (TextUtils.isEmpty(mobile.get())) {
            ToastUtils.showShort(StringUtils.getString(R.string.mobile_hint));
            return;
        }
        if (TextUtils.isEmpty(code.get())) {
            ToastUtils.showShort(R.string.playcc_please_input_code);
            return;
        }

        model.v2Login(mobile.get(), code.get(), ApiUitl.getAndroidId(),areaCode.get().getCode())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<UserDataBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<UserDataBean> response) {
                        dismissHUD();
                        UserDataBean authLoginUserEntity = response.getData();
                        TokenBean tokenEntity = new TokenBean(authLoginUserEntity.getToken(),authLoginUserEntity.getUserID(),authLoginUserEntity.getUserSig(), authLoginUserEntity.getIsContract());
                        model.saveLoginInfo(tokenEntity);
                        model.putKeyValue("areaCode",new Gson().toJson(areaCode.get()));
                        model.putKeyValue(AppConfig.LOGIN_TYPE,"phone");
                        if (response.getData() != null && response.getData().getIsNewUser() != null && response.getData().getIsNewUser().intValue() == 1) {
                            CCApplication.instance().logEvent(AppsFlyerEvent.register_start);
                            model.saveIsNewUser(true);
                        }
                        CCApplication.instance().logEvent(AppsFlyerEvent.LOG_IN_WITH_PHONE_NUMBER);
                        loadProfile();
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    private void register() {
        if (!agree.get()) {
            ToastUtils.showShort(R.string.playcc_warn_agree_terms);
            return;
        }
        start(RegisterFragment.class.getCanonicalName());
    }


    /**
     * ???????????????
     *
     * @param id       ??????ID
     * @param type     ?????? facebook/line
     * @param email    ??????
     * @param avatar   ??????
     * @param nickname ??????
     */
    public void authLogin(String id, String type, String email, String avatar, String nickname, String business) {
        if (id == null || type == null) {
            ToastUtils.showShort(R.string.get_userdata_defail);
            return;
        }
        id += type;
        model.authLoginPost(id, type, email, avatar, nickname, ApiUitl.getAndroidId(), business)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<Map<String, String>>>() {

                    @Override
                    public void onSuccess(BaseDataResponse<Map<String, String>> mapBaseDataResponse) {
                        dismissHUD();
                        Map<String, String> dataToken = mapBaseDataResponse.getData();
                        TokenBean tokenEntity = new TokenBean(dataToken.get("token"), dataToken.get("userID"), dataToken.get("userSig"), Integer.parseInt(dataToken.get("is_contract")));
                        if (mapBaseDataResponse.getData() != null && mapBaseDataResponse.getData().get("is_new_user") != null && mapBaseDataResponse.getData().get("is_new_user").equals("1")) {
                            CCApplication.instance().logEvent(AppsFlyerEvent.register_start);
                        }
                        model.saveLoginInfo(tokenEntity);
                        model.putKeyValue(AppConfig.LOGIN_TYPE,type);
                        loadProfile();
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    /**
     * ??????????????????
     */
    private void loadProfile() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        KLog.w(LoginViewModel.class.getCanonicalName(), "getInstanceId failed exception = " + task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    String token = task.getResult();
                    KLog.d(LoginViewModel.class.getCanonicalName(), "google fcm getToken = " + token);
                    ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
                    CCApplication.instance().pushDeviceToken(token);
                });
        //RaJava????????????
        model.getUserData()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<UserDataBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<UserDataBean> response) {
                        dismissHUD();
                        UserDataBean userDataEntity = response.getData();
                        //??????????????????
                        // MobclickAgent.onProfileSignIn(String.valueOf(userDataEntity.getId()));
                        AppsFlyerLib.getInstance().setCustomerUserId(String.valueOf(userDataEntity.getId()));
                        CCApplication.instance().mFirebaseAnalytics.setUserId(String.valueOf(userDataEntity.getId()));
                        try {
                            //??????????????????id
                            FirebaseCrashlytics.getInstance().setUserId(String.valueOf(userDataEntity.getId()));
                        }catch (Exception crashErr){
                            Log.e("Crashlytics setUserid ",crashErr.getMessage());
                        }
                        model.saveUserData(userDataEntity);
                        if (userDataEntity.getCertification() == 1) {
                            model.saveNeedVerifyFace(true);
                        }
                        dismissHUD();
                        AppConfig.userClickOut = false;
                        if (userDataEntity.getSex() != null && userDataEntity.getSex() >= 0 && !StringUtil.isEmpty(userDataEntity.getNickname()) && !StringUtil.isEmpty(userDataEntity.getBirthday()) && !StringUtil.isEmpty(userDataEntity.getAvatar())) {
                            startWithPopTo(MainFragment.class.getCanonicalName(), LoginFragment.class.getCanonicalName(), true);
                        } else {
                            start(PerfectProfileFragment.class.getCanonicalName());
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    private void reqVerifyCode() {
        if (TextUtils.isEmpty(mobile.get())) {
            ToastUtils.showShort(R.string.mobile_hint);
            return;
        }
        if (ObjectUtils.isEmpty(areaCode.get())) {
            ToastUtils.showShort(R.string.register_error_hint);
            return;
        }
        if (!isDownTime) {
            Map<String, String> mapData = new HashMap<String, String>();
            mapData.put("regionCode", areaCode.get().getCode());
            mapData.put("phone", mobile.get());
            model.verifyCodePost(ApiUitl.getBody(GsonUtils.toJson(mapData)))
                    .doOnSubscribe(this)
                    .compose(RxUtils.schedulersTransformer())
                    .compose(RxUtils.exceptionTransformer())
                    .doOnSubscribe(disposable -> showHUD())
                    .subscribe(new BaseObserver<BaseResponse>() {
                        @Override
                        public void onSuccess(BaseResponse baseResponse) {
                            ToastUtils.showShort(R.string.code_sended);

                            /**
                             * ?????????60????????????1???
                             */
                            CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    isDownTime = true;
                                    if (millisUntilFinished / 1000 == 58){
                                        getCodeSuccess.call();
                                    }
                                    downTimeStr.set(StringUtils.getString(R.string.again_send) + "(" + millisUntilFinished / 1000 + "???");
                                }

                                @Override
                                public void onFinish() {
                                    isDownTime = false;
                                    downTimeStr.set(StringUtils.getString(R.string.again_send));
                                }
                            }.start();

                        }

                        @Override
                        public void onComplete() {
                            super.onComplete();
                            dismissHUD();
                        }
                    });
        }
    }

    //????????????ip????????????
    public void getUserIpCode() {
        SystemConfigBean systemConfigEntity = model.readSystemConfig();
        if (systemConfigEntity != null) {
            ChooseAreaItemBean chooseAreaItemEntity = new ChooseAreaItemBean();
            chooseAreaItemEntity.setCode(systemConfigEntity.getRegionCode());
            areaCode.set(chooseAreaItemEntity);
        }
    }

    public String getAreaPhoneCode(ChooseAreaItemBean chooseAreaItem) {
        if (chooseAreaItem != null) {
            if (chooseAreaItem.getCode() != null) {
                return "+" + chooseAreaItem.getCode();
            }
        }
        return null;
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        ItemChooseAreaSubscription = RxBus.getDefault().toObservable(ItemChooseAreaEvent.class)
                .subscribe(event -> {
                    if (event.getChooseAreaItemEntity() != null) {
                        areaCode.set(event.getChooseAreaItemEntity());
                    }
                    setAreaSuccess.call();
                });
        RxSubscriptions.add(ItemChooseAreaSubscription);
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(ItemChooseAreaSubscription);
    }
}