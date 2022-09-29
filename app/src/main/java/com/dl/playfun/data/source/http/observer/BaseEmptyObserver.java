package com.dl.playfun.data.source.http.observer;

import com.dl.playfun.data.source.http.exception.RequestException;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.viewmodel.BaseViewModel;
import com.dl.playfun.widget.emptyview.EmptyState;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public abstract class BaseEmptyObserver<T extends BaseResponse> extends BaseDisposableObserver<T> {

    private BaseViewModel baseViewModel;

    public BaseEmptyObserver() {
    }

    public BaseEmptyObserver(BaseViewModel baseViewModel) {
        this.baseViewModel = baseViewModel;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (baseViewModel != null) {
            baseViewModel.stateModel.setEmptyState(EmptyState.PROGRESS);
        }
    }

    @Override
    public void onSuccess(T t) {
        if (baseViewModel != null) {
            baseViewModel.stateModel.setEmptyState(EmptyState.NORMAL);
        }
    }

    @Override
    public void onError(RequestException e) {
        ToastUtils.showShort(e.getMessage());
        if (baseViewModel != null) {
            baseViewModel.stateModel.bindThrowable(e);
            baseViewModel.stateModel.setEmptyState(EmptyState.EMPTY);
        }
        onComplete();
    }

    @Override
    public void onComplete() {

    }
}
