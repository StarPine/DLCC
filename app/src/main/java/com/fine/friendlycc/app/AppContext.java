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
import com.fine.friendlycc.entity.ImUserSigEntity;
import com.fine.friendlycc.entity.TokenEntity;
import com.fine.friendlycc.entity.UserDataEntity;
import com.fine.friendlycc.event.LoginExpiredEvent;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.manager.LocaleManager;
import com.fine.friendlycc.manager.ThirdPushTokenMgr;
import com.fine.friendlycc.tim.TUIUtils;
import com.fine.friendlycc.MainActivity;
import com.fine.friendlycc.utils.ElkLogEventUtils;
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
public class AppContext extends Application {

    public static final String TAG = "AppContext";
    public static final String LOG_TAG = "AppsFlyerOneLinkSimApp";
    public static final String DL_ATTRS = "dl_attrs";
    public static boolean isHomePage = true;
    public static boolean isCalling = false;
    public static boolean isShowNotPaid = false;

    private static AppContext instance;
    //谷歌支付购买工具累
    private BillingClientLifecycle billingClientLifecycle;

    static {
        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
            @Override
            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
                //开始设置全局的基本参数（可以被下面的DefaultRefreshHeaderCreator覆盖）
                //取消内容不满一页时开启上拉加载功能
                layout.setEnableLoadMoreWhenContentNotFull(false);
                //回弹动画时长（毫秒）
                layout.setReboundDuration(300);
//                layout.setFooterHeight(100);
                //是否在加载的时候禁止列表的操作
                layout.setDisableContentWhenLoading(false);
                //是否启用越界拖动（仿苹果效果）
                layout.setEnableOverScrollDrag(true);
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
            }
        });

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new MaterialHeader(context);
            }
        });

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                ClassicsFooter footer = new ClassicsFooter(context);
                footer.setDrawableSize(20);
                return footer;
            }
        });
    }

    public AppRepository appRepository;
    public FirebaseAnalytics mFirebaseAnalytics;

    public static AppContext instance() {
        return instance;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // 清理所有图片内存缓存
        Glide.get(this).onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // 根据手机内存剩余情况清理图片内存缓存
        Glide.get(this).onTrimMemory(level);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        ElkLogEventUtils.setDefaultLocalLanguage(Locale.getDefault().getLanguage());
        if(newBase!=null){
            LocaleManager.setLocal(newBase);
        }
        super.attachBaseContext(LocaleManager.setLocal(newBase));
        LocaleManager.setLocal(newBase);
    }

    @Override
    public void onCreate() {
        ElkLogEventUtils.setDefaultLocalLanguage(Locale.getDefault().getLanguage());
        Locale localeCache = LocaleManager.getSystemLocale(this);
        Configuration config = new Configuration();
        config.locale = localeCache;
        LocaleManager.setLocal(this);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate();
        //初始化创建后。再次执行设置当前应用语言。确保部分机型不兼容问题
        LocaleManager.setLocal(this);
        //注册美颜渲染
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

        //是否开启日志打印
        KLog.init(BuildConfig.DEBUG);

        Utils.init(this);

        MMKV.initialize(this);

//        if (BuildConfig.DEBUG) {
//            com.facebook.stetho.Stetho.initializeWithDefaults(this);
//        }
        appRepository = Injection.provideDemoRepository();

        //配置全局异常崩溃操作
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(BuildConfig.DEBUG) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(BuildConfig.DEBUG) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.mipmap.ic_launcher) //错误图标
                .restartActivity(MainActivity.class) //重新启动后的activity
                //.errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
                //.eventListener(new YourCustomEventListener()) //崩溃后的错误监听
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

        UserDataEntity userDataEntity = appRepository.readUserData();
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
         * 深度连接打开应用
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
                        Log.e(LOG_TAG, "Conversion: 首次启动");
                    } else {
                        Log.e(LOG_TAG, "Conversion: 不是首次启动");
                    }
                } else {
                    Log.e(LOG_TAG, "Conversion: 这是一个自然安装.");
                }
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                Log.e(LOG_TAG, "获取转换数据时出错: " + errorMessage);
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
        //启动OneLink模板
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
         * onde Link 归因
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
                    if (mediaSource != null) {//获取渠道
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

    //上报事件-金额
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

    //上报普通事件
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
//        context：应用程序/活动上下文
//        publicKey：从 Google Play Console 获得的许可证密钥
//        signature:从data.INAPP_DATA_SIGNATUREonActivityResult
//        purchaseData:从data.INAPP_PURCHASE_DATAonActivityResult
//        price: 购买价格，应来源于 skuDetails.getStringArrayList("DETAILS_LIST")
//        currency: 购买货币，应来源于 skuDetails.getStringArrayList("DETAILS_LIST")
//        additionalParameters - 要记录的其他事件参数
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
//                if (bundle != null) { // 若bundle不为空则程序异常结束
//                    // 重启整个程序
//                    Intent intent = new Intent(activity, SplashActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                foregroundActivities++;
                if (foregroundActivities == 1 && !isChangingConfiguration) {
                    // 应用切到前台
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
                    // 应用切到后台
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
                .subscribe(new BaseObserver<BaseDataResponse<ImUserSigEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<ImUserSigEntity> response) {
                        ImUserSigEntity data = response.getData();
                        TokenEntity tokenEntity = appRepository.readLoginInfo();
                        if ((data == null || StringUtils.isEmpty(data.getUserSig())) || tokenEntity == null){
                            RxBus.getDefault().post(new LoginExpiredEvent());
                            return;
                        }
                        tokenEntity.setUserSig(data.getUserSig());
                        TUILogin.login(AppContext.this, appRepository.readApiConfigManagerEntity().getImAppId(), tokenEntity.getUserID(), tokenEntity.getUserSig(), new TUICallback() {
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
                        Log.e("上报推送Token", "成功");
                    }

                    @Override
                    public void onError(RequestException e) {
                        e.printStackTrace();
                        Log.e("上报推送Token", "失败");
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
                        appRepository.clearOneLinkCode();//清除本地邀请码缓存
                    }

                });
    }

    /**
    * @Desc TODO(获取阿里云mqtt实列)
    * @author 彭石林
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
