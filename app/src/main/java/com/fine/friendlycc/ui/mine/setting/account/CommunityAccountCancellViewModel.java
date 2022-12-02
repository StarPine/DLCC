package com.fine.friendlycc.ui.mine.setting.account;

import android.app.Application;

import androidx.annotation.NonNull;

import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.ui.login.LoginFragment;
import com.fine.friendlycc.ui.main.MainFragment;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;

public class CommunityAccountCancellViewModel extends BaseViewModel<AppRepository> {
    public SingleLiveEvent<Boolean> cancellType = new SingleLiveEvent();

    public CommunityAccountCancellViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
    }

    public void logout() {
        AppConfig.userClickOut = true;
        showHUD();
        model.logout();
        startWithPopTo(LoginFragment.class.getCanonicalName(), MainFragment.class.getCanonicalName(), true);
        dismissHUD();
    }

    /**
     * 注销账号
     *
     * @return
     */
    public void cancellation() {
        model.cancellation()
                .doOnSubscribe(this)
                .compose(RxUtils.exceptionTransformer())
                .compose(RxUtils.schedulersTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        dismissHUD();
                        cancellType.setValue(true);
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        dismissHUD();
                        if (e.getCode() == 10101) {
                            cancellType.setValue(false);
                        }
                    }
                });
    }

}
