package com.fine.friendlycc.ui.message.chatmessage;

import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.BrowseNumberEntity;
import com.fine.friendlycc.entity.IMTransUserEntity;
import com.fine.friendlycc.entity.ImUserSigEntity;
import com.fine.friendlycc.entity.TokenEntity;
import com.fine.friendlycc.event.LoginExpiredEvent;
import com.fine.friendlycc.ui.mine.trace.man.TraceManFragment;
import com.fine.friendlycc.ui.userdetail.detail.UserDetailFragment;
import com.fine.friendlycc.utils.ToastCenterUtils;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.tencent.qcloud.tuicore.TUILogin;
import com.tencent.qcloud.tuicore.interfaces.TUICallback;

import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.Utils;

/**
 * @author wulei
 */
public class ChatMessageViewModel extends BaseViewModel<AppRepository> {

    UIChangeObservable uc = new UIChangeObservable();

    public ObservableField<String> NewNumberText = new ObservableField<String>();
    //跳转谁看过我
    public BindingCommand traceOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            AppContext.instance().logEvent(AppsFlyerEvent.chat_seen_me);
            Bundle bundle = new Bundle();
            bundle.putInt("userId", model.readUserData().getId());
            start(TraceManFragment.class.getCanonicalName(), bundle);
            NewNumberText.set(null);
        }
    });

    public ChatMessageViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }
    //请求谁看过我、粉丝间隔时间
    private Long intervalTime = null;

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
                .subscribe(new BaseObserver<BaseDataResponse<BrowseNumberEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<BrowseNumberEntity> browseNumberEntity) {
                        uc.loadBrowseNumber.setValue(browseNumberEntity.getData());
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
    public void transUserIM(String ImUserId,boolean userDetailView){
        model.transUserIM(ImUserId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(dispose -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<IMTransUserEntity>>() {

                    @Override
                    public void onSuccess(BaseDataResponse<IMTransUserEntity> response) {
                        IMTransUserEntity  imTransUserEntity = response.getData();
                        if(imTransUserEntity!=null && imTransUserEntity.getUserId()!=null){
                            userMessageCollation(imTransUserEntity.getUserId(), userDetailView);
                        }else {
                            dismissHUD();
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        dismissHUD();
                    }
                });
    }

    public void userMessageCollation(int userId, boolean userDetailView) {
        model.userMessageCollation(userId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if(userDetailView){//去往用户主页
                            Bundle bundle = UserDetailFragment.getStartBundle(userId);
                            start(UserDetailFragment.class.getCanonicalName(), bundle);
                        }else{//进入私聊页面
                            uc.startChatUserView.postValue(userId);
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        if (e.getCode() == 11111) { //注销
                            ToastCenterUtils.showShort(R.string.playcc_user_detail_user_disable3);
                        } else {
                            super.onError(e);
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void flushSign() {
        model.flushSign()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<ImUserSigEntity>>() {

                    @Override
                    public void onSuccess(BaseDataResponse<ImUserSigEntity> response) {
                        ImUserSigEntity data = response.getData();
                        TokenEntity tokenEntity = model.readLoginInfo();
                        if (data == null || TextUtils.isEmpty(data.getUserSig()) || tokenEntity == null){
                            RxBus.getDefault().post(new LoginExpiredEvent());
                            return;
                        }
                        tokenEntity.setUserSig(data.getUserSig());
                        TUILogin.login(Utils.getContext(), model.readApiConfigManagerEntity().getImAppId(), tokenEntity.getUserID(), tokenEntity.getUserSig(), new TUICallback() {
                            @Override
                            public void onSuccess() {
                                model.saveLoginInfo(tokenEntity);
                                uc.loginSuccess.call();
                            }

                            @Override
                            public void onError(int errorCode, String errorMessage) {
                                RxBus.getDefault().post(new LoginExpiredEvent());
                            }
                        });
                    }

                    @Override
                    public void onError(RequestException e) {
                        RxBus.getDefault().post(new LoginExpiredEvent());
                    }

                    @Override
                    public void onComplete() {
                    }

                });
    }

    public static class UIChangeObservable {
        public SingleLiveEvent<BrowseNumberEntity> loadBrowseNumber = new SingleLiveEvent<>();
        //跳转进入私聊页面
        public SingleLiveEvent<Integer> startChatUserView = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> loginSuccess = new SingleLiveEvent<>();
    }

}
