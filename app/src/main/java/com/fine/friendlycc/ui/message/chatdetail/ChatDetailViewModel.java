package com.fine.friendlycc.ui.message.chatdetail;

import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.app.Injection;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.bean.CallingInviteInfo;
import com.fine.friendlycc.bean.ChatDetailCoinBean;
import com.fine.friendlycc.bean.EvaluateBean;
import com.fine.friendlycc.bean.EvaluateItemBean;
import com.fine.friendlycc.bean.EvaluateObjBean;
import com.fine.friendlycc.bean.GiftBagBean;
import com.fine.friendlycc.bean.IMTransUserBean;
import com.fine.friendlycc.bean.MallWithdrawTipsInfoBean;
import com.fine.friendlycc.bean.PhotoAlbumBean;
import com.fine.friendlycc.bean.PriceConfigBean;
import com.fine.friendlycc.bean.PrivacyBean;
import com.fine.friendlycc.bean.ShowFloatWindowBean;
import com.fine.friendlycc.bean.TagBean;
import com.fine.friendlycc.bean.UserConnMicStatusBean;
import com.fine.friendlycc.bean.UserDataBean;
import com.fine.friendlycc.event.AddBlackListEvent;
import com.fine.friendlycc.event.CallChatingHangupEvent;
import com.fine.friendlycc.event.MessageGiftNewEvent;
import com.fine.friendlycc.event.MineInfoChangeEvent;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.userdetail.detail.UserDetailFragment;
import com.fine.friendlycc.utils.ApiUitl;
import com.fine.friendlycc.utils.LogUtils;
import com.fine.friendlycc.utils.ToastCenterUtils;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.google.gson.Gson;
import com.tencent.qcloud.tuicore.Status;
import com.tencent.qcloud.tuicore.custom.entity.MediaGalleryEditEntity;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;
import com.tencent.qcloud.tuikit.tuichat.event.InsufficientBalanceEvent;

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
import me.goldze.mvvmhabit.utils.StringUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public class ChatDetailViewModel extends BaseViewModel<AppRepository> {
    public static final String TAG = "ChatDetailViewModel";
    public final String ALLOW_TYPE_VIDEO = "video";
    public final String ALLOW_TYPE_AUDIO = "audio";
    //是否是常联系
    public ObservableBoolean isContactsEnabled = new ObservableBoolean(false);
    public ObservableField<Boolean> isTagShow = new ObservableField<>(false);
    public ObservableField<Boolean> inBlacklist = new ObservableField<>(false);
    public ObservableField<Boolean> isTrack = new ObservableField<>(false);//todo 还没根据后端进行初始化
    public ObservableField<Boolean> isHideExchangeRules = new ObservableField<>(true);
    public ObservableField<Boolean> isShoweCallingVideo = new ObservableField<>(false);//是否显示马上视讯入口
    public ObservableField<String> menuTrack = new ObservableField<>();//追踪
    public ObservableField<String> menuBlockade = new ObservableField<>();//封锁
    public ObservableField<TagBean> tagEntitys = new ObservableField<>();
    public ObservableField<List<String>> sensitiveWords = new ObservableField<>();
    public boolean mySelfVideoFlag;
    public boolean mySelfAudioFlag;
    //聊天对方IM 用户ID
    public String TMToUserId;
    //IM聊天价格配置
    public PriceConfigBean priceConfigEntityField = null;
    //男生钻石总额
    public Integer maleBalance = 0;

    public Integer ChatInfoId = null;
    public UIChangeObservable uc = new UIChangeObservable();
    //创建动画待播放数组
    public volatile ArrayList<MessageGiftNewEvent> animGiftList = new ArrayList<>();

    public ArrayList<String> giftIDList = new ArrayList();
    //是否在播放动画
    public volatile boolean animGiftPlaying = false;
    //RxBus订阅事件
    private Disposable messageGiftNewEventSubscriber;
    private Disposable CallChatingHangupSubscriber;
    private Disposable InsufficientBalanceSubscriber;
    private Disposable ShowFloatWindowEntitySubscriber;
    //礼物消息防抖
    private String lastClickFunName;
    private long lastClickTime;

    private PhotoAlbumBean photoAlbumEntity;

    //点击与它视频
    public BindingCommand callVideoClick = new BindingCommand(() -> uc.callVideoViewEvent.call());

    //点击关闭马上视讯入口
    public BindingCommand closeOnClick = new BindingCommand(() -> isShoweCallingVideo.set(false));

    public BindingCommand moreOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            hideKeyboard();
            uc.clickMore.call();
        }
    });

    /**
     * 笔记点击
     */
    public BindingCommand noteOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.starNotepad.call();
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


    public ChatDetailViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
        sensitiveWords.set(model.readSensitiveWords());
    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
    }

    public void loadUserInfo(int userId) {
        model.isBlacklist(String.valueOf(userId))
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseObserver<BaseDataResponse<Map<String, String>>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<Map<String, String>> response) {
                        Map<String, String> mapData = response.getData();
                        if (!ObjectUtils.isEmpty(mapData)) {
                            inBlacklist.set(mapData.get("is_blacklist").equals("1"));
                        }

                    }

                    @Override
                    public void onError(RequestException e) {

                    }
                });
    }

    /**
     * 加载用户标签
     *
     * @param toUserId
     */
    public void loadTagUser(String toUserId) {
        model.tag(toUserId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<TagBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<TagBean> tagEntityBaseDataResponse) {
                        TagBean tagEntity = tagEntityBaseDataResponse.getData();
                        if (tagEntity != null) {
                            uc.loadTag.postValue(tagEntity);
                            tagEntitys.set(tagEntity);
                            isTrack.set(tagEntity.getIsCollect() == 1);
                            model.saveChatPushStatus(tagEntitys.get().getIsChatPush());
                            Integer isContacts = tagEntity.getIsContact();
                            isContactsEnabled.set(isContacts != null && isContacts == 1);
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }


    //获取当前用户数据
    public UserDataBean getLocalUserDataEntity() {
        return model.readUserData();
    }


    public void addBlackList(int userId) {
        model.addBlack(userId)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        inBlacklist.set(true);
                        if (tagEntitys.get().getBlacklistStatus() == 0){
                            tagEntitys.get().setBlacklistStatus(1);
                        }else if(tagEntitys.get().getBlacklistStatus() == 2){
                            tagEntitys.get().setBlacklistStatus(3);
                        }
                        RxBus.getDefault().post(new AddBlackListEvent());
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void delBlackList(int userId) {
        model.deleteBlack(userId)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        inBlacklist.set(false);
                        if (tagEntitys.get().getBlacklistStatus() == 1){
                            tagEntitys.get().setBlacklistStatus(0);
                        }else if(tagEntitys.get().getBlacklistStatus() == 3){
                            tagEntitys.get().setBlacklistStatus(2);
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void checkConnMic(int userId) {
        model.userIsConnMic(userId)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<UserConnMicStatusBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<UserConnMicStatusBean> response) {
                        if (response.getData().getConnection()) {
                            uc.clickConnMic.call();
                        } else {
                            ToastUtils.showShort(R.string.playcc_opposite_mic_disabled);
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //获取用户聊天相册
    public void getPhotoAlbum(Integer userId){
        model.getPhotoAlbum(userId)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseObserver<BaseDataResponse<PhotoAlbumBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<PhotoAlbumBean> photoAlbumEntityBaseDataResponse) {
                        if(photoAlbumEntityBaseDataResponse.getData()!=null && photoAlbumEntityBaseDataResponse.getData().getImg()!=null && photoAlbumEntityBaseDataResponse.getData().getImg().size()>0){
                            photoAlbumEntity = photoAlbumEntityBaseDataResponse.getData();
                        }
                    }
                    @Override
                    public void onComplete() {
                        uc.putPhotoAlbumEntity.setValue(photoAlbumEntity);

                    }
                });
    }

    /**
     * 获取在线状态标识图
     * @param detailEntity
     * @return
     */
    public Drawable onLineDrawables(TagBean detailEntity) {
        if (detailEntity == null
                || detailEntity.getCallingStatus() == null
                || detailEntity.getIsOnline() == null)return null;

        if (detailEntity.getCallingStatus() == 0) {
            if (detailEntity.getIsOnline() == 1) {
                return com.blankj.utilcode.util.Utils.getApp().getResources().getDrawable(R.drawable.user_detail_online);
            }
        } else if (detailEntity.getCallingStatus() == 1) {
            return com.blankj.utilcode.util.Utils.getApp().getResources().getDrawable(R.drawable.user_detail_calling);
        } else if (detailEntity.getCallingStatus() == 2) {
            return com.blankj.utilcode.util.Utils.getApp().getResources().getDrawable(R.drawable.user_detail_video);
        }
        return com.blankj.utilcode.util.Utils.getApp().getResources().getDrawable(R.drawable.user_detail_online);
    }

    //根据用户ID获取评价
    public void getUserEvaluate(Integer userId,boolean sendIM) {
        model.evaluate(userId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(dispose -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<List<EvaluateBean>>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<List<EvaluateBean>> response) {
                        if(response.getData()!=null){
                            List<EvaluateBean> evaluateEntityList = response.getData();
                            List<EvaluateObjBean> list = null;
                            if (!ConfigManager.getInstance().isMale()) {
                                list = Injection.provideDemoRepository().readMaleEvaluateConfig();
                            } else {
                                list = Injection.provideDemoRepository().readFemaleEvaluateConfig();
                            }
                            List<EvaluateItemBean> items = new ArrayList<>();
                            if(sendIM){
                                for (EvaluateObjBean configEntity : list) {
                                    //好的评价
                                    if(configEntity.getType()==0){
                                        EvaluateItemBean evaluateItemEntity = new EvaluateItemBean(configEntity.getId(), configEntity.getName(), configEntity.getType() == 1);
                                        items.add(evaluateItemEntity);
                                        for (EvaluateBean evaluateEntity : evaluateEntityList) {
                                            if (configEntity.getId() == evaluateEntity.getTagId()) {
                                                evaluateItemEntity.setNumber(evaluateEntity.getNumber());
                                            }
                                        }
                                    }
                                }
                                uc.sendIMEvaluate.setValue(items);
                            }else{
                                for (EvaluateObjBean configEntity : list) {
                                    EvaluateItemBean evaluateItemEntity = new EvaluateItemBean(configEntity.getId(), configEntity.getName(), configEntity.getType() == 1);
                                    items.add(evaluateItemEntity);
                                }
                                uc.AlertMEvaluate.setValue(items);
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
     * 提交评价
     *
     * @param tagId 评价标签ID
     */
    public void commitUserEvaluate(int userId, int tagId, DialogInterface dialog) {
        model.evaluateCreate(userId, tagId, null)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        ToastUtils.showShort(R.string.playcc_submittd);
                        uc.removeEvaluateMessage.call();
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
    }

    public void sendUserGift(Dialog dialog, GiftBagBean.giftEntity giftEntity, Integer to_user_id, Integer amount) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("giftId", giftEntity.getId());
        map.put("toUserId", to_user_id);
        map.put("type", 1);
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
                        maleBalance -= giftEntity.getMoney();
//                        GiftEntity _giftEntity = new GiftEntity();
//                        _giftEntity.setSvgaPath(giftEntity.getLink());
//                        animGiftList.add(_giftEntity);
//                        if(!animGiftPlaying){
//                            uc.signGiftAnimEvent.call();
//                        }
                        //dialog.dismiss();
                    }
                    @Override
                    public void onError(RequestException e) {
                        dialog.dismiss();
                        dismissHUD();
                        if (e.getCode() != null && e.getCode().intValue() == 21001) {
                            ToastCenterUtils.showToast(R.string.playcc_dialog_exchange_integral_total_text1);
                            CCApplication.instance().logEvent(AppsFlyerEvent.im_gifts_Insufficient_topup);
                            uc.sendUserGiftError.call();
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
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
                        LogUtils.i("onSuccess: "+data);
                        uc.clickCrystalExchange.setValue(data);

                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //拨打语音、视频
    public void getCallingInvitedInfo(int callingType, String IMUserId, String toIMUserId) {
        if (Status.mIsShowFloatWindow){
            ToastUtils.showShort(R.string.audio_in_call);
            return;
        }
        if(callingType==1){
            //男女点击拨打语音
            CCApplication.instance().logEvent(ConfigManager.getInstance().isMale() ? AppsFlyerEvent.call_voice_male : AppsFlyerEvent.call_voice_female);
        }else{
            //男女点击拨打视频
            CCApplication.instance().logEvent(ConfigManager.getInstance().isMale() ? AppsFlyerEvent.call_video_male : AppsFlyerEvent.call_video_female);
        }
        model.callingInviteInfo(callingType, IMUserId, toIMUserId, 0)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<CallingInviteInfo>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CallingInviteInfo> callingInviteInfoBaseDataResponse) {
                        if (callingInviteInfoBaseDataResponse.getCode() == 2) {//忙线中
                            uc.otherBusy.call();
                            return;
                        }
                        if (callingInviteInfoBaseDataResponse.getCode() == 22001) {//游戏中
                            Toast.makeText(CCApplication.instance(), R.string.playcc_in_game, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        CallingInviteInfo callingInviteInfo = callingInviteInfoBaseDataResponse.getData();
                        if (callingInviteInfo != null) {
                            com.fine.friendlycc.calling.Utils.tryStartCallSomeone(callingType, toIMUserId, callingInviteInfo.getRoomId(), new Gson().toJson(callingInviteInfo));
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        if(e.getCode()!=null && e.getCode() ==21001 && e.getCode()==1 ){//钻石余额不足
                            uc.sendDialogViewEvent.call();
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //IM聊天价格配置
    public void getPriceConfig(Integer toUserId){
        model.getPriceConfig(toUserId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<PriceConfigBean>>(){
                    @Override
                    public void onSuccess(BaseDataResponse<PriceConfigBean> response) {
                        PriceConfigBean priceConfigEntity = response.getData();
                        if(priceConfigEntity != null){
                            priceConfigEntityField = priceConfigEntity;
                            mySelfAudioFlag = priceConfigEntity.getCurrent().getAllowAudio() == 1;
                            mySelfVideoFlag = priceConfigEntity.getCurrent().getAllowVideo() == 1;
                            uc.imProfit.call();
                        }
                    }

                    @Override
                    public void onError(RequestException e) {

                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void verifyGoddessTips(Integer userId) {
        Boolean sendSuccess = model.readVerifyGoddessTipsUser(model.readUserData().getId() + "_" + userId);
        if (sendSuccess.booleanValue()) {
            return;
        }
        model.verifyGoddessTips(userId)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<Map<String, Integer>>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<Map<String, Integer>> response) {
                        if (response.getData() != null) {
                            Map<String, Integer> mapData = response.getData();
                            if (mapData.get("status") != null && mapData.get("status").intValue() == 1) {
                                model.putVerifyGoddessTipsUser(model.readUserData().getId() + "_" + userId, "true");
                            }
                        }
                    }
                });
    }

    //存储键值对
    public void putKeyValue(String key, String value) {
        model.putKeyValue(key, value);
    }

    public String readKeyValue(String key) {
        return model.readKeyValue(key);
    }

    //追踪
    public void addLike(Integer toUserId, String msgId) {
        model.addCollect(toUserId)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        isTrack.set(true);
                        Toast.makeText(CCApplication.instance(), R.string.playcc_cancel_zuizong_3, Toast.LENGTH_SHORT).show();
                        uc.addLikeSuccess.postValue(msgId);
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissHUD();
                    }
                });
    }

    /**
     * 取消追蹤
     * @param toUserId 对方的userID
     */
    public void delLike(Integer toUserId) {
        model.deleteCollect(toUserId)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        isTrack.set(false);
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissHUD();
                    }
                });
    }

    /**
     * @return void
     * @Desc TODO(查询剩余钻石)
     * @author 彭石林
     * @parame []
     * @Date 2022/3/21
     */
    public void getTotalCoins() {
        model.getTotalCoins(1)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<ChatDetailCoinBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<ChatDetailCoinBean> responseBody) {
                        if (responseBody.getData() != null) {
                            ChatDetailCoinBean chatDetailCoinEntity = responseBody.getData();
                            Integer totalCoins = chatDetailCoinEntity.getTotalCoins();
                            if (totalCoins != null) {
                                if (totalCoins <= 0) {
                                    maleBalance = 0;
                                } else {
                                    maleBalance = totalCoins;
                                }
                            } else {
                                ToastUtils.showShort(R.string.playcc_network_text);
                                pop();
                            }
                        } else {
                            ToastUtils.showShort(R.string.playcc_network_text);
                            pop();
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        ToastUtils.showShort(R.string.playcc_network_text);
                        pop();
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }
    /**
     * @Desc TODO(转换IM用户id)
     * @author 彭石林
     * @parame [ImUserId]
     * @return void
     * @Date 2022/4/2
     */
    public void transUserIM(String ImUserId){
        model.transUserIM(ImUserId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(dispose -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<IMTransUserBean>>() {

                    @Override
                    public void onSuccess(BaseDataResponse<IMTransUserBean> response) {
                        IMTransUserBean  imTransUserEntity = response.getData();
                        if(imTransUserEntity!=null && imTransUserEntity.getUserId()!=null){
                            Bundle bundle = UserDetailFragment.getStartBundle(imTransUserEntity.getUserId());
                            start(UserDetailFragment.class.getCanonicalName(), bundle);
                        }
                    }
                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void friendAddFrequent(boolean typeFlag,String otherImUserId,Integer friendUserId){
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("friendImId", otherImUserId);
        dataMap.put("friendUserId", friendUserId);
        if(!typeFlag){
            model.friendAddFrequent(ApiUitl.getBody(GsonUtils.toJson(dataMap)))
                    .doOnSubscribe(this)
                    .compose(RxUtils.schedulersTransformer())
                    .compose(RxUtils.exceptionTransformer())
                    .doOnSubscribe(dismissHUD -> showHUD())
                    .subscribe(new BaseObserver<BaseResponse>(){
                        @Override
                        public void onSuccess(BaseResponse baseResponse) {
                            isContactsEnabled.set(!isContactsEnabled.get());
                            ToastUtils.showShort(StringUtils.getString(R.string.playcc_contact_text1));
                        }
                        @Override
                        public void onComplete() {
                            dismissHUD();
                        }
                    });
        }else{
            model.friendDeleteFrequent(ApiUitl.getBody(GsonUtils.toJson(dataMap)))
                    .doOnSubscribe(this)
                    .compose(RxUtils.schedulersTransformer())
                    .compose(RxUtils.exceptionTransformer())
                    .doOnSubscribe(dismissHUD -> showHUD())
                    .subscribe(new BaseObserver<BaseResponse>(){
                        @Override
                        public void onSuccess(BaseResponse baseResponse) {
                            isContactsEnabled.set(!isContactsEnabled.get());
                            ToastUtils.showShort(StringUtils.getString(R.string.playcc_contact_text2));
                        }
                        @Override
                        public void onComplete() {
                            dismissHUD();
                        }
                    });
        }

    }
    /**
    * @Desc TODO(解锁IM付费消息)
    * @author 彭石林
    * @parame []
    * @return void
    * @Date 2022/9/17
    */
    public void mediaGalleryPay(MediaGalleryEditEntity mediaGalleryEditEntity){
        model.mediaGalleryPay(mediaGalleryEditEntity.getMsgKeyId(),mediaGalleryEditEntity.getToUserId())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>(){

                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        mediaGalleryEditEntity.setStateUnlockPhoto(true);
                        uc.mediaGalleryPayEvent.setValue(mediaGalleryEditEntity);
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        if(e.getCode()!=null && e.getCode() == 21001 ){//钻石余额不足
                            uc.sendDialogViewEvent.call();
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissHUD();
                    }
                });
    }

    public void setAllowPrivacy(String type) {
        PrivacyBean entity = new PrivacyBean();
        if (type.equals(ALLOW_TYPE_AUDIO)){
            if (mySelfAudioFlag)return;
            entity.setAllowAudio(true);
        }else if (type.equals(ALLOW_TYPE_VIDEO)){
            if (mySelfVideoFlag)return;
            entity.setAllowVideo(true);
        }
        model.setPrivacy(entity)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (type.equals(ALLOW_TYPE_AUDIO)){
                            mySelfAudioFlag = true;
                            ToastUtils.showShort(R.string.playcc_activated_audio);
                        }else if (type.equals(ALLOW_TYPE_VIDEO)){
                            mySelfVideoFlag = true;
                            ToastUtils.showShort(R.string.playcc_activated_video);
                        }
                        RxBus.getDefault().post(new MineInfoChangeEvent());
                        dismissHUD();
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }


    public void playSVGAGift(){
        if (animGiftList.size() > 0 && !animGiftPlaying){
            animGiftPlaying = true;
            uc.signGiftAnimEvent.call();
        }
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();

        messageGiftNewEventSubscriber = RxBus.getDefault().toObservable(MessageGiftNewEvent.class).subscribe(event -> {
            if (event != null){
                boolean containsKey = giftIDList.contains(event.getMsgId());
                if (!containsKey){
                    giftIDList.add(event.getMsgId());
                    animGiftList.add(event);
                    playSVGAGift();
                }
            }
        });
        CallChatingHangupSubscriber = RxBus.getDefault().toObservable(CallChatingHangupEvent.class).subscribe(event -> {
            UserDataBean localUser = getLocalUserDataEntity();
            if (localUser != null && localUser.getSex() != null && localUser.getSex().intValue() == 1) {
                getTotalCoins();
            }
        });
        InsufficientBalanceSubscriber = RxBus.getDefault().toObservable(InsufficientBalanceEvent.class).subscribe(event -> {
            //发送本地余额不足消息
            uc.sendLoaclInsufficientBalance.call();
            //调起储值界面
            uc.sendDialogViewEvent.call();
        });
        ShowFloatWindowEntitySubscriber = RxBus.getDefault().toObservable(ShowFloatWindowBean.class).subscribe(event -> {
            isShoweCallingVideo.set(!event.isShow);
        });


        //将订阅者加入管理站
        RxSubscriptions.add(messageGiftNewEventSubscriber);
        RxSubscriptions.add(ShowFloatWindowEntitySubscriber);
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(messageGiftNewEventSubscriber);
        RxSubscriptions.remove(ShowFloatWindowEntitySubscriber);
    }

    public class UIChangeObservable {
        public SingleLiveEvent<Void> clickConnMic = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> imProfit = new SingleLiveEvent<>();
        public SingleLiveEvent clickMore = new SingleLiveEvent<>();
        public SingleLiveEvent<MallWithdrawTipsInfoBean> clickCrystalExchange = new SingleLiveEvent<>();
        //对方忙线
        public SingleLiveEvent otherBusy = new SingleLiveEvent<>();
        //新增
        public SingleLiveEvent<List<Integer>> askUseChatNumber = new SingleLiveEvent<>();
        public SingleLiveEvent<Integer> useChatNumberSuccess = new SingleLiveEvent<>();
        //查询对方资料 判断是否为机器人、最后在线时间-判断用户是否在线
        public SingleLiveEvent<Map<String, String>> ChatUserDetailEntity = new SingleLiveEvent<>();

        public SingleLiveEvent<TagBean> loadTag = new SingleLiveEvent<>();

        //刷新页面数据
        public SingleLiveEvent<Boolean> loadMessage = new SingleLiveEvent<>();
        //插入相扑数据
        public SingleLiveEvent<PhotoAlbumBean> putPhotoAlbumEntity = new SingleLiveEvent<>();
        //是否可以评价
        public SingleLiveEvent<Boolean> canEvaluate = new SingleLiveEvent<>();
        //发送IM评价插入
        public SingleLiveEvent<List<EvaluateItemBean>> sendIMEvaluate = new SingleLiveEvent<>();
        //弹出评价框等待用户评价
        public SingleLiveEvent<List<EvaluateItemBean>> AlertMEvaluate = new SingleLiveEvent<>();
        //删除评价窗体
        public SingleLiveEvent<Void> removeEvaluateMessage = new SingleLiveEvent<>();
        //发送礼物失败。充值钻石
        public SingleLiveEvent<Void> sendUserGiftError = new SingleLiveEvent<>();
        //追踪成功
        public SingleLiveEvent<String> addLikeSuccess = new SingleLiveEvent<>();
        //播放礼物效果
        public SingleLiveEvent<Void> signGiftAnimEvent = new SingleLiveEvent<>();
        //上传文件成功。发送消息
        public SingleLiveEvent<TUIMessageBean> signUploadSendMessage = new SingleLiveEvent<>();
        //钻石不足。唤起充值
        public SingleLiveEvent<Void> sendDialogViewEvent = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> starNotepad = new SingleLiveEvent<>();
        //拨打视频电话
        public SingleLiveEvent<Void> callVideoViewEvent = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> sendLoaclInsufficientBalance = new SingleLiveEvent<>();
        //解锁付费消息成功
        public SingleLiveEvent<MediaGalleryEditEntity> mediaGalleryPayEvent = new SingleLiveEvent<>();
    }


}