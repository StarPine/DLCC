package com.fine.friendlycc.calling.viewmodel;

import android.app.Application;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.bean.CallingInfoBean;
import com.fine.friendlycc.bean.CallingInviteInfo;
import com.fine.friendlycc.bean.CallingStatusBean;
import com.fine.friendlycc.bean.CallingVideoTryToReconnectEvent;
import com.fine.friendlycc.bean.GiftBagBean;
import com.fine.friendlycc.bean.MallWithdrawTipsInfoBean;
import com.fine.friendlycc.bean.UserDataBean;
import com.fine.friendlycc.bean.UserProfileInfo;
import com.fine.friendlycc.event.CallVideoUserEnterEvent;
import com.fine.friendlycc.calling.view.CCTUICallVideoView;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.utils.ApiUitl;
import com.fine.friendlycc.utils.LogUtils;
import com.fine.friendlycc.utils.ToastCenterUtils;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.google.gson.Gson;
import com.tencent.custom.GiftEntity;
import com.tencent.custom.IMGsonUtils;
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageReceipt;
import com.tencent.liteav.trtccalling.TUICalling;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;
import com.tencent.qcloud.tuikit.tuichat.ui.view.MyImageSpan;
import com.tencent.qcloud.tuikit.tuichat.util.ChatMessageBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class VideoCallViewModel extends BaseViewModel<AppRepository> {
    public int TimeCount = 0;
    public int roomId = 0;// 房间ID必须持有

    //是否发送过礼物
    public boolean sendGiftBagSuccess = false;
    public boolean isCallingInviteInfoNull = false;

    public ObservableField<Boolean> isShowCountdown = new ObservableField(false);
    public ObservableField<Boolean> isShowBeauty = new ObservableField(false);

    //是否已经显示过兑换规则
    public ObservableField<Boolean> isHideExchangeRules = new ObservableField<>(false);

    //录音文案数组坐标
    public int sayHiePosition = 0;
    public int sayHiePage = 1;
    //订阅者
    private Disposable userEnterSubscription;
    private Disposable tryToReconnectSubscription;

    //对方用户信息
    public ObservableField<CallingInviteInfo> callingInviteInfoField = new ObservableField<>();
    public Integer $coinBalance = 0;
    public ObservableField<Boolean> isCalledWaitingBinding = new ObservableField<>(true);
    // 是否被叫
    public ObservableField<Boolean> isCalledBinding = new ObservableField<>(false);
    // 未接听时的收入提示（黄色字体）
    public ObservableField<String> callHintBinding = new ObservableField<>("");
    public ObservableField<Boolean> mainVIewShow = new ObservableField<>(false);
    //防录屏提示开关
    public ObservableField<Boolean> tipSwitch = new ObservableField(true);
    protected CCTUICallVideoView mCallVideoView;
    public String mfromUserId;
    public String mtoUserId;
    //当前用户是否男性
    public boolean isMale = false;
    //收益开关
    public ObservableField<Boolean> isShowTipMoney = new ObservableField(true);
    //当前用户是否为收款人
    public boolean isPayee = false;
    public boolean videoSuccess = false;
    private String mMyUserId;
    private String mOtherUserId;
    private TUICalling.Role mRole;
    //是否是拨打方
    public boolean userCall = false;
    //通话数据加载完成
    public boolean callInfoLoaded = false;
    //男生钻石总余额
    public int maleBalanceMoney = 0;
    //男生总分钟数
    public int totalMinutes = 0;
    //男生剩余聊天分钟数
    public int totalMinutesRemaining = 0;
    //聊天收益
    public double payeeProfits;
    //断网总时间
    int disconnectTime = 0;
    //是否已追踪0未追踪1已追踪
    public Integer collected;
    //余额不足临界提示分钟数
    public int balanceNotEnoughTipsMinutes;
    //余额不足提示标记
    public boolean flagMoneyNotWorth = false;
    //价格配置表
    public List<CallingInfoBean.CallingUnitPriceInfo> unitPriceList;
    //时间提示
    public ObservableField<String> timeTextField = new ObservableField<>();
    //对方用户信息
    public ObservableField<CallingInfoBean.FromUserProfile> callingVideoInviteInfoField = new ObservableField<>();
    //男生收入框是否展示
    public ObservableBoolean maleTextLayoutSHow = new ObservableBoolean(false);
    //男性收入内容
    public ObservableField<String> maleTextMoneyField = new ObservableField();
    //女性收入弹窗是否显示
    public ObservableBoolean girlEarningsField = new ObservableBoolean(false);
    //收入文字
    public ObservableField<SpannableString> girlEarningsText = new ObservableField<>();
    //是否已经追踪
    public ObservableInt collectedField = new ObservableInt(1);
    //是否静音
    public ObservableBoolean micMuteField = new ObservableBoolean(false);
    //是否免提
    public ObservableBoolean handsFreeField = new ObservableBoolean(false);
    //破冰文案
    public List<CallingInfoBean.SayHiEntity> sayHiEntityList = new ArrayList<>();
    //破冰文案
    public ObservableField<CallingInfoBean.SayHiEntity> sayHiEntityField = new ObservableField<>();
    //破冰文案是否显示
    public ObservableBoolean sayHiEntityHidden = new ObservableBoolean(true);

    public BindingRecyclerViewAdapter<VideoCallChatingItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<VideoCallChatingItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<VideoCallChatingItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_call_video_chating);
    public UIChangeObservable uc = new UIChangeObservable();

    public BindingCommand acceptOnclick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (!videoSuccess) {
                return;
            }
            mainVIewShow.set(true);
            mCallVideoView.acceptCall();
            Log.e("接听电话按钮点击", mMyUserId + "=======" + mOtherUserId);
            //getCallingInfo(roomId, ChatUtils.imUserIdToSystemUserId(mMyUserId), ChatUtils.imUserIdToSystemUserId(mOtherUserId));
            if (mRole == TUICalling.Role.CALLED) {
                isCalledWaitingBinding.set(false);
            }
        }
    });

    //点击文字充值
    public BindingCommand referMoney = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            CCApplication.instance().logEvent(AppsFlyerEvent.videocall_topup);
            uc.sendUserGiftError.postValue(false);
        }
    });
    //关闭男生隐藏余额不足提示
    public BindingCommand closeMoney = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            maleTextLayoutSHow.set(false);
        }
    });

    //关闭女生界面男生余额不足提示
    public BindingCommand closeMoney2 = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isShowCountdown.set(false);
            girlEarningsField.set(false);
        }
    });

    /**
     * 打开美颜
     */
    public BindingCommand beauty = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isShowBeauty.set(!isShowBeauty.get());
        }
    });


    /**
     * 水晶兑换规则
     */
    public BindingCommand crystalOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            getMallWithdrawTipsInfo(1);
        }
    });

    public BindingCommand closeOnclick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.closeViewHint.call();
            //mCallVideoView.hangup();
        }
    });
    //发送礼物
    public BindingCommand giftBagOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            CCApplication.instance().logEvent(AppsFlyerEvent.videocall_gift);
            getCallingStatus(roomId);
            uc.callGiftBagAlert.call();
        }
    });

    //禁用麦克风点击
    public BindingCommand micMuteOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (micMuteField.get()) {//开启免提
                ToastUtils.showShort(R.string.playcc_call_message_deatail_txt_4);
            } else {
                ToastUtils.showShort(R.string.playcc_call_message_deatail_txt_3);
            }
            boolean minMute = !micMuteField.get();
            micMuteField.set(minMute);
            mCallVideoView.setMicMute(minMute);
        }
    });

    //声音展示
    public BindingCommand handsFreeOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (handsFreeField.get()) {//开启免提
                ToastUtils.showShort(R.string.playcc_call_message_deatail_txt_2);
            } else {
                ToastUtils.showShort(R.string.playcc_call_message_deatail_txt_1);
            }
            boolean handsFree = !handsFreeField.get();
            handsFreeField.set(handsFree);
            mCallVideoView.setHandsFree(handsFree);
        }
    });

    public BindingCommand switchCameraOnclick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            mCallVideoView.switchCamera();
        }
    });

    public BindingCommand rejectOnclick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            mCallVideoView.hangup();
        }
    });

    //关注
    public BindingCommand addlikeOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            CCApplication.instance().logEvent(AppsFlyerEvent.videocall_follow);
            addLike(false);
        }
    });
    //切换破冰文案提示
    public BindingCommand upSayHiEntityOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            CCApplication.instance().logEvent(AppsFlyerEvent.videocall_ice_change);
            uc.startVideoUpSayHiAnimotor.call();
//            getSayHiList();
        }
    });
    //关闭破冰文案提示
    public BindingCommand colseSayHiEntityOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            CCApplication.instance().logEvent(AppsFlyerEvent.videocall_ice_close);
            sayHiEntityHidden.set(true);
        }
    });

    public void hangup(){
        mCallVideoView.hangup();
    }

    //拨打语音、视频
    public void getCallingInvitedInfo(int callingType, String fromUserId) {
        String userId = model.readUserData().getImUserId();
        model.callingInviteInfo(callingType, fromUserId, userId, 0)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<CallingInviteInfo>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CallingInviteInfo> callingInviteInfoBaseDataResponse) {
                        CallingInviteInfo callingInviteInfo = callingInviteInfoBaseDataResponse.getData();
                        roomId = callingInviteInfo.getRoomId();
                        Log.e("当前用户进入房间ID", roomId + "=====================");
                        mfromUserId = fromUserId;
                        mtoUserId = userId;
                        callingInviteInfoField.set(callingInviteInfo);
                        if(callingInviteInfo.getPaymentRelation().getPayerUserId() == model.readUserData().getId()){
                            if (!ObjectUtils.isEmpty(callingInviteInfo.getMessages()) && callingInviteInfo.getMessages().size() > 0) {
                                String valueData = "";
                                for (String value : callingInviteInfo.getMessages()) {
                                    valueData += value + "\n";
                                }
                                callHintBinding.set(valueData);
                            }
                        }
                        if (callingInviteInfo.getMinutesRemaining() != null && callingInviteInfo.getMinutesRemaining().intValue() <= 0) {
                            hangup();
                            return;
                        }
                        if (callingInviteInfo.getMinutesRemaining() != null && callingInviteInfo.getMinutesRemaining().intValue() > 0) {
                            videoSuccess = true;
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void getTips(Integer toUserId, int type, String isShowCountdown){
        model.getTips(toUserId, type,isShowCountdown)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse>() {
                    @Override
                    public void onSuccess(BaseDataResponse baseDataResponse) {
                    }

                    @Override
                    public void onError(RequestException e) {

                    }
                });
    }

    public VideoCallViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public VideoCallViewModel(@NonNull @NotNull Application application, AppRepository model) {
        super(application, model);
    }

    //    protected TRTCCallingDelegate mTRTCCallingDelegate;
    public void init(String myUserId, String otherUserId, TUICalling.Role role, CCTUICallVideoView view) {
        this.mMyUserId = myUserId;
        this.mOtherUserId = otherUserId;
        this.mRole = role;
        this.mCallVideoView = view;
        this.isMale = ConfigManager.getInstance().isMale();
//        isShowTipMoney.set(ConfigManager.getInstance().getTipMoneyShowFlag());
        this.isCalledBinding.set(role == TUICalling.Role.CALLED);
        this.isCalledWaitingBinding.set(role == TUICalling.Role.CALLED);
    }

    public void init(String myUserId, String otherUserId, TUICalling.Role role, CCTUICallVideoView view, Integer roomId) {
        this.mMyUserId = myUserId;
        this.mOtherUserId = otherUserId;
        this.mRole = role;
        this.mCallVideoView = view;
        this.isMale = ConfigManager.getInstance().isMale();
        this.isCalledBinding.set(role == TUICalling.Role.CALLED);
        this.isCalledWaitingBinding.set(role == TUICalling.Role.CALLED);
        this.roomId = roomId;
    }

    public void getMallWithdrawTipsInfo(Integer channel){
        model.getMallWithdrawTipsInfo(channel)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<MallWithdrawTipsInfoBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<MallWithdrawTipsInfoBean> response) {
                        MallWithdrawTipsInfoBean data = response.getData();
                        uc.clickCrystalExchange.setValue(data);

                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public Drawable getVipGodsImg(CallingInviteInfo callingInviteInfo) {
        if (callingInviteInfo != null) {
            UserProfileInfo userProfileInfo = callingInviteInfo.getUserProfileInfo();
            if (userProfileInfo != null) {
                if (userProfileInfo.getSex() == 1) {
                    if (userProfileInfo.getIsVip() == 1) {
                        return CCApplication.instance().getDrawable(R.drawable.ic_vip);
                    } else {
                        if (userProfileInfo.getCertification() == 1) {
                            return CCApplication.instance().getDrawable(R.drawable.ic_real_people);
                        }
                    }
                } else {//女生
                    if (userProfileInfo.getIsVip() == 1) {
                        return CCApplication.instance().getDrawable(R.drawable.ic_good_goddess);
                    } else {
                        if (userProfileInfo.getCertification() == 1) {
                            return CCApplication.instance().getDrawable(R.drawable.ic_real_people);
                        }
                    }
                }
            }
        }

        return null;
    }

    public String gameUrl(String gameChannel) {
        return ConfigManager.getInstance().getGameUrl(gameChannel);
    }

    public boolean isEmpty(String obj) {
        return obj == null || obj.equals("");
    }

    public String ageAndConstellation(CallingInviteInfo callingInviteInfo) {
        if (callingInviteInfo != null) {
            return String.format(StringUtils.getString(R.string.playcc_age_and_constellation), callingInviteInfo.getUserProfileInfo().getAge());
        }
        return "";
    }

    //添加文案到公屏
    public void putRcvItemMessage(SpannableString stringBuilder, String imgPath, boolean sendGiftBagShow) {
        observableList.add(new VideoCallChatingItemViewModel(VideoCallViewModel.this, stringBuilder, imgPath, sendGiftBagShow));
        uc.scrollToEnd.postValue(null);
    }

    //获取破冰文案
    public void getSayHiList() {
        //录音文案数组坐标
        if (!ObjectUtils.isEmpty(sayHiEntityList) && sayHiePosition + 1 <= sayHiEntityList.size() - 1) {
            sayHiePosition++;
            sayHiEntityField.set(sayHiEntityList.get(sayHiePosition));
            return;
        }
        if (sayHiePosition < sayHiEntityList.size()) {
            return;
        }
        sayHiePage++;
        model.getSayHiList(sayHiePage, 20)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<CallingInfoBean.SayHiList>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CallingInfoBean.SayHiList> response) {
                        try {
                            List<CallingInfoBean.SayHiEntity> dataList = response.getData().getData();
                            if (!ObjectUtils.isEmpty(dataList)) {
                                sayHiEntityList.addAll(dataList);
                                if (sayHiEntityList.size() > 1) {
                                    sayHiEntityField.set(sayHiEntityList.get(sayHiePosition++));
                                }
                            } else {
                                sayHiEntityField.set(sayHiEntityList.get(0));
                            }
                        } catch (Exception e) {

                        }
                    }
                });
    }

    //通话中获取资料
    public void getCallingInfo(Integer roomId, String fromUserId, String toUserId) {
        if (isCallingInviteInfoNull) {
            return;
        }
        isCallingInviteInfoNull = true;
        Log.e("通话中调用接口", "====================" + roomId + "======" + fromUserId + "==========" + toUserId);
        model.getCallingInfo(roomId, 2, fromUserId, toUserId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<CallingInfoBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CallingInfoBean> response) {
                        Log.e("通话中获取资料", "================");
                        isCallingInviteInfoNull = true;
                        CallingInfoBean callingInviteInfo = response.getData();
                        UserDataBean userDataEntity = model.readUserData();
                        mCallVideoView.acceptCall();
                        sayHiEntityList = callingInviteInfo.getSayHiList().getData();
                        if (callingInviteInfo.getPaymentRelation().getPayeeImId().equals(ConfigManager.getInstance().getUserImID())){
                            isPayee = true;
                        }
                        if (sayHiEntityList.size() > 1) {
                            sayHiEntityHidden.set(false);
                            sayHiEntityField.set(sayHiEntityList.get(0));
                        }
                        if (userDataEntity.getId().intValue() == callingInviteInfo.getFromUserProfile().getId().intValue()) {
                            callingVideoInviteInfoField.set(callingInviteInfo.getToUserProfile());
                        } else {
                            callingVideoInviteInfoField.set(callingInviteInfo.getFromUserProfile());
                        }
                        //是否已追踪0未追踪1已追踪
                        collected = callingInviteInfo.getCollected();
                        collectedField.set(collected);
                        //余额不足提示分钟数
                        balanceNotEnoughTipsMinutes = callingInviteInfo.getBalanceNotEnoughTipsMinutes();
                        //价格配置表
                        unitPriceList = callingInviteInfo.getUnitPriceList();
                        initIMListener();
                        mCallVideoView.enableAGC(true);
                        mCallVideoView.enableAEC(true);
                        mCallVideoView.enableANS(true);
                    }

                    @Override
                    public void onError(RequestException e) {
                        Log.e("接口调用失败", "失败原因：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                        uc.callAudioStart.call();
                    }
                });
    }

    //获取房间状态
    public void getRoomStatus(Integer roomId) {
        model.getRoomStatus(roomId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<CallingStatusBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CallingStatusBean> response) {
                        CallingStatusBean data = response.getData();
                        Integer roomStatus = data.getRoomStatus();
                        LogUtils.i("onSuccess: " + roomStatus);
                        if (roomStatus != null && roomStatus != 101) {
                            hangup();
                        }
                    }
                });
    }

    //获取通话状态
    public void getCallingStatus(Integer roomId) {
        model.getCallingStatus(roomId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<CallingStatusBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CallingStatusBean> response) {
                        CallingStatusBean data = response.getData();
                        if (data != null) {
                            maleBalanceMoney = data.getPayerCoinBalance();
                            payeeProfits = data.getPayeeProfits().doubleValue();
                            totalMinutes = data.getTotalMinutes() * 60;
                            totalMinutesRemaining = totalMinutes - TimeCount;
                            callInfoLoaded = true;
                        }
                    }
                });
    }

    //发送礼物
    public void sendUserGift(Dialog dialog, GiftBagBean.giftEntity giftEntity, Integer to_user_id, Integer amount) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("giftId", giftEntity.getId());
        map.put("toUserId", to_user_id);
        map.put("type", 2);
        map.put("amount", amount);
        model.sendUserGift(ApiUitl.getBody(GsonUtils.toJson(map)))
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        dismissHUD();
                        getCallingStatus(roomId);
                        sendGiftBagSuccess = true;
                        dialog.dismiss();
                        String textTip = null;
                        if (isMale) {
                            textTip = StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt_male);
                        } else {
                            textTip = StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt_gift);
                        }
                        String nickname = callingVideoInviteInfoField.get().getNickname();
                        textTip += " " + nickname;
                        int startLength = textTip.length();
                        textTip += " " + giftEntity.getName() + " x" + amount;
                        SpannableString stringBuilder = new SpannableString(textTip);

                        ForegroundColorSpan blueSpan = new ForegroundColorSpan(ColorUtils.getColor(R.color.call_message_deatail_hint2));
                        ForegroundColorSpan blueSpanWhite = new ForegroundColorSpan(ColorUtils.getColor(R.color.white));
                        stringBuilder.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.call_message_deatail_hint2)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        stringBuilder.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.white)), 2, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        stringBuilder.setSpan(blueSpan, 5, 5 + nickname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        stringBuilder.setSpan(blueSpanWhite, startLength, textTip.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        putRcvItemMessage(stringBuilder, giftEntity.getImg(), false);
                        Map<String, Object> mapData = new HashMap<>();
                        mapData.put("account", amount);
                        mapData.put("giftEntity", giftEntity);
                        uc.sendUserGiftAnim.postValue(mapData);
                    }

                    @Override
                    public void onError(RequestException e) {
                        dialog.dismiss();
                        dismissHUD();
                        if (e.getCode() != null && e.getCode().intValue() == 21001) {
                            ToastCenterUtils.showToast(R.string.playcc_dialog_exchange_integral_total_text1);
                            CCApplication.instance().logEvent(AppsFlyerEvent.videocall_gift_Insu_topup);
                            uc.sendUserGiftError.postValue(true);
                        }
                    }
                });
    }

    //追踪
    public void addLike(boolean isHangup) {
        model.addCollect(callingVideoInviteInfoField.get().getId())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        if (isHangup) {
                            mCallVideoView.hangup();
                        } else {
                            collected = 1;
                            collectedField.set(1);
                            ToastUtils.showShort(R.string.playcc_cancel_zuizong_3);
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

    //注册RxBus
    @Override
    public void registerRxBus() {
        super.registerRxBus();
        userEnterSubscription = RxBus.getDefault().toObservable(CallVideoUserEnterEvent.class)
                .subscribe(event -> {
                    mainVIewShow.set(true);
                    if (roomId != 0 && mfromUserId != null && mtoUserId != null) {
                        getCallingInfo(roomId, mfromUserId, mtoUserId);
                    } else {
                        getCallingInfo(roomId, model.readUserData().getImUserId(), event.getUserId());
                    }
                });
        tryToReconnectSubscription = RxBus.getDefault().toObservable(CallingVideoTryToReconnectEvent.class)
                .subscribe(callingVideoTryToReconnectEvent -> {
                    getRoomStatus(roomId);
                });
        //将订阅者加入管理站
        RxSubscriptions.add(userEnterSubscription);
        RxSubscriptions.add(tryToReconnectSubscription);
    }

    //移除RxBus
    @Override
    public void removeRxBus() {
        super.removeRxBus();
        //将订阅者从管理站中移除
        RxSubscriptions.remove(userEnterSubscription);
        RxSubscriptions.remove(tryToReconnectSubscription);
    }

    public static boolean isJSON2(String str) {
        boolean result = false;
        try {
            new Gson().fromJson(str, Map.class);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;

    }

    //监听IM消息
    private void initIMListener() {
        V2TIMManager.getMessageManager().addAdvancedMsgListener(new V2TIMAdvancedMsgListener() {
            @Override
            public void onRecvNewMessage(V2TIMMessage msg) {//新消息提醒
                if (msg != null && callingVideoInviteInfoField.get() != null) {
                    TUIMessageBean info = ChatMessageBuilder.buildMessage(msg);
                    if (info != null) {
                        if (info.getV2TIMMessage().getSender().equals(callingVideoInviteInfoField.get().getImId())) {
                            String text = String.valueOf(info.getExtra());
                            if (isJSON2(text) && text.indexOf("type") != -1) {//做自定义通知判断
                                Map<String, Object> map_data = new Gson().fromJson(text, Map.class);
                                //礼物消息
                                if (map_data != null && map_data.get("type") != null && map_data.get("type").equals("message_gift")
                                        && map_data.get("is_accost") == null) {
                                    GiftEntity giftEntity = IMGsonUtils.fromJson(String.valueOf(map_data.get("data")), GiftEntity.class);
                                    uc.acceptUserGift.postValue(giftEntity);
                                    //显示礼物弹幕
                                    showGiftBarrage(giftEntity);

                                    //礼物收益提示
                                    giftIncome(giftEntity);
                                }else if (map_data != null && map_data.get("type") != null && map_data.get("type").equals("message_countdown")) {//对方余额不足
                                    if (isPayee && isShowTipMoney.get()) {
                                        String data = (String) map_data.get("data");
                                        Map<String, Object> dataMapCountdown = new Gson().fromJson(data, Map.class);
                                        String isShow = (String) dataMapCountdown.get("is_show");
                                        if (isShow != null && isShow.equals("1")) {
                                            isShowCountdown.set(true);
                                            girlEarningsField.set(true);
                                            String girlEarningsTex = StringUtils.getString(R.string.playcc_insufficient_balance_of_counterparty);
                                            SpannableString stringBuilder = new SpannableString(girlEarningsTex);
                                            stringBuilder.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.white)), 0, girlEarningsTex.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            girlEarningsText.set(stringBuilder);

                                        }else if (isShow != null && isShow.equals("0")){
                                            isShowCountdown.set(false);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onRecvC2CReadReceipt(List<V2TIMMessageReceipt> receiptList) {
                super.onRecvC2CReadReceipt(receiptList);
            }

            @Override
            public void onRecvMessageRevoked(String msgID) {
                super.onRecvMessageRevoked(msgID);
            }

            @Override
            public void onRecvMessageModified(V2TIMMessage msg) {
                super.onRecvMessageModified(msg);
            }
        });
    }

    /**
     * 显示礼物弹幕
     *
     * @param giftEntity
     */
    private void showGiftBarrage(GiftEntity giftEntity) {
        int nickNameLength = callingVideoInviteInfoField.get().getNickname().length();
        String sexText = isMale ? StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt3) : StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt2);
        String messageText = callingVideoInviteInfoField.get().getNickname() + " " + StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt1) + " " + sexText + " " + giftEntity.getTitle() + "x" + giftEntity.getAmount();
        int youIndex = messageText.indexOf(sexText);
        SpannableString stringBuilder = new SpannableString(messageText);

        stringBuilder.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.white)), 0, messageText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.call_message_deatail_hint2)), 0, nickNameLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.call_message_deatail_hint2)), youIndex, youIndex + sexText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        putRcvItemMessage(stringBuilder, giftEntity.getImgPath(), false);
    }

    /**
     * 礼物收益提示
     *
     * @param giftEntity
     */
    private void giftIncome(GiftEntity giftEntity) {
        String itemMessage = String.format(StringUtils.getString(R.string.profit), String.format("%.2f", giftEntity.getAmount().intValue() * giftEntity.getProfitTwd().doubleValue()));
        SpannableString itemMessageBuilder = new SpannableString(itemMessage);
        itemMessageBuilder.setSpan(new MyImageSpan(getApplication(),R.drawable.icon_crystal),0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        itemMessageBuilder.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.white)), 1, itemMessage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        putRcvItemMessage(itemMessageBuilder, null, false);
    }

    public UserDataBean readUserData() {
        return model.readUserData();
    }

    public class UIChangeObservable {
        //接听成功
        public SingleLiveEvent<Void> callAudioStart = new SingleLiveEvent<>();
        //调用发送礼物弹窗
        public SingleLiveEvent<Void> callGiftBagAlert = new SingleLiveEvent<>();
        //发送礼物失败。充值钻石
        public SingleLiveEvent<Boolean> sendUserGiftError = new SingleLiveEvent<>();
        //发送礼物效果
        public SingleLiveEvent<Map<String, Object>> sendUserGiftAnim = new SingleLiveEvent<>();
        //接收礼物效果
        public SingleLiveEvent<GiftEntity> acceptUserGift = new SingleLiveEvent<>();
        //关闭消息
        public SingleLiveEvent<Void> closeViewHint = new SingleLiveEvent<>();
        public SingleLiveEvent<MallWithdrawTipsInfoBean> clickCrystalExchange = new SingleLiveEvent<>();
        //滚动到屏幕底部
        public SingleLiveEvent<Void> scrollToEnd = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> startVideoUpSayHiAnimotor = new SingleLiveEvent<>();
    }

}