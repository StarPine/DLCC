package com.fine.friendlycc.ui.mine;

import android.app.Application;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.app.EaringlSwitchUtil;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.bean.AlbumPhotoBean;
import com.fine.friendlycc.bean.ApiConfigManagerBean;
import com.fine.friendlycc.bean.BannerItemBean;
import com.fine.friendlycc.bean.BrowseNumberBean;
import com.fine.friendlycc.bean.EvaluateBean;
import com.fine.friendlycc.bean.PrivacyBean;
import com.fine.friendlycc.bean.SystemConfigTaskBean;
import com.fine.friendlycc.bean.UserDataBean;
import com.fine.friendlycc.bean.UserInfoBean;
import com.fine.friendlycc.event.AvatarChangeEvent;
import com.fine.friendlycc.event.FaceCertificationEvent;
import com.fine.friendlycc.event.MyPhotoAlbumChangeEvent;
import com.fine.friendlycc.event.ProfileChangeEvent;
import com.fine.friendlycc.event.RefreshUserDataEvent;
import com.fine.friendlycc.event.TraceEmptyEvent;
import com.fine.friendlycc.event.MineInfoChangeEvent;
import com.fine.friendlycc.helper.JumpHelper;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.certification.certificationfemale.CertificationFemaleFragment;
import com.fine.friendlycc.ui.mine.audio.TapeAudioFragment;
import com.fine.friendlycc.ui.mine.broadcast.BroadcastFragment;
import com.fine.friendlycc.ui.mine.exclusive.ExclusiveCallActivity;
import com.fine.friendlycc.ui.mine.invitewebdetail.InviteWebDetailFragment;
import com.fine.friendlycc.ui.mine.level.LevelEquityFragment;
import com.fine.friendlycc.ui.mine.likelist.LikeListFragment;
import com.fine.friendlycc.ui.mine.myphotoalbum.MyPhotoAlbumFragment;
import com.fine.friendlycc.ui.mine.myphotoalbum.MyPhotoAlbumItemViewModel;
import com.fine.friendlycc.ui.mine.profile.EditProfileFragment;
import com.fine.friendlycc.ui.mine.setting.MeSettingFragment;
import com.fine.friendlycc.ui.mine.trace.TraceFragment;
import com.fine.friendlycc.ui.mine.trace.man.TraceManFragment;
import com.fine.friendlycc.ui.mine.vipsubscribe.VipSubscribeFragment;
import com.fine.friendlycc.ui.mine.wallet.WalletFragment;
import com.fine.friendlycc.ui.mine.webview.WebViewFragment;
import com.fine.friendlycc.utils.ChatUtils;
import com.fine.friendlycc.utils.ExceptionReportUtils;
import com.fine.friendlycc.utils.FileUploadUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.ui.certification.certificationmale.CertificationMaleFragment;
import com.fine.friendlycc.ui.viewmodel.BaseMyPhotoAlbumViewModel;
import com.tencent.qcloud.tuikit.tuichat.component.AudioPlayer;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public class MineViewModel extends BaseMyPhotoAlbumViewModel<AppRepository> {

    public final String ALLOW_TYPE_VIDEO = "video";
    public final String ALLOW_TYPE_AUDIO = "audio";
    //积分夺宝右侧提示
    public ObservableField<String> entryLabelLable = new ObservableField<>();
    //本地UserData
    public ObservableField<UserDataBean> localUserDataEntity = new ObservableField<>();
    public ObservableField<BannerItemBean> banner = new ObservableField<>();
    //推荐用户弹窗
    public ObservableField<Integer> sex = new ObservableField<>();
    //是否显示邀请入口
    public ObservableField<Boolean> isShowInvite = new ObservableField<>(false);
    public ObservableField<String> albumPrivacyText = new ObservableField<>();
    public ObservableField<UserInfoBean> userInfoEntity = new ObservableField<>(new UserInfoBean());
    //点击顶部广告
    public BindingCommand bannerOnClickCommand = new BindingCommand(() -> {
        if (!StringUtils.isEmpty(banner.get().getLandingPage())) {
            JumpHelper.jump(this, banner.get().getLandingPage());
        }
        banner.set(null);
    });
    //个人资料按钮的点击事件
    public BindingCommand editProfileOnClickCommand = new BindingCommand(() ->
            start(EditProfileFragment.class.getCanonicalName()));
    //钱包按钮的点击事件
    public BindingCommand walletOnClickCommand = new BindingCommand(() -> {
            start(WalletFragment.class.getCanonicalName());
    });

    //点击等级权益
    public BindingCommand levelEquityOnClickCommand = new BindingCommand(() -> {
        start(LevelEquityFragment.class.getCanonicalName());
    });
     //点击主播中心
    public BindingCommand anchorCenterOnClickCommand = new BindingCommand(() -> {
         try {
             Bundle bundle = new Bundle();
             bundle.putString("link", model.readApiConfigManagerEntity().getPlayFunWebUrl() + "/anchor");
             start(WebViewFragment.class.getCanonicalName(), bundle);
         } catch (Exception e) {
             e.printStackTrace();
         }
    });

    //我喜欢的按钮的点击事件
    public BindingCommand fondOnClickCommand = new BindingCommand(() -> {
        CCApplication.instance().logEvent(AppsFlyerEvent.Following);
        if (ObjectUtils.isEmpty(userInfoEntity.get())) {
            return;
        }
        if (userInfoEntity.get().getSex() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString("title", StringUtils.getString(R.string.playcc_mine_my_likes));
            bundle.putInt("sel_idx", 0);
            start(TraceFragment.class.getCanonicalName(), bundle);//女号
        } else {
            start(LikeListFragment.class.getCanonicalName());//男号
        }
    }
    );
    //谁看过我、粉丝的按钮的点击事件
    public BindingCommand traceOnClickCommand = new BindingCommand(() -> {
        if (ObjectUtils.isEmpty(userInfoEntity.get())) {
            return;
        }
        if (userInfoEntity.get().getSex() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString("title", StringUtils.getString(R.string.playcc_mine_my_visitors_many));
            bundle.putInt("sel_idx", 1);
            start(TraceFragment.class.getCanonicalName(), bundle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt("userId", userInfoEntity.get().getId());
            start(TraceManFragment.class.getCanonicalName(), bundle);
        }
    }
    );
    //认证中心按钮的点击事件
    public BindingCommand certificationOnClickCommand = new BindingCommand(() -> {
        if (model.readUserData().getSex() != null) {
            CCApplication.instance().logEvent(AppsFlyerEvent.Verify_Your_Profile);
            if (model.readUserData().getSex() == AppConfig.MALE) {
                start(CertificationMaleFragment.class.getCanonicalName());
                return;
            } else if (model.readUserData().getSex() == AppConfig.FEMALE) {
                start(CertificationFemaleFragment.class.getCanonicalName());
                return;
            }
        }
        ToastUtils.showShort(R.string.playcc_sex_unknown);
    });
    //邀请码按钮的点击事件
    public BindingCommand invitationCodeOnClickCommand = new BindingCommand(() -> {
        if (userInfoEntity.get() == null) {
            return;
        }
        try {
            ApiConfigManagerBean apiConfigManagerEntity = ConfigManager.getInstance().getAppRepository().readApiConfigManagerEntity();
            if(apiConfigManagerEntity!=null && apiConfigManagerEntity.getPlayChatApiUrl()!=null){
                Bundle bundle = InviteWebDetailFragment.getStartBundle(apiConfigManagerEntity.getPlayChatApiUrl() + userInfoEntity.get().getInviteUrl(), userInfoEntity.get().getCode());
                start(InviteWebDetailFragment.class.getCanonicalName(), bundle);
            }
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    //商店点击入口
    public BindingCommand shopOnClickCommand = new BindingCommand(() -> {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("link", model.readApiConfigManagerEntity().getPlayFunWebUrl() + "/shop");
            start(WebViewFragment.class.getCanonicalName(), bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    });

    //会员按钮的点击事件
    public BindingCommand memberOnClickCommand = new BindingCommand(() -> {
        CCApplication.instance().logEvent(AppsFlyerEvent.VIP_Center);
        start(VipSubscribeFragment.class.getCanonicalName());
    });
    //我的声音按钮的点击事件
    public BindingCommand tapeAudioOnClickCommand = new BindingCommand(() -> {
        if(!StringUtils.isEmpty(userInfoEntity.get().getSound())){
            if(!ObjectUtils.isEmpty(userInfoEntity.get().getSoundStatus()) && userInfoEntity.get().getSoundStatus()==0 ){
                return;
            }else if(!ObjectUtils.isEmpty(userInfoEntity.get().getSoundStatus()) && userInfoEntity.get().getSoundStatus()==1 ){
                return;
            }
        }

        CCApplication.instance().logEvent(AppsFlyerEvent.Add_voice);
        start(TapeAudioFragment.class.getCanonicalName());
    });
    //我的广播按钮的点击事件
    public BindingCommand broadcastOnClickCommand = new BindingCommand(() -> {
        CCApplication.instance().logEvent(AppsFlyerEvent.My_Post);
        start(BroadcastFragment.class.getCanonicalName());
    });
    //我的相册按钮的点击事件
    public BindingCommand albumOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            CCApplication.instance().logEvent(AppsFlyerEvent.Add_photos_Video);
            start(MyPhotoAlbumFragment.class.getCanonicalName());
        }
    });
    //设置按钮的点击事件
    public BindingCommand settingOnClickCommand = new BindingCommand(() -> {
        start(MeSettingFragment.class.getCanonicalName());
    });

    //联系客服按钮的点击事件
    public BindingCommand serviceOnClickCommand = new BindingCommand(() -> {
        try {
            CCApplication.instance().logEvent(AppsFlyerEvent.Contact_Us);
            ChatUtils.chatUser(AppConfig.CHAT_SERVICE_USER_ID_SEND, 0,StringUtils.getString(R.string.playcc_chat_service_name), MineViewModel.this);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    //投诉客服按钮的点击事件
    public BindingCommand complaintOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtils.showShort(R.string.playcc_mine_complaint_service);
        }
    });
    UIChangeObservable uc = new UIChangeObservable();
    //我的评价按钮的点击事件
    public BindingCommand evaluateOnClickCommand = new BindingCommand(() -> getMyEvaluate());
    //专属招呼
    public BindingCommand exclusiveOnClickCommand = new BindingCommand(() -> {
        startActivity(ExclusiveCallActivity.class);
    });
    //点击我的头像
    public BindingCommand avatarOnClickCommand = new BindingCommand(() -> uc.clickAvatar.call());
    //紅包照片
    public BindingCommand redPackagePhotoOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.clickSetRedPackagePhoto.call();
        }
    });
    //相册隐私按钮的点击事件
    public BindingCommand privacyOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (userInfoEntity.get() == null) {
                return;
            }
            CCApplication.instance().logEvent(AppsFlyerEvent.Photos_settings);
            uc.clickPrivacy.call();
        }
    });
    //阅后即焚恢复
    public BindingCommand burnRecoverOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.clickRecoverBurn.call();
        }
    });
    //删除视频
    public BindingCommand removeAudio = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (AudioPlayer.getInstance().isPlaying()) {
                AudioPlayer.getInstance().stopPlay();
            }
            uc.removeAudioAlert.call();
        }
    });

    private Disposable mAvatarChangeSubscription, mProfileChangeSubscription, mPhotoAlbumChangeSubscription, mFaceCertificationSubscription, UserUpdateVipEvent, mVipRechargeSubscription, mTraceEmptyEvent,refreshUserDataEvent;
    //请求谁看过我、粉丝间隔时间
    private Long intervalTime = null;

    public MineViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
        sex.set(model.readUserData().getSex());
        isShowInvite.set(model.readSwitches(EaringlSwitchUtil.INVITE_FRIEND) == 1);
    }

    //相册属性 1公开 2付费解锁 3查看需要我验证
    public String getAlbumPrivacy() {
        if (userInfoEntity.get().getAlbumType() == 1) {
            return StringUtils.getString(R.string.playcc_public_open);
        } else if (userInfoEntity.get().getAlbumType() == 2) {
            return String.format(StringUtils.getString(R.string.playcc_pay_ro_unlock_diamond), userInfoEntity.get().getAlbumMoney());
        } else if (userInfoEntity.get().getAlbumType() == 3) {
            return StringUtils.getString(R.string.playcc_need_verify_before_viewing);
        }
        return StringUtils.getString(R.string.playcc_unknown);
    }

    public String getVipTimeText(UserInfoBean userInfoEntity) {
        if(userInfoEntity != null){
            String validTime =  StringUtils.getString(R.string.playfun_valid_time);
            validTime += "\n"+userInfoEntity.getEndTime();
            return validTime;
        }
        return "";
    }

    public void setAllowPrivacy(String type, boolean isOpen) {
        PrivacyBean entity = new PrivacyBean();
        if (type.equals(ALLOW_TYPE_AUDIO)){
            entity.setAllowAudio(isOpen);
        }else if (type.equals(ALLOW_TYPE_VIDEO)){
            entity.setAllowVideo(isOpen);
        }
        model.setPrivacy(entity)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (type.equals(ALLOW_TYPE_AUDIO) && !isOpen){
                            ToastUtils.showShort(R.string.playcc_mine_ban_audio_tips);
                        }else if (type.equals(ALLOW_TYPE_VIDEO) && !isOpen){
                            ToastUtils.showShort(R.string.playcc_mine_ban_video_tips);
                        }
                        dismissHUD();
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        refreshUserDataEvent = RxBus.getDefault().toObservable(RefreshUserDataEvent.class)
                .subscribe(event->{
                    userInfoEntity.get().setSound(event.getSound());
                    userInfoEntity.get().setSoundTime(event.getSoundTime());
                    userInfoEntity.get().setSoundStatus(0);
                    UserInfoBean user = userInfoEntity.get();
                    userInfoEntity.set(null);
                    userInfoEntity.set(user);
                });
        mAvatarChangeSubscription = RxBus.getDefault().toObservable(AvatarChangeEvent.class)
                .subscribe(event -> userInfoEntity.get().setAvatar(event.getAvatarPath()));
        mProfileChangeSubscription = RxBus.getDefault().toObservable(ProfileChangeEvent.class)
                .subscribe(event -> loadUserInfo());
        mPhotoAlbumChangeSubscription = RxBus.getDefault().toObservable(MyPhotoAlbumChangeEvent.class)
                .subscribe(event -> {
                    if (event.getType() == MyPhotoAlbumChangeEvent.TYPE_REFRESH) {
                        loadAlbumDetail(8);
                    } else if (event.getType() == MyPhotoAlbumChangeEvent.TYPE_SET_DATA) {
                        observableList.clear();
                        photoEntityList.clear();
                        for (AlbumPhotoBean datum : event.getPhotos()) {
                            photoEntityList.add(datum);
                            MyPhotoAlbumItemViewModel itemViewModel = new MyPhotoAlbumItemViewModel(MineViewModel.this, datum);
                            observableList.add(itemViewModel);
                        }
                    } else if (event.getType() == MyPhotoAlbumChangeEvent.TYPE_CANCEL_RED_PACKAGE) {
//                        for (MyPhotoAlbumItemViewModel myPhotoAlbumItemViewModel : observableList) {
//                            if (myPhotoAlbumItemViewModel.itemEntity.get().getIsRedPackage() == 1) {
//                                myPhotoAlbumItemViewModel.itemEntity.get().setIsRedPackage(0);
//                            }
//                        }
                        loadAlbumDetail(8);
                    } else {
                        for (MyPhotoAlbumItemViewModel myPhotoAlbumItemViewModel : observableList) {
                            if (myPhotoAlbumItemViewModel.itemEntity.get().getId().intValue() == event.getPhotoId().intValue()) {
                                if (event.getType() == MyPhotoAlbumChangeEvent.TYPE_DELETE) {
                                    loadAlbumDetail(8);
                                } else if (event.getType() == MyPhotoAlbumChangeEvent.TYPE_SET_BURN) {
//                                    if (myPhotoAlbumItemViewModel.itemEntity.get().getIsBurn() != 1) {
//                                        myPhotoAlbumItemViewModel.itemEntity.get().setIsBurn(1);
//                                    }
                                    loadAlbumDetail(8);
                                } else if (event.getType() == MyPhotoAlbumChangeEvent.TYPE_CANCEL_BURN) {
//                                    if (myPhotoAlbumItemViewModel.itemEntity.get().getIsBurn() != 0) {
//                                        myPhotoAlbumItemViewModel.itemEntity.get().setIsBurn(0);
//                                    }
                                    loadAlbumDetail(8);
                                } else if (event.getType() == MyPhotoAlbumChangeEvent.TYPE_SET_RED_PACKAGE) {
//                                    if (myPhotoAlbumItemViewModel.itemEntity.get().getIsRedPackage() != 1) {
//                                        myPhotoAlbumItemViewModel.itemEntity.get().setIsRedPackage(1);
//                                    }
                                    loadAlbumDetail(8);
                                }
                                break;
                            }
                        }
                    }
                });
        mFaceCertificationSubscription = RxBus.getDefault().toObservable(FaceCertificationEvent.class)
                .subscribe(event -> {
                    loadUserInfo();
                    loadAlbumDetail(8);
                });
        mVipRechargeSubscription = RxBus.getDefault().toObservable(MineInfoChangeEvent.class)
                .subscribe(event -> {
                    loadUserInfo();
                });
        mTraceEmptyEvent = RxBus.getDefault().toObservable(TraceEmptyEvent.class)
                .subscribe(event -> {
                    uc.loadBrowseNumber.setValue(null);
                });
        UserUpdateVipEvent = RxBus.getDefault().toObservable(com.fine.friendlycc.event.UserUpdateVipEvent.class)
                .subscribe(userUpdateVipEvent -> {
                    userInfoEntity.get().setIsVip(1);
                    userInfoEntity.get().setEndTime(userUpdateVipEvent.getEndTime());
                    UserInfoBean user = userInfoEntity.get();
                    userInfoEntity.set(null);
                    userInfoEntity.set(user);
                });
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(mAvatarChangeSubscription);
        RxSubscriptions.remove(mProfileChangeSubscription);
        RxSubscriptions.remove(mPhotoAlbumChangeSubscription);
        RxSubscriptions.remove(mFaceCertificationSubscription);
        RxSubscriptions.remove(UserUpdateVipEvent);
    }

    @Override
    public void loadDatas(int page) {
        loadUserInfo();
        loadAlbumDetail(8);
        newsBrowseNumber();
    }

    @Override
    public void onResume() {
        //String appVersionName = AppUtils.getAppVersionName();
        //currentVersion.set(appVersionName);
        //loadUserInfo();
        //loadAlbumDetail(8);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        loadUserInfo();
        loadAlbumDetail(8);
    }

    public void loadUserInfo() {
        model.getUserInfo()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<UserInfoBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<UserInfoBean> response) {
                        userInfoEntity.set(response.getData());
                        albumPrivacyText.set(getAlbumPrivacy());
                        banner.set(response.getData().getMessage());
                        UserDataBean userDataEntity = model.readUserData();
                        userDataEntity.setIsVip(response.getData().getIsVip());
                        userDataEntity.setCertification(response.getData().getCertification());
                        userDataEntity.setEndTime(response.getData().getEndTime());
                        localUserDataEntity.set(userDataEntity);
                        model.saveUserData(userDataEntity);
                        SystemConfigTaskBean systemConfigTaskEntity = ConfigManager.getInstance().getTaskConfig();
                        if (!ObjectUtils.isEmpty(systemConfigTaskEntity) && !StringUtils.isTrimEmpty(systemConfigTaskEntity.getEntryLabel())) {
                            entryLabelLable.set(systemConfigTaskEntity.getEntryLabel());
                        }
                        uc.entryLabelLableEvent.call();
                        boolean allowAudioDef = true;
                        boolean allowVideoDef = true;
                        if(userInfoEntity.get()!=null){
                            if(userInfoEntity.get().getAllowAudio() != null){
                                allowAudioDef = userInfoEntity.get().getAllowAudio();
                            }
                            if(userInfoEntity.get().getAllowVideo() != null){
                                allowVideoDef = userInfoEntity.get().getAllowVideo();
                            }
                        }
                        uc.allowAudio.setValue(allowAudioDef);
                        uc.allowVideo.setValue(allowVideoDef);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        stopRefreshOrLoadMore();
                    }
                });
    }

    //删除录音文件
    public void removeSound(){
        model.removeUserSound()
                .doOnSubscribe(MineViewModel.this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        UserInfoBean userEntity = userInfoEntity.get();
                        userEntity.setSound(null);
                        userInfoEntity.set(null);
                        userInfoEntity.set(userEntity);
                    }
                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    /**
     * 获取我的评价
     */
    private void getMyEvaluate() {
        CCApplication.instance().logEvent(AppsFlyerEvent.Impression);
        model.evaluate(null)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<List<EvaluateBean>>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<List<EvaluateBean>> response) {
                        uc.clickMyEvaluate.postValue(response.getData());
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void saveAvatar(String filePath) {
        Observable.just(filePath)
                .doOnSubscribe(this)
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribeOn(Schedulers.io())
                .map((Function<String, String>) s -> FileUploadUtils.ossUploadFile("avatar/", FileUploadUtils.FILE_TYPE_IMAGE, s))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String fileKey) {
                        userInfoEntity.get().setAvatar(fileKey);
                        updateAvatar(fileKey);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissHUD();
                        ToastUtils.showShort(R.string.playcc_upload_failed);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void updateAvatar(String avatar) {
        model.updateAvatar(avatar)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(MineViewModel.this)
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        ToastUtils.showShort(R.string.playcc_updata_head_success);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void setAlbumPrivacy(Integer type, Integer money) {
        model.setAlbumPrivacy(type, money)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        userInfoEntity.get().setAlbumType(type);
                        userInfoEntity.get().setAlbumMoney(money);
                        albumPrivacyText.set(getAlbumPrivacy());
                        ToastUtils.showShort(R.string.playcc_setting_success);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void burnReset() {
        model.burnReset()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        ToastUtils.showShort(R.string.playcc_restored);
                        userInfoEntity.get().setBurnCount(0);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void newsBrowseNumber() {
        long dayTime = System.currentTimeMillis();
        if (intervalTime != null && (dayTime / 1000) - intervalTime.longValue() <= 2) {
            return;
        }
        if (intervalTime == null) {
            intervalTime = dayTime / 1000;
        }
        model.newsBrowseNumber()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<BrowseNumberBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<BrowseNumberBean> browseNumberEntity) {
                        uc.loadBrowseNumber.setValue(browseNumberEntity.getData());
                    }
                });

    }

    public Integer getIsSound(Integer empty){
        if(!ObjectUtils.isEmpty(empty)){
            if(StringUtils.isEmpty(userInfoEntity.get().getSound()) && empty==1){
                return View.VISIBLE;
            }else{
                return  View.GONE;
            }
        }else{
            return View.GONE;
        }
    }

    public Integer getSoundNo(Integer empty){
        if(StringUtils.isEmpty(userInfoEntity.get().getSound()) && (!ObjectUtils.isEmpty(empty) && empty==0)){
            return View.VISIBLE;
        }else{
            return View.GONE;
        }
    }

    public Integer getSoundStatusShow(Integer status) {
        if(!ObjectUtils.isEmpty(status)){
            if(status.intValue()==0){
                return View.VISIBLE;
            }else {
                return View.GONE;
            }
        }else{
            return View.GONE;
        }
    }
    public Integer getSoundStatusShow2(Integer status) {
        if(!ObjectUtils.isEmpty(status)){
            if (status.intValue()==2){
                return View.VISIBLE;
            }else {
                return View.GONE;
            }
        }else{
            return View.GONE;
        }
    }
    public Integer getSoundStatusShow3(Integer status) {
        if(!StringUtils.isEmpty(userInfoEntity.get().getSound()) && !ObjectUtils.isEmpty(status) && status.intValue()==1){
                return View.VISIBLE;
        }else{
            return View.GONE;
        }
    }

    //是否现实等级权益入口
    public Integer getLevelViewShow(Integer isLevel) {
        if (userInfoEntity.get() != null) {
            if (!ObjectUtils.isEmpty(userInfoEntity.get()) && !ObjectUtils.isEmpty(isLevel) && isLevel.intValue() == 1) {
                return View.VISIBLE;
            }
        }
        return View.GONE;
    }

    public class UIChangeObservable {
        public SingleLiveEvent<List<EvaluateBean>> clickMyEvaluate = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> clickPrivacy = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> clickAvatar = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> clickSetRedPackagePhoto = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> clickRecoverBurn = new SingleLiveEvent<>();
        public SingleLiveEvent<BrowseNumberBean> loadBrowseNumber = new SingleLiveEvent<>();
        //动画效果
        public SingleLiveEvent<Void> entryLabelLableEvent = new SingleLiveEvent<>();
        //删除录音提示
        public SingleLiveEvent<Void> removeAudioAlert = new SingleLiveEvent<>();
        public SingleLiveEvent<Boolean> allowAudio = new SingleLiveEvent<>();
        public SingleLiveEvent<Boolean> allowVideo = new SingleLiveEvent<>();
    }
}