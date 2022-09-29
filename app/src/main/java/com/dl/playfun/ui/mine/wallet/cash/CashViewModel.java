package com.dl.playfun.ui.mine.wallet.cash;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseDataResponse;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.entity.CashWalletEntity;
import com.dl.playfun.viewmodel.BaseRefreshViewModel;

import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * @author wulei
 */
public class CashViewModel extends BaseRefreshViewModel<AppRepository> {

    public ObservableField<CashWalletEntity> cashWalletEntity = new ObservableField<>();

    public CashViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        startRefresh();
    }

    @Override
    public void loadDatas(int page) {
        loadCashWallet();
    }

    public void loadCashWallet() {
        model.cashWallet()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseObserver<BaseDataResponse<CashWalletEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CashWalletEntity> response) {
                        cashWalletEntity.set(response.getData());
                    }

                    @Override
                    public void onComplete() {
                        stopRefreshOrLoadMore();
                    }
                });
    }

    /**
     * 现金提现
     */
    public void cashWithdraw(float money) {
        model.cashOut(money)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        loadCashWallet();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissHUD();
                    }
                });
    }

}