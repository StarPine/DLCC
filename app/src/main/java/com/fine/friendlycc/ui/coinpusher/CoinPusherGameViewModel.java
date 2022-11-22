package com.fine.friendlycc.ui.coinpusher;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;

import com.blankj.utilcode.util.ObjectUtils;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.bean.CoinPusherBalanceDataBean;
import com.fine.friendlycc.bean.CoinPusherDataInfoBean;
import com.fine.friendlycc.event.CoinPusherGamePlayingEvent;
import com.fine.friendlycc.manager.V2TIMCustomManagerUtil;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMCustomElem;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tuicore.custom.CustomConstants;
import com.tencent.qcloud.tuicore.custom.CustomConvertUtils;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;
import com.tencent.qcloud.tuikit.tuichat.util.ChatMessageBuilder;

import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.StringUtils;

/**
 * Author: 彭石林
 * Time: 2022/8/26 11:08
 * Description: This is CoinPusherGameViewModel
 */
public class CoinPusherGameViewModel extends BaseViewModel <AppRepository> {
    //加载状态
    public final String loadingPlayer = "loadingPlayer";
    //游戏状态
    public String gamePlayingState;

    //livedata页面交互
    public UIChangeObservable gameUI = new UIChangeObservable();
    public ObservableInt totalMoney = new ObservableInt(0);
    public CoinPusherDataInfoBean coinPusherDataInfoEntity;

    //消费者
    private Disposable coinPusherGamePlayingSubscription;

    private IMAdvancedMsgListener imAdvancedMsgListener;

    public CoinPusherGameViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
    }
    //关闭页面点击
    public BindingCommand<Void> gameCloseView = new BindingCommand<>(()->gameUI.backViewApply.call());

    public BindingCommand<Void> playCoinClick = new BindingCommand<>(() -> {
        playingCoinPusherThrowCoin(coinPusherDataInfoEntity.getRoomInfo().getRoomId());
    });

    public BindingCommand<Void> playPusherActClick = new BindingCommand<>(() -> {
        playingCoinPusherAct(coinPusherDataInfoEntity.getRoomInfo().getRoomId());
    });

    //是否是小游戏状态
    private boolean isLittleGameWinning(){
        if(StringUtils.isEmpty(gamePlayingState)){
            return false;
        }
        return gamePlayingState.equals(CustomConstants.CoinPusher.LITTLE_GAME_WINNING);
    }
    //投币
    public void playingCoinPusherThrowCoin(Integer roomId){
        //当前处于中奖状态、并且是小游戏中奖状态 什么都不处理
        if(isLittleGameWinning()){
            gamePlayingState = CustomConstants.CoinPusher.LITTLE_GAME_WINNING;
        }else{
            gamePlayingState = loadingPlayer;
        }
        model.playingCoinPusherThrowCoin(roomId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> {
                    loadingShow();
                    //禁用投币按钮
                    gameUI.playingBtnEnable.postValue(false);
                })
                .subscribe(new BaseObserver<BaseDataResponse<CoinPusherBalanceDataBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CoinPusherBalanceDataBean> coinPusherDataEntityResponse) {
                        CoinPusherBalanceDataBean coinPusherBalanceDataEntity = coinPusherDataEntityResponse.getData();
                        if(ObjectUtils.isNotEmpty(coinPusherBalanceDataEntity)){
                            totalMoney.set(coinPusherBalanceDataEntity.getTotalGold());
                        }
                        gameUI.playingBtnEnable.postValue(true);
                        //当前处于中奖状态、并且是小游戏中奖
                        if(isLittleGameWinning()){
                            //取消倒计时
                            gameUI.cancelDownTimeEvent.postValue(null);
                        }else{
                            //否则重新开始倒计时。且清除游戏状态
                            gameUI.resetDownTimeEvent.postValue(null);
                            gamePlayingState = null;
                        }

                    }

                    @Override
                    public void onError(RequestException e) {
                        //余额不足
                        if(e.getCode() == 21001){
                            gameUI.payDialogViewEvent.call();
                            gameUI.playingBtnEnable.postValue(true);
                            if(!isLittleGameWinning()){
                                //清除当前投币状态
                                gamePlayingState = null;
                            }
                        }else if(e.getCode() == 72000){
                            //中奖--置灰并停止倒计时
                            gameUI.playingBtnEnable.postValue(false);
                            //开始落币
                            gamePlayingState = CustomConstants.CoinPusher.START_WINNING;
                            gameUI.cancelDownTimeEvent.postValue(null);
                        }else{
                            gameUI.playingBtnEnable.postValue(true);
                            //清除当前投币状态
                            gamePlayingState = null;
                        }
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        loadingHide();
                    }
                });
    }
    //操作雨刷
    public void playingCoinPusherAct(Integer roomId){
        model.playingCoinPusherAct(roomId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> loadingShow())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {

                    }

                    @Override
                    public void onComplete() {
                        loadingHide();
                    }
                });
    }


    //查询用户当前余额
    public void qryUserGameBalance(){
        model.qryUserGameBalance()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<CoinPusherBalanceDataBean>>(){
                    @Override
                    public void onSuccess(BaseDataResponse<CoinPusherBalanceDataBean> coinPusherBalanceDataEntityResponse) {
                        CoinPusherBalanceDataBean coinPusherBalanceDataEntity = coinPusherBalanceDataEntityResponse.getData();
                        if(ObjectUtils.isNotEmpty(coinPusherBalanceDataEntity)){
                            totalMoney.set(coinPusherBalanceDataEntity.getTotalGold());
                            gameUI.resetDownTimeEvent.postValue(null);
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissHUD();
                    }
                });
    }

    public String tvTotalMoneyRefresh(int moneyNum){
        return moneyNum > 99999 ? "99999+" : moneyNum+"";
    }

    public static class UIChangeObservable{
        //取消倒计时
        public SingleLiveEvent<Void> cancelDownTimeEvent = new SingleLiveEvent<>();
        //重置倒计时
        public SingleLiveEvent<Void> resetDownTimeEvent = new SingleLiveEvent<>();
        //开始显示loading进度条
        public SingleLiveEvent<Void> loadingShow = new SingleLiveEvent<>();
        //关闭进度条Loading显示
        public SingleLiveEvent<Void> loadingHide = new SingleLiveEvent<>();
        //toast弹窗居中
        public SingleLiveEvent<CoinPusherGamePlayingEvent> toastCenter = new SingleLiveEvent<>();
        //禁止投币按钮操作
        public SingleLiveEvent<Boolean> playingBtnEnable = new SingleLiveEvent<>();
        //返回上一页
        public SingleLiveEvent<Void> backViewApply = new SingleLiveEvent<>();
        //余额不足。弹出充值弹窗
        public SingleLiveEvent<Void> payDialogViewEvent = new SingleLiveEvent<>();
    }
    //显示loading
    public void loadingShow(){
        gameUI.loadingShow.call();
    }
    //隐藏loading
    public void loadingHide(){
        gameUI.loadingHide.call();
    }

    //添加IM消息监听器
    public void initIMListener () {
        if(imAdvancedMsgListener==null){
            imAdvancedMsgListener = new IMAdvancedMsgListener();
            V2TIMManager.getMessageManager().addAdvancedMsgListener(imAdvancedMsgListener);
        }
    }

    //移除IM消息监听
    public void removeIMListener(){
        if(imAdvancedMsgListener!=null){
            V2TIMManager.getMessageManager().removeAdvancedMsgListener(imAdvancedMsgListener);
        }
    }

    private static class IMAdvancedMsgListener extends V2TIMAdvancedMsgListener {
        @Override
        public void onRecvNewMessage(V2TIMMessage msg) {
            TUIMessageBean info = ChatMessageBuilder.buildMessage(msg);
            if (info != null) {
                if (info.getMsgType() == 2) { //自定义消息类型
                    V2TIMCustomElem v2TIMCustomElem = info.getV2TIMMessage().getCustomElem();
                    Map<String, Object> contentBody = CustomConvertUtils.CustomMassageConvertMap(v2TIMCustomElem);
                    if (ObjectUtils.isNotEmpty(contentBody)) {
                        //模块类型--判断
                        if (contentBody.containsKey(CustomConstants.Message.MODULE_NAME_KEY)) {
                            //获取moudle-pushCoinGame 推币机
                            if (CustomConvertUtils.ContainsMessageModuleKey(contentBody, CustomConstants.Message.MODULE_NAME_KEY, CustomConstants.CoinPusher.MODULE_NAME)) {
                                V2TIMCustomManagerUtil.CoinPusherManager(contentBody);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void registerRxBus() {
        initIMListener();
        coinPusherGamePlayingSubscription = RxBus.getDefault().toObservable(CoinPusherGamePlayingEvent.class)
                .subscribe(coinPusherGamePlayingEvent -> {
                    if(coinPusherGamePlayingEvent!=null){
                        switch (coinPusherGamePlayingEvent.getState()) {
                            case CustomConstants.CoinPusher.START_WINNING:
                                //开始落币
                                gamePlayingState = CustomConstants.CoinPusher.START_WINNING;
                                gameUI.cancelDownTimeEvent.postValue(null);
                                gameUI.playingBtnEnable.postValue(false);
                                break;
                            case CustomConstants.CoinPusher.END_WINNING:
                                //落币结束
                                gamePlayingState = null;
                                gameUI.resetDownTimeEvent.postValue(null);
                                gameUI.playingBtnEnable.postValue(true);
                                break;
                            case CustomConstants.CoinPusher.DROP_COINS:
                                //落币奖励
                                //gamePlayingState = null;
                                gameUI.toastCenter.postValue(coinPusherGamePlayingEvent);
                                break;
                            case CustomConstants.CoinPusher.LITTLE_GAME_WINNING:
                                //中奖 小游戏（叠叠乐、小玛利）
                                gamePlayingState = CustomConstants.CoinPusher.LITTLE_GAME_WINNING;
                                gameUI.cancelDownTimeEvent.postValue(null);
                                break;
                        }
                    }
                });
        //将订阅者加入管理站
        RxSubscriptions.add(coinPusherGamePlayingSubscription);
    }

    @Override
    public void removeRxBus() {
        removeIMListener();
        RxSubscriptions.remove(coinPusherGamePlayingSubscription);
    }
}