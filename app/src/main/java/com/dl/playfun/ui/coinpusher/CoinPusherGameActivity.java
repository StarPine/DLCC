package com.dl.playfun.ui.coinpusher;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.ColorUtils;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.app.AppConfig;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.databinding.ActivityCoinpusherGameBinding;
import com.dl.playfun.entity.CoinPusherBalanceDataEntity;
import com.dl.playfun.entity.CoinPusherDataInfoEntity;
import com.dl.playfun.entity.GoodsEntity;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.manager.LocaleManager;
import com.dl.playfun.ui.base.BaseActivity;
import com.dl.playfun.ui.coinpusher.dialog.CoinPusherConvertDialog;
import com.dl.playfun.ui.coinpusher.dialog.CoinPusherDialogAdapter;
import com.dl.playfun.ui.coinpusher.dialog.CoinPusherGameHistoryDialog;
import com.dl.playfun.ui.coinpusher.dialog.CoinPusherHelpDialog;
import com.dl.playfun.utils.AutoSizeUtils;
import com.dl.playfun.utils.CoinPusherApiUtil;
import com.dl.playfun.utils.ImmersionBarUtils;
import com.dl.playfun.widget.coinrechargesheet.CoinRechargeSheetView;
import com.jakewharton.rxbinding2.view.RxView;
import com.misterp.toast.SnackUtils;
import com.tencent.liteav.trtccalling.ui.floatwindow.FloatWindowService;
import com.tencent.qcloud.tuicore.custom.CustomConstants;
import com.wangsu.libwswebrtc.WsWebRTCObserver;
import com.wangsu.libwswebrtc.WsWebRTCParameters;
import com.wangsu.libwswebrtc.WsWebRTCPortalReport;
import com.wangsu.libwswebrtc.WsWebRTCView;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.utils.StringUtils;

/**
 * Author: 彭石林
 * Time: 2022/8/26 11:07
 * Description: 推币机游戏页面
 */
public class CoinPusherGameActivity extends BaseActivity<ActivityCoinpusherGameBinding,CoinPusherGameViewModel> {

    //倒计时 30秒
    public static CountDownTimer downTimer = null;

    private final String TAG = "CoinPusherGameActivity";
    //玩法说明
    private CoinPusherHelpDialog dialogCoinPusherHelp = null;
    //兑换列表
    private CoinPusherConvertDialog coinPusherConvertDialog = null;
    //历史记录
    private CoinPusherGameHistoryDialog coinPusherGameHistoryDialog = null;

    //倒计时30秒
    private  long downTimeMillisInFuture = 30;
    //倒计时剩余多少时间提示
    private  long downTimeMillisHint = 10;
    //提示状态标识
    private boolean downTimeMillisHintFlag = false;

    private CoinPusherDataInfoEntity coinPusherDataInfoEntity;

    //充值弹窗
    private CoinRechargeSheetView coinRechargeSheetView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleManager.setLocal(newBase));
    }

    /**
     * 就算你在Manifest.xml设置横竖屏切换不重走生命周期。横竖屏切换还是会走这里

     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        if(newConfig!=null){
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

    @Override
    protected void onResume() {
        super.onResume();
        FloatWindowService.stopService(this);
        AppContext.isCalling = true;
        ImmersionBarUtils.setupStatusBar(this, false, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ImmersionBarUtils.setupStatusBar(this, true, true);
    }



    @Override
    public int initContentView(Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(getResources());
        return R.layout.activity_coinpusher_game;
    }

    @Override
    public int initVariableId() {
        return BR.gameViewModel;
    }

    @Override
    public void initParam() {
        super.initParam();
       Intent intent =  getIntent();
       if(intent!=null){
           coinPusherDataInfoEntity = (CoinPusherDataInfoEntity) intent.getSerializableExtra("CoinPusherInfo");
           //倒计时多少时间结束游戏
           downTimeMillisInFuture = coinPusherDataInfoEntity.getOutTime();
           ////倒计时剩余多少时间提示
           downTimeMillisHint = coinPusherDataInfoEntity.getCountdown();
       }
    }

    @Override
    public CoinPusherGameViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(CoinPusherGameViewModel.class);
    }

    private void gameInit() {
        LoadingVideoShow(true);
        binding.flLayoutLoadingVideo.setVisibility(View.VISIBLE);
        WsWebRTCView webrtcView = binding.WebRtcSurfaceView;
        WsWebRTCParameters webrtcParam = new WsWebRTCParameters();
        //设置客户 id,由网宿分配给客户的 id 字符串
        webrtcParam.setCustomerID(viewModel.coinPusherDataInfoEntity.getClientWsRtcId());
        //设置播放流
        webrtcParam.setStreamUrl(viewModel.coinPusherDataInfoEntity.getRtcUrl());//http://webrtc.pull.azskj.cn/live/tbtest-39.sdp
        //设置是否使用 dtls 加密，默认加密，false：加密；true：不加密
        webrtcParam.disableDTLS(false);
        //设置视频是否使用硬解，默认硬解，false：软解；true：硬解
        webrtcParam.enableHw(false);
        //设置音频使用格式,默认 OPUS,音频格式类型见 WsWebRTCParameters 类定义
        webrtcParam.setAudioFormat(WsWebRTCParameters.OPUS);
        //设置连接超时时间，默认 5s，单位 ms
        webrtcParam.setConnTimeOutInMs(10000);
        //设置音频 JitterBuffer 队列最大报文数，影响时延，默认 50
        webrtcParam.setAudioJitterBufferMaxPackets(50);
        //设置是否开启追帧，默认开启，false：不开启；true：开启
        webrtcParam.enableAudioJitterBufferFastAccelerate(true);
        //设置统计值回调频率，默认 1s，单位 ms
        webrtcParam.setPortalReportPeriodInMs(1000);
        //设置 rtc 日志等级，默认 LOG_NONE，日志等级类型见 WsWebRTCParameters
        webrtcParam.setLoggingLevel(WsWebRTCParameters.LOG_INFO);
        //设置 rtc 日志回调函数，loggable：WsWebRTCParameters.Loggable 对象，
        webrtcParam.setLoggable((s, i, s1) -> {

        });
        WsWebRTCObserver observer = new WsWebRTCObserver() {
            @Override
            public void onWsWebrtcError(String s, ErrCode errCode) {
                if(errCode == ErrCode.ERR_CODE_WEBRTC_DISCONN){
                    //链接断开
                    SnackUtils.showCenterShort(getContentShowView(),StringUtils.getString(R.string.playfun_network_text));
                }
                Log.e(TAG,"onWsWebrtcError："+s+"============"+errCode.toString());
            }

            @Override
            public void onFirstPacketReceived(int i) {
                Log.e(TAG,"onFirstPacketReceived："+i);
            }

            @Override
            public void onFirstFrameRendered() {
            }

            @Override
            public void onResolutionRatioChanged(int i, int i1) {
            }

            @Override
            public void onPortalReport(WsWebRTCPortalReport wsWebRTCPortalReport) {
                //Log.e(TAG,"onPortalReport："+wsWebRTCPortalReport.toString());
            }

            @Override
            public void onNotifyCaton(int i) {
                Log.e(TAG,"onNotifyCaton："+i);
            }

            @Override
            public void onEventSEIReceived(ByteBuffer byteBuffer) {
            }

            @Override
            public void onEventConnected() {
                LoadingVideoShow(false);
            }
        };
        webrtcView.initilize(webrtcParam, observer);
        webrtcView.start();
    }

    private void LoadingVideoShow(boolean isShow){
        binding.flLayoutLoadingVideo.post(()-> binding.flLayoutLoadingVideo.setVisibility(isShow ? View.VISIBLE : View.GONE));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initData() {
        super.initData();
        viewModel.coinPusherDataInfoEntity = coinPusherDataInfoEntity;
        viewModel.totalMoney.set(coinPusherDataInfoEntity.getTotalGold());
        binding.tvMoneyHint.setText(String.format(StringUtils.getString(R.string.playfun_coinpusher_game_text_2),coinPusherDataInfoEntity.getRoomInfo().getMoney()));
        doubleClick(binding.imgHelp);
        doubleClick(binding.rlCoin);
        doubleClick(binding.imgHistroy);
        binding.btnPlaying.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                binding.svgaPlayer.setVisibility(View.VISIBLE);
                binding.svgaPlayer.startAnimation();
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                binding.svgaPlayer.stopAnimation();
                binding.svgaPlayer.setVisibility(View.GONE);
            }
            return false;
        });
        gameInit();
        //开始倒计时
        downTime();
    }


    @SuppressLint("CheckResult")
    public void doubleClick(View view){
        RxView.clicks(view)
                .throttleFirst(1, TimeUnit.SECONDS)//1秒钟内只允许点击1次
                .subscribe(o -> {
                    if (view.getId() == binding.imgHelp.getId()) {
                        String helpWebUrl = ConfigManager.getInstance().getAppRepository().readApiConfigManagerEntity().getPlayChatWebUrl()+"/coinPusherGame/helpExplain/";
                        dialogCoinPusherHelp = new CoinPusherHelpDialog(CoinPusherGameActivity.this,helpWebUrl );
                        dialogCoinPusherHelp.setCanceledOnTouchOutside(false);
                        dialogCoinPusherHelp.setOnDismissListener(dialog -> {
                            if(dialogCoinPusherHelp != null){
                                dialogCoinPusherHelp.dismissHud();
                            }
                        });
                        if(!otherDigLogIsShowing()){
                            dialogCoinPusherHelp.show();
                        }

                    }else if(view.getId() == binding.imgHistroy.getId()){
                        if(coinPusherGameHistoryDialog == null){
                            coinPusherGameHistoryDialog = new CoinPusherGameHistoryDialog(CoinPusherGameActivity.this,viewModel.coinPusherDataInfoEntity.getRoomInfo());
                            coinPusherGameHistoryDialog.setOnDismissListener(dialog -> {
                                if(coinPusherGameHistoryDialog!=null){
                                    coinPusherGameHistoryDialog.dismissHud();
                                }
                            });
                            coinPusherGameHistoryDialog.setCanceledOnTouchOutside(false);
                        }else{
                            coinPusherGameHistoryDialog.loadData(viewModel.coinPusherDataInfoEntity.getRoomInfo().getRoomId());
                        }
                        if(!otherDigLogIsShowing()){
                            coinPusherGameHistoryDialog.show();
                        }
                    }else if(view.getId() == binding.rlCoin.getId()){
                        //弹出兑换
                        coinPusherConvertDialogShow();
                    }
                });
    }

    /**
     * @return
     */
    //是否有其它dialog正在显示
    private boolean otherDigLogIsShowing() {
        if(coinPusherGameHistoryDialog!=null && coinPusherGameHistoryDialog.isShowing()){
            return true;
        }
        if(dialogCoinPusherHelp!=null && dialogCoinPusherHelp.isShowing()){
            return true;
        }
        if(coinPusherConvertDialog!=null && coinPusherConvertDialog.isShowing()){
            return true;
        }
        return false;
    }


    /**
     * 获取当前窗体显示的view
     */
    public View getContentShowView() {
        if(coinPusherGameHistoryDialog!=null && coinPusherGameHistoryDialog.isShowing()){
            return coinPusherGameHistoryDialog.getWindow().getDecorView();
        }
        if(dialogCoinPusherHelp!=null && dialogCoinPusherHelp.isShowing()){
            return dialogCoinPusherHelp.getWindow().getDecorView();
        }
        if(coinPusherConvertDialog!=null && coinPusherConvertDialog.isShowing()){
            return coinPusherConvertDialog.getWindow().getDecorView();
        }
        return binding.getRoot();
    }

    private void coinPusherConvertDialogShow(){
        //购买兑换金币
        if(coinPusherConvertDialog == null){
            coinPusherConvertDialog = new CoinPusherConvertDialog(this);
            coinPusherConvertDialog.setCanceledOnTouchOutside(false);
            coinPusherConvertDialog.setItemConvertListener(new CoinPusherConvertDialog.ItemConvertListener() {
                @Override
                public void convertSuccess(CoinPusherBalanceDataEntity coinPusherBalanceDataEntity) {
                    viewModel.totalMoney.set(coinPusherBalanceDataEntity.getTotalGold());
                    //取消倒计时
                    cancelDownTimer();
                    //重新开始倒计时
                    downTime();
                }

                @Override
                public void buyError() {
                    //关闭当前弹窗
                    coinPusherConvertDialog.dismiss();
                    //购买充值
                    payCoinRechargeDialog();
                }
            });
        }else{
            coinPusherConvertDialog.loadData();
        }
        if(!otherDigLogIsShowing()){
            coinPusherConvertDialog.show();
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        //返回上一页
        viewModel.gameUI.backViewApply.observe(this, unused -> {
            interceptBackPressed();
        });
        //取消倒计时
        viewModel.gameUI.resetDownTimeEvent.observe(this, unused -> {
            //取消倒计时
            cancelDownTimer();
            //重新开始倒计时
            downTime();
        });
        //开始显示loading
        viewModel.gameUI.loadingShow.observe(this,unused->{
            binding.flLayoutLoading.post(()->{
                if(binding.flLayoutLoading.getVisibility() != View.VISIBLE){
                    binding.flLayoutLoading.setVisibility(View.VISIBLE);
                }

            });
        });
        //取消显示loading
        viewModel.gameUI.loadingHide.observe(this,unused->{
            binding.flLayoutLoading.post(()->{
                if(binding.flLayoutLoading.getVisibility() != View.GONE){
                    binding.flLayoutLoading.setVisibility(View.GONE);
                }

            });
        });
        //toast弹窗居中
        viewModel.gameUI.toastCenter.observe(this,coinPusherGamePlayingEvent->{
            //中奖落币
            String textContent = com.blankj.utilcode.util.StringUtils.getString(R.string.playfun_coinpusher_coin_text_reward);
            String valueText = String.format(textContent, coinPusherGamePlayingEvent.getGoldNumber());
            viewModel.totalMoney.set(coinPusherGamePlayingEvent.getTotalGold());
            SnackUtils.showCenterShort(getContentShowView(),valueText);
        });
        //是否禁用投币按钮
        viewModel.gameUI.playingBtnEnable.observe(this, flag ->{
            binding.btnPlaying.setEnabled(flag);
            if(flag){
                binding.btnPlaying.setTextColor(ColorUtils.getColor(R.color.black));
            }else{
                binding.btnPlaying.setTextColor(ColorUtils.getColor(R.color.yellow_548));
            }
        });
        //取消倒计时
        viewModel.gameUI.cancelDownTimeEvent.observe(this, unused -> {
            //取消倒计时
            cancelDownTimer();
        });
        //拉起充值弹窗
        viewModel.gameUI.payDialogViewEvent.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                coinPusherConvertDialogShow();
            }
        });
    }

    @Override
    public  void onDestroy() {
        AutoSizeUtils.closeAdapt(getResources());
        if(downTimer!=null){
            downTimer.cancel();
            downTimer = null;
        }
        CoinPusherApiUtil.endGamePaying(viewModel.coinPusherDataInfoEntity.getRoomInfo().getRoomId());
        try {
            if(coinRechargeSheetView!=null){
                if(coinRechargeSheetView.isShowing()){
                    coinRechargeSheetView.dismiss();
                }
                coinRechargeSheetView = null;
            }
            if(dialogCoinPusherHelp != null){
                dialogCoinPusherHelp.destroy();
                if(dialogCoinPusherHelp.isShowing()){
                    dialogCoinPusherHelp.dismiss();
                }
                dialogCoinPusherHelp = null;
            }
            if(coinPusherConvertDialog != null){
                if(coinPusherConvertDialog.isShowing()){
                    coinPusherConvertDialog.dismiss();
                }
                coinPusherConvertDialog.dismiss();
                coinPusherConvertDialog = null;
            }
            if(coinPusherGameHistoryDialog != null){
                if(coinPusherGameHistoryDialog.isShowing()){
                    coinPusherGameHistoryDialog.dismiss();
                }
                coinPusherGameHistoryDialog = null;
            }
            //暂停播放。释放资源
            binding.WebRtcSurfaceView.stop();
            binding.WebRtcSurfaceView.uninitilize();
        }catch (Exception ignored) {

        }
        super.onDestroy();
    }

    /**
    * @Desc TODO(拦截返回按键)
    * @author 彭石林
    * @parame []
    * @return void
    * @Date 2022/9/6
    */
    @Override
    public void onBackPressed() {
        interceptBackPressed();
    }

    private void interceptBackPressed(){
        if(viewModel!=null){
            if(viewModel.gamePlayingState==null){ //状态为空
                super.onBackPressed();
            }else{
                Integer stringResId = null;
                if(viewModel.gamePlayingState.equals(viewModel.loadingPlayer)){//投币状态
                    stringResId = R.string.playfun_coinpusher_hint_retain;
                }else if(viewModel.gamePlayingState.equals(CustomConstants.CoinPusher.START_WINNING)){ //落币状态
                    stringResId = R.string.playfun_coinpusher_hint_retain2;
                }else if(viewModel.gamePlayingState.equals(CustomConstants.CoinPusher.LITTLE_GAME_WINNING)){
                    //小游戏提示
                    stringResId = R.string.playfun_coinpusher_hint_retain3;
                }
                if(stringResId != null){
                    CoinPusherDialogAdapter.getDialogCoinPusherRetainHint(this, stringResId, new CoinPusherDialogAdapter.CoinPusherDialogListener() {
                        @Override
                        public void onConfirm(Dialog dialog) {
                            dialog.dismiss();
                            //结束当前页面
                            finish();
                        }
                    }).show();
                }
            }
        }else{
            super.onBackPressed();
        }
    }

    //倒计时开始
    private void downTime() {
        downTimer = new CountDownTimer(downTimeMillisInFuture * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(!downTimeMillisHintFlag){
                    if(millisUntilFinished / 1000 <= downTimeMillisHint){
                        downTimeMillisHintFlag = true;
                        SnackUtils.showCenterShort(getContentShowView(),String.format(StringUtils.getString(R.string.playfun_coinpusher_text_downtime),millisUntilFinished/1000));
                    }
                }

            }

            @Override
            public void onFinish() {
                if(downTimeMillisHintFlag){
                    AppConfig.CoinPusherGameNotPushed = true;
                }
                finish();
            }
        };
        downTimer.start();
    }
    //取消倒计时
    private void cancelDownTimer() {
        downTimeMillisHintFlag = false;
        if(downTimer!=null){
            downTimer.cancel();
            downTimer = null;
        }
    }
    //充值弹窗
    private void payCoinRechargeDialog(){
        if (coinRechargeSheetView == null){
            coinRechargeSheetView = new CoinRechargeSheetView(this);
            coinRechargeSheetView.setClickListener(new CoinRechargeSheetView.ClickListener() {
                @Override
                public void paySuccess(GoodsEntity goodsEntity) {
                    //充值成功  查询当前用户余额
                    //viewModel.qryUserGameBalance();
                }
            });
        }
        if (!coinRechargeSheetView.isShowing()){
            coinRechargeSheetView.show();
        }
    }
}
