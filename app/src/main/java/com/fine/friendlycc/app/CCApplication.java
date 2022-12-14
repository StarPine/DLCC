package com.fine.friendlycc.app;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.android.billingclient.api.Purchase;
import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerInAppPurchaseValidatorListener;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.deeplink.DeepLink;
import com.appsflyer.deeplink.DeepLinkListener;
import com.appsflyer.deeplink.DeepLinkResult;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.fine.friendlycc.BuildConfig;
import com.fine.friendlycc.R;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseDisposableObserver;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.bean.ImUserSigBean;
import com.fine.friendlycc.bean.TokenBean;
import com.fine.friendlycc.bean.UserDataBean;
import com.fine.friendlycc.event.LoginExpiredEvent;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.manager.LocaleManager;
import com.fine.friendlycc.manager.ThirdPushTokenMgr;
import com.fine.friendlycc.tim.TUIUtils;
import com.fine.friendlycc.MainActivity;
import com.fine.friendlycc.utils.StringUtil;
import com.faceunity.nama.FURenderer;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshInitializer;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversationListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.mmkv.MMKV;
import com.tencent.qcloud.tuicore.TUILogin;
import com.tencent.qcloud.tuicore.interfaces.TUICallback;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import me.goldze.mvvmhabit.base.BaseApplication;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.crash.CaocConfig;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.RxUtils;


/**
 * @author wulei
 */
public class CCApplication extends Application {

    public static final String TAG = "AppContext";
    public static final String LOG_TAG = "AppsFlyerOneLinkSimApp";
    public static final String DL_ATTRS = "dl_attrs";
    public static boolean isHomePage = true;
    public static boolean isCalling = false;
    public static boolean isShowNotPaid = false;

    private static CCApplication instance;
    //???????????????????????????
    private BillingClientLifecycle billingClientLifecycle;

    static {
        //????????????????????????????????????????????????????????????????????????
        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
            @Override
            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
                //??????????????????????????????????????????????????????DefaultRefreshHeaderCreator?????????
                //???????????????????????????????????????????????????
                layout.setEnableLoadMoreWhenContentNotFull(false);
                //??????????????????????????????
                layout.setReboundDuration(300);
//                layout.setFooterHeight(100);
                //?????????????????????????????????????????????
                layout.setDisableContentWhenLoading(false);
                //?????????????????????????????????????????????
                layout.setEnableOverScrollDrag(true);
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
            }
        });

        //???????????????Header?????????
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new MaterialHeader(context);
            }
        });

        //???????????????Footer?????????
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //???????????????Footer???????????? BallPulseFooter
                ClassicsFooter footer = new ClassicsFooter(context);
                footer.setDrawableSize(20);
                return footer;
            }
        });
    }

    public AppRepository appRepository;
    public FirebaseAnalytics mFirebaseAnalytics;

    public static CCApplication instance() {
        return instance;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // ??????????????????????????????
        Glide.get(this).onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // ??????????????????????????????????????????????????????
        Glide.get(this).onTrimMemory(level);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleManager.setLocal(newBase));
    }

    @Override
    public void onCreate() {
        Locale localeCache = LocaleManager.getSystemLocale(this);
        Configuration config = new Configuration();
        config.locale = localeCache;
        LocaleManager.setLocal(this);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate();
        //?????????????????????????????????????????????????????????????????????????????????????????????
        LocaleManager.setLocal(this);
        //??????????????????
        FURenderer.getInstance().setup(this);
        try {
            File cacheDir = new File(this.getApplicationContext().getExternalCacheDir().getPath(), "https");
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128);
        } catch (Exception e) {
            e.printStackTrace();
        }
        instance = this;
        BaseApplication.setApplication(this);

        FirebaseApp.initializeApp(this);

        billingClientLifecycle = BillingClientLifecycle.getInstance(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(billingClientLifecycle);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        initAppflyer();

        //????????????????????????
        KLog.init(BuildConfig.DEBUG);

        Utils.init(this);

        MMKV.initialize(this);

//        if (BuildConfig.DEBUG) {
//            com.facebook.stetho.Stetho.initializeWithDefaults(this);
//        }
        appRepository = Injection.provideDemoRepository();

        //??????????????????????????????
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //????????????,???????????????
                .enabled(true) //??????????????????????????????
                .showErrorDetails(BuildConfig.DEBUG) //??????????????????????????????
                .showRestartButton(true) //????????????????????????
                .trackActivities(BuildConfig.DEBUG) //????????????Activity
                .minTimeBetweenCrashesMs(2000) //?????????????????????(??????)
                .errorDrawable(R.mipmap.ic_launcher) //????????????
                .restartActivity(MainActivity.class) //??????????????????activity
                //.errorActivity(YourCustomErrorActivity.class) //??????????????????activity
                //.eventListener(new YourCustomEventListener()) //????????????????????????
                .apply();
    }

    public void initIM() {
        AppRepository repository = ConfigManager.getInstance().getAppRepository();
        if(repository==null || repository.readApiConfigManagerEntity()==null){
            return;
        }

        TUIUtils.init(this,repository.readApiConfigManagerEntity().getImAppId() , new V2TIMSDKConfig(), new V2TIMSDKListener() {
            @Override
            public void onConnecting() {
                super.onConnecting();
            }

            @Override
            public void onConnectSuccess() {
                super.onConnectSuccess();
            }

            @Override
            public void onConnectFailed(int code, String error) {
                super.onConnectFailed(code, error);
            }

            @Override
            public void onKickedOffline() {
                super.onKickedOffline();
                RxBus.getDefault().post(new LoginExpiredEvent());
            }

            @Override
            public void onUserSigExpired() {
                super.onUserSigExpired();
                flushSign();
            }

            @Override
            public void onSelfInfoUpdated(V2TIMUserFullInfo info) {
                super.onSelfInfoUpdated(info);
            }
        });

        UserDataBean userDataEntity = appRepository.readUserData();
        if (userDataEntity == null || userDataEntity.getId() == null) {
            return;
        }
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        KLog.w(TAG, "getInstanceId failed exception = " + task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    String
                            token = task.getResult();
                    KLog.d(TAG, "google fcm getToken = " + token);
                    ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
                    pushDeviceToken(token);
                });
    }

    public void initAppflyer() {
        /**
         * ????????????????????????
         * @param fruitName
         * @param dlData
         */
        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionDataMap) {
                Object code = conversionDataMap.get("af_sub1");
                Object channel = conversionDataMap.get("af_sub2");
                if (!ObjectUtils.isEmpty(code) || !ObjectUtils.isEmpty(channel)) {
                    Map<String, String> map = new HashMap<>();
                    map.put("code", String.valueOf(code));
                    map.put("channel", String.valueOf(channel));
                    ConfigManager.getInstance().getAppRepository().saveOneLinkCode(GsonUtils.toJson(map));

                }
                String status = Objects.requireNonNull(conversionDataMap.get("af_status")).toString();
                if (status.equals("Non-organic")) {
                    if (Objects.requireNonNull(conversionDataMap.get("is_first_launch")).toString().equals("true")) {
                        Log.e(LOG_TAG, "Conversion: ????????????");
                    } else {
                        Log.e(LOG_TAG, "Conversion: ??????????????????");
                    }
                } else {
                    Log.e(LOG_TAG, "Conversion: ????????????????????????.");
                }
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                Log.e(LOG_TAG, "???????????????????????????: " + errorMessage);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> attributionData) {
                Log.e(LOG_TAG, "onAppOpenAttribution: This is fake call.");
            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.e(LOG_TAG, "error onAttributionFailure : " + errorMessage);
            }
        };
        //??????OneLink??????
        AppsFlyerLib.getInstance().setAppInviteOneLink(getString(R.string.app_flyer_invite_pathPrefix));
        AppsFlyerLib.getInstance().setDebugLog(true);
        AppsFlyerLib.getInstance().init(getString(R.string.app_flyer), conversionListener, this);
        AppsFlyerLib.getInstance().start(this);
        AppsFlyerLib.getInstance().registerValidatorListener(this,new
                AppsFlyerInAppPurchaseValidatorListener() {
                    public void onValidateInApp() {
                        Log.d(TAG, "Purchase validated successfully");
                    }

                    public void onValidateInAppFailure(String error) {
                        Log.d(TAG, "onValidateInAppFailure called: " + error);
                    }
                });
        /**
         * onde Link ??????
         */
        AppsFlyerLib.getInstance().subscribeForDeepLink(new DeepLinkListener() {
            @Override
            public void onDeepLinking(@NonNull DeepLinkResult deepLinkResult) {
                DeepLinkResult.Status dlStatus = deepLinkResult.getStatus();
                if (dlStatus == DeepLinkResult.Status.FOUND) {
                    Log.e(LOG_TAG, "Deep link found");
                } else if (dlStatus == DeepLinkResult.Status.NOT_FOUND) {
                    Log.e(LOG_TAG, "Deep link not found");
                    return;
                } else {
                    // dlStatus == DeepLinkResult.Status.ERROR
                    DeepLinkResult.Error dlError = deepLinkResult.getError();
                    Log.e(LOG_TAG, "There was an error getting Deep Link data: " + dlError.toString());
                    return;
                }
                DeepLink deepLinkObj = deepLinkResult.getDeepLink();
                try {
                    String mediaSource = deepLinkObj.getMediaSource();
                    if (mediaSource != null) {//????????????
                        appRepository.saveChannelAF(mediaSource);
                    }
                    String code = deepLinkObj.getAfSub1();
                    String channel = deepLinkObj.getAfSub2();
                    if (!StringUtil.isEmpty(code) || !StringUtil.isEmpty(channel)) {
                        Map<String, String> map = new HashMap<>();
                        map.put("code", code);
                        map.put("channel", channel);
                        ConfigManager.getInstance().getAppRepository().saveOneLinkCode(GsonUtils.toJson(map));
                    }
                    Log.e(LOG_TAG, "The DeepLink data is: " + deepLinkObj.toString());
                } catch (Exception e) {
                    Log.e(LOG_TAG, "DeepLink data came back null");
                    return;
                }
                // An example for using is_deferred
                if (deepLinkObj.isDeferred()) {
                    Log.e(LOG_TAG, "This is a deferred deep link");
                } else {
                    Log.e(LOG_TAG, "This is a direct deep link");
                }
                // An example for using a generic getter
                String fruitName = "";
                try {
                    fruitName = deepLinkObj.getDeepLinkValue();
                    Log.e(LOG_TAG, "The DeepLink will route to: " + fruitName);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Custom param fruit_name was not found in DeepLink data");
                    return;
                }
                goToFruit(fruitName, deepLinkObj);
            }
        });
    }

    private void goToFruit(String fruitName, DeepLink dlData) {
        String fruitClassName = (fruitName.substring(0, 1).toUpperCase() + fruitName.substring(1)).concat("Activity");
        try {
            Class fruitClass = Class.forName(this.getPackageName().concat(".").concat(fruitClassName));
            Log.e(LOG_TAG, "Looking for class " + fruitClass);
            Intent intent = new Intent(getApplicationContext(), fruitClass);
            if (dlData != null) {
                // TODO - make DeepLink Parcelable
                String objToStr = new Gson().toJson(dlData);
                intent.putExtra(DL_ATTRS, objToStr);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            Log.e(LOG_TAG, "Deep linking failed looking for " + fruitName);
            e.printStackTrace();
        }
    }

    //????????????-??????
    public void logEvent(String eventName, String money,Purchase purchase) {
        validateGooglePlayLog(purchase,money);
        logEvent(eventName);
//        Map<String, Object> eventValues = new HashMap<>();
//        eventValues.put(AFInAppEventParameterName.CONTENT_TYPE, eventName);
//        eventValues.put(AFInAppEventParameterName.REVENUE, money);
//        eventValues.put(AFInAppEventParameterName.CONTENT_ID, money);
//        eventValues.put(AFInAppEventParameterName.CURRENCY, "USD");
//        AppsFlyerLib.getInstance().logEvent(this, eventName, eventValues);
//        Bundle bundleEvent = new Bundle();
//        bundleEvent.putDouble(FirebaseAnalytics.Param.VALUE, 3.99);
//        bundleEvent.putString(FirebaseAnalytics.Param.CURRENCY, "USD");  // e.g. $3.99 USD
//        mFirebaseAnalytics.logEvent(eventName, bundleEvent);
    }

    //??????????????????
    public void logEvent(String eventName) {
        Map<String, Object> eventValues = new HashMap<>();
        eventValues.put(AFInAppEventParameterName.CONTENT_TYPE, eventName);
        AppsFlyerLib.getInstance().logEvent(this, eventName, eventValues);

        Bundle bundleEvent = new Bundle();
        bundleEvent.putLong(eventName, System.currentTimeMillis());
        bundleEvent.putString("key", "value");
        mFirebaseAnalytics.logEvent(eventName, bundleEvent);
    }

    public void validateGooglePlayLog(Purchase purchase,String money) {
//        context???????????????/???????????????
//        publicKey?????? Google Play Console ????????????????????????
//        signature:???data.INAPP_DATA_SIGNATUREonActivityResult
//        purchaseData:???data.INAPP_PURCHASE_DATAonActivityResult
//        price: ??????????????????????????? skuDetails.getStringArrayList("DETAILS_LIST")
//        currency: ??????????????????????????? skuDetails.getStringArrayList("DETAILS_LIST")
//        additionalParameters - ??????????????????????????????
        Map<String, String> eventValues = new HashMap<>();
        eventValues.put("some_parameter", "some_value");
        //eventValues.put(AFInAppEventParameterName.CONTENT_TYPE, eventName);
        AppsFlyerLib.getInstance().validateAndLogInAppPurchase(this,getString(R.string.google_public_key), purchase.getSignature(),
                purchase.getOriginalJson(),money,
                "USD",
                eventValues);
    }


    public void initActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            private final V2TIMConversationListener unreadListener = new V2TIMConversationListener() {
                @Override
                public void onTotalUnreadMessageCountChanged(long totalUnreadCount) {
                    // HUAWEIHmsMessageService.updateBadge(DemoApplication.this, (int) totalUnreadCount);
                }
            };
            private int foregroundActivities = 0;
            private boolean isChangingConfiguration;

            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                KLog.i(TAG, "onActivityCreated bundle: " + bundle);
//                if (bundle != null) { // ???bundle??????????????????????????????
//                    // ??????????????????
//                    Intent intent = new Intent(activity, SplashActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                foregroundActivities++;
                if (foregroundActivities == 1 && !isChangingConfiguration) {
                    // ??????????????????
                    KLog.i(TAG, "application enter foreground");
                    V2TIMManager.getOfflinePushManager().doForeground(new V2TIMCallback() {
                        @Override
                        public void onError(int code, String desc) {
                            KLog.e(TAG, "doForeground err = " + code + ", desc = " + desc);
                        }

                        @Override
                        public void onSuccess() {
                            KLog.i(TAG, "doForeground success");
                        }
                    });
                    V2TIMManager.getConversationManager().removeConversationListener(unreadListener);
                    //MessageNotification.getInstance().cancelTimeout();
                }
                isChangingConfiguration = false;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                foregroundActivities--;
                if (foregroundActivities == 0) {
                    // ??????????????????
                    Log.i(TAG, "application enter background");
                    V2TIMManager.getConversationManager().getTotalUnreadMessageCount(new V2TIMValueCallback<Long>() {
                        @Override
                        public void onSuccess(Long aLong) {
                            int totalCount = aLong.intValue();
                            V2TIMManager.getOfflinePushManager().doBackground(totalCount, new V2TIMCallback() {
                                @Override
                                public void onError(int code, String desc) {
                                    Log.e(TAG, "doBackground err = " + code + ", desc = " + desc);
                                }

                                @Override
                                public void onSuccess() {
                                    Log.i(TAG, "doBackground success");
                                }
                            });
                        }

                        @Override
                        public void onError(int code, String desc) {

                        }
                    });

                    V2TIMManager.getConversationManager().addConversationListener(unreadListener);
                }
                isChangingConfiguration = activity.isChangingConfigurations();
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public void flushSign() {
        appRepository.flushSign()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<ImUserSigBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<ImUserSigBean> response) {
                        ImUserSigBean data = response.getData();
                        TokenBean tokenEntity = appRepository.readLoginInfo();
                        if ((data == null || StringUtils.isEmpty(data.getUserSig())) || tokenEntity == null){
                            RxBus.getDefault().post(new LoginExpiredEvent());
                            return;
                        }
                        tokenEntity.setUserSig(data.getUserSig());
                        TUILogin.login(CCApplication.this, appRepository.readApiConfigManagerEntity().getImAppId(), tokenEntity.getUserID(), tokenEntity.getUserSig(), new TUICallback() {
                                    @Override
                                    public void onSuccess() {
                                        appRepository.saveLoginInfo(tokenEntity);
                                        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
                                    }

                                    @Override
                                    public void onError(int errorCode, String errorMessage) {
                                        RxBus.getDefault().post(new LoginExpiredEvent());
                                    }
                                });
                    }

                    @Override
                    public void onError(RequestException e) {
                        RxBus.getDefault().post(new LoginExpiredEvent());
                    }

                });
    }

    public void pushDeviceToken(String deviceToken) {
        AppConfig.DEVICE_CODE = deviceToken;
        appRepository.pushDeviceToken(deviceToken, AppConfig.VERSION_NAME)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseDisposableObserver<BaseResponse>() {

                    @Override
                    public void onSuccess(BaseResponse response) {
                        Log.e("????????????Token", "??????");
                    }

                    @Override
                    public void onError(RequestException e) {
                        e.printStackTrace();
                        Log.e("????????????Token", "??????");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println();
                    }

                });
    }

    public void pushInvite(String code, Integer type, String channel) {
        appRepository.userInvite(code, type, channel)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        appRepository.clearOneLinkCode();//???????????????????????????
                    }

                });
    }

    /**
    * @Desc TODO(???????????????mqtt??????)
    * @author ?????????
    * @parame []
    * @return com.dl.playcc.app.AliYunMqttClientLifecycle
    * @Date 2022/7/5
    */
    public AliYunMqttClientLifecycle getBqttClientLifecycle() {
        return AliYunMqttClientLifecycle.getInstance(this);
    }

    public BillingClientLifecycle getBillingClientLifecycle() {
        if(billingClientLifecycle==null){
            synchronized(BillingClientLifecycle.class){
                if(billingClientLifecycle==null){
                    billingClientLifecycle =  BillingClientLifecycle.getInstance(this);
                    ProcessLifecycleOwner.get().getLifecycle().addObserver(billingClientLifecycle);
                }
            }
        }
        return billingClientLifecycle;
    }



}