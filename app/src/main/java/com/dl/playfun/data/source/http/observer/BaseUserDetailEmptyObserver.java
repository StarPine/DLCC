package com.dl.playfun.data.source.http.observer;

import com.dl.playfun.data.source.http.exception.RequestException;
import com.dl.playfun.data.source.http.response.BaseDataResponse;
import com.dl.playfun.viewmodel.BaseViewModel;
import com.dl.playfun.widget.emptyview.EmptyState;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Author: 彭石林
 * Time: 2021/11/10 20:02
 * Description: This is BaseUserDetailEmptyObserver
 */
public class BaseUserDetailEmptyObserver <T extends BaseDataResponse> extends BaseObserver<T> {

    private BaseViewModel baseViewModel;

    public BaseUserDetailEmptyObserver() {
    }

    public BaseUserDetailEmptyObserver(BaseViewModel baseViewModel) {
        this.baseViewModel = baseViewModel;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onSuccess(T t) {
        if (baseViewModel != null) {
            if (t.getData()==null && t.isDataEmpty()) {
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