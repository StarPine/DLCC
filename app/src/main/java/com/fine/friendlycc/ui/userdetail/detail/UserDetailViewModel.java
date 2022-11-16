package com.fine.friendlycc.ui.userdetail.detail;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.app.Injection;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.observer.BaseUserDetailEmptyObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.AlbumPhotoEntity;
import com.fine.friendlycc.entity.CallingInviteInfo;
import com.fine.friendlycc.entity.EvaluateEntity;
import com.fine.friendlycc.entity.EvaluateItemEntity;
import com.fine.friendlycc.entity.EvaluateObjEntity;
import com.fine.friendlycc.entity.IsChatEntity;
import com.fine.friendlycc.entity.StatusEntity;
import com.fine.friendlycc.entity.UserDetailEntity;
import com.fine.friendlycc.event.AccostEvent;
import com.fine.friendlycc.event.AddBlackListEvent;
import com.fine.friendlycc.event.LikeChangeEvent;
import com.fine.friendlycc.event.TheirPhotoAlbumChangeEvent;
import com.fine.friendlycc.event.UserRemarkChangeEvent;
import com.fine.friendlycc.event.UserUpdateVipEvent;
import com.fine.friendlycc.kl.Utils;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.manager.LocationManager;
import com.fine.friendlycc.utils.ChatUtils;
import com.fine.friendlycc.utils.FileUploadUtils;
import com.fine.friendlycc.utils.ListUtils;
import com.fine.friendlycc.utils.TimeUtils;
import com.fine.friendlycc.utils.ToastCenterUtils;
import com.fine.friendlycc.widget.emptyview.EmptyState;
import com.google.gson.Gson;
import com.fine.friendlycc.R;
import com.fine.friendlycc.ui.userdetail.userdynamic.UserDynamicFragment;
import com.fine.friendlycc.ui.viewmodel.BaseTheirPhotoAlbumViewModel;
import com.tencent.qcloud.tuicore.Status;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public class UserDetailViewModel extends BaseTheirPhotoAlbumViewModel<AppRepository> {

    public ObservableField<Boolean> userDisable = new ObservableField<>(false);
    public ObservableField<Boolean> canEvaluate = new ObservableField<>(false);
    public ObservableField<Boolean> showCanTrack = new ObservableField<>(false);
    public ObservableField<String> sexName = new ObservableField<>("");
    public ObservableField<String> sexNameSheAndHis = new ObservableField<>("");
    public ObservableField<String> distanceText = new ObservableField<>();
    public ObservableField<String> onlineStatusText = new ObservableField<>();

    public ObservableField<UserDetailEntity> detailEntity = new ObservableField<>();

    public ObservableField<String> lineAccount = new ObservableField<>();
    public ObservableField<String> remark = new ObservableField<>();
    public ObservableField<String> unLockAlbumText = new ObservableField<>();
    public ObservableField<String> albumNumberText = new ObservableField<>();

    public ObservableField<String> dynamicPic1 = new ObservableField<>();
    public ObservableField<String> dynamicPic2 = new ObservableField<>();
    public ObservableField<String> dynamicPic3 = new ObservableField<>();
    public ObservableField<String> dynamicPic4 = new ObservableField<>();
    public int position ;
    UIChangeObservable uc = new UIChangeObservable();
    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(() -> {
        loadData();
    });
    /**
     * 停止下拉刷新或加载更多动画
     */
    protected void stopRefreshOrLoadMore() {
        uc.finishRefreshing.call();
    }


    //是否显示用户标签
    public ObservableField<Boolean> isUserTagShow = new ObservableField<>(false);
    //进入心情
    public BindingCommand userDynamicOnClickCommand = new BindingCommand(() -> {
        if (detailEntity.get() == null) {
            return;
        }
        if (ObjectUtils.isEmpty(detailEntity.get().getNews()) || ObjectUtils.isEmpty(detailEntity.get().getNews())) {
            return;
        }
        AppContext.instance().logEvent(AppsFlyerEvent.His_Her_Monents);
        Bundle bundle = UserDynamicFragment.getStartBundle(userId.get(), detailEntity.get().getSex());
        start(UserDynamicFragment.class.getCanonicalName(), bundle);
    });
    public BindingCommand collectOnClickCommand = new BindingCommand(() -> {
        if (userDisable.get()) {
            return;
        }
        if (detailEntity.get() == null) {
            return;
        }
        AppContext.instance().logEvent(AppsFlyerEvent.Following_2);
        if (!detailEntity.get().getCollect()) {
            addLike();
        } else {
            delLike();
        }
    });
    //完成按钮的点击事件
    public BindingCommand moreOnClickCommand = new BindingCommand(() -> uc.clickMore.call());
    //评价流程
    public BindingCommand evaluateOnClickCommand = new BindingCommand(() -> {
        loadCanEvaluate();
    });
    /**
     * 点击拨打视频语音
     */
    public BindingCommand socialAccountOnClickCommand = new BindingCommand(() -> {
        if (Status.mIsShowFloatWindow){
            ToastUtils.showShort(R.string.audio_in_call);
            return;
        }
        if (detailEntity.get() == null) {
            return;
        }
        uc.startVideo.call();
    });
    /**
     * 語音通話
     */
    public BindingCommand connMicOnClickCommand = new BindingCommand(() -> {
        if (Status.mIsShowFloatWindow){
            ToastUtils.showShort(R.string.audio_in_call);
            return;
        }
        if (detailEntity.get() == null) {
            return;
        }
        uc.startAudio.call();
    });
    //申请查看
    public BindingCommand applyCheckDetailOnClickCommand = new BindingCommand(() -> {
        if (detailEntity.get() == null) {
            return;
        }
        uc.clickApplyCheckDetail.postValue(detailEntity.get().getNickname());
    });
    private Disposable mSubscription;
    private Disposable mPhotoStateChangeSubscription;
    private Disposable UserUpdateVipEventSub;
    private boolean isLinkMic = false;
    //进入私聊页面
    public BindingCommand chatOnClickCommand = new BindingCommand(() -> {
        if (detailEntity.get() == null) {
            return;
        }
        if (detailEntity.get().getIsAccost() == 1){
            AppContext.instance().logEvent(AppsFlyerEvent.Send_message);
            ChatUtils.chatUser(detailEntity.get().getImToUserId(),userId.get(), detailEntity.get().getNickname(), this);
        }else {
            putAccostFirst();
        }

    });
    //申请查看相册
    public BindingCommand applyCheckAlbumOnClickCommand = new BindingCommand(() -> {
        AppContext.instance().logEvent(AppsFlyerEvent.View_photos);
        isChat(userId.get(), 2);
    }
    );

    public UserDetailViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        loadData();
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        mSubscription = RxBus.getDefault().toObservable(UserRemarkChangeEvent.class)
                .subscribe(event -> {
                    if (detailEntity.get() == null) {
                        return;
                    }
                    if (userId.get() == event.getUserId()) {
                        detailEntity.get().setRemarkNickname(event.getRemarkName());
                    }
                });
        mPhotoStateChangeSubscription = RxBus.getDefault().toObservable(TheirPhotoAlbumChangeEvent.class)
                .subscribe(event -> {
                    if (detailEntity.get() == null) {
                        return;
                    }
                    for (AlbumPhotoEntity albumPhotoEntity : detailEntity.get().getAlbum()) {
                        if (event.getType() == TheirPhotoAlbumChangeEvent.TYPE_BURN) {
                            if (albumPhotoEntity.getId().intValue() == event.getPhotoId().intValue()) {
                                if (albumPhotoEntity.getBurnStatus() != 1) {
                                    albumPhotoEntity.setBurnStatus(1);
                                }
                                break;
                            }
                        } else if (event.getType() == TheirPhotoAlbumChangeEvent.TYPE_PAY_RED_PACKAGE) {
                            if (albumPhotoEntity.getId().intValue() == event.getPhotoId().intValue()) {
                                if (albumPhotoEntity.getIsPay() != 1) {
                                    albumPhotoEntity.setIsPay(1);
                                }
                                break;
                            }
                        }
                    }
                    showAlbum(detailEntity.get().getAlbum(), 8);
                });
        UserUpdateVipEventSub = RxBus.getDefault().toObservable(UserUpdateVipEvent.class)
                .subscribe(userUpdateVipEvent -> {
                    uc.VipSuccessFlag.call();
                });
        //将订阅者加入管理站
        RxSubscriptions.add(mSubscription);
        RxSubscriptions.add(mPhotoStateChangeSubscription);
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        //将订阅者从管理站中移除
        RxSubscriptions.remove(mSubscription);
        RxSubscriptions.remove(mPhotoStateChangeSubscription);
    }

    public boolean personalInfoIsEmpty(UserDetailEntity userDetailEntity){
        if (userDetailEntity != null){
            if (TextUtils.isEmpty(userDetailEntity.getDesc())
                    && (userDetailEntity.getWeight() == null || userDetailEntity.getWeight() <= 0)
                    && (userDetailEntity.getHeight() == null || userDetailEntity.getHeight() <= 0)){
                return true;
            }
        }
        return false;
    }

    public void refreshDistance() {
        String distance = StringUtils.getString(R.string.playcc_unknown);
        if (detailEntity.get() == null) {
            return;
        }
        Double d = detailEntity.get().getDistance();
        if (d != null) {
            if (d == -1) {
                distance = StringUtils.getString(R.string.playcc_unknown);
            } else if (d == -2) {
                distance = StringUtils.getString(R.string.playcc_keep);
            } else {
                if (d > 1000) {
                    double df = d / 1000;
                    if (df > 999) {
                        distance = String.format(">%.0fkm", df);
                    } else {
                        distance = String.format("%.1fkm", df);
                    }
                } else {
                    distance = String.format("%sm", d.intValue());
                }
            }
        }
        distanceText.set(distance);
    }

    public void refreshOnlineStatus() {
        if (detailEntity.get() == null) {
            return;
        }
        String onlineStatus = StringUtils.getString(R.string.playcc_unknown);
        if (detailEntity.get().getIsOnline() == -1) {
            onlineStatus = StringUtils.getString(R.string.playcc_keep);
        } else if (detailEntity.get().getIsOnline() == 1) {
            onlineStatus = StringUtils.getString(R.string.playcc_on_line);
        } else if (detailEntity.get().getIsOnline() == 0) {
            if (StringUtils.isEmpty(detailEntity.get().getOfflineTime())) {
                onlineStatus = StringUtils.getString(R.string.playcc_unknown);
            } else {
                onlineStatus = TimeUtils.getFriendlyTimeSpan(detailEntity.get().getOfflineTime());
            }
        }
        onlineStatusText.set(onlineStatus);
    }

    public void loadData() {
        model.userMain(userId.get(), LocationManager.getInstance().getLng(), LocationManager.getInstance().getLat())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseUserDetailEmptyObserver<BaseDataResponse<UserDetailEntity>>(this) {
                    @Override
                    public void onSuccess(BaseDataResponse<UserDetailEntity> response) {
                        super.onSuccess(response);
//                        if (response.getData().getMoreNumber() <= 0) {
//                            uc.todayCheckNumber.postValue(0);
//                            if (!response.getData().isBrowse()) {
//                                return;
//                            }
//                        } else if (response.getData().getMoreNumber() <= 4) {
//                            uc.todayCheckNumber.postValue(response.getData().getMoreNumber());
//                        }
                        detailEntity.set(response.getData());
                        isShowCanTrack();
//                        onLineDrawables();
                        if (response.getData().isBrowse()) {
                            if (response.getData().getMoreNumber() != null && response.getData().getMoreNumber().intValue() > 2) {

                            } else {
                                uc.isAlertVipMonetyunlock.call();
                            }
                        } else {
                            uc.isAlertVipMonetyunlock.call();
                        }
                        refreshDistance();
                        refreshOnlineStatus();
                        sexName.set(detailEntity.get().getSex() == 1 ? StringUtils.getString(R.string.playcc_user_sex_his) : StringUtils.getString(R.string.playcc_user_sex_her));
                        sexNameSheAndHis.set(detailEntity.get().getSex() == 1 ? StringUtils.getString(R.string.playcc_user_sex_his) : StringUtils.getString(R.string.playcc_user_sex_she));
                        showAlbum2(response.getData().getAlbum(), response.getData().getAlbumImgTotal());
                        if (model.readUserData().getSex() == 1) {
                            unLockAlbumText.set(String.format(StringUtils.getString(R.string.playcc_unlock_her_photo_album_member_free), detailEntity.get().getAlbumPayMoney()));
                        } else {
                            unLockAlbumText.set(String.format(StringUtils.getString(R.string.playcc_unlock_he_photo_album_goddess_free), detailEntity.get().getAlbumPayMoney()));
                        }
                        albumNumberText.set(String.format(StringUtils.getString(R.string.playcc_have_photos), detailEntity.get().getAlbum().size()));


                        List<String> news = detailEntity.get().getNews();
                        if (news != null && !news.isEmpty()) {
                            try {
                                dynamicPic1.set(news.get(0));
                            } catch (Exception e) {
                            }
                            try {
                                dynamicPic2.set(news.get(1));
                            } catch (Exception e) {
                            }
                            try {
                                dynamicPic3.set(news.get(2));
                            } catch (Exception e) {
                            }
                            try{
                                dynamicPic4.set(news.get(3));
                            }catch(Exception e){

                            }
                        }
                        loadEvaluate();
                    }

                    @Override
                    public void onError(RequestException e) {
                        if (e.getCode() == 10022) {
                            stateModel.setEmptyState(EmptyState.NORMAL);
                            userDisable.set(true);
//                            detailEntity.get().setNickname(StringUtils.getString(R.string.user_is_disabled));
                        } else if (e.getCode() == 10050) {
//                            stateModel.setEmptyState(EmptyState.EMPTY);
//                            uc.todayCheckNumber.postValue(0);
                            super.onError(e);
                        } else {
                            super.onError(e);
                        }
                    }
                    @Override
                    public void onComplete() {
                        dismissHUD();
                        stopRefreshOrLoadMore();
                    }

                });
    }

    //搭讪
    public void putAccostFirst() {
        model.putAccostFirst(userId.get())
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        ToastUtils.showShort(R.string.playcc_text_accost_success1);
                        RxBus.getDefault().post(new AccostEvent(position));
                        loadData();
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        if(e.getCode()!=null && e.getCode() == 21001 ){//钻石余额不足
                            ToastCenterUtils.showToast(R.string.playcc_dialog_exchange_integral_total_text3);
                            uc.sendAccostFirstError.call();
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissHUD();
                    }
                });
    }

    public void addLike() {
        model.addCollect(userId.get())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        if (detailEntity.get() == null) {
                            return;
                        }
                        UserDetailEntity userDisable = detailEntity.get();
                        userDisable.setCollect(true);
                        detailEntity.set(null);
                        detailEntity.set(userDisable);
                        isShowCanTrack();
                        RxBus.getDefault().post(new LikeChangeEvent(userId.get(), true));
                        ToastUtils.showShort(R.string.playcc_cancel_zuizong_3);
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        if (detailEntity.get() == null) {
                            return;
                        }
                        detailEntity.get().setCollect(false);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void delLike() {
        model.deleteCollect(userId.get())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        ToastUtils.showShort(R.string.playcc_cancel_zuizong_2);
                        if (detailEntity.get() == null) {
                            return;
                        }
                        detailEntity.get().setCollect(false);
                        isShowCanTrack();
                        RxBus.getDefault().post(new LikeChangeEvent(userId.get(), false));
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        if (detailEntity.get() == null) {
                            return;
                        }
                        detailEntity.get().setCollect(true);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void addBlackList() {
        model.addBlack(userId.get())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        RxBus.getDefault().post(new AddBlackListEvent());
                        if (detailEntity.get() == null) {
                            return;
                        }
                        detailEntity.get().setIsBlacklist(1);
                        if (detailEntity.get().getBlacklistStatus() == 0) {
                            detailEntity.get().setBlacklistStatus(1);
                        }else if (detailEntity.get().getBlacklistStatus() == 2){
                            detailEntity.get().setBlacklistStatus(3);
                        }
                        if (detailEntity.get().getCollect()){
                            delLike();
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void delBlackList() {
        model.deleteBlack(userId.get())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        if (detailEntity.get() == null) {
                            return;
                        }
                        detailEntity.get().setIsBlacklist(0);
                        if (detailEntity.get().getBlacklistStatus() == 1) {
                            detailEntity.get().setBlacklistStatus(0);
                        }else if (detailEntity.get().getBlacklistStatus() == 3){
                            detailEntity.get().setBlacklistStatus(2);
                        }
                        isShowCanTrack();
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void loadCanEvaluate() {
        model.evaluateStatus(userId.get())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<StatusEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<StatusEntity> response) {
                        canEvaluate.set(response.getData().getStatus() == 1);
                        if(response.getData().getStatus() == 1){
                            getUserEvaluate();
                        }else if(response.getData().getStatus() ==2){
                            ToastUtils.showShort(R.string.playcc_no_information_cant_comment_hims);
                        }else{
                            ToastUtils.showShort(R.string.playcc_no_information_cant_comment_him);
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        e.printStackTrace();
                    }

                });
    }

    public void getUserEvaluate() {
        model.evaluate(userId.get())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<List<EvaluateEntity>>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<List<EvaluateEntity>> response) {
                        AppContext.instance().logEvent(AppsFlyerEvent.Impression_2);
                        uc.clickEvaluate.postValue(response.getData());
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void isShowCanTrack() {
        Boolean collect = detailEntity.get().getCollect();
        Integer blacklistStatus = detailEntity.get().getBlacklistStatus();
        if (blacklistStatus == 0 && !collect) {
            showCanTrack.set(true);
        } else {
            showCanTrack.set(false);
        }
    }

    public Drawable onLineDrawables(UserDetailEntity detailEntity) {
        if (detailEntity == null) return null;
        if (detailEntity.getCallingStatus() == 0) {
            if (detailEntity.getIsOnline() == 1) {
                return AppContext.instance().getResources().getDrawable(R.drawable.user_detail_online);
            }
        } else if (detailEntity.getCallingStatus() == 1) {
            return AppContext.instance().getResources().getDrawable(R.drawable.user_detail_calling);
        } else if (detailEntity.getCallingStatus() == 2) {
            return AppContext.instance().getResources().getDrawable(R.drawable.user_detail_video);
        }
        return AppContext.instance().getResources().getDrawable(R.drawable.user_detail_online);
    }

    /**
     * 提交负面评价
     *
     * @param tagId
     * @param imageSrc
     */
    public void commitNegativeEvaluate(int tagId, String imageSrc) {
        Observable.just(imageSrc)
                .doOnSubscribe(this)
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return FileUploadUtils.ossUploadFile("evaluate/", FileUploadUtils.FILE_TYPE_IMAGE, s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String fileKey) {
                        commitUserEvaluate(tagId, fileKey);
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

    /**
     * 提交评价
     *
     * @param tagId 评价标签ID
     */
    public void commitUserEvaluate(int tagId, String imgPath) {
        model.evaluateCreate(userId.get(), tagId, imgPath)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        ToastUtils.showShort(R.string.playcc_user_detail_evaluation_hint);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void isChat(int userId, int type) {
        model.isChat(userId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<IsChatEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<IsChatEntity> response) {
                        if (type == 2) {
                            //查看相册
                            if (response.getData().getIsVip() == 1) {
                                //会员查看相册
                                uc.clickVipCheckAlbum.postValue(response.getData().getChatNumber());
                            } else {
                                //非会员查看相册
                                uc.clickPayAlbum.postValue(userId);
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    /**
     * 使用VIP解鎖機會
     *
     * @param userId
     * @param type        1 聊天 & 解鎖社交賬號  2 相冊
     * @param operateType 1 聊天 2 相冊 3 解鎖社交賬號
     */
    public void useVipChat(int userId, int type, int operateType) {
        model.useVipChat(userId, type)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        if (operateType == 1) {
                            chatPaySuccess();
                        } else if (operateType == 2) {
                            payLockAlbumSuccess(userId);
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    /**
     * 申請查看相冊
     *
     * @param userId
     * @param image
     */
    public void applyCheckAlbum(int userId, String image) {
        model.userVerify(userId, image)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        detailEntity.get().setAlbumApply(0);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void uploadImage(String filePath) {
        Observable.just(filePath)
                .doOnSubscribe(this)
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return FileUploadUtils.ossUploadFile("send/", FileUploadUtils.FILE_TYPE_IMAGE, s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String fileKey) {
                        dismissHUD();
                        applyCheckAlbum(userId.get(), fileKey);
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

    /**
     * 支付相册成功
     *
     * @param userId
     */
    public void payLockAlbumSuccess(Integer userId) {
        if (userId.intValue() == this.userId.get().intValue()) {
            detailEntity.get().setAlbumType(1);
        }
    }

    /**
     * 解锁联系方式成功
     */
    public void chatPaySuccess() {
        canEvaluate.set(true);
        addFriend();
    }

    public void addFriend() {
        if (detailEntity.get() == null) {
            return;
        }
        if (isLinkMic) {
            getCallingInvitedInfo(1, detailEntity.get().getImUserId(), detailEntity.get().getImToUserId());
        } else {
            ChatUtils.chatUser(detailEntity.get().getImToUserId(), userId.get(), detailEntity.get().getNickname(), this);
        }
    }

    /**
     * @return void
     * @Desc TODO(进入页面加载用户评价)
     * @author 彭石林
     * @parame []
     * @Date 2021/8/5
     */
    public void loadEvaluate() {
        model.evaluate(userId.get())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<List<EvaluateEntity>>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<List<EvaluateEntity>> response) {

                        List<EvaluateObjEntity> list = null;
                        if (detailEntity.get().getSex() == 1) {
                            list = Injection.provideDemoRepository().readMaleEvaluateConfig();
                        } else {
                            list = Injection.provideDemoRepository().readFemaleEvaluateConfig();
                        }
                        boolean flag = false;
                        List<EvaluateItemEntity> list1 = new ArrayList<EvaluateItemEntity>();
                        for (EvaluateObjEntity configEntity : list) {
                            EvaluateItemEntity evaluateItemEntity = new EvaluateItemEntity(configEntity.getId(), configEntity.getName(), configEntity.getType() == 1);
                            for (EvaluateEntity evaluateEntity : response.getData()) {
                                flag = evaluateEntity.getNumber() > 0;
                                if (configEntity.getId() == evaluateEntity.getTagId()) {
                                    evaluateItemEntity.setNumber(evaluateEntity.getNumber());
                                    if (flag) {
                                        list1.add(evaluateItemEntity);
                                        flag = false;
                                    }
                                }
                            }
                        }
                        if (ListUtils.isEmpty(list1)) {
                            isUserTagShow.set(false);
                        } else {
                            isUserTagShow.set(true);
                        }
                        uc.evaluateItemEntityList.setValue(list1);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //拨打语音、视频
    public void getCallingInvitedInfo(int callingType, String IMUserId, String toIMUserId) {
        if(callingType==1){
            //男女点击拨打语音
            AppContext.instance().logEvent(ConfigManager.getInstance().isMale() ? AppsFlyerEvent.call_voice_male : AppsFlyerEvent.call_voice_female);
        }else{
            //男女点击拨打视频
            AppContext.instance().logEvent(ConfigManager.getInstance().isMale() ? AppsFlyerEvent.call_video_male : AppsFlyerEvent.call_video_female);
        }
        model.callingInviteInfo(callingType, IMUserId, toIMUserId, 0)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<CallingInviteInfo>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CallingInviteInfo> callingInviteInfoBaseDataResponse) {
                        if (callingInviteInfoBaseDataResponse.getCode() == 2) {//對方忙線中
                            uc.otherBusy.call();
                            return;
                        }
                        if (callingInviteInfoBaseDataResponse.getCode() == 22001) {//游戏中
                            Toast.makeText(AppContext.instance(), R.string.playcc_in_game, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        CallingInviteInfo callingInviteInfo = callingInviteInfoBaseDataResponse.getData();
                        if (callingInviteInfo != null) {
                            Utils.tryStartCallSomeone(callingType, toIMUserId, callingInviteInfo.getRoomId(), new Gson().toJson(callingInviteInfo));
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        if (e != null) {
                            if (e.getCode() == 21001) {
                                uc.sendDialogViewEvent.call();
                            }
                        }
                    }


                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public Integer getSoundShow(Integer empty) {
        if (detailEntity.get() != null && StringUtils.isEmpty(detailEntity.get().getSound())) {
            return View.GONE;
        } else {
            if (empty != null && empty == 1) {
                return View.VISIBLE;
            }
            return View.GONE;
        }
    }

    public class UIChangeObservable {
        public SingleLiveEvent startAudio = new SingleLiveEvent<>();
        public SingleLiveEvent startVideo = new SingleLiveEvent<>();
        public SingleLiveEvent clickMore = new SingleLiveEvent<>();
        //对方忙线
        public SingleLiveEvent otherBusy = new SingleLiveEvent<>();
        public SingleLiveEvent<String> clickApplyCheckDetail = new SingleLiveEvent<>();
        public SingleLiveEvent<List<EvaluateEntity>> clickEvaluate = new SingleLiveEvent<>();
        public SingleLiveEvent<Integer> clickPayAlbum = new SingleLiveEvent<>();
        public SingleLiveEvent<Integer> clickVipCheckAlbum = new SingleLiveEvent<>();
        public SingleLiveEvent<Integer> clickVipChat = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> clickConnMic = new SingleLiveEvent<>();
        public SingleLiveEvent<Integer> todayCheckNumber = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> isAlertVipMonetyunlock = new SingleLiveEvent<>();
        public SingleLiveEvent VipSuccessFlag = new SingleLiveEvent();

        public SingleLiveEvent<List<EvaluateItemEntity>> evaluateItemEntityList = new SingleLiveEvent<List<EvaluateItemEntity>>();
        //下拉刷新完成
        public SingleLiveEvent<Void> finishRefreshing = new SingleLiveEvent<>();
        //钻石不足。唤起充值
        public SingleLiveEvent<Void> sendDialogViewEvent = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> sendAccostFirstError = new SingleLiveEvent<>();
    }
}