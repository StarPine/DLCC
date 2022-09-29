package com.dl.playfun.data.source.http.observer;

import com.dl.playfun.data.source.http.exception.RequestException;
import com.dl.playfun.data.source.http.response.BaseListDataResponse;
import com.dl.playfun.viewmodel.BaseViewModel;
import com.dl.playfun.widget.emptyview.EmptyState;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public abstract class BaseListEmptyObserver<T extends BaseListDataResponse> extends BaseObserver<T> {

    private BaseViewModel baseViewModel;

    public BaseListEmptyObserver() {
    }

    public BaseListEmptyObserver(BaseViewModel baseViewModel) {
        this.baseViewModel = baseViewModel;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onSuccess(T t) {
        if (baseViewModel != null) {
            if (t.getData().getTotal() == 0 && t.isEmpty()) {
                baseViewModel.stateModel.setEmptyState(EmptyState.EMPTY);
            } else {
                baseViewModel.stateModel.setEmptyState(EmptyState.NORMAL);
            }
        }
    }

    @Override
    public void onError(RequestException e) {
        ToastUtils.showShort(e.getMessage());
        onComplete();
    }

    @Override
    public void onComplete() {

    }
}
