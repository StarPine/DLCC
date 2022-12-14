package com.fine.friendlycc.calling.viewmodel;

import android.app.Application;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.fine.friendlycc.bean.CallingStatusBean;
import com.fine.friendlycc.bean.GiftBagBean;
import com.fine.friendlycc.bean.MallWithdrawTipsInfoBean;
import com.fine.friendlycc.bean.UserDataBean;
import com.fine.friendlycc.event.AudioCallingCancelEvent;
import com.fine.friendlycc.calling.Utils;
import com.fine.friendlycc.calling.view.Ifinish;
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
import com.tencent.liteav.trtccalling.model.TRTCCalling;
import com.tencent.liteav.trtccalling.model.TRTCCallingDelegate;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;
import com.tencent.qcloud.tuikit.tuichat.ui.view.MyImageSpan;
import com.tencent.qcloud.tuikit.tuichat.util.ChatMessageBuilder;
import com.tencent.trtc.TRTCCloudDef;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class AudioCallChatingViewModel extends BaseViewModel<AppRepository> {
    //????????????-???
    public int TimeCount = 0;
    //?????????????????????
    public boolean sendGiftBagSuccess = false;
    public Integer roomId;
    public String fromUserId;
    public String toUserId;

    //????????????????????????
    public boolean callInfoLoaded = false;
    //?????????????????????
    public int maleBalanceMoney = 0;
    //??????????????????
    public int totalMinutes = 0;
    //???????????????????????????
    public int totalMinutesRemaining = 0;
    //????????????
    public double payeeProfits;
    //???????????????
    int disconnectTime = 0;

    //????????????????????????
    public int sayHiePosition = 0;
    public int sayHiePage = 1;

    //????????????????????????
    public boolean isMale = false;
    //????????????
    public boolean isShowTipMoney = false;
    //??????????????????????????????
    public boolean isPayee = false;
    //???????????????0?????????1?????????
    public Integer collected;
    //?????????????????????????????????
    public int balanceNotEnoughTipsMinutes;
    //????????????????????????
    public boolean flagMoneyNotWorth = false;
    public ObservableField<Boolean> isShowCountdown = new ObservableField(false);
    //?????????????????????
    public ObservableField<Boolean> tipSwitch = new ObservableField(true);
    //???????????????
    public List<CallingInfoBean.CallingUnitPriceInfo> unitPriceList;

    //????????????
    public ObservableField<String> timeTextField = new ObservableField<>();
    public ObservableField<CallingInfoBean.FromUserProfile> rightUserInfoField = new ObservableField<>();
    public ObservableField<CallingInfoBean.FromUserProfile> leftUserInfoField = new ObservableField<>();
    //?????????????????????????????????
    public ObservableField<Boolean> isHideExchangeRules = new ObservableField<>(false);
    //???????????????????????????
    public ObservableBoolean maleTextLayoutSHow = new ObservableBoolean(false);
    //??????????????????
    public ObservableField<String> maleTextMoneyField = new ObservableField();
    //??????????????????????????????
    public ObservableBoolean girlEarningsField = new ObservableBoolean(false);
    //????????????
    public ObservableField<SpannableString> girlEarningsText = new ObservableField<>();
    public ObservableField<UserDataBean> audioUserDataEntity = new ObservableField<>();
    public ObservableField<CallingInfoBean> audioCallingInfoEntity = new ObservableField<>();
    //??????????????????
    public ObservableInt collectedField = new ObservableInt(1);
    //????????????
    public ObservableBoolean micMuteField = new ObservableBoolean(false);
    //????????????
    public ObservableBoolean handsFreeField = new ObservableBoolean(false);
    //????????????
    public List<CallingInfoBean.SayHiEntity> sayHiEntityList = new ArrayList<>();
    //????????????
    public ObservableField<CallingInfoBean.SayHiEntity> sayHiEntityField = new ObservableField<>();
    //????????????????????????
    public ObservableBoolean sayHiEntityHidden = new ObservableBoolean(true);
    public BindingRecyclerViewAdapter<AudioCallChatingItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<AudioCallChatingItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<AudioCallChatingItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_call_audio_chating);

    private final static String TAG = "trtcJoy";
    private static final int MIN_DURATION_SHOW_LOW_QUALITY = 5000; //????????????????????????????????????

    public UIChangeObservable uc = new UIChangeObservable();
    //??????????????????
    public BindingCommand referMoney = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.sendUserGiftError.postValue(false);
        }
    });
    /**
     * ??????????????????
     */
    public BindingCommand crystalOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            getMallWithdrawTipsInfo(1);
        }
    });

    protected Ifinish mView;
    protected TRTCCalling mTRTCCalling;
    protected TRTCCallingDelegate mTRTCCallingDelegate;
    //    private TUICalling.Role mRole;
    public View.OnClickListener closeOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            uc.closeViewHint.call();
        }
    };
    //????????????
    public BindingCommand giftBagOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            CCApplication.instance().logEvent(AppsFlyerEvent.voicecall_gift);
            getCallingStatus(roomId);
            uc.callGiftBagAlert.call();
        }
    });
    //????????????????????????????????????
    public BindingCommand closeMoney = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            maleTextLayoutSHow.set(false);
        }
    });

    //??????????????????????????????????????????
    public BindingCommand closeMoney2 = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isShowCountdown.set(false);
            girlEarningsField.set(false);
        }
    });


    //?????????
    private Disposable mSubscription;
    private long mSelfLowQualityTime;
    private long mOtherPartyLowQualityTime;

    public AudioCallChatingViewModel(@NonNull @NotNull Application application, AppRepository model) {
        super(application, model);
    }

    //??????
    public BindingCommand addlikeOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            CCApplication.instance().logEvent(AppsFlyerEvent.voicecall_follow);
            addLike(false);
        }
    });

    //?????????????????????
    public BindingCommand micMuteOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (micMuteField.get()) {//????????????
                ToastUtils.showShort(R.string.playcc_call_message_deatail_txt_4);
            } else {
                ToastUtils.showShort(R.string.playcc_call_message_deatail_txt_3);
            }
            boolean minMute = !micMuteField.get();
            micMuteField.set(minMute);
            mTRTCCalling.setMicMute(minMute);
        }
    });

    //????????????
    public BindingCommand handsFreeOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (handsFreeField.get()) {//????????????
                ToastUtils.showShort(R.string.playcc_call_message_deatail_txt_2);
            } else {
                ToastUtils.showShort(R.string.playcc_call_message_deatail_txt_1);
            }
            boolean handsFree = !handsFreeField.get();
            handsFreeField.set(handsFree);
            mTRTCCalling.setHandsFree(handsFree);
        }
    });
    //????????????????????????
    public BindingCommand upSayHiEntityOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            CCApplication.instance().logEvent(AppsFlyerEvent.voicecall_ice_change);
            uc.startUpSayHiAnimotor.call();
//            getSayHiList();
        }
    });
    //????????????????????????
    public BindingCommand colseSayHiEntityOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            CCApplication.instance().logEvent(AppsFlyerEvent.voicecall_ice_close);
            sayHiEntityHidden.set(true);
        }
    });

    //????????????
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
//                        dialog.dismiss();
                        String textTip = null;

                        if (isMale) {
                            textTip = StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt_male);
                        } else {
                            textTip = StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt_gift);
                        }
                        String nickname = leftUserInfoField.get().getNickname();
                        textTip += " " + nickname;
                        int startLength = textTip.length();
                        textTip += " " + giftEntity.getName() + " x" + amount;
                        SpannableString stringBuilder = new SpannableString(textTip);
                        int nicknameIndex = textTip.indexOf(nickname);

                        ForegroundColorSpan blueSpanWhite = new ForegroundColorSpan(ColorUtils.getColor(R.color.white));
                        stringBuilder.setSpan(blueSpanWhite, 0, textTip.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
                            CCApplication.instance().logEvent(AppsFlyerEvent.voicecall_gift_Ins_topup);
                            uc.sendUserGiftError.postValue(true);
                        }
                    }
                });
    }

    //??????RxBus
    @Override
    public void registerRxBus() {
        super.registerRxBus();
        mSubscription = RxBus.getDefault().toObservable(AudioCallingCancelEvent.class)
                .subscribe(event -> {
                    // ??????????????????
                    hangup();
                });
        //???????????????????????????
        RxSubscriptions.add(mSubscription);
    }

    //??????RxBus
    @Override
    public void removeRxBus() {
        super.removeRxBus();
        //?????????????????????????????????
        RxSubscriptions.remove(mSubscription);
    }

    public void init(Ifinish iview) {
        isMale = ConfigManager.getInstance().isMale();
        isShowTipMoney = ConfigManager.getInstance().getTipMoneyShowFlag();
        mTRTCCalling = TRTCCalling.sharedInstance(CCApplication.instance());
        initListener();
        mView = iview;
        //??????IM??????
        initIMListener();
    }

    public void hangup() {
        unListener();
        mTRTCCalling.hangup();
        mView.finishView();
        Utils.show(CCApplication.instance().getString(R.string.playcc_call_ended));
    }

    private void endChattingAndShowHint(String msg) {
        Utils.runOnUiThread(() -> {
            unListener();
            mView.finishView();
            Utils.show(msg);
        });
    }

    protected void initListener() {
        mTRTCCallingDelegate = new EmptyTRTCCallingDelegate() {
            @Override
            public void onError(int code, String msg) {
                Log.e(TAG, "onError: " + code + " " + msg);
                endChattingAndShowHint(CCApplication.instance().getString(com.tencent.liteav.trtccalling.R.string.trtccalling_toast_call_error_msg, code, msg));
            }

            @Override
            public void onCallEnd() {
                Log.i(TAG, "onCallEnd: ");
                endChattingAndShowHint(CCApplication.instance().getString(R.string.playcc_call_ended));
            }

            @Override
            public void onNetworkQuality(TRTCCloudDef.TRTCQuality localQuality, ArrayList<TRTCCloudDef.TRTCQuality> remoteQuality) {
                //??????????????????
                if (localQuality.quality == 6 || remoteQuality.isEmpty()) {
                    disconnectTime++;
                    if (disconnectTime > 30 || (remoteQuality.isEmpty() && disconnectTime >15)) {
                        hangup();
                    }
                }else {
                    disconnectTime  = 0;
                }
                updateNetworkQuality(localQuality, remoteQuality);
            }

            @Override
            public void onTryToReconnect() {
                getRoomStatus(roomId);
            }

            @Override
            public void onCallingCancel() {
                unListener();
                mTRTCCalling.hangup();
                mView.finishView();
                Utils.show("??????????????????");
            }
        };
        mTRTCCalling.addDelegate(mTRTCCallingDelegate);
    }

    protected void unListener() {
        Utils.runOnUiThread(() -> {
            if (null != mTRTCCallingDelegate) {
                mTRTCCalling.removeDelegate(mTRTCCallingDelegate);
            }
        });
    }

    //localQuality ????????????????????? remoteQualityList??????????????????????????????????????????1v1?????????????????????
    protected void updateNetworkQuality(TRTCCloudDef.TRTCQuality localQuality, List<TRTCCloudDef.TRTCQuality> remoteQualityList) {
        //????????????????????????????????????????????????????????????????????????
        boolean isLocalLowQuality = isLowQuality(localQuality);
        if (isLocalLowQuality) {
            updateLowQualityTip(true);
        } else {
            if (!remoteQualityList.isEmpty()) {
                TRTCCloudDef.TRTCQuality remoteQuality = remoteQualityList.get(0);
                if (isLowQuality(remoteQuality)) {
                    updateLowQualityTip(false);
                }
            }
        }
    }

    private boolean isLowQuality(TRTCCloudDef.TRTCQuality qualityInfo) {
        if (qualityInfo == null) {
            return false;
        }
        int quality = qualityInfo.quality;
        boolean lowQuality;
        switch (quality) {
            case TRTCCloudDef.TRTC_QUALITY_Vbad:
            case TRTCCloudDef.TRTC_QUALITY_Down:
                lowQuality = true;
                break;
            default:
                lowQuality = false;
        }
        return lowQuality;
    }

    public Drawable getVipGodsImg(CallingInfoBean.FromUserProfile fromUserProfile) {
        if (fromUserProfile != null) {
            if (fromUserProfile.getSex() == 1) {
                if (fromUserProfile.getIsVip() == 1) {
                    return CCApplication.instance().getDrawable(R.drawable.ic_vip);
                } else {
                    if (fromUserProfile.getCertification() == 1) {
                        return CCApplication.instance().getDrawable(R.drawable.ic_real_people);
                    }
                }
            } else {//??????
                if (fromUserProfile.getIsVip() == 1) {
                    return CCApplication.instance().getDrawable(R.drawable.ic_good_goddess);
                } else {
                    if (fromUserProfile.getCertification() == 1) {
                        return CCApplication.instance().getDrawable(R.drawable.ic_real_people);
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

    private void updateLowQualityTip(boolean isSelf) {
        long currentTime = System.currentTimeMillis();
        if (isSelf) {
            if (currentTime - mSelfLowQualityTime > MIN_DURATION_SHOW_LOW_QUALITY) {
                Toast.makeText(CCApplication.instance(), com.tencent.liteav.trtccalling.R.string.trtccalling_self_network_low_quality, Toast.LENGTH_SHORT).show();
                mSelfLowQualityTime = currentTime;
            }
        } else {
            if (currentTime - mOtherPartyLowQualityTime > MIN_DURATION_SHOW_LOW_QUALITY) {
                Toast.makeText(CCApplication.instance(), com.tencent.liteav.trtccalling.R.string.trtccalling_other_party_network_low_quality, Toast.LENGTH_SHORT).show();
                mOtherPartyLowQualityTime = currentTime;
            }
        }
    }

    //??????IM??????
    private void initIMListener() {
        V2TIMManager.getMessageManager().addAdvancedMsgListener(new V2TIMAdvancedMsgListener() {
            @Override
            public void onRecvNewMessage(V2TIMMessage msg) {//???????????????
                if (msg != null && leftUserInfoField.get() != null) {
                    TUIMessageBean info = ChatMessageBuilder.buildMessage(msg);
                    if (info != null) {
                        if (info.getV2TIMMessage().getSender().equals(leftUserInfoField.get().getImId())) {
                            String text = String.valueOf(info.getExtra());
                            if (isJSON2(text) && text.indexOf("type") != -1) {//????????????????????????
                                Map<String, Object> map_data = new Gson().fromJson(text, Map.class);
                                //????????????
                                if (map_data != null && map_data.get("type") != null && map_data.get("type").equals("message_gift")
                                        && map_data.get("is_accost") == null) {
                                    GiftEntity giftEntity = IMGsonUtils.fromJson(String.valueOf(map_data.get("data")), GiftEntity.class);
                                    uc.acceptUserGift.postValue(giftEntity);
                                    //??????????????????
                                    showGiftBarrage(giftEntity);
                                    //??????????????????
                                    giftIncome(giftEntity);
                                }else if (map_data != null && map_data.get("type") != null && map_data.get("type").equals("message_countdown")) {//??????????????????
                                    if (isPayee && isShowTipMoney) {
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
     * ??????????????????
     *
     * @param giftEntity
     */
    private void showGiftBarrage(GiftEntity giftEntity) {
        int nickNameLength = leftUserInfoField.get().getNickname().length();
        String sexText = isMale ? StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt3) : StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt2);
        String messageText = leftUserInfoField.get().getNickname() + " " + StringUtils.getString(R.string.playcc_call_message_deatail_girl_txt1) + " " + sexText + " " + giftEntity.getTitle() + "x" + giftEntity.getAmount();
        int youIndex = messageText.indexOf(sexText);
        SpannableString stringBuilder = new SpannableString(messageText);
        ForegroundColorSpan blueSpanWhite = new ForegroundColorSpan(ColorUtils.getColor(R.color.white));
        stringBuilder.setSpan(blueSpanWhite, 0, messageText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        putRcvItemMessage(stringBuilder, giftEntity.getImgPath(), false);
    }

    /**
     * ??????????????????
     *
     * @param giftEntity
     */
    private void giftIncome(GiftEntity giftEntity) {
        double total = giftEntity.getAmount().intValue() * giftEntity.getProfitTwd().doubleValue();
        String itemMessage = String.format(StringUtils.getString(R.string.profit), String.format("%.2f", total));
        SpannableString itemMessageBuilder = new SpannableString(itemMessage);
        itemMessageBuilder.setSpan(new MyImageSpan(getApplication(),R.drawable.icon_crystal),0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        itemMessageBuilder.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.white)), 1, itemMessage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        putRcvItemMessage(itemMessageBuilder, null, false);
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

    //??????????????????
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

    //??????????????????
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


    public void getCallingInfo(Integer roomId, String fromUserId, String toUserId) {
        model.getCallingInfo(roomId, 1, fromUserId, toUserId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<CallingInfoBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CallingInfoBean> response) {
                        CallingInfoBean callingInviteInfo = response.getData();
                        UserDataBean userDataEntity = model.readUserData();
                        audioUserDataEntity.set(userDataEntity);
                        audioCallingInfoEntity.set(callingInviteInfo);
                        if (callingInviteInfo.getPaymentRelation().getPayeeImId().equals(ConfigManager.getInstance().getUserImID())){
                            isPayee = true;
                        }

                        sayHiEntityList = callingInviteInfo.getSayHiList().getData();
                        if (sayHiEntityList.size() > 1) {
                            sayHiEntityHidden.set(false);
                            sayHiEntityField.set(sayHiEntityList.get(0));
                        }
                        if (userDataEntity.getId().intValue() == callingInviteInfo.getFromUserProfile().getId().intValue()) {
                            rightUserInfoField.set(callingInviteInfo.getFromUserProfile());
                            leftUserInfoField.set(callingInviteInfo.getToUserProfile());
                        } else {
                            rightUserInfoField.set(callingInviteInfo.getToUserProfile());
                            leftUserInfoField.set(callingInviteInfo.getFromUserProfile());
                        }
                        //???????????????0?????????1?????????
                        collected = callingInviteInfo.getCollected();
                        collectedField.set(collected);
                        //???????????????????????????
                        balanceNotEnoughTipsMinutes = callingInviteInfo.getBalanceNotEnoughTipsMinutes();
                        //???????????????
                        unitPriceList = callingInviteInfo.getUnitPriceList();

                        mTRTCCalling.enableAGC(true);
                        mTRTCCalling.enableAEC(true);
                        mTRTCCalling.enableANS(true);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                        uc.callAudioStart.call();
                    }
                });
    }

    public void getTips(Integer toUserId,int type,String isShowCountdown){
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
    //??????????????????
    public void getSayHiList() {
        //????????????????????????
        if (!ObjectUtils.isEmpty(sayHiEntityList) && sayHiePosition + 1 <= sayHiEntityList.size() - 1) {
            sayHiePosition++;
            sayHiEntityField.set(sayHiEntityList.get(sayHiePosition));
            return;
        }
        sayHiePosition++;
        if (sayHiePosition < sayHiEntityList.size()) {
            return;
        }
        sayHiePage++;
        model.getSayHiList(sayHiePage, 30)
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
                                sayHiePosition = 0;
                                sayHiEntityField.set(sayHiEntityList.get(0));
                            }
                        } catch (Exception e) {

                        }
                    }
                });
    }

    //?????????????????????
    public void putRcvItemMessage(SpannableString stringBuilder, String imgPath, boolean sendGiftBagShow) {
        observableList.add(new AudioCallChatingItemViewModel(AudioCallChatingViewModel.this, stringBuilder, imgPath, sendGiftBagShow));
        uc.scrollToEnd.postValue(null);
    }

    public class UIChangeObservable {
        //??????????????????
        public SingleLiveEvent<MallWithdrawTipsInfoBean> clickCrystalExchange = new SingleLiveEvent<>();
        //????????????
        public SingleLiveEvent<Void> callAudioStart = new SingleLiveEvent<>();
        //????????????????????????
        public SingleLiveEvent<Void> callGiftBagAlert = new SingleLiveEvent<>();
        //?????????????????????????????????
        public SingleLiveEvent<Boolean> sendUserGiftError = new SingleLiveEvent<>();
        //??????????????????
        public SingleLiveEvent<Map<String, Object>> sendUserGiftAnim = new SingleLiveEvent<>();
        //??????????????????
        public SingleLiveEvent<GiftEntity> acceptUserGift = new SingleLiveEvent<>();
        //????????????
        public SingleLiveEvent<Void> closeViewHint = new SingleLiveEvent<>();
        //?????????????????????
        public SingleLiveEvent<Void> scrollToEnd = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> startUpSayHiAnimotor = new SingleLiveEvent<>();
    }

    //??????
    public void addLike(boolean isHangup) {
        model.addCollect(leftUserInfoField.get().getId())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        if (isHangup) {
                            hangup();
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

    /**
     * ???????????? ??????????????????SpannableString ??????????????????????????????
     */
    private SpannableString matcherSearchText(int color, String text, String keyword) {
        if (text == null || TextUtils.isEmpty(text)) {
            return SpannableString.valueOf("");
        }
        SpannableString spannableString = new SpannableString(text);
        //?????? keyword
        Pattern pattern = Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE);
        //??????
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            //ForegroundColorSpan ??????new ??????????????????????????????
            spannableString.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        //???????????????????????????
        return spannableString;
    }

}