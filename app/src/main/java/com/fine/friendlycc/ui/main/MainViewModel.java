package com.fine.friendlycc.ui.main;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.bean.BubbleBean;
import com.fine.friendlycc.bean.CallingInviteInfo;
import com.fine.friendlycc.bean.DayRewardInfoBean;
import com.fine.friendlycc.bean.MqBroadcastGiftBean;
import com.fine.friendlycc.bean.MqGiftDataBean;
import com.fine.friendlycc.bean.RestartActivityBean;
import com.fine.friendlycc.bean.VersionBean;
import com.fine.friendlycc.event.BubbleTopShowEvent;
import com.fine.friendlycc.event.DailyAccostEvent;
import com.fine.friendlycc.event.MainTabEvent;
import com.fine.friendlycc.event.MessageCountChangeEvent;
import com.fine.friendlycc.event.MessageGiftNewEvent;
import com.fine.friendlycc.event.RewardRedDotEvent;
import com.fine.friendlycc.event.TaskMainTabEvent;
import com.fine.friendlycc.calling.Utils;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.manager.LocationManager;
import com.fine.friendlycc.manager.V2TIMCustomManagerUtil;
import com.fine.friendlycc.utils.FastCallFunUtil;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.google.gson.Gson;
import com.tencent.custom.GiftEntity;
import com.tencent.custom.IMGsonUtils;
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMCustomElem;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tuicore.Status;
import com.tencent.qcloud.tuicore.custom.CustomConstants;
import com.tencent.qcloud.tuicore.custom.entity.VideoEvaluationEntity;
import com.tencent.qcloud.tuicore.custom.entity.VideoPushEntity;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;
import com.tencent.qcloud.tuikit.tuichat.util.ChatMessageBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public class MainViewModel extends BaseViewModel<AppRepository> {

    public ObservableField<String> lockPassword = new ObservableField<>("");
    public ObservableField<Boolean> isHaveRewards = new ObservableField<>(false);
    public List<MqBroadcastGiftBean> publicScreenBannerGiftEntity = new ArrayList<>();
    public boolean playing = false;
    public boolean isShowedReward = true;//今日是否显示过奖励
    public int giveCoin = 0;
    public int videoCard = 0;
    public int chatCardNum = 0;
    public int assostCardNum = 0;
    public int nextGiveCoin = 0;
    public int nextVideoCard = 0;
    public String dayRewardKey = "";

    UIChangeObservable uc = new UIChangeObservable();
    private Disposable mSubscription, taskMainTabEventReceive, mainTabEventReceive, rewardRedDotEventReceive, BubbleTopShowEventSubscription, ResatrtActSubscription2, videoEvaluationSubscription;

    private IMAdvancedMsgListener imAdvancedMsgListener;

    public MainViewModel(@NonNull Application application, AppRepository appRepository) {
        super(application, appRepository);
        if (appRepository.readUserData() != null && !ObjectUtils.isEmpty(appRepository.readUserData().getSex())) {
            uc.gender.set(appRepository.readUserData().getSex() != 1);
        }
    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        initIMListener();
        lockPassword.set(model.readPassword());
        if (!StringUtil.isEmpty(lockPassword.get())) {
            uc.lockDialog.call();
        }
        if (model.readNeedVerifyFace()) {
            uc.showFaceRecognitionDialog.call();
        }
        LocationManager.getInstance().initloadLocation(new LocationManager.LocationListener() {
            @Override
            public void onLocationSuccess(double lat, double lng) {
                loadSendLocation(lat, lng, null, null);//调用上报当前定位
            }

            @Override
            public void onLocationFailed() {
                Log.e("初始化当前定位获取异常：", "");
            }
        });
        sendInviteCode();
        //加载屏蔽字数据
        getSensitiveWords();

        //每日奖励
        dayRewardKey = StringUtil.getDailyFlag("dailyReward");
        String value = model.readKeyValue(dayRewardKey);
        if (value == null){
            isShowedReward = false;
        }else {
            isShowedReward = true;
        }

    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        mSubscription = RxBus.getDefault().toObservable(MessageCountChangeEvent.class)
                .subscribe(messageCountChangeEvent -> uc.allMessageCountChange.postValue(messageCountChangeEvent.getCount()));
        rewardRedDotEventReceive = RxBus.getDefault().toObservable(RewardRedDotEvent.class).subscribe(event -> {
            isHaveRewards.set(event.isHaveReward());
        });

        BubbleTopShowEventSubscription = RxBus.getDefault().toObservable(BubbleTopShowEvent.class).subscribe(event -> {
            uc.bubbleTopShow.postValue(false);
        });
        ResatrtActSubscription2 = RxBus.getDefault().toObservable(RestartActivityBean.class).subscribe(event -> {
            uc.restartActivity.postValue(event.getIntent());
        });
        taskMainTabEventReceive = RxBus.getDefault().toObservable(TaskMainTabEvent.class)
                .compose(RxUtils.exceptionTransformer())
                .compose(RxUtils.schedulersTransformer())
                .subscribe(o -> uc.taskCenterclickTab.postValue(((TaskMainTabEvent) o)));

        //将订阅者加入管理站
        RxSubscriptions.add(taskMainTabEventReceive);
        RxSubscriptions.add(mainTabEventReceive);
        RxSubscriptions.add(rewardRedDotEventReceive);
        RxSubscriptions.add(BubbleTopShowEventSubscription);
        RxSubscriptions.add(ResatrtActSubscription2);
        RxSubscriptions.add(videoEvaluationSubscription);
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(mSubscription);
        RxSubscriptions.remove(taskMainTabEventReceive);
        RxSubscriptions.remove(mainTabEventReceive);
        RxSubscriptions.remove(rewardRedDotEventReceive);
        RxSubscriptions.remove(BubbleTopShowEventSubscription);
        RxSubscriptions.remove(ResatrtActSubscription2);
        RxSubscriptions.remove(videoEvaluationSubscription);
        removeIMListener();
    }

    public void logout() {
        model.logout();
        //startWithPopTo(LoginFragment.class.getCanonicalName(), MainFragment.class.getCanonicalName(), true);
    }

    //检测新的版本
    public void versionOnClickCommand() {
        model.detectionVersion("Android").compose(RxUtils.schedulersTransformer())
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

    public void loadSendLocation(Double lat, Double lng, String county_name, String province_name) {
        if (lat == null || lng == null) {
            return;
        }
        model.coordinate(lat, lng, county_name, province_name)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {

                    }
                });
    }

    public void sendInviteCode() {
        Map<String, String> map = model.readOneLinkCode();
        if (ObjectUtils.isEmpty(map)) {
            return;
        }
        String code = map.get("code");
        String channel = map.get("channel");
        model.userInvite(code, 1, channel)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        model.clearOneLinkCode();//清除本地邀请码缓存
                    }

                    @Override
                    public void onError(RequestException e) {
                        if (e.getCode() != null && e.getCode().intValue() == 10109) {
                            model.clearOneLinkCode();//清除本地邀请码缓存
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //今日缘分打开上报
    public void pushGreet(Integer type) {
        model.pushGreet(type)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {

                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //批量搭讪
    public void putAccostList(List<Integer> userIds) {
        model.putAccostList(userIds)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {

                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //回复收入气泡提示
    public void getBubbleSetting() {
        //防抖拦截 最多两秒触发一次
        if(FastCallFunUtil.getInstance().isFastCallFun("getBubbleEntity",1500)){
            return;
        }
        model.getBubbleEntity()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<BubbleBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<BubbleBean> bubbleEntityBaseDataResponse) {
                        BubbleBean bubble = bubbleEntityBaseDataResponse.getData();
                        if (bubble != null) {
                            if (bubble.getStatus() == 1) {
                                uc.bubbleTopShow.postValue(true);
                            }
                        }
                    }
                });
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

    public class IMAdvancedMsgListener extends V2TIMAdvancedMsgListener{
        @Override
        public void onRecvNewMessage(V2TIMMessage msg) {
            TUIMessageBean info = ChatMessageBuilder.buildMessage(msg);
            if (info != null) {
                switch (info.getMsgType()){
                    //文本类型消息
                    case 1:
                        String text = String.valueOf(info.getExtra());
                        if (StringUtil.isJSON2(text)) {//做自定义通知判断
                            //普通自定义类型
                            if (text.contains("type")){
                                Map<String, Object> map_data = new Gson().fromJson(text, Map.class);
                                if (map_data != null && map_data.get("type") != null) {
                                    String type = Objects.requireNonNull(map_data.get("type")).toString();
                                    String data = (String) map_data.get("data");
                                    if (StringUtil.isJSON2(data)) {
                                        switch (type) {
                                            case "message_pushPay"://未支付儲值鑽石
                                                if (CCApplication.isShowNotPaid){
                                                    if(!FastCallFunUtil.getInstance().isFastCallFun("message_pushPay",5000)){
                                                        Map<String, Object> dataMapPushPay = new Gson().fromJson(data, Map.class);
                                                        String dataType = Objects.requireNonNull(dataMapPushPay.get("type")).toString();
                                                        if (dataType.equals("1") || dataType.equals("1.0")) {
                                                            uc.notPaidDialog.setValue("1");
                                                        } else {
                                                            uc.notPaidDialog.setValue("2");
                                                        }
                                                    }
                                                }
                                                break;
                                            case "message_gift"://接收礼物
                                                if (map_data.get("is_accost") == null) {//不是搭讪礼物
                                                    if (!CCApplication.isCalling){
                                                        GiftEntity giftEntity = IMGsonUtils.fromJson(data, GiftEntity.class);
                                                        //是特效礼物才发送订阅通知事件
                                                        if (!StringUtils.isEmpty(giftEntity.getSvgaPath())) {
                                                            RxBus.getDefault().post(new MessageGiftNewEvent(giftEntity,msg.getMsgID(),info.getV2TIMMessage().getSender()));
                                                        }
                                                    }
                                                }
                                                break;
                                        }
                                    }
                                }
                            }
                            //公屏礼物数据
                            if (text.contains("giftBroadcast") && text.contains("messageType")) {
                                if (CCApplication.isHomePage){
                                    setPublicScreenGiftData(text);
                                }
                            }
                        }
                        break;
                    case 2: //自定义消息类型
                        V2TIMCustomElem v2TIMCustomElem = info.getV2TIMMessage().getCustomElem();
                        byte[] data = v2TIMCustomElem.getData();
                        if (data != null && ConfigManager.getInstance().getTipMoneyShowFlag()){
                            String customData = new String(data);
                            if (customData.contains(CustomConstants.PushMessage.VIDEO_CALL_PUSH)){
                                VideoPushEntity videoPushEntity = V2TIMCustomManagerUtil.videoPushManager(customData);
                                if (videoPushEntity != null && AppConfig.isMainPage){
                                    uc.videoPush.postValue(videoPushEntity);
                                }
                            }

                            if (customData.contains(CustomConstants.PushMessage.VIDEO_CALL_FEEDBACK)){
                                VideoEvaluationEntity evaluationEntity = V2TIMCustomManagerUtil.videoEvaluationManager(customData);
                                if (evaluationEntity != null){
                                    uc.videoEvaluation.postValue(evaluationEntity);
                                }
                            }

                        }
                        break;
                }
            }
        }
    };

    /**
     * 设置公屏礼物数据
     * @param text
     */
    private void setPublicScreenGiftData(String text) {
        try {
            MqGiftDataBean giftDataEntity = new Gson().fromJson(text, MqGiftDataBean.class);
            MqBroadcastGiftBean content = giftDataEntity.getContent();
            if (content != null){
                publicScreenBannerGiftEntity.add(content);
                playBannerGift();
            }
        }catch (Exception e){
            Log.i("imsdk", "setPublicScreenGiftData: 解析异常");
        }
    }

    public void playBannerGift(){
        if (publicScreenBannerGiftEntity.size() > 0 && !playing){
            uc.giftBanner.setValue(publicScreenBannerGiftEntity.get(0));
        }
    }

    public void getSensitiveWords() {
        model.getSensitiveWords()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse>() {

                    @Override
                    public void onSuccess(BaseDataResponse baseDataResponse) {
                        String data = (String) baseDataResponse.getData();
                        if (data == null)return;
                        String[] split = data.split(",");
                        List<String> config = Arrays.asList(split);
                        model.saveSensitiveWords(config);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    //视频评价上报
    public void videoFeedback(long videoCallPushLogId, int feedback) {
        model.videoFeedback(videoCallPushLogId,feedback)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse>() {

                    @Override
                    public void onSuccess(BaseDataResponse baseDataResponse) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    //拨打语音、视频
    public void getCallingInvitedInfo(String myImUserId, String otherImUserId,int videoCallPushLogId) {
        if (Status.mIsShowFloatWindow){
            ToastUtils.showShort(R.string.audio_in_call);
            return;
        }
        model.callingInviteInfo(2, myImUserId, otherImUserId, 2,videoCallPushLogId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<CallingInviteInfo>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CallingInviteInfo> callingInviteInfoBaseDataResponse) {
                        if (callingInviteInfoBaseDataResponse.getCode() == 2) {//忙线中
                            ToastUtils.showShort(R.string.custom_message_other_busy);
                            return;
                        }
                        CallingInviteInfo callingInviteInfo = callingInviteInfoBaseDataResponse.getData();
                        if (callingInviteInfo != null) {
                            Utils.tryStartCallSomeone(2, otherImUserId, callingInviteInfo.getRoomId(), new Gson().toJson(callingInviteInfo));
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    /**
     * 每日奖励
     */
    public void getDayReward() {
        model.getDayReward()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<DayRewardInfoBean>>() {

                    @Override
                    public void onSuccess(BaseDataResponse<DayRewardInfoBean> baseDataResponse) {
                        model.putKeyValue(dayRewardKey,"true");
                        DayRewardInfoBean dayRewardInfoEntity = baseDataResponse.getData();
                        if (dayRewardInfoEntity == null){
                            RxBus.getDefault().post(new DailyAccostEvent());
                            return;
                        }
                        nextGiveCoin = dayRewardInfoEntity.getNext();
                        nextVideoCard = dayRewardInfoEntity.getNextCard();
                        List<DayRewardInfoBean.NowBean> now = dayRewardInfoEntity.getNow();
                        if (now == null) {
                            return;
                        }
                        for (DayRewardInfoBean.NowBean nowBean : now) {
                            String type = nowBean.getType();
                            if (type.equals("video_card")){
                                videoCard = nowBean.getNum();
                            }else if (type.equals("coin")){
                                giveCoin = nowBean.getNum();
                            }
                        }
                        uc.showDayRewardDialog.call();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 注册奖励
     */
    public void getRegisterReward() {
        model.getRegisterReward()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<DayRewardInfoBean>>() {

                    @Override
                    public void onSuccess(BaseDataResponse<DayRewardInfoBean> baseDataResponse) {
                        DayRewardInfoBean dayRewardInfoEntity = baseDataResponse.getData();
                        if (dayRewardInfoEntity == null){
                            RxBus.getDefault().post(new DailyAccostEvent());
                            return;
                        }
                        List<DayRewardInfoBean.NowBean> now = dayRewardInfoEntity.getNow();
                        if (now == null) {
                            return;
                        }
                        for (DayRewardInfoBean.NowBean nowBean : now) {
                            String type = nowBean.getType();
                            if (type.equals("accost_card")){
                                assostCardNum = nowBean.getNum();
                            }else if (type.equals("chat_card")){
                                chatCardNum = nowBean.getNum();
                            }
                        }
                        uc.showRegisterRewardDialog.call();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public class UIChangeObservable {
        //气泡提示
        public SingleLiveEvent<Boolean> bubbleTopShow = new SingleLiveEvent<>();
        public SingleLiveEvent<Intent> restartActivity = new SingleLiveEvent<>();
        //        public SingleLiveEvent<Void> showAgreementDialog = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> showFaceRecognitionDialog = new SingleLiveEvent<>();
        //每日奖励弹框
        public SingleLiveEvent<Void> showDayRewardDialog = new SingleLiveEvent<>();
        //注册奖励
        public SingleLiveEvent<Void> showRegisterRewardDialog = new SingleLiveEvent<>();
        public SingleLiveEvent<String> startFace = new SingleLiveEvent<>();
        public SingleLiveEvent<Integer> allMessageCountChange = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> lockDialog = new SingleLiveEvent<>();
        public ObservableField<Boolean> gender = new ObservableField<>(false);
        public SingleLiveEvent<MainTabEvent> mainTab = new SingleLiveEvent<>();
        //更新版本
        public SingleLiveEvent<VersionBean> versionEntitySingl = new SingleLiveEvent<>();
        //未付费弹窗
        public SingleLiveEvent<String> notPaidDialog = new SingleLiveEvent<>();
        public SingleLiveEvent<MqBroadcastGiftBean> giftBanner = new SingleLiveEvent<>();
        //任务中心跳转
        public SingleLiveEvent<TaskMainTabEvent> taskCenterclickTab = new SingleLiveEvent<>();
        public SingleLiveEvent<VideoEvaluationEntity> videoEvaluation = new SingleLiveEvent<>();
        public SingleLiveEvent<VideoPushEntity> videoPush = new SingleLiveEvent<>();

    }

}