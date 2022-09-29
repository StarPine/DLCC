package com.dl.playfun.data.source.http.observer;

import com.dl.playfun.data.source.http.exception.ApiErrorException;
import com.dl.playfun.data.source.http.exception.ApiFailException;
import com.dl.playfun.data.source.http.exception.RequestException;
import com.dl.playfun.data.source.http.response.BaseResponse;

import io.reactivex.observers.DisposableObserver;
import me.goldze.mvvmhabit.http.ResponseThrowable;

/**
 * @author wulei
 */
public abstract class BaseDisposableObserver<T extends BaseResponse> extends DisposableObserver<T> {

    @Override
    public void onNext(T t) {
        if (t.isSuccess()) {
            onSuccess(t);
        }else if (t.isInfo()) {
            onSuccess(t);
        } else if (t.isFail()) {
            ApiFailException apiFailException = new ApiFailException(t.getCode(), t.getMessage());
            onError(apiFailException);
        } else if (t.isError()) {
            ApiErrorException apiErrorException = new ApiErrorException(t.getCode(), t.getMessage());
            onError(apiErrorException);
        } else {
            ApiFailException apiFailException = new ApiFailException(t.getCode(), t.getMessage());
            onError(apiFailException);
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof ResponseThrowable) {
            ResponseThrowable responseThrowable = (ResponseThrowable) e;
            onError(new RequestException(responseThrowable.code, responseThrowable.message));
        } else if (e instanceof ApiFailException) {
            ApiFailException responseThrowable = (ApiFailException) e;
            onError(new RequestException(responseThrowable.getCode(), responseThrowable.getMessage()));
        } else {
            onError(new RequestException(-1, e.getMessage()));
        }
        onComplete();
    }

    public abstract void onSuccess(T t);

    public abstract void onError(RequestException e);

}
