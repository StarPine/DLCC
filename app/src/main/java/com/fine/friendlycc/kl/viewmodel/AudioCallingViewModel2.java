package com.fine.friendlycc.kl.viewmodel;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.entity.CallingInviteInfo;
import com.fine.friendlycc.entity.UserProfileInfo;
import com.fine.friendlycc.event.AudioCallingCancelEvent;
import com.fine.friendlycc.kl.view.Ifinish;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.fine.friendlycc.R;
import com.fine.friendlycc.kl.Utils;
import com.tencent.liteav.trtccalling.TUICalling;
import com.tencent.liteav.trtccalling.model.TRTCCalling;
import com.tencent.liteav.trtccalling.model.TRTCCallingDelegate;
import com.tencent.trtc.TRTCCloudDef;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;

public class AudioCallingViewModel2 extends BaseViewModel<AppRepository> implements Ifinish {

    private static final int MIN_DURATION_SHOW_LOW_QUALITY = 5000; //显示网络不佳最小间隔时间
    public ObservableField<String> maleBinding = new ObservableField<>("");
    public ObservableField<Boolean> isCallBinding = new ObservableField<>(false);

    public ObservableField<CallingInviteInfo> callingInviteInfoField = new ObservableField<>();
    protected TRTCCalling mTRTCCalling;
    protected TRTCCallingDelegate mTRTCCallingDelegate;
    //返回上一页
    public SingleLiveEvent<Void> backViewEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Integer> startAudioActivity = new SingleLiveEvent<>();

    public boolean audioSuccess = false;
    public View.OnClickListener acceptOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!audioSuccess) {
                return;
            }
            if (isCancel){
                finishView();
                return;
            }
            mTRTCCalling.accept();
            unListen();
            startAudioActivity.postValue(roomId);
        }
    };
    protected int roomId;
    private String fromUserId;
    private String toUserId;
    private TUICalling.Role mRole;
    public View.OnClickListener closeOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancelCallClick();
        }
    };
    public View.OnClickListener rejectOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancelCallClick();
        }
    };
    //订阅者
    private Disposable mSubscription;
    private long mSelfLowQualityTime;
    private long mOtherPartyLowQualityTime;
    private boolean isCancel;

    public AudioCallingViewModel2(@NonNull @NotNull Application application, AppRepository model) {
        super(application, model);
//        mTRTCCalling = TRTCCalling.sharedInstance(application);
    }

    //注册RxBus
    @Override
    public void registerRxBus() {
        super.registerRxBus();
        mSubscription = RxBus.getDefault().toObservable(AudioCallingCancelEvent.class)
                .subscribe(event -> {
                    cancelCallClick();
                });
        //将订阅者加入管理站
        RxSubscriptions.add(mSubscription);
    }

    //移除RxBus
    @Override
    public void removeRxBus() {
        super.removeRxBus();
        //将订阅者从管理站中移除
        RxSubscriptions.remove(mSubscription);
    }

    public void init(String fromUserId, String toUserId, TUICalling.Role role, int roomId) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.mRole = role;
        isCallBinding.set(role == TUICalling.Role.CALL);
        this.roomId = roomId;
        mTRTCCalling = TRTCCalling.sharedInstance(AppContext.instance());
    }

    public void init(String fromUserId, String toUserId, TUICalling.Role role) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.mRole = role;
        isCallBinding.set(role == TUICalling.Role.CALL);
        mTRTCCalling = TRTCCalling.sharedInstance(AppContext.instance());
    }

    public void start() {
        if (mRole == TUICalling.Role.CALL) {
            startCall();
        } else {
            startCalled();
        }
        mTRTCCalling.addDelegate(mTRTCCallingDelegate);
    }

    public void cancelCallClick() {
        unListen();
        endTRTCCalling();
        finishView();
    }

    // 只设计监听取消和界面返回， 不涉及mTRTCCalling
    public void unListen() {
        Utils.runOnUiThread(() -> {
            if (null != mTRTCCallingDelegate) {
                mTRTCCalling.removeDelegate(mTRTCCallingDelegate);
            }
        });
    }

    // 正常拨打等待接听中的挂断， 不涉及界面
    public void endTRTCCalling() {
        Utils.runOnUiThread(() -> {
            if (TUICalling.Role.CALL == mRole) {
                mTRTCCalling.hangup();
            } else {
                mTRTCCalling.reject();
            }
        });
    }

    // 主叫
    public void startCall() {
        // 添加 监听
        mTRTCCallingDelegate = new EmptyTRTCCallingDelegate() {
            @Override
            public void onError(int code, String msg) {
                // TODO
                unListen();
                finishView();
                ToastUtils.showLong(AppContext.instance().getString(com.tencent.liteav.trtccalling.R.string.trtccalling_toast_call_error_msg, code, msg));
            }

            @Override
            public void onCallingCancel() {
                unListen();
                finishView();
                Utils.show("對方取消通話");
                isCancel = true;
            }

            @Override
            public void onUserEnter(String userId) {
                unListen();
                startAudioActivity.postValue(roomId);
            }

            @Override
            public void onReject(String userId) {
                unListen();
                finishView();
                Utils.show(AppContext.instance().getString(R.string.playfun_the_other_party_refuses_to_answer));

            }

            @Override
            public void onNoResp(String userId) {
                unListen();
                finishView();
                Utils.show(AppContext.instance().getString(R.string.playfun_the_other_party_is_temporarily_unavailable));
            }

            @Override
            public void onLineBusy(String userId) {
                unListen();
                finishView();
                Utils.show(AppContext.instance().getString(R.string.playfun_the_other_party_is_on_a_call));
            }

            @Override
            public void onCallEnd() {
                unListen();
                finishView();
                Utils.show(AppContext.instance().getString(R.string.playfun_call_ended));
            }

            @Override
            public void onNetworkQuality(TRTCCloudDef.TRTCQuality localQuality, ArrayList<TRTCCloudDef.TRTCQuality> remoteQuality) {
                updateNetworkQuality(localQuality, remoteQuality);
            }
        };
        List<String> list = new ArrayList<>(1);
        list.add(toUserId);
        mTRTCCalling.call(roomId, list, TRTCCalling.TYPE_AUDIO_CALL);
    }

    // 被叫
    public void startCalled() {
        // 1. 添加监听
        mTRTCCallingDelegate = new EmptyTRTCCallingDelegate() {
            @Override
            public void onError(int code, String msg) {
                unListen();
                finishView();
                ToastUtils.showLong(AppContext.instance().getString(com.tencent.liteav.trtccalling.R.string.trtccalling_toast_call_error_msg, code, msg));
            }

            @Override
            public void onCallingCancel() {
                unListen();
                finishView();
                Utils.show(AppContext.instance().getString(R.string.playfun_the_other_party_cancels_the_call));
            }

            @Override
            public void onCallingTimeout() {
                unListen();
                finishView();
                Utils.show(AppContext.instance().getString(R.string.playfun_the_other_party_is_temporarily_unavailable));
            }

            @Override
            public void onNetworkQuality(TRTCCloudDef.TRTCQuality localQuality, ArrayList<TRTCCloudDef.TRTCQuality> remoteQuality) {
                updateNetworkQuality(localQuality, remoteQuality);
            }
        };
    }

    //localQuality 己方网络状态， remoteQualityList对方网络状态列表，取第一个为1v1通话的网络状态
    protected void updateNetworkQuality(TRTCCloudDef.TRTCQuality localQuality, List<TRTCCloudDef.TRTCQuality> remoteQualityList) {
        //如果己方网络和对方网络都很差，优先显示己方网络差
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

    private void updateLowQualityTip(boolean isSelf) {
        long currentTime = System.currentTimeMillis();
        if (isSelf) {
            if (currentTime - mSelfLowQualityTime > MIN_DURATION_SHOW_LOW_QUALITY) {
                Toast.makeText(AppContext.instance(), com.tencent.liteav.trtccalling.R.string.trtccalling_self_network_low_quality, Toast.LENGTH_SHORT).show();
                mSelfLowQualityTime = currentTime;
            }
        } else {
            if (currentTime - mOtherPartyLowQualityTime > MIN_DURATION_SHOW_LOW_QUALITY) {
                Toast.makeText(AppContext.instance(), com.tencent.liteav.trtccalling.R.string.trtccalling_other_party_network_low_quality, Toast.LENGTH_SHORT).show();
                mOtherPartyLowQualityTime = currentTime;
            }
        }
    }

    public Drawable getVipGodsImg(CallingInviteInfo callingInviteInfo) {
        if (callingInviteInfo != null){
            UserProfileInfo userProfileInfo = callingInviteInfo.getUserProfileInfo();
            if (userProfileInfo != null) {
                if (userProfileInfo.getSex() == 1) {
                    if(userProfileInfo.getIsVip()==1){
                        return AppContext.instance().getDrawable(R.drawable.ic_vip);
                    }else {
                        if(userProfileInfo.getCertification()==1){
                            return AppContext.instance().getDrawable(R.drawable.ic_real_people);
                        }
                    }
                } else {//女生
                    if(userProfileInfo.getIsVip()==1){
                        return AppContext.instance().getDrawable(R.drawable.ic_good_goddess);
                    }else {
                        if(userProfileInfo.getCertification()==1){
                            return  AppContext.instance().getDrawable(R.drawable.ic_real_people);
                        }
                    }
                }
            }
        }

        return null;
    }

    public String gameUrl(String gameChannel){
        return ConfigManager.getInstance().getGameUrl(gameChannel);
    }

    public boolean isEmpty(String obj){
        return obj == null || obj.equals("");
    }

    public String ageAndConstellation(CallingInviteInfo callingInviteInfo) {
        if (callingInviteInfo != null) {
            return String.format(StringUtils.getString(R.string.playfun_age_and_constellation), callingInviteInfo.getUserProfileInfo().getAge(), callingInviteInfo.getUserProfileInfo().getConstellation());
        }
        return "";
    }

    //拨打语音、视频---接听人
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
                        if (model.readUserData().getSex() == 0 && ConfigManager.getInstance().getTipMoneyShowFlag()) {
                            if (!ObjectUtils.isEmpty(callingInviteInfo.getMessages()) && callingInviteInfo.getMessages().size() > 0) {
                                String valueData = "";
                                for (String value : callingInviteInfo.getMessages()) {
                                    valueData += value + "\n";
                                }
                                maleBinding.set(valueData);
                            }
                        }
                        callingInviteInfoField.set(callingInviteInfo);
                        if (callingInviteInfo.getMinutesRemaining() != null && callingInviteInfo.getMinutesRemaining().intValue() <= 0) {
                            cancelCallClick();
                            return;
                        }
                        if (callingInviteInfo.getMinutesRemaining() != null && callingInviteInfo.getMinutesRemaining().intValue() > 0) {
                            audioSuccess = true;
                        }
                        start();
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    @Override
    public void finishView() {
        backViewEvent.call();
    }
}
