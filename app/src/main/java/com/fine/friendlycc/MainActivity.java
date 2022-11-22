package com.fine.friendlycc;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.aliyun.svideo.crop.bean.AlivcCropOutputParam;
import com.android.billingclient.api.BillingClient;
import com.blankj.utilcode.util.KeyboardUtils;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.BillingClientLifecycle;
import com.fine.friendlycc.event.LoginExpiredEvent;
import com.fine.friendlycc.event.UserDisableEvent;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.manager.LocaleManager;
import com.fine.friendlycc.ui.base.MySupportActivity;
import com.fine.friendlycc.ui.login.LoginFragment;
import com.fine.friendlycc.ui.main.MainFragment;
import com.fine.friendlycc.ui.splash.SplashFragment;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.utils.ImmersionBarUtils;
import com.fine.friendlycc.widget.dialog.MVDialog;
import com.tencent.qcloud.tuicore.TUILogin;
import com.tencent.qcloud.tuicore.interfaces.TUICallback;
import com.tencent.qcloud.tuicore.util.ConfigManagerUtil;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * @author wulei
 */
public class MainActivity extends MySupportActivity {
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;
    private MVDialog userDisableDialog;
    private MVDialog loginExpiredDialog;
    private BillingClientLifecycle billingClientLifecycle;

    private long onCreateTime = 0l ;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleManager.setLocal(newBase));
    }

    /**
     * 就算你在Manifest.xml设置横竖屏切换不重走生命周期。横竖屏切换还是会走这里

     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(newConfig!=null){
//            restartApp(this);
            LocaleManager.setLocal(this);
        }
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocal(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LocaleManager.setLocal(this);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
                //something to do
                restartApp(MainActivity.this);
            }
        }
    };

    //重启应用
    public void restartApp(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加切换语言广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_LOCALE_CHANGED);
        registerReceiver(mReceiver, filter);

        onCreateTime = System.currentTimeMillis() / 1000;
        isLaunchMain();
        AutoSizeUtils.applyAdapt(this.getResources());
        setContentView(R.layout.activity_main_container);
        ImmersionBarUtils.setupStatusBar(this, true, false);
        //栈内查找 如果存在及复用
        loadRootFragment(R.id.fl_container, new SplashFragment());
        //改变游戏装填---兼容代码
        ConfigManagerUtil.getInstance().putPlayGameFlag(false);
        registerRxBus();

        this.billingClientLifecycle = ((AppContext)getApplication()).getBillingClientLifecycle();
        if(!billingClientLifecycle.isConnectionSuccessful()){
            queryAndConsumePurchase();
        }
        queryAndConsumePurchase();
        billingClientLifecycle.CONNECTION_SUCCESS.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    queryAndConsumePurchase();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==-1){
            if(AppConfig.isCorpAliyun){
                AlivcCropOutputParam alivcCropOutputParam = (AlivcCropOutputParam) data.getSerializableExtra(AlivcCropOutputParam.RESULT_KEY_OUTPUT_PARAM);
                if(alivcCropOutputParam!=null){
                    RxBus.getDefault().post(alivcCropOutputParam);
                }

            }
            AppConfig.isCorpAliyun = false;
        }
    }

    private void registerRxBus() {
        RxBus.getDefault().toObservable(UserDisableEvent.class)
                .subscribe(event -> {
                    if(this.isFinishing() || this.isDestroyed()){
                        return;
                    }
                    if (userDisableDialog == null) {
                        userDisableDialog = MVDialog.getInstance(MainActivity.this)
                                .setTitele(getString(R.string.playcc_dialog_user_disable_title))
                                .setContent(getString(R.string.playcc_dialog_user_disable_content))
                                .setConfirmText(getString(R.string.playcc_dialog_user_disable_btn_text))
                                .setCancelable(true)
                                .setConfirmOnlick(dialog -> {
                                    //跳转到登录界面
                                    startWithPopTo(new LoginFragment(), MainActivity.class, true);
                                })
                                .chooseType(MVDialog.TypeEnum.CENTERWARNED);
                    }
                    if(this.isFinishing() || this.isDestroyed()){
                        return;
                    }
                    if (!userDisableDialog.isShowing()) {
                        userDisableDialog.show();
                    }
                });

        //登錄過期
        Disposable loginExpiredRe = RxBus.getDefault().toObservable(LoginExpiredEvent.class)
                .compose(RxUtils.schedulersTransformer())
                .subscribe(event -> {

                    if (AppConfig.userClickOut) {
                        return;
                    }
                    if(this.isFinishing() || this.isDestroyed()){
                        return;
                    }
                    ConfigManager.getInstance().getAppRepository().logout();
                    if (loginExpiredDialog == null) {
                        loginExpiredDialog = MVDialog.getInstance(this)
                                .setContent(getString(R.string.playcc_again_login))
                                .setConfirmText(getString(R.string.playcc_confirm))
                                .setCancelable(true)
                                .setNotClose(true)
                                .setConfirmOnlick(dialog -> {
                                    dialog.dismiss();
                                    TUILogin.logout(new TUICallback() {
                                        @Override
                                        public void onSuccess() {
                                            startWithPopTo(new LoginFragment(), MainFragment.class, true);
                                        }

                                        @Override
                                        public void onError(int errorCode, String errorMessage) {
                                            startWithPopTo(new LoginFragment(), MainFragment.class, true);
                                        }
                                    });
                                })
                                .chooseType(MVDialog.TypeEnum.CENTERWARNED);
                    }
                    if(this.isFinishing() || this.isDestroyed()){
                        return;
                    }
                    if (!loginExpiredDialog.isShowing()) {
                        loginExpiredDialog.show();
                    }
                });
        RxSubscriptions.add(loginExpiredRe);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
        // 设置无动画
        // return new DefaultNoAnimator();
        // 设置自定义动画
        // return new FragmentAnimator(enter,exit,popEnter,popExit);

        // 默认竖向(和安卓5.0以上的动画相同)
//        return super.onCreateFragmentAnimator();
    }

    @Override
    public void onBackPressedSupport() {
        ISupportFragment topFragment = getTopFragment();

        // 主页的Fragment
        if (topFragment instanceof MainFragment) {
        }

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressedSupport();
        } else {
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                this.finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                ToastUtils.showShort(R.string.playcc_exit_app);
            }
        }
    }

    public void isLaunchMain() {
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            // 如果当前 Activity 是通过桌面图标启动进入的
            if (intent != null && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                    && Intent.ACTION_MAIN.equals(intent.getAction())) {
                // 对当前 Activity 执行销毁操作，避免重复实例化入口
                finish();
                return;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            AppContext.instance().mFirebaseAnalytics.setCurrentScreen(this, "Screen Name", this.getClass().getSimpleName());
            //页面处于可见状态最后依次连接时间
//            long onResumeLastTime = System.currentTimeMillis() / 1000;
//            if(onCreateTime != 0L){
//                //页面可见时间 - 页面创建时间 < 10秒。说明再次进入。继续查询订单
//                if(onResumeLastTime - onCreateTime  >= 10){
//                    queryAndConsumePurchase();
//                }
//            }
        }catch(Exception ignored){

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }

    //查询最近的购买交易，并消耗商品
    public void queryAndConsumePurchase() {
        long onResumeLastTime = System.currentTimeMillis() / 1000;
        if(onCreateTime != 0L){
            //页面可见时间 - 页面创建时间 < 10秒。说明再次进入。继续查询订单
            if(onResumeLastTime - onCreateTime  >= 10){
                onCreateTime = onResumeLastTime;
            }else {
                return;
            }
        }
        //queryPurchases() 方法会使用 Google Play 商店应用的缓存，而不会发起网络请求
        billingClientLifecycle.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS);
        //购买商品上报补偿机制
        billingClientLifecycle.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AutoSizeUtils.closeAdapt(this.getResources());
    }
}
