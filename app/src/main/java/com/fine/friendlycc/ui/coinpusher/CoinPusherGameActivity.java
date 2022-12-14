package com.fine.friendlycc.ui.coinpusher;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.ColorUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.databinding.ActivityCoinpusherGameBinding;
import com.fine.friendlycc.bean.CoinPusherBalanceDataBean;
import com.fine.friendlycc.bean.CoinPusherDataInfoBean;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.manager.LocaleManager;
import com.fine.friendlycc.ui.base.BaseActivity;
import com.fine.friendlycc.ui.coinpusher.dialog.CoinPusherConvertDialog;
import com.fine.friendlycc.ui.coinpusher.dialog.CoinPusherDialogAdapter;
import com.fine.friendlycc.ui.coinpusher.dialog.CoinPusherGameHistoryDialog;
import com.fine.friendlycc.ui.coinpusher.dialog.CoinPusherHelpDialog;
import com.fine.friendlycc.ui.mine.wallet.diamond.recharge.DialogDiamondRechargeActivity;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.utils.CoinPusherApiUtil;
import com.fine.friendlycc.utils.ImmersionBarUtils;
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

import me.goldze.mvvmhabit.utils.StringUtils;

/**
 * Author: ?????????
 * Time: 2022/8/26 11:07
 * Description: ?????????????????????
 */
public class CoinPusherGameActivity extends BaseActivity<ActivityCoinpusherGameBinding,CoinPusherGameViewModel> {

    //????????? 30???
    public static CountDownTimer downTimer = null;

    private final String TAG = "CoinPusherGameActivity";
    //????????????
    private CoinPusherHelpDialog dialogCoinPusherHelp = null;
    //????????????
    private CoinPusherConvertDialog coinPusherConvertDialog = null;
    //????????????
    private CoinPusherGameHistoryDialog coinPusherGameHistoryDialog = null;

    //?????????30???
    private  long downTimeMillisInFuture = 30;
    //?????????????????????????????????
    private  long downTimeMillisHint = 10;
    //??????????????????
    private boolean downTimeMillisHintFlag = false;

    private CoinPusherDataInfoBean coinPusherDataInfoEntity;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleManager.setLocal(newBase));
    }

    /**
     * ????????????Manifest.xml??????????????????????????????????????????????????????????????????????????????

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
        CCApplication.isCalling = true;
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
           coinPusherDataInfoEntity = (CoinPusherDataInfoBean) intent.getSerializableExtra("CoinPusherInfo");
           //?????????????????????????????????
           downTimeMillisInFuture = coinPusherDataInfoEntity.getOutTime();
           ////?????????????????????????????????
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
        //???????????? id,??????????????????????????? id ?????????
        webrtcParam.setCustomerID(viewModel.coinPusherDataInfoEntity.getClientWsRtcId());
        //???????????????
        webrtcParam.setStreamUrl(viewModel.coinPusherDataInfoEntity.getRtcUrl());//http://webrtc.pull.azskj.cn/live/tbtest-39.sdp
        //?????????????????? dtls ????????????????????????false????????????true????????????
        webrtcParam.disableDTLS(false);
        //????????????????????????????????????????????????false????????????true?????????
        webrtcParam.enableHw(false);
        //????????????????????????,?????? OPUS,????????????????????? WsWebRTCParameters ?????????
        webrtcParam.setAudioFormat(WsWebRTCParameters.OPUS);
        //????????????????????????????????? 5s????????? ms
        webrtcParam.setConnTimeOutInMs(10000);
        //???????????? JitterBuffer ????????????????????????????????????????????? 50
        webrtcParam.setAudioJitterBufferMaxPackets(50);
        //??????????????????????????????????????????false???????????????true?????????
        webrtcParam.enableAudioJitterBufferFastAccelerate(true);
        //???????????????????????????????????? 1s????????? ms
        webrtcParam.setPortalReportPeriodInMs(1000);
        //?????? rtc ????????????????????? LOG_NONE???????????????????????? WsWebRTCParameters
        webrtcParam.setLoggingLevel(WsWebRTCParameters.LOG_INFO);
        //?????? rtc ?????????????????????loggable???WsWebRTCParameters.Loggable ?????????
        webrtcParam.setLoggable((s, i, s1) -> {

        });
        WsWebRTCObserver observer = new WsWebRTCObserver() {
            @Override
            public void onWsWebrtcError(String s, ErrCode errCode) {
                if(errCode == ErrCode.ERR_CODE_WEBRTC_DISCONN){
                    //????????????
                    SnackUtils.showCenterShort(getContentShowView(),StringUtils.getString(R.string.playcc_network_text));
                }
                Log.e(TAG,"onWsWebrtcError???"+s+"============"+errCode.toString());
            }

            @Override
            public void onFirstPacketReceived(int i) {
                Log.e(TAG,"onFirstPacketReceived???"+i);
            }

            @Override
            public void onFirstFrameRendered() {
            }

            @Override
            public void onResolutionRatioChanged(int i, int i1) {
            }

            @Override
            public void onPortalReport(WsWebRTCPortalReport wsWebRTCPortalReport) {
                //Log.e(TAG,"onPortalReport???"+wsWebRTCPortalReport.toString());
            }

            @Override
            public void onNotifyCaton(int i) {
                Log.e(TAG,"onNotifyCaton???"+i);
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
        binding.tvMoneyHint.setText(String.format(StringUtils.getString(R.string.playcc_coinpusher_game_text_2),coinPusherDataInfoEntity.getRoomInfo().getMoney()));
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
        //???????????????
        downTime();
    }


    @SuppressLint("CheckResult")
    public void doubleClick(View view){
        RxView.clicks(view)
                .throttleFirst(1, TimeUnit.SECONDS)//1????????????????????????1???
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
                        //????????????
                        coinPusherConvertDialogShow();
                    }
                });
    }

    /**
     * @return
     */
    //???????????????dialog????????????
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
     * ???????????????????????????view
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
        //??????????????????
        if(coinPusherConvertDialog == null){
            coinPusherConvertDialog = new CoinPusherConvertDialog(this);
            coinPusherConvertDialog.setCanceledOnTouchOutside(false);
            coinPusherConvertDialog.setItemConvertListener(new CoinPusherConvertDialog.ItemConvertListener() {
                @Override
                public void convertSuccess(CoinPusherBalanceDataBean coinPusherBalanceDataEntity) {
                    viewModel.totalMoney.set(coinPusherBalanceDataEntity.getTotalGold());
                    //???????????????
                    cancelDownTimer();
                    //?????????????????????
                    downTime();
                }

                @Override
                public void buyError() {
                    //??????????????????
                    coinPusherConvertDialog.dismiss();
                    //????????????
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
        //???????????????
        viewModel.gameUI.backViewApply.observe(this, unused -> {
            interceptBackPressed();
        });
        //???????????????
        viewModel.gameUI.resetDownTimeEvent.observe(this, unused -> {
            //???????????????
            cancelDownTimer();
            //?????????????????????
            downTime();
        });
        //????????????loading
        viewModel.gameUI.loadingShow.observe(this,unused->{
            binding.flLayoutLoading.post(()->{
                if(binding.flLayoutLoading.getVisibility() != View.VISIBLE){
                    binding.flLayoutLoading.setVisibility(View.VISIBLE);
                }

            });
        });
        //????????????loading
        viewModel.gameUI.loadingHide.observe(this,unused->{
            binding.flLayoutLoading.post(()->{
                if(binding.flLayoutLoading.getVisibility() != View.GONE){
                    binding.flLayoutLoading.setVisibility(View.GONE);
                }

            });
        });
        //toast????????????
        viewModel.gameUI.toastCenter.observe(this,coinPusherGamePlayingEvent->{
            //????????????
            String textContent = com.blankj.utilcode.util.StringUtils.getString(R.string.playcc_coinpusher_coin_text_reward);
            String valueText = String.format(textContent, coinPusherGamePlayingEvent.getGoldNumber());
            viewModel.totalMoney.set(coinPusherGamePlayingEvent.getTotalGold());
            SnackUtils.showCenterShort(getContentShowView(),valueText);
        });
        //????????????????????????
        viewModel.gameUI.playingBtnEnable.observe(this, flag ->{
            binding.btnPlaying.setEnabled(flag);
            if(flag){
                binding.btnPlaying.setTextColor(ColorUtils.getColor(R.color.black));
            }else{
                binding.btnPlaying.setTextColor(ColorUtils.getColor(R.color.yellow_548));
            }
        });
        //???????????????
        viewModel.gameUI.cancelDownTimeEvent.observe(this, unused -> {
            //???????????????
            cancelDownTimer();
        });
        //??????????????????
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
            //???????????????????????????
            binding.WebRtcSurfaceView.stop();
            binding.WebRtcSurfaceView.uninitilize();
        }catch (Exception ignored) {

        }
        super.onDestroy();
    }

    /**
    * @Desc TODO(??????????????????)
    * @author ?????????
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
            if(viewModel.gamePlayingState==null){ //????????????
                super.onBackPressed();
            }else{
                Integer stringResId = null;
                if(viewModel.gamePlayingState.equals(viewModel.loadingPlayer)){//????????????
                    stringResId = R.string.playcc_coinpusher_hint_retain;
                }else if(viewModel.gamePlayingState.equals(CustomConstants.CoinPusher.START_WINNING)){ //????????????
                    stringResId = R.string.playcc_coinpusher_hint_retain2;
                }else if(viewModel.gamePlayingState.equals(CustomConstants.CoinPusher.LITTLE_GAME_WINNING)){
                    //???????????????
                    stringResId = R.string.playcc_coinpusher_hint_retain3;
                }
                if(stringResId != null){
                    CoinPusherDialogAdapter.getDialogCoinPusherRetainHint(this, stringResId, new CoinPusherDialogAdapter.CoinPusherDialogListener() {
                        @Override
                        public void onConfirm(Dialog dialog) {
                            dialog.dismiss();
                            //??????????????????
                            finish();
                        }
                    }).show();
                }
            }
        }else{
            super.onBackPressed();
        }
    }

    //???????????????
    private void downTime() {
        downTimer = new CountDownTimer(downTimeMillisInFuture * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(!downTimeMillisHintFlag){
                    if(millisUntilFinished / 1000 <= downTimeMillisHint){
                        downTimeMillisHintFlag = true;
                        SnackUtils.showCenterShort(getContentShowView(),String.format(StringUtils.getString(R.string.playcc_coinpusher_text_downtime),millisUntilFinished/1000));
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
    //???????????????
    private void cancelDownTimer() {
        downTimeMillisHintFlag = false;
        if(downTimer!=null){
            downTimer.cancel();
            downTimer = null;
        }
    }
    //????????????
    private void payCoinRechargeDialog(){
        Intent intent = new Intent(this, DialogDiamondRechargeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pop_enter_anim, 0);
    }
}