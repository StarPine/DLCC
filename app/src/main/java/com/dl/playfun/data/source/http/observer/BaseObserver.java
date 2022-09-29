package com.dl.playfun.data.source.http.observer;

import com.dl.playfun.data.source.http.exception.RequestException;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.event.LoginExpiredEvent;
import com.dl.playfun.event.UserDisableEvent;

import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public abstract class BaseObserver<T extends BaseResponse> extends BaseDisposableObserver<T> {

    @Override
    public abstract void onSuccess(T t);

    @Override
    public void onError(RequestException e) {
        if (e.getCode() == 10103) {
            RxBus.getDefault().post(new UserDisableEvent());
        } else if (e.getCode() == 10100) {
            //Log.e("接收服务器的登录过期数据",e.getClass().getCanonicalName());
            RxBus.getDefault().post(new LoginExpiredEvent());
        } else {
            ToastUtils.showShort(e.getMessage() == null ? "" : e.getMessage());
        }
        onComplete();
    }

    @Override
    public void onComplete() {

    }
}
