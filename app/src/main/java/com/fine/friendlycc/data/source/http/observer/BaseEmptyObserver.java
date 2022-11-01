package com.fine.friendlycc.data.source.http.observer;

import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.fine.friendlycc.widget.emptyview.EmptyState;

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
