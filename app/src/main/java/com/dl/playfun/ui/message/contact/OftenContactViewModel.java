package com.dl.playfun.ui.message.contact;

import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.dl.playfun.R;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.exception.RequestException;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseDataResponse;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.entity.IMTransUserEntity;
import com.dl.playfun.entity.ImUserSigEntity;
import com.dl.playfun.entity.TokenEntity;
import com.dl.playfun.event.LoginExpiredEvent;
import com.dl.playfun.ui.userdetail.detail.UserDetailFragment;
import com.dl.playfun.utils.ToastCenterUtils;
import com.dl.playfun.viewmodel.BaseViewModel;
import com.tencent.qcloud.tuicore.TUILogin;
import com.tencent.qcloud.tuicore.interfaces.TUICallback;

import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.Utils;

/**
 * Author: 彭石林
 * Time: 2022/8/11 17:36
 * Description: This is OftenContactViewModel
 */
public class OftenContactViewModel extends BaseViewModel<AppRepository> {

    //跳转进入私聊页面
    public SingleLiveEvent<Integer> startChatUserView = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> loginSuccess = new SingleLiveEvent<>();

    public OftenContactViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
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
                                loginSuccess.call();
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
                            startChatUserView.postValue(userId);
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        if (e.getCode() == 11111) { //注销
                            ToastCenterUtils.showShort(R.string.playfun_user_detail_user_disable3);
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
}
